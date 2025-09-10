package com.ujs.drivingapp;

/**
 * @author HWH.
 * @email huangwenhuan2019@outlook.com
 * @create time 1/27/2022 8:40 PM.
 * @description 主界面
 */

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.weather.LocalWeatherForecastResult;
import com.amap.api.services.weather.LocalWeatherLive;
import com.amap.api.services.weather.LocalWeatherLiveResult;
import com.amap.api.services.weather.WeatherSearch;
import com.amap.api.services.weather.WeatherSearchQuery;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.ujs.drivingapp.Fragments.DeviceFragment;
import com.ujs.drivingapp.Fragments.HomeFragment;
import com.ujs.drivingapp.Fragments.ProfileFragment;
import com.ujs.drivingapp.Fragments.StatisticsFragment;
import com.ujs.drivingapp.Pojo.NavItem;
import com.ujs.drivingapp.Utils.NavFragmentUtil;
import com.ujs.drivingapp.Utils.StatusBarStyleUtil;
import com.ujs.drivingapp.Utils.TimerUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static int REQUEST_ENABLE_BLUETOOTH = 1;//请求码

    BluetoothAdapter bluetoothAdapter;//蓝牙适配器

    private BluetoothReceiver bluetoothReceiver;//蓝牙广播接收器

    private RxPermissions rxPermissions;//权限请求

    private BluetoothGattCharacteristic mCharacteristic;


    //GATT service UUID
    public static final UUID DEVICE_INFO_SERVICE_UUID = UUID.fromString("0000180f-0000-1000-8000-00805f9b34fb");
    //Charcteristic UUID
    public static final UUID CHARACTERISTIC_UUID = UUID.fromString("00002a19-0000-1000-8000-00805f9b34fb");

    private BluetoothGatt mBluetoothGatt;
    List<BluetoothDevice> list = new ArrayList<>();//数据来源
    ArrayList<Fragment> fragments = new ArrayList<>();
    ArrayList<NavItem> navItems = new ArrayList<>();
    NavFragmentUtil navFragmentUtil;
    private boolean isRetry = false;


    String VID = "";
    String PID = "";
    private boolean isCruise = true; // 是否处于巡航状态
    private WeatherSearchQuery mquery;
    private WeatherSearch mweathersearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StatusBarStyleUtil.changeStatusBarTextColor(getWindow(), true);
        Objects.requireNonNull(getSupportActionBar()).hide();//这行代码必须写在setContentView()方法的后面
        navItems.add(new NavItem(R.drawable.ic_home, "首页"));
        navItems.add(new NavItem(R.drawable.ic_device, "设备"));
        navItems.add(new NavItem(R.drawable.ic_statistics, "统计"));
        navItems.add(new NavItem(R.drawable.ic_profile, "我的"));
        fragments.add(HomeFragment.newInstance("HomePage", "TODO"));
        fragments.add(DeviceFragment.newInstance("DevicePage", "TODO"));
        fragments.add(StatisticsFragment.newInstance("StatisticsPage", "TODO"));
        fragments.add(ProfileFragment.newInstance("ProfilePage", "TODO"));

        AMapLocationClient.updatePrivacyAgree(this, true);
        AMapLocationClient.updatePrivacyShow(this, true, true);
        checkVersion();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (navFragmentUtil == null) {
            navFragmentUtil = NavFragmentUtil.getInstance(this, fragments, navItems);
            navFragmentUtil.initNavFragment();
        }

        //检索参数为城市和天气类型，实况天气为WEATHER_TYPE_LIVE、天气预报为WEATHER_TYPE_FORECAST
        mquery = new WeatherSearchQuery("北京", WeatherSearchQuery.WEATHER_TYPE_LIVE);
        try {
            mweathersearch = new WeatherSearch(this);
        } catch (AMapException e) {
            e.printStackTrace();
        }
        mweathersearch.setOnWeatherSearchListener(new WeatherSearch.OnWeatherSearchListener() {
            @Override
            public void onWeatherLiveSearched(LocalWeatherLiveResult localWeatherLiveResult, int i) {
                if (i == 1000) {
                    if (localWeatherLiveResult != null && localWeatherLiveResult.getLiveResult() != null) {
                        LocalWeatherLive weatherLive = localWeatherLiveResult.getLiveResult();
                        Log.e(TAG, "onWeatherLiveSearched: " + weatherLive.getCity() + " " + weatherLive.getTemperature());
                    } else {
                        showMsg("无结果", Toast.LENGTH_SHORT);
                    }
                } else {
                    showMsg(i + "", Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onWeatherForecastSearched(LocalWeatherForecastResult localWeatherForecastResult, int i) {

            }
        });
        mweathersearch.setQuery(mquery);
        mweathersearch.searchWeatherAsyn(); //异步搜索


    }


    public void onChange(View v) {
        TextView change = findViewById(R.id.tv_change);
        TextView type = findViewById(R.id.tv_type);
        int height = type.getHeight();
        int width = type.getWidth();
        long time = 300;
        ValueAnimator va = ValueAnimator.ofInt(width, height);
        va.setDuration(time);
        va.setInterpolator(new DecelerateInterpolator());
        va.addUpdateListener(animation -> {
            type.getLayoutParams().width = (int) animation.getAnimatedValue();
            type.requestLayout();
        });

        va.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);

                v.setClickable(false);

                ValueAnimator va1 = ValueAnimator.ofInt(255, 0);
                va1.setDuration(time);
                va1.addUpdateListener(animation1 -> {
                    int val = (int) animation1.getAnimatedValue();
                    type.setTextColor(Color.argb(val, 24, 24, 24));
                });
                va1.start();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (isCruise) {
                    change.setText("切换至巡航");
                    type.setText("智能导航");
                } else {
                    change.setText("切换至导航");
                    type.setText("智能巡航");
                }
                isCruise = !isCruise;

                ValueAnimator va2=ValueAnimator.ofInt(height,width);
                va2.setDuration(time);
                va2.setInterpolator(new DecelerateInterpolator());
                va2.addUpdateListener(animation2 -> {
                    type.getLayoutParams().width = (int) animation2.getAnimatedValue();
                    type.requestLayout();
                });

                va2.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        ValueAnimator va3 = ValueAnimator.ofInt(0, 255);
                        va3.setDuration(time);
                        va3.addUpdateListener(animation1 -> {
                            int val = (int) animation1.getAnimatedValue();
                            type.setTextColor(Color.argb(val, 24, 24, 24));
                        });
                        va3.start();
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        v.setClickable(true);
                    }
                });
                va2.start();

            }
        });
        va.start();


    }

    public void onScanCode(View v) {
        Intent intent = new Intent("com.ujs.driving.app.SCANCODE");
        startActivity(intent);
    }


    public void onScan(View v) {

        if (bluetoothAdapter != null) {//是否支持蓝牙
            if (bluetoothAdapter.isEnabled()) {//打开
                //开始扫描周围的蓝牙设备,如果扫描到蓝牙设备，通过广播接收器发送广播
                bluetoothAdapter.startDiscovery();
                Log.e(TAG, "onScan: startDiscovery");
            } else {//未打开
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intent, REQUEST_ENABLE_BLUETOOTH);
            }
        } else {
            showMsg("你的设备不支持蓝牙", Toast.LENGTH_SHORT);
        }
    }


    /**
     * 检查Android版本
     */
    private void checkVersion() {
        if (Build.VERSION.SDK_INT >= 23) {//6.0或6.0以上
            permissionsRequest();//动态权限申请
        } else {//6.0以下
            initBlueTooth();//初始化蓝牙配置
        }
    }

    /**
     * 动态权限申请
     */
    @SuppressLint("CheckResult")
    private void permissionsRequest() {//使用这个框架使用了Lambda表达式，设置JDK版本为 1.8或者更高
        rxPermissions = new RxPermissions(this);//实例化这个权限请求框架，否则会报错
        rxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(granted -> {
                    if (granted) {//申请成功
                        initBlueTooth();//初始化蓝牙配置
                    } else {//申请失败
                        showMsg("权限未开启", Toast.LENGTH_SHORT);
                    }
                });
    }

    /**
     * 初始化蓝牙配置
     */
    private void initBlueTooth() {
        IntentFilter intentFilter = new IntentFilter();//创建一个IntentFilter对象
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);//获得扫描结果
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);//绑定状态变化
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);//开始扫描
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);//扫描结束
        bluetoothReceiver = new BluetoothReceiver();//实例化广播接收器
        registerReceiver(bluetoothReceiver, intentFilter);//注册广播接收器
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();//获取蓝牙适配器
    }

    /**
     * 消息提示
     *
     * @param msg  消息
     * @param type 类型(时间长短)
     */
    private void showMsg(String msg, int type) {
        Toast.makeText(this, msg, type).show();
    }


    /**
     * 结果返回
     *
     * @param requestCode 请求码
     * @param resultCode  结果码
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BLUETOOTH) {
            if (resultCode == RESULT_OK) {
                showMsg("蓝牙打开成功", Toast.LENGTH_SHORT);
            } else {
                showMsg("蓝牙打开失败", Toast.LENGTH_SHORT);
            }
        }
    }

    /**
     * 广播接收器
     */
    private class BluetoothReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.e(TAG, "onReceive: " + action);
            switch (action) {
                case BluetoothDevice.ACTION_FOUND://扫描到设备
                    getEffectiveDevice(intent);//获取已绑定的设备
                    break;
                case BluetoothDevice.ACTION_BOND_STATE_CHANGED://设备绑定状态发生改变
                    break;
                case BluetoothAdapter.ACTION_DISCOVERY_STARTED://开始扫描
                    showMsg("扫描中...", Toast.LENGTH_LONG);
                    break;
                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED://扫描结束
                    break;
            }
        }

    }


    /**
     * 获取有效设备 包括已匹配和未匹配设备 并进行筛选
     *
     * @param intent Intent对象
     */
    private void getEffectiveDevice(Intent intent) {
        boolean updated = false;
        // 获取已匹配设备
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {//如果获取的结果大于0，则开始逐个解析
            for (BluetoothDevice device : pairedDevices) {
                if (!list.contains(device) && device.getName() != null) {//防止重复添加 过滤掉设备名称为null的设备
                    switch (device.getBluetoothClass().getMajorDeviceClass()) {
                        case BluetoothClass.Device.AUDIO_VIDEO_HEADPHONES://耳机
                        case BluetoothClass.Device.AUDIO_VIDEO_WEARABLE_HEADSET://穿戴式耳机
                        case BluetoothClass.Device.AUDIO_VIDEO_HANDSFREE://蓝牙耳机
                        case BluetoothClass.Device.Major.AUDIO_VIDEO://音频设备
                        case BluetoothClass.Device.AUDIO_VIDEO_CAR_AUDIO://车载设备
                        case BluetoothClass.Device.AUDIO_VIDEO_VIDEO_MONITOR://可穿戴设备
                            list.add(device);
                            updated = true;
                        default://其它
                            break;
                    }
                }
            }
        }

        //获取周围蓝牙设备
        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

        if (!list.contains(device) && device.getName() != null) {//防止重复添加 过滤掉设备名称为null的设备
            switch (device.getBluetoothClass().getMajorDeviceClass()) {
                case BluetoothClass.Device.AUDIO_VIDEO_HEADPHONES://耳机
                case BluetoothClass.Device.AUDIO_VIDEO_WEARABLE_HEADSET://穿戴式耳机
                case BluetoothClass.Device.AUDIO_VIDEO_HANDSFREE://蓝牙耳机
                case BluetoothClass.Device.Major.AUDIO_VIDEO://音频设备
                case BluetoothClass.Device.AUDIO_VIDEO_CAR_AUDIO://车载设备
                case BluetoothClass.Device.AUDIO_VIDEO_VIDEO_MONITOR://可穿戴设备
                    list.add(device);
                    updated = true;
                default://其它
                    break;
            }
        }
        Log.e(TAG, "getBondedDevice: " + list.toString());
        // 如果设备列表更新则重新显示设备
        if (updated) showDevices();
    }


    /**
     * 显示设备
     */
    private void showDevices() {
        FrameLayout rlayout_device = findViewById(R.id.rlayout_device);
        float minRadius = 350f;
        float maxRadius = rlayout_device.getWidth() / 2f - 100f;
        float maxAngle = 45f;
        float minAngle = 30f;
        float centerX = rlayout_device.getWidth() / 2f;
        float centerY = rlayout_device.getHeight() / 2f;
        double allAngle = 0.0;
        rlayout_device.removeAllViews();
        for (int i = 0; i < list.size(); i++) {

            int temp = i;

            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(new FrameLayout.LayoutParams(192, 192));
            imageView.setImageResource(R.drawable.ic_devices);

            double radius = Math.random() * (maxRadius - minRadius) + minRadius;
            double angle = Math.random() * (maxAngle - minAngle) + minAngle;
            allAngle += angle;
            imageView.setX((float) (centerX + radius * Math.cos(allAngle * Math.PI / 180)));
            imageView.setY((float) (centerY - radius * Math.sin(allAngle * Math.PI / 180)));
            imageView.setAlpha(0f);
            imageView.setVisibility(View.VISIBLE);
            imageView.animate().alpha(1f).setDuration(1000).setListener(null).setStartDelay((long) (Math.random() * 1000));
            imageView.setId(i);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BluetoothDevice bluetoothDevice = list.get(imageView.getId());
                    Log.e(TAG, "onClick: device: " + bluetoothDevice);
                    showDialog("连接到 " + bluetoothDevice.getName() + " ？", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //连接
                            connectDevice(bluetoothDevice);

                            Toast toast = Toast.makeText(MainActivity.this, "连接中...", Toast.LENGTH_LONG);
                            toast.show();

                            TimerUtil timerUtil = new TimerUtil(new TimerUtil.Run() {
                                @Override
                                public void run() {
                                    switch (list.get(temp).getBluetoothClass().getMajorDeviceClass()) {
                                        case BluetoothClass.Device.AUDIO_VIDEO_HEADPHONES://耳机
                                        case BluetoothClass.Device.AUDIO_VIDEO_WEARABLE_HEADSET://穿戴式耳机
                                        case BluetoothClass.Device.AUDIO_VIDEO_HANDSFREE://蓝牙耳机
                                        case BluetoothClass.Device.Major.AUDIO_VIDEO://音频设备
                                            imageView.setImageResource(R.drawable.ic_headset_connected);
                                            break;
                                        case BluetoothClass.Device.AUDIO_VIDEO_CAR_AUDIO://车载设备
                                            imageView.setImageResource(R.drawable.ic_car_connected);
                                            break;
                                        case BluetoothClass.Device.AUDIO_VIDEO_VIDEO_MONITOR://可穿戴设备
                                            imageView.setImageResource(R.drawable.ic_wearable_connected);
                                            break;
                                        default://其它
                                            imageView.setImageResource(R.mipmap.icon_bluetooth);
                                            break;
                                    }
                                    Looper.prepare();
                                    Toast.makeText(MainActivity.this, "连接成功", Toast.LENGTH_SHORT).show();
                                    Looper.loop();
                                }
                            }, 3500, 5000, true);
                            timerUtil.start();


                        }
                    });
                }

            });

            switch (list.get(i).getBluetoothClass().getMajorDeviceClass()) {
                case BluetoothClass.Device.AUDIO_VIDEO_HEADPHONES://耳机
                case BluetoothClass.Device.AUDIO_VIDEO_WEARABLE_HEADSET://穿戴式耳机
                case BluetoothClass.Device.AUDIO_VIDEO_HANDSFREE://蓝牙耳机
                case BluetoothClass.Device.Major.AUDIO_VIDEO://音频设备
                    imageView.setImageResource(R.drawable.ic_headset_normal);
                    break;
                case BluetoothClass.Device.AUDIO_VIDEO_CAR_AUDIO://车载设备
                    imageView.setImageResource(R.drawable.ic_car_normal);
                    break;
                case BluetoothClass.Device.AUDIO_VIDEO_VIDEO_MONITOR://可穿戴设备
                    imageView.setImageResource(R.drawable.ic_wearable_normal);
                    break;
                default://其它
                    imageView.setImageResource(R.mipmap.icon_bluetooth);
                    break;
            }
            rlayout_device.addView(imageView);
        }
    }


    /**
     * 连接设备
     *
     * @param bluetoothDevice 蓝牙设备对象
     */
    private void connectDevice(BluetoothDevice bluetoothDevice) {
        if (bluetoothAdapter == null) {
            Log.d(TAG, "connectDevice: BluetoothAdapter not initialized.");
            return;
        }
        if (!bluetoothAdapter.isEnabled()) {
            Log.d(TAG, "connectDevice: BluetoothAdapter is disabled");
            return;
        }
        if (bluetoothDevice == null) {
            Log.d(TAG, "connectDevice: Unspecified device.");
            return;
        }
        if (Build.VERSION.SDK_INT >= 23) {
            mBluetoothGatt = bluetoothDevice.connectGatt(getApplicationContext(), false, new GattCallback(), BluetoothDevice.TRANSPORT_LE);
        } else {
            mBluetoothGatt = bluetoothDevice.connectGatt(getApplicationContext(), false, new GattCallback());
        }
    }


    /**
     * 蓝牙连接回调类
     */
    private class GattCallback extends BluetoothGattCallback {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            Log.e(TAG, "onConnectionStateChange newstate:" + newState + " status:" + status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                isRetry = false;
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    Log.e(TAG, "onConnectionStateChange: STATE_CONNECTED");
                    mBluetoothGatt.discoverServices();
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    Log.e(TAG, "onConnectionStateChange: STATE_DISCONNECTED");
                    if (mBluetoothGatt != null) {
                        mBluetoothGatt.close();
                        mBluetoothGatt = null;
                    }
                }
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            Log.i(TAG, "onServicesDiscovered(), status = " + status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                //获取服务对象
                BluetoothGattService mDeviceInfoService = gatt.getService(DEVICE_INFO_SERVICE_UUID);
                if (mDeviceInfoService == null) {
                    Log.i(TAG, "Device Info Service is null ,disconnect GATT...");
                    gatt.disconnect();
                    gatt.close();
                    return;
                }
                //获取Characteristic对象
                mCharacteristic = mDeviceInfoService.getCharacteristic(CHARACTERISTIC_UUID);
                if (mCharacteristic == null) {
                    Log.e(TAG, "read mModelCharacteristic not found");
                } else {
                    //读取Characteristic特性
                    mBluetoothGatt.readCharacteristic(mCharacteristic);
                }
            } else {
                Log.i(TAG, "onServicesDiscovered status false");
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                String value = "";
                if (characteristic.getUuid().equals(CHARACTERISTIC_UUID)) {
                    //读取出characteristic的value值
                    value = new String(characteristic.getValue()).trim().replace(" ", "");
                    Log.i(TAG, "=====>读取到 value =" + value);
                    //此处为ascii表字符，需转换为十进制ascii值
                    //再将十进制ascii值，转换为十六进制
                    VID = changeAsciiTo16(value.charAt(0));
                    PID = changeAsciiTo16(value.charAt(value.length() - 1));
                }
            } else {
                Log.i(TAG, "onCharacteristicRead status wrong");
                if (mBluetoothGatt != null)
                    mBluetoothGatt.disconnect();
            }
        }

    }


    /**
     * Ascaii码转16进制字符串
     *
     * @param a 字符
     * @return 16进制字符串
     */
    private String changeAsciiTo16(char a) {
        Log.i(TAG, "change from a =" + a);
        String value = "";
        int val = (int) a;
        Log.i(TAG, "change to 10进制ASCII值 val =" + val);
        //ascii值到
        value = Integer.toHexString(val).toUpperCase();
        Log.i(TAG, "change to 16进制字符串 value =" + value);
        return value;
    }


    /**
     * 弹窗
     *
     * @param dialogTitle     标题
     * @param onClickListener 按钮的点击事件
     */
    private void showDialog(String dialogTitle, @NonNull DialogInterface.OnClickListener
            onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(dialogTitle);
        builder.setPositiveButton("确定", onClickListener);
        builder.setNegativeButton("取消", null);
        builder.create().show();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        navFragmentUtil = null;
        //卸载广播接收器
        unregisterReceiver(bluetoothReceiver);
        mBluetoothGatt.disconnect();
        mBluetoothGatt.close();
        mBluetoothGatt = null;
    }

}
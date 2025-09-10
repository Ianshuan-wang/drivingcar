package com.ujs.drivingapp.Activities;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Poi;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AimlessModeListener;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.ServiceSettings;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.ujs.drivingapp.Adapters.InfoWindowAdapter;
import com.ujs.drivingapp.Commen.Constants;
import com.ujs.drivingapp.Component.CircleProgress;
import com.ujs.drivingapp.Component.HintDialog;
import com.ujs.drivingapp.Pojo.Path;
import com.ujs.drivingapp.R;
import com.ujs.drivingapp.Room.PathDao;
import com.ujs.drivingapp.Room.PathDataBase;
import com.ujs.drivingapp.Utils.MapUtil;
import com.ujs.drivingapp.Utils.SoundUtil;
import com.ujs.drivingapp.Utils.StatusBarStyleUtil;
import com.ujs.drivingapp.Utils.TimerUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

public class DriveActivity extends AppCompatActivity {

    AMap aMap;
    AMapLocationClient aMapLocationClient;
    AMapNavi aMapNavi;
    PoiSearch poiSearch;
    Map<Marker, PoiItem> poiMarkers;
    InfoWindowAdapter infoWindowAdapter;
    Marker oldMarker = null;
    boolean infoWindowShown = true;

    MapView mapView;

    LinearLayout llayout_box;
    LinearLayout llayout_large;
    LinearLayout llayout_small;

    RelativeLayout rlayout_img;

    TextView tv_time;
    TextView tv_time2;
    TextView tv_distance1;
    TextView tv_distance2;
    TextView tv_speed;

    ImageView img_start;
    ImageView img_stop;
    ImageView img_cancel;
    ImageView img_lock;
    ImageView img_sound;

    CircleProgress circle_progress;

    int hour = 0;//记录计时器的小时
    int minute = 0;//记录计时器的分钟
    int second = 0;//记录计时器的秒钟
    Map<String, Map<String, String>> path;//点的集合
    double currentLatitude;//当前点的经度
    double currentLongitude;//当前点的纬度
    double distance;//移动的距离 米
    TimerUtil timerUtil;//计时器工具类
    boolean isMoved = false;//llayout_box是否移动
    boolean isLocked = false;//是否被锁定
    boolean isMuted = false;//是否静音
    double maxSpeed = 0;
    double minSpeed = 0;
    String startTime = "";//开始时间
    HintDialog.Builder builder;

    ValueAnimator valueAnimator;

    String poiType = Constants.Poi.TYPE_ALL;
    SoundUtil soundUtil;

    //初始化组件
    void initView() {
        llayout_box = findViewById(R.id.llayout_box);
        llayout_large = findViewById(R.id.llayout_large);
        llayout_small = findViewById(R.id.llayout_small);
        tv_time = findViewById(R.id.tv_time);
        tv_time2 = findViewById(R.id.tv_time2);
        img_start = findViewById(R.id.img_start);
        img_stop = findViewById(R.id.img_stop);
        img_cancel = findViewById(R.id.img_cancel);
        img_sound = findViewById(R.id.img_sound);
        img_lock = findViewById(R.id.img_lock);
        rlayout_img = findViewById(R.id.rlayout_img);
        mapView = findViewById(R.id.map);
        tv_distance1 = findViewById(R.id.tv_distance1);
        tv_distance2 = findViewById(R.id.tv_distance2);
        tv_speed = findViewById(R.id.tv_speed);
        circle_progress = findViewById(R.id.circle_progress);
    }

    //初始化定时器
    void initTimer() {
        timerUtil = new TimerUtil(() -> {
            second++;
            if (second == 60) {
                second = 0;
                minute++;
                if (minute == 60) {
                    hour++;
                }
            }
            String text = ((hour + "").length() == 1 ? ("0" + (hour + "")) : (hour + ""))
                    + ":"
                    + ((minute + "").length() == 1 ? ("0" + (minute + "")) : (minute + ""))
                    + ":"
                    + ((second + "").length() == 1 ? ("0" + (second + "")) : (second + ""));
            tv_time.post(() -> {
                tv_time.setText(text);
                tv_time2.setText(text);
                double t = hour + Double.parseDouble(minute + "") / 60 + Double.parseDouble(second + "") / 3600;
                double speed = t == 0 ? 0 : Double.parseDouble(String.format("%.2f", distance / 1000)) / t;
                tv_speed.setText(String.format("%.2f", speed));
            });
        }, 0, 1000);
    }


    /**
     * 以latLng为圆心以range为半径的圆内所有的poi点画上标记
     *
     * @param latLng 经纬度坐标
     * @param range  半径
     */
    private void addPoiMarker(LatLng latLng, int range) {
        poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(latLng.latitude,
                latLng.longitude), range));//设置周边搜索的中心点以及半径
        poiSearch.searchPOIAsyn();
    }

    void initDialog() {
        builder = new HintDialog.Builder(DriveActivity.this);
    }

    //初始化地图对象
    void initMap() {
        try {
            aMap = mapView.getMap();
            aMapNavi = AMapNavi.getInstance(getApplicationContext());
            path = new LinkedHashMap<>();
            poiMarkers = new HashMap<>();
            infoWindowAdapter = new InfoWindowAdapter(getApplicationContext());
            infoWindowAdapter.setOnInfoWindowListener(new InfoWindowAdapter.OnInfoWindowListener() {
                @Override
                public void initData(InfoWindowAdapter infoWindowAdapter) {
                    boolean b = new Random().nextBoolean();
                    infoWindowAdapter.setCollect(b);
                }

                @Override
                public void onClickCollect(InfoWindowAdapter infoWindowAdapter) {
                    infoWindowAdapter.setCollect(!infoWindowAdapter.getCollect());
                }

                @Override
                public void onClickNavi(InfoWindowAdapter infoWindowAdapter) {
                    PoiItem poiItem = infoWindowAdapter.getPoiItem();
                    MapUtil.startNavi(getApplicationContext(), new Poi(poiItem.getTitle(), new LatLng(poiItem.getLatLonPoint().getLatitude(), poiItem.getLatLonPoint().getLongitude()), poiItem.getPoiId()));
                }
            });
            //初始化定位蓝点
            MyLocationStyle locationStyle = new MyLocationStyle();
            locationStyle.interval(1000);
            locationStyle.showMyLocation(true);
            locationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);
            locationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.gps_point));
            aMap.setMyLocationStyle(locationStyle);
            aMap.getUiSettings().setMyLocationButtonEnabled(false);


            aMap.setMyLocationEnabled(true);//设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false
            aMap.moveCamera(CameraUpdateFactory.zoomTo(18));//  设置地图缩放级数
            aMapLocationClient = new AMapLocationClient(getApplicationContext());

            //初始化定位监听器 只定位一次 作用：刚进入界面时自身在地图中心点
            aMapLocationClient.setLocationOption(new AMapLocationClientOption().setOnceLocationLatest(true));
            AMapLocationListener aMapLocationListener = new AMapLocationListener() {
                @Override
                public void onLocationChanged(AMapLocation aMapLocation) {
                    if (aMapLocation.getErrorCode() == 0) {
                        aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude())));
                        aMapLocationClient.stopLocation();
                        aMapLocationClient.unRegisterLocationListener(this);
                    }
                }
            };
            aMapLocationClient.setLocationListener(aMapLocationListener);
            aMapLocationClient.startLocation();
            aMapLocationClient.setLocationOption(new AMapLocationClientOption().setOnceLocationLatest(false).setInterval(1000));

            //初始化定位监听器 执行画线和画标记
            aMapLocationClient.setLocationListener(aMapLocation -> {
                if (aMapLocation.getErrorCode() == 0) {
                    double latitude = aMapLocation.getLatitude();
                    double longitude = aMapLocation.getLongitude();
                    Log.e("initMap", "initMap: " + latitude + "  " + longitude);
                    double s = aMapLocation.getSpeed();
                    if (minSpeed == 0 && s != 0) {
                        minSpeed = s;
                    }
                    if (s < minSpeed) {
                        minSpeed = s;
                    } else if (s > maxSpeed) {
                        maxSpeed = s;
                    }
                    long time = aMapLocation.getTime();
                    Map<String, String> map = new HashMap<>();
                    map.put("latitude", latitude + "");
                    map.put("longitude", longitude + "");
                    addPoiMarker(new LatLng(latitude, longitude), 2000);
                    if (path.size() == 0) {
                        path.put(time + "", map);
                        currentLatitude = latitude;
                        currentLongitude = longitude;
                    } else if (!(latitude == currentLatitude && longitude == currentLongitude) && aMapLocation.getLocationType() != AMapLocation.LOCATION_TYPE_CELL) {
                        //画线
                        PolylineOptions polylineOptions = new PolylineOptions();
                        polylineOptions.width(14).color(Color.argb(255, 25, 198, 73));
                        LatLng currentLatLng = new LatLng(currentLatitude, currentLongitude);
                        LatLng latLng = new LatLng(latitude, longitude);
                        polylineOptions.add(currentLatLng, latLng);
                        aMap.addPolyline(polylineOptions);
                        Log.e("initMap", "initMap: s:" + s);

                        //计算距离 单位米
                        distance += AMapUtils.calculateLineDistance(currentLatLng, latLng);
                        tv_distance1.setText(String.format("%.2f", distance / 1000));
                        tv_distance2.setText(String.format("%.2f", distance / 1000));

                        path.put(time + "", map);
                        currentLatitude = latitude;
                        currentLongitude = longitude;
                    } else {
                        Log.e("initMap", "initMap: 相等");
                    }
                } else {
                    Log.e("onLocationChanged", "onLocationChanged: Error");
                    Log.e("initMap", "initMap: " + aMapLocation.getLocationDetail());
                }
            });

            //设置巡航回调
            aMapNavi.addAimlessModeListener(new AimlessModeListener() {
                // 巡航道路设施信息更新回调
                @Override
                public void onUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] infos) {
                    aMapNavi.readTrafficInfo(0);
                }

                // 巡航电子眼信息更新回调
                @Override
                public void onUpdateAimlessModeElecCameraInfo(AMapNaviTrafficFacilityInfo[] cameraInfo) {
                    aMapNavi.readTrafficInfo(0);
                }

                // 巡航统计信息更新回调
                @Override
                public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {
                    aMapNavi.readTrafficInfo(0);
                }

                // 巡航拥堵信息更新回调
                @Override
                public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {
                    aMapNavi.readTrafficInfo(0);
                }
            });
            aMapNavi.setUseInnerVoice(true, false);

            //设置Poi查询监听器
            PoiSearch.Query query = new PoiSearch.Query("", poiType);
            query.setPageSize(10);
            poiSearch = new PoiSearch(DriveActivity.this, query);
            poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
                @Override
                public void onPoiSearched(PoiResult poiResult, int i) {
                    ArrayList<PoiItem> pois = poiResult.getPois();

                    Map<Marker, PoiItem> m = new HashMap<>(poiMarkers);
                    Collection<PoiItem> values = m.values();
                    values.removeAll(pois);
                    for (PoiItem value : values) {
                        Marker key = getKey(poiMarkers, value);
                        if (key != null && !key.isInfoWindowShown()) {
                            key.remove();
                            poiMarkers.remove(key);
                        }
                    }

                    if (pois.size() > 0) {
                        for (PoiItem poiItem : pois) {
                            if (!poiMarkers.containsValue(poiItem)) {
                                LatLonPoint latLonPoint = poiItem.getLatLonPoint();
                                MarkerOptions markerOptions = new MarkerOptions();
                                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_poi_marker_default));
                                markerOptions.position(new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude()));
                                Marker marker = aMap.addMarker(markerOptions);
                                poiMarkers.put(marker, poiItem);
                            }
                        }
                        infoWindowAdapter.setPoiMarkers(poiMarkers);
                        aMap.setInfoWindowAdapter(infoWindowAdapter);
                    }
                }

                @Override
                public void onPoiItemSearched(PoiItem poiItem, int i) {

                }
            });


            //点击Marker标记弹出
            aMap.addOnMarkerClickListener(marker -> {
                Set<Marker> markers = poiMarkers.keySet();
                //如果marker不是定位小蓝点
                if (markers.contains(marker)) {
                    if (oldMarker != null) {
                        oldMarker.hideInfoWindow();
                        infoWindowShown = true;
                    }
                    oldMarker = marker;
                    oldMarker.showInfoWindow();
                }
                return false;
            });

            aMap.addOnMapClickListener(latLng -> {
                if (oldMarker != null) {
                    if (oldMarker.isInfoWindowShown() && infoWindowShown) {
                        infoWindowShown = false;
                    } else if (oldMarker.isInfoWindowShown() && !infoWindowShown) {
                        oldMarker.hideInfoWindow();
                    }
                }
            });

            aMap.addOnInfoWindowClickListener(marker -> {
                if (oldMarker != null) {
                    infoWindowShown = true;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive);
        StatusBarStyleUtil.changeStatusBarTextColor(getWindow(), true);
        Objects.requireNonNull(getSupportActionBar()).hide();//这行代码必须写在setContentView()方法的后面
        ServiceSettings.updatePrivacyShow(getApplicationContext(), true, true);
        ServiceSettings.updatePrivacyAgree(getApplicationContext(), true);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        window.setStatusBarColor(Color.TRANSPARENT);
        window.setNavigationBarColor(Color.TRANSPARENT);
        initView();
        initTimer();
        initDialog();
        initMap();
        mapView.onCreate(savedInstanceState);
        soundUtil = new SoundUtil();
        soundUtil.create(getApplicationContext(), R.raw.road_tip_001);
        TimerUtil timerUtil1 = new TimerUtil(() -> {
            soundUtil.start();
            TextView tv_exception = findViewById(R.id.tv_exception);
            tv_exception.post(() -> {
                tv_exception.setText("1");
            });
        }, 30000, 10000, true);
        timerUtil1.start();

        img_cancel.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (valueAnimator != null) {
                        valueAnimator.pause();
                    }
                    long duration = 1500;
                    valueAnimator = ValueAnimator.ofInt(0, 100);
                    valueAnimator.setDuration(duration);
                    valueAnimator.setInterpolator(new LinearInterpolator());
                    valueAnimator.setCurrentPlayTime(duration - duration / 100 * (100 - circle_progress.getProgress()));
                    valueAnimator.addUpdateListener(animation -> {
                        int progress = (int) animation.getAnimatedValue();
                        circle_progress.setProgress(progress);
                        if (progress == 100) {
                            if (hour == 0 && minute == 0 && second < 10) {
                                builder.setTitle("提示")
                                        .setMessage("本次行驶时间过短，是否结束驾驶？")
                                        .setCancelButton("继续驾驶", Dialog::cancel)
                                        .setOkButton("结束驾驶", dialog1 -> {
                                            finish();
                                            onBackPressed();
                                        })
                                        .setCanceledOnTouchOutside(false)
                                        .setCancelable(false)
                                        .build()
                                        .show();
                            } else {
                                builder.setTitle("提示")
                                        .setMessage("是否结束本次驾驶？")
                                        .setCancelButton("继续驾驶", Dialog::cancel)
                                        .setOkButton("结束驾驶", dialog1 -> {
                                            onEndDrive();
                                            finish();
                                            startActivity(new Intent("com.ujs.driving.app.SUMMARY"));
                                        })
                                        .setCanceledOnTouchOutside(false)
                                        .setCancelable(false)
                                        .build()
                                        .show();
                            }
                        }
                    });
                    valueAnimator.start();
                    break;
                case MotionEvent.ACTION_UP:
                    if (circle_progress.getProgress() != 100) {
                        valueAnimator.reverse();
                    } else {
                        circle_progress.setProgress(0);
                    }
                    break;
                default:
                    break;
            }
            return true;
        });
    }

    /**
     * 结束驾驶 将数据存入数据库中
     */
    public void onEndDrive() {
        PathDataBase dataBase = PathDataBase.getInstance(DriveActivity.this);
        PathDao pathDao = dataBase.pathDao();
        StringBuilder p = new StringBuilder();
        for (String timestamp : path.keySet()) {
            Map<String, String> map = path.get(timestamp);
            if (map != null) {
                p.append(map.get("latitude")).append(",").append(map.get("longitude")).append(",").append(timestamp).append(";");
            }
        }
        pathDao.deleteAll();
        pathDao.insert(new Path(null, p.toString(), startTime, hour * 3600L + minute * 60L + second, distance, maxSpeed, minSpeed, distance / 1000 / (hour + minute / 60 + second / 3600)));
    }

    public void onMoveView(View view) {
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, !isMoved ? llayout_large.getHeight() - llayout_small.getHeight() : llayout_small.getHeight() - llayout_large.getHeight());
        translateAnimation.setDuration(500);
        translateAnimation.setFillAfter(true);

        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (!isMoved) {
                    AlphaAnimation a1 = new AlphaAnimation(1, 0);
                    a1.setStartOffset(200);
                    a1.setFillAfter(true);
                    a1.setDuration(200);
                    a1.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            llayout_large.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    llayout_large.startAnimation(a1);
                    AlphaAnimation a2 = new AlphaAnimation(0, 1);
                    a2.setStartOffset(200);
                    a2.setDuration(200);
                    a2.setFillAfter(true);
                    a2.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            llayout_small.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    llayout_small.startAnimation(a2);
                } else {
                    AlphaAnimation a1 = new AlphaAnimation(1, 0);
                    a1.setStartOffset(200);
                    a1.setFillAfter(true);
                    a1.setDuration(200);
                    a1.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            llayout_small.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    llayout_small.startAnimation(a1);
                    AlphaAnimation a2 = new AlphaAnimation(0, 1);
                    a2.setStartOffset(200);
                    a2.setDuration(200);
                    a2.setFillAfter(true);
                    a2.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            llayout_large.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    llayout_large.startAnimation(a2);
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                TranslateAnimation ta = new TranslateAnimation(0, 0, 0, 0);
                ta.setDuration(1);
                llayout_box.startAnimation(ta);
                llayout_box.setTranslationY((llayout_large.getHeight() - llayout_small.getHeight()) * (isMoved ? 0 : 1));
                isMoved = !isMoved;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        llayout_box.startAnimation(translateAnimation);
    }

    //控制按钮是否可点击
    public void setClickable(boolean isClickable) {
        img_start.setClickable(isClickable);
        img_stop.setClickable(isClickable);
        img_cancel.setClickable(isClickable);
    }

    //点击开始按钮
    public void onStart(View view) {
        if (startTime.equals("")) {
            startTime = new SimpleDateFormat("yyyy年MM月dd日 HH:mm").format(new Date());
        }
        //播报电子眼和特殊路段
        aMapNavi.startAimlessMode(3);
        aMapLocationClient.startLocation();
        String time = (String) tv_time.getText();
        String[] times = time.split(":");
        hour = Integer.parseInt(times[0]);
        minute = Integer.parseInt(times[1]);
        second = Integer.parseInt(times[2]);
        timerUtil.start();


        img_start.clearAnimation();
        img_stop.clearAnimation();
        img_cancel.clearAnimation();
        rlayout_img.clearAnimation();
        float deltaX = (float) (img_cancel.getRight() - img_start.getRight()) / 2;
        long translateTime = 500;//位移动画持续时间
        //控制开始按钮的位移动画
        TranslateAnimation ta1 = new TranslateAnimation(0, deltaX, 0, 0);
        ta1.setDuration(translateTime);
        ta1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                setClickable(false);
                //控制取消按钮的位移动画
                TranslateAnimation ta2 = new TranslateAnimation(0, -deltaX, 0, 0);
                ta2.setDuration(translateTime);
                //控制开始和取消按钮的透明度动画
                AlphaAnimation aa = new AlphaAnimation(1, 0);
                aa.setDuration(500);
                aa.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        TranslateAnimation ta = new TranslateAnimation(0, 0, 0, 0);
                        ta.setDuration(1);
                        img_start.startAnimation(ta);
                        img_cancel.startAnimation(ta);
                        img_start.setTranslationX(deltaX);
                        img_cancel.setTranslationX(-deltaX);
                        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 0);
                        alphaAnimation.setDuration(1);
                        rlayout_img.startAnimation(alphaAnimation);
                        rlayout_img.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                //控制暂停按钮的显示动画
                AlphaAnimation aa3 = new AlphaAnimation(0, 1);
                aa3.setStartOffset(300);
                aa3.setDuration(translateTime - 200);
                aa3.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        img_stop.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                rlayout_img.startAnimation(aa);
                img_stop.startAnimation(aa3);
                img_cancel.startAnimation(ta2);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setClickable(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        img_start.startAnimation(ta1);
    }

    //点击暂停按钮
    public void onStop(View view) {
        timerUtil.stop();
        aMapNavi.stopAimlessMode();
        aMapLocationClient.stopLocation();
        img_start.clearAnimation();
        img_stop.clearAnimation();
        img_cancel.clearAnimation();
        rlayout_img.clearAnimation();
        float deltaX = img_start.getTranslationX();
        long translateTime = 500;//位移动画持续时间
        AlphaAnimation aa = new AlphaAnimation(1, 0);
        aa.setDuration(200);
        aa.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                setClickable(false);
                //控制开始按钮的位移动画
                TranslateAnimation ta1 = new TranslateAnimation(0, -deltaX, 0, 0);
                ta1.setDuration(translateTime);
                ta1.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        //控制取消按钮的位移动画
                        TranslateAnimation ta2 = new TranslateAnimation(0, deltaX, 0, 0);
                        ta2.setDuration(translateTime);
                        img_cancel.startAnimation(ta2);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        TranslateAnimation ta = new TranslateAnimation(0, 0, 0, 0);
                        ta.setDuration(1);
                        img_start.startAnimation(ta);
                        img_cancel.startAnimation(ta);
                        img_start.setTranslationX(0);
                        img_cancel.setTranslationX(0);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                AlphaAnimation aa = new AlphaAnimation(0, 1);
                aa.setDuration(500);
                rlayout_img.startAnimation(aa);
                img_start.startAnimation(ta1);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                img_stop.setVisibility(View.INVISIBLE);
                rlayout_img.setVisibility(View.VISIBLE);
                setClickable(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        img_stop.startAnimation(aa);
    }

    public void onCheckLock(View view) {
        isLocked = !isLocked;
        ScaleAnimation sa = new ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(200);
        sa.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                img_lock.setClickable(false);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                img_lock.setClickable(true);
                if (isLocked) {
                    //被锁定了
                    img_lock.setImageResource(R.drawable.bg_lock);
                    setClickable(false);
                    img_cancel.setEnabled(false);
                } else {
                    img_lock.setImageResource(R.drawable.bg_unlock);
                    setClickable(true);
                    img_cancel.setEnabled(true);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        img_lock.startAnimation(sa);
    }

    public void onCheckSound(View view) {
        isMuted = !isMuted;
        ScaleAnimation sa = new ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(200);
        sa.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                img_sound.setClickable(false);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                img_sound.setClickable(true);
                if (isMuted) {
                    //被静音了
                    img_sound.setImageResource(R.drawable.bg_mute);
                } else {
                    img_sound.setImageResource(R.drawable.bg_sound);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        img_sound.startAnimation(sa);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (hour == 0 && minute == 0 && second < 10) {
                builder.setTitle("提示")
                        .setMessage("本次行驶时间过短，是否结束驾驶？")
                        .setCancelButton("继续驾驶", Dialog::cancel)
                        .setOkButton("结束驾驶", dialog1 -> {
                            finish();
                            onBackPressed();
                        })
                        .setCanceledOnTouchOutside(false)
                        .setCancelable(false)
                        .build()
                        .show();
            } else {
                builder.setTitle("提示")
                        .setMessage("是否结束本次驾驶？")
                        .setCancelButton("继续驾驶", Dialog::cancel)
                        .setOkButton("结束驾驶", dialog1 -> {
                            onEndDrive();
                            finish();
                            startActivity(new Intent("com.ujs.driving.app.SUMMARY"));
                        })
                        .setCanceledOnTouchOutside(false)
                        .setCancelable(false)
                        .build()
                        .show();
            }
            return true;//屏蔽后退键
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        aMapLocationClient.onDestroy();
        timerUtil.destroy();
        soundUtil.stop();
        soundUtil.release();
    }


    private Marker getKey(Map<Marker, PoiItem> map, PoiItem item) {
        for (Marker marker : map.keySet()) {
            if (map.get(marker).equals(item)) {
                return marker;
            }
        }
        return null;
    }
}
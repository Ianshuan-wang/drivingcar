package com.ujs.drivingapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.ujs.drivingapp.R;
import com.ujs.drivingapp.Utils.FullScreenUtil;
import com.ujs.drivingapp.Utils.StatusBarStyleUtil;

import java.util.Objects;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

public class ScanActivity extends AppCompatActivity {

    private ZXingView zXingView;
    private RxPermissions rxPermissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
//        StatusBarStyleUtil.changeStatusBarTextColor(getWindow(), true);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();//这行代码必须写在setContentView()方法的后面
        FullScreenUtil.setFullScreen(this);
        checkVersion();
    }

    private void initScan() {
        zXingView = findViewById(R.id.zxing_view);
        //扫二维码
        zXingView.changeToScanQRCodeStyle();
        zXingView.startSpot();
        zXingView.setDelegate(new QRCodeView.Delegate() {
            @Override
            public void onScanQRCodeSuccess(String result) {
                //扫描得到结果震动一下表示
                Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vibrator.vibrate(200);
                //获取结果后三秒后，重新开始扫描
                new Handler().postDelayed(zXingView::startSpot, 3000);
            }

            @Override
            public void onScanQRCodeOpenCameraError() {
                showMsg("打开相机错误,请返回重试~", Toast.LENGTH_SHORT);
            }
        });
    }

    /**
     * 检查Android版本
     */
    private void checkVersion() {
        if (Build.VERSION.SDK_INT >= 23) {//6.0或6.0以上
            permissionsRequest();//动态权限申请
        } else {//6.0以下
            initScan();//初始化蓝牙配置
        }
    }

    /**
     * 动态权限申请
     */
    @SuppressLint("CheckResult")
    private void permissionsRequest() {//使用这个框架使用了Lambda表达式，设置JDK版本为 1.8或者更高
        rxPermissions = new RxPermissions(this);//实例化这个权限请求框架，否则会报错
        rxPermissions.request(
                Manifest.permission.CAMERA,
                Manifest.permission.VIBRATE)
                .subscribe(granted -> {
                    if (granted) {//申请成功
                        initScan();
                    } else {//申请失败
                        showMsg("权限未开启", Toast.LENGTH_SHORT);
                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        zXingView.stopCamera();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        zXingView.stopCamera();
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
}
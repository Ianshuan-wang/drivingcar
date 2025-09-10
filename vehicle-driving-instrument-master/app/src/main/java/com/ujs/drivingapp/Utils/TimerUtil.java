package com.ujs.drivingapp.Utils;

import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author huanqiu
 * @email 2364521714@qq.com
 * @create time 06/02/2022 下午 8:25
 * @description 计时器工具类
 */
public class TimerUtil {

    private static final String TAG = "TimerUtil";
    private Run run;

    private long time;
    private long delayTime;

    private Timer timer;

    private Boolean isOnce=false;

    public interface Run {
        void run();
    }

    /**
     * @param run       每time毫秒执行的回调函数
     * @param delayTime 第一次执行的延时 单位毫秒
     * @param time      每两次执行的时间间隔 单位毫秒
     */
    public TimerUtil(Run run, long delayTime, long time) {
        this.run = run;
        this.time = time;
        this.delayTime = delayTime;
    }

    /**
     *
     * @param run       每time毫秒执行的回调函数
     * @param delayTime 第一次执行的延时 单位毫秒
     * @param time      每两次执行的时间间隔 单位毫秒
     * @param isOnce    是否只执行一次
     */
    public TimerUtil(Run run,long delayTime,long time,Boolean isOnce){
        this.run = run;
        this.time = time;
        this.delayTime = delayTime;
        this.isOnce=isOnce;
    }

    /**
     * 开始执行回调
     */
    public void start() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                run.run();
                if (isOnce){
                    destroy();
                }
            }
        }, delayTime, time);
    }

    /**
     * 停止执行回调
     */
    public void stop() {
        timer.cancel();
    }

    /**
     * 销毁
     */
    public void destroy() {
        if (timer != null) {
            timer.cancel();
            run = null;
            timer = null;
        } else {
            Log.e(TAG, "destroy: timer is already null");
        }

    }

}

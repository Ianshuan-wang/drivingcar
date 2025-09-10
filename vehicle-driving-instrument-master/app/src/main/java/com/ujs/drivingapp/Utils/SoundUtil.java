package com.ujs.drivingapp.Utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

/**
 * @author huanqiu
 * @email 2364521714@qq.com
 * @create time 10/02/2022 下午 9:16
 * @description
 */
public class SoundUtil {

    private MediaPlayer mediaPlayer;

    /**
     * 默认播放完后立即释放
     *
     * @param context Context对象
     * @param resId   音频文件
     * @return SoundUtil对象
     */
    public SoundUtil create(Context context, int resId) {
        mediaPlayer = MediaPlayer.create(context, resId);
        mediaPlayer.setOnCompletionListener(mp -> {
            Log.e("create", "create: 已释放");
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        });
        return this;
    }

    /**
     * 开始播放
     */
    public void start(){
        if (mediaPlayer!=null){
            mediaPlayer.start();
        }
    }


    /**
     * @param context            Context对象
     * @param resId              音频文件
     * @param isReleaseAfterPlay 播放完后是否立即释放
     * @return SoundUtil对象
     */
    public SoundUtil create(Context context, int resId, boolean isReleaseAfterPlay) {
        mediaPlayer = MediaPlayer.create(context, resId);
        if (isReleaseAfterPlay) {
            mediaPlayer.setOnCompletionListener(mp -> {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            });
        }
        return this;
    }

    public void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    public void release() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }
}

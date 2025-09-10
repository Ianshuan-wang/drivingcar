package com.ujs.drivingapp.Component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.ujs.drivingapp.R;

/**
 * @author HWH.
 * @email huangwenhuan2019@outlook.com
 * @create time 2/16/2022 3:23 PM.
 * @description 加载 view$
 */
public class LoadingView extends LinearLayout {

    private static final String TAG = "LoadingView";
    private final View inflate;
    private ImageView mImageView;
    private AnimatedVectorDrawable mAnimatedVectorDrawable;

    public LoadingView(Context context) {
        this(context, null, 0);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflate = inflater.inflate(R.layout.loading_layout, this);
        mImageView = inflate.findViewById(R.id.img_loading);
        mAnimatedVectorDrawable = (AnimatedVectorDrawable) getContext().getDrawable(R.drawable.heart_vector_animator);//得到对应的AnimatedVectorDrawable对象
        mImageView.setImageDrawable(mAnimatedVectorDrawable);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

    /**
     * 开始单次动画
     */
    public void startAnime(Animatable2.AnimationCallback callback) {
        if (mAnimatedVectorDrawable != null) {
            mAnimatedVectorDrawable.start();
            inflate.setVisibility(VISIBLE);
            mAnimatedVectorDrawable.registerAnimationCallback(callback);
        } else {
            Log.e(TAG, "startAnime: mAnimatedVectorDrawable is null");
        }
    }

    /**
     * 开始循环动画
     */
    public void startLoopAnime() {
        if (mAnimatedVectorDrawable != null) {
            inflate.setVisibility(VISIBLE);
            mAnimatedVectorDrawable.start();
            mAnimatedVectorDrawable.registerAnimationCallback(new Animatable2.AnimationCallback() {
                @Override
                public void onAnimationEnd(Drawable drawable) {
                    super.onAnimationEnd(drawable);
                    mAnimatedVectorDrawable.start();
                }
            });
        } else {
            Log.e(TAG, "startAnime: mAnimatedVectorDrawable is null");
        }
    }


    /**
     * 停止动画
     */
    public void stopAnime() {
        if (mAnimatedVectorDrawable != null) {
            inflate.setVisibility(GONE);
        } else {
            Log.e(TAG, "startAnime: mAnimatedVectorDrawable is null");
        }
    }

}

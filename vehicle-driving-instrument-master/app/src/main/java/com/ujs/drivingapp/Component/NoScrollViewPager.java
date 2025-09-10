package com.ujs.drivingapp.Component;

/**
 * @author HWH.
 * @email huangwenhuan2019@outlook.com
 * @create time 1/27/2022 8:25 PM.
 * @description 自定义无滑动的 ViewPager
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import org.jetbrains.annotations.NotNull;

public class NoScrollViewPager extends ViewPager {
    public NoScrollViewPager(@NonNull @NotNull Context context) {
        super(context);
    }

    public NoScrollViewPager(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }
}

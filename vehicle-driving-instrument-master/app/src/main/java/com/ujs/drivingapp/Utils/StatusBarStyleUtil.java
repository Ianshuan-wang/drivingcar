package com.ujs.drivingapp.Utils;

import android.os.Build;
import android.view.View;
import android.view.Window;

/**
 * @author HWH.
 * @email huangwenhuan2019@outlook.com
 * @create time 1/28/2022 4:53 PM.
 * @description 状态栏样式修改工具类
 */
public class StatusBarStyleUtil {
    public static void changeStatusBarTextColor(Window window, boolean isBlack) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = window.getDecorView();
            int flags = 0;
            if (isBlack) {
                flags = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            } else {
                flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            }
            decor.setSystemUiVisibility(flags);
        }
    }
}

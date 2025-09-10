package com.ujs.drivingapp.Utils;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * @author huanqiu
 * @email 2364521714@qq.com
 * @create time 14/02/2022 下午 10:27
 * @description
 */
public class MyUtil {

    /**
     * 隐藏小键盘
     * @param context 上下文对象
     * @param editText EditText对象
     */
    public static void hideSoftInputFromWindow(Context context, EditText editText){
        InputMethodManager service = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
        service.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        editText.clearFocus();
    }

    /**
     * 自动获取焦点并弹出小键盘
     * @param context 上下文对象
     * @param editText EditText对象
     */
    public static void showSoftInput(Context context,EditText editText){
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        InputMethodManager inputManager =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editText, 0);
    }
}

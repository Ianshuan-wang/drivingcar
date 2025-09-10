package com.ujs.drivingapp.Component;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ujs.drivingapp.R;

/**
 * @author huanqiu
 * @email 2364521714@qq.com
 * @create time 09/02/2022 下午 9:58
 * @description
 */
public class HintDialog extends Dialog {
    public HintDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);

    }

    public interface OnClickListener {
        void onClick(HintDialog dialog);
    }

    public static class Builder {
        private ImageView divide_line;
        private TextView tv_title;
        private TextView tv_message;
        private TextView btn_cancel;
        private TextView btn_ok;
        private View layout;

        private HintDialog dialog;

        OnClickListener cancelBtnListener;
        OnClickListener okBtnListener;

        public Builder(Context context) {
            //初始化
            dialog = new HintDialog(context, R.style.Theme_AppCompat_Dialog);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = inflater.inflate(R.layout.dialog_hint, null, false);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            tv_title = layout.findViewById(R.id.tv_title);
            tv_message = layout.findViewById(R.id.tv_message);
            btn_cancel = layout.findViewById(R.id.btn_cancel);
            btn_ok = layout.findViewById(R.id.btn_ok);
            divide_line = layout.findViewById(R.id.img_divide_line_1);
            divide_line.setSelected(true);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }


        /**
         * 设置标题
         *
         * @param title 标题文字
         * @return 返回Builder自身对象
         */
        public Builder setTitle(String title) {
            tv_title.setText(title);
            return this;
        }

        /**
         * 设置信息
         *
         * @param message 信息文字
         * @return 返回Builder自身对象
         */
        public Builder setMessage(String message) {
            tv_message.setText(message);
            return this;
        }

        /**
         * 设置取消按钮文本和监听器
         *
         * @param text     按钮文字
         * @param listener 监听器
         * @return 返回Builder自身对象
         */
        public Builder setCancelButton(String text, OnClickListener listener) {
            btn_cancel.setText(text);
            cancelBtnListener = listener;
            return this;
        }

        /**
         * 设置确认按钮文本和监听器
         *
         * @param text     按钮文字
         * @param listener 监听器
         * @return 返回Builder自身对象
         */
        public Builder setOkButton(String text, OnClickListener listener) {
            btn_ok.setText(text);
            okBtnListener = listener;
            return this;
        }

        /**
         * 设置点击后退键是否可关闭dialog
         *
         * @param cancelable 是否可关闭dialog
         * @return 返回Builder自身对象
         */
        public Builder setCancelable(boolean cancelable) {
            dialog.setCancelable(cancelable);
            return this;
        }

        /**
         * 设置点击外部是否可关闭dialog
         *
         * @param canceledOnTouchOutside 是否可关闭dialog
         * @return 返回Builder自身对象
         */
        public Builder setCanceledOnTouchOutside(boolean canceledOnTouchOutside) {
            dialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
            return this;
        }

        /**
         * 创建dialog对象
         *
         * @return DriverDialog对象
         */
        public HintDialog build() {
            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancelBtnListener.onClick(dialog);
                }
            });
            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    okBtnListener.onClick(dialog);
                }
            });
            dialog.setContentView(layout);
            return dialog;
        }
    }
}

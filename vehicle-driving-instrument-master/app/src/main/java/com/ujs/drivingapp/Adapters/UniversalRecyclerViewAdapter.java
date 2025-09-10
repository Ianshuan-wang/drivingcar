package com.ujs.drivingapp.Adapters;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author HWH.
 * @email huangwenhuan2019@outlook.com
 * @create time 1/29/2022 3:25 PM.
 * @description 通用 RecyclerView 适配器$
 */
public abstract class UniversalRecyclerViewAdapter<T> extends RecyclerView.Adapter<UniversalRecyclerViewAdapter.RecyclerViewHolder> {
    private List<T> mData;
    private int mLayoutRes;           //布局id

    public UniversalRecyclerViewAdapter(List<T> mData, int mLayoutRes) {
        this.mData = mData;
        this.mLayoutRes = mLayoutRes;
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new RecyclerViewHolder(LayoutInflater.from(parent.getContext()).inflate(mLayoutRes, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerViewHolder holder, int position) {
        bindView(holder, mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public abstract void bindView(RecyclerViewHolder holder, T obj);


    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {


        private SparseArray<View> mViews;
        private View itemView;
        private Context context;

        public Context getContext() {
            return context;
        }

        public void setContext(Context context) {
            this.context = context;
        }

        public RecyclerViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            mViews = new SparseArray<>();
            this.itemView = itemView;
            context = this.itemView.getContext();
        }

        @SuppressWarnings("unchecked")
        public <T extends View> T getView(int id) {
            T t = (T) mViews.get(id);
            if (t == null) {
                t = (T) itemView.findViewById(id);
                mViews.put(id, t);
            }
            return t;
        }

        public void setText(int id, CharSequence text) {
            View view = getView(id);
            if (view instanceof TextView) {
                ((TextView) view).setText(text);
            }
        }

        public void setDrawableRes(int id, int drawableRes) {
            View view = getView(id);
            if (view instanceof ImageView) {
                ((ImageView) view).setImageResource(drawableRes);
            } else {
                view.setBackgroundResource(drawableRes);
            }
        }

        /**
         * 设置布局内控件点击监听
         */
        public void setOnClickListener(int id, View.OnClickListener listener) {
            getView(id).setOnClickListener(listener);
        }

        /**
         * 设置布局点击监听
         */
        public void setOnClickListener(View.OnClickListener listener) {
            itemView.setOnClickListener(listener);
        }


        /**
         * 设置布局长按监听
         *
         * @param listener 长按监听器对象
         */
        public void setOnLongClickListener(View.OnLongClickListener listener) {
            itemView.setOnLongClickListener(listener);
        }

    }


}

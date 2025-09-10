package com.ujs.drivingapp.Adapters;


/**
 * @author HWH.
 * @email huangwenhuan2019@outlook.com
 * @create time 1/27/2022 8:42 PM.
 * @description 主界面 导航按钮 适配器
 */

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ujs.drivingapp.Pojo.NavItem;
import com.ujs.drivingapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class NavRecyclerViewAdapter extends RecyclerView.Adapter<NavRecyclerViewAdapter.NavViewHolder> {

    List<NavItem> navItemList= new ArrayList<>();

    public NavRecyclerViewAdapter(List<NavItem> navItemList) {
        this.navItemList = navItemList;
    }

    @NonNull
    @NotNull
    @Override
    public NavViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new NavViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.nav_item, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull @NotNull NavRecyclerViewAdapter.NavViewHolder holder, int position) {
        holder.imageView.setImageResource(navItemList.get(position).getIc_nav());
        holder.textView.setText(navItemList.get(position).getName_nav());

        if (navItemList.get(position).getIc_nav() == R.drawable.ic_profile_selected){

            holder.textView.setTextColor(Color.parseColor("#F08000"));
        }else{
            holder.textView.setTextColor(Color.parseColor("#7A7E83"));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(position);
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return navItemList.isEmpty()?0:navItemList.size();
    }

    static class NavViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;

        public NavViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.nav_icon);
            textView = itemView.findViewById(R.id.nav_name);
        }
    }


    //第一步 定义接口
    public interface OnNavClickListener {
        void onClick(int position);
    }

    private OnNavClickListener listener;

    //第二步， 写一个公共的方法
    public void setOnNavClickListener(OnNavClickListener listener) {
        this.listener = listener;
    }
}

package com.ujs.drivingapp.Utils;

/**
 * @author HWH.
 * @email huangwenhuan2019@outlook.com
 * @create time 1/27/2022 8:33 PM.
 * @description 首页+底部导航创建工具类
 */

import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.ujs.drivingapp.Adapters.MainFragmentAdapter;
import com.ujs.drivingapp.Adapters.NavRecyclerViewAdapter;
import com.ujs.drivingapp.Adapters.UniversalRecyclerViewAdapter;
import com.ujs.drivingapp.Pojo.NavItem;
import com.ujs.drivingapp.R;

import java.util.ArrayList;

public class NavFragmentUtil {
    private final static String TAG="NavUtil";
    private ViewPager2 viewPager;
    private RecyclerView recyclerView;
    private ArrayList<Fragment> fragments =new ArrayList<>();// 存放Home和Profile页面
    private ArrayList<NavItem> navItems = new ArrayList<>();
    private final AppCompatActivity activity;

    private static NavFragmentUtil navFragmentUtil = null;

    /**
     *
     * @param activity 继承自 AppCompatActivity 类的对象
     * @param fragments 所有的 fragment
     * @param navItems  与 fragment 对应的 navItem, navItem 为 NavItem 类对象
     */
    private NavFragmentUtil(AppCompatActivity activity, ArrayList<Fragment> fragments, ArrayList<NavItem> navItems) {
        this.fragments = fragments;
        this.navItems = navItems;
        this.activity = activity;
    }

    public static synchronized NavFragmentUtil getInstance(AppCompatActivity activity, ArrayList<Fragment> fragments, ArrayList<NavItem> navItems){
        if(navFragmentUtil == null){
            navFragmentUtil = new NavFragmentUtil(activity,fragments,navItems);
        }
        return navFragmentUtil;
    }

    public void initNavFragment(){
        initViewPager();
        initNav();
    }


    private void initNav() {

        recyclerView = activity.findViewById(R.id.rv_nav);

        // 设置列数为 navItems的个数
        recyclerView.setLayoutManager(new GridLayoutManager(activity,navItems.size()));
        NavRecyclerViewAdapter adapter = new NavRecyclerViewAdapter(navItems);
        Handler handler = new Handler(Looper.getMainLooper());
        adapter.setOnNavClickListener(new NavRecyclerViewAdapter.OnNavClickListener() {
            @Override
            public void onClick(int position) {
                Log.e(TAG, "onNavClick: "+position);
                handler.postDelayed(() -> {
                    for(int i=0;i<navItems.size();i++){
                        ImageView imageView = recyclerView.getChildAt(i).findViewById(R.id.nav_icon);
                        TextView textView = recyclerView.getChildAt(i).findViewById(R.id.nav_name);
                        if(i!=position){
                            imageView.setSelected(false);
                            textView.setTextColor(Color.parseColor("#7A7E83"));
                        }else{
                            imageView.setSelected(true);
                            textView.setTextColor(Color.parseColor("#F08000"));
                        }
                    }
                    viewPager.setCurrentItem(position,true);
                },200);

            }
        });
        recyclerView.setAdapter(adapter);

    }

    private void initViewPager() {

        viewPager = activity.findViewById(R.id.pager_main);

        viewPager.setAdapter(new MainFragmentAdapter(activity.getSupportFragmentManager(),activity.getLifecycle(),fragments));

        Handler handler = new Handler(Looper.getMainLooper());
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Log.e(TAG, "onPageSelected: "+position);
                handler.postDelayed(() -> {
                    for(int i=0;i<navItems.size();i++){
                        ImageView imageView = recyclerView.getChildAt(i).findViewById(R.id.nav_icon);
                        TextView textView = recyclerView.getChildAt(i).findViewById(R.id.nav_name);
                        if(i!=position){
                            imageView.setSelected(false);
                            textView.setTextColor(Color.parseColor("#7A7E83"));
                        }else{
                            imageView.setSelected(true);
                            textView.setTextColor(Color.parseColor("#F08000"));
                        }
                    }

                },200);

            }
        });

    }

}

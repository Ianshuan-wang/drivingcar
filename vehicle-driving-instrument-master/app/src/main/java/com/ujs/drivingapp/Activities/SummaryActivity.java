package com.ujs.drivingapp.Activities;

/**
 * @author HWH.
 * @email huangwenhuan2019@outlook.com
 * @create time 1/27/2022 8:29 PM.
 * @description 汇总页面Activity, 包括 轨迹、图表、详情 三个页面，由 TabLayout+ViewPager编写而成，
 * 其中 ViewPager 对象实际采用的是 NoScrollViewPager 自定义无滑动 ViewPager
 */

import android.graphics.drawable.Animatable2;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.ujs.drivingapp.Component.LoadingView;
import com.ujs.drivingapp.Fragments.ChartFragment;
import com.ujs.drivingapp.Fragments.DetailFragment;
import com.ujs.drivingapp.Fragments.MapFragment;
import com.ujs.drivingapp.R;
import com.ujs.drivingapp.Utils.StatusBarStyleUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SummaryActivity extends AppCompatActivity {

    private static final String TAG = "SummaryActivity";
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private final String[] tabs = {"轨迹", "图表", "详情"};
    private List<Fragment> fragments = new ArrayList<>();
    private LoadingView loading_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getColor(R.color.white));
        setContentView(R.layout.activity_summary);
        StatusBarStyleUtil.changeStatusBarTextColor(getWindow(), true);
        Objects.requireNonNull(getSupportActionBar()).hide();//这行代码必须写在setContentView()方法的后面
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.pager_summary);
        loading_view = findViewById(R.id.loading_view);
        loading_view.startAnime(new Animatable2.AnimationCallback() {
            @Override
            public void onAnimationEnd(Drawable drawable) {
                super.onAnimationEnd(drawable);
                loading_view.stopAnime();

                for (int i = 0; i < tabs.length; i++) {
                    tabLayout.addTab(tabLayout.newTab().setText(tabs[i]));
                }

                String statisticId = getIntent().getStringExtra("statisticId");
                Log.e(TAG, "onCreate: " + statisticId);
                Bundle sendBundle = new Bundle();
                sendBundle.putString("statisticId", statisticId);

                MapFragment mapFragment = MapFragment.newInstance();
                mapFragment.setArguments(sendBundle);

                ChartFragment chartFragment = ChartFragment.newInstance();
                chartFragment.setArguments(sendBundle);

                DetailFragment detailFragment = DetailFragment.newInstance();
                detailFragment.setArguments(sendBundle);

                fragments.add(mapFragment);
                fragments.add(chartFragment);
                fragments.add(detailFragment);

                viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
                    @NonNull
                    @Override
                    public Fragment getItem(int position) {
                        return fragments.get(position);
                    }

                    @Override
                    public int getCount() {
                        return fragments.size();
                    }

                    @Nullable
                    @Override
                    public CharSequence getPageTitle(int position) {
                        return tabs[position];
                    }
                });

                //设置TabLayout和ViewPager联动
                tabLayout.setupWithViewPager(viewPager, false);


            }
        });

    }

    public void onNavBack(View view) {
        onBackPressed();
    }
}
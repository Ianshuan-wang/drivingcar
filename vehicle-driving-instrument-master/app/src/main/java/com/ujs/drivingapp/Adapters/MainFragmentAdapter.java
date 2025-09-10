package com.ujs.drivingapp.Adapters;

/**
 * @author HWH.
 * @email huangwenhuan2019@outlook.com
 * @create time 1/27/2022 8:41 PM.
 * @description 主界面 Fragment 适配器
 */

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainFragmentAdapter extends FragmentStateAdapter {
    List<Fragment> fragmentList= new ArrayList<>();
    public MainFragmentAdapter(@NonNull @org.jetbrains.annotations.NotNull FragmentManager fragmentManager,
                               @NonNull @org.jetbrains.annotations.NotNull Lifecycle lifecycle,
                               List<Fragment> fragments) {
        super(fragmentManager, lifecycle);
        fragmentList = fragments;
    }

    @NonNull
    @org.jetbrains.annotations.NotNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getItemCount() {
        return fragmentList.isEmpty()?0:fragmentList.size();
    }
}

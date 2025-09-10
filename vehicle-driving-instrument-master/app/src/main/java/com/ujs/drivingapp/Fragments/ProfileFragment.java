package com.ujs.drivingapp.Fragments;

/**
 * @author HWH.
 * @email huangwenhuan2019@outlook.com
 * @create time 1/27/2022 8:35 PM.
 * @description 我的 页面
 */

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.ujs.drivingapp.Adapters.UniversalAdapter;
import com.ujs.drivingapp.Adapters.UniversalRecyclerViewAdapter;
import com.ujs.drivingapp.Pojo.Option;
import com.ujs.drivingapp.Pojo.Permission;
import com.ujs.drivingapp.R;
import com.ujs.drivingapp.Utils.TimerUtil;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private List<Permission> permissions;
    private List<Option> options = new ArrayList<>();
    private ArrayList<Option> more = new ArrayList<>();
    private final String TAG = "ProfileFragment";
    TimerUtil timerUtil;
    TextView tv_current_time;


    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        permissions = new ArrayList<>();
        permissions.add(new Permission("地图装扮", "换定位标"));
        permissions.add(new Permission("电影通用券", "满8.1-8"));
        permissions.add(new Permission("免费抽奖", "赢iPhone13"));
        permissions.add(new Permission("洗车通用券", "5元代金券"));

        options.add(new Option("收藏", R.drawable.ic_collect, "COLLECT"));
        options.add(new Option("足迹", R.drawable.ic_track, "FOOTER"));
        options.add(new Option("签到", R.drawable.ic_punch, "PUNCH"));
        options.add(new Option("卡券", R.drawable.ic_coupon));
        options.add(new Option("资料", R.drawable.ic_archives, "ARCHIVES"));

        more.add(new Option("帮助", R.drawable.ic_help));
        more.add(new Option("建议", R.drawable.ic_advice, "ADVICE"));
        more.add(new Option("邀请", R.drawable.ic_invite, "INVITE"));
        more.add(new Option("客服", R.drawable.ic_cs));
        more.add(new Option("设置", R.drawable.ic_setting, "SETTING"));
        more.add(new Option("更新", R.drawable.ic_update, "UPDATE"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        tv_current_time = requireView().findViewById(R.id.tv_current_time);
        RecyclerView permissionsView = requireView().findViewById(R.id.rv_permissions);
        permissionsView.setLayoutManager(new GridLayoutManager(requireContext(), 4));
        permissionsView.setAdapter(new UniversalRecyclerViewAdapter<Permission>(permissions, R.layout.permission_item) {
            @Override
            public void bindView(RecyclerViewHolder holder, Permission obj) {
                holder.setText(R.id.tv_permission_name, obj.getName());
                holder.setText(R.id.tv_permission_detail, obj.getDetail());
            }
        });


        timerUtil = new TimerUtil(() -> {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            tv_current_time.post(() -> {
                tv_current_time.setText(sdf.format(new Date()));
            });
        }, 0, 1000);
        timerUtil.start();

        RecyclerView optionsView = requireView().findViewById(R.id.rv_profile_options);
        optionsView.setLayoutManager(new GridLayoutManager(requireContext(), 5));
        optionsView.setAdapter(new UniversalRecyclerViewAdapter<Option>(options, R.layout.profile_option_item) {

            @Override
            public void bindView(RecyclerViewHolder holder, Option obj) {
                holder.setDrawableRes(R.id.img_profile_option_icon, obj.getIcon());
                holder.setText(R.id.tv_profile_option_name, obj.getName());
                holder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.e(TAG, "onClick: name: " + obj.getName() + "action: " + obj.getAction());
                        if (obj.getAction() != null) {
                            Intent intent = new Intent("com.ujs.driving.app." + obj.getAction());
                            startActivity(intent);
                        }

                    }
                });
            }
        });

        GridView moreView = requireView().findViewById(R.id.grid_more);
        moreView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        moreView.setAdapter(new UniversalAdapter<Option>(more, R.layout.more_item) {
            @Override
            public void bindView(ViewHolder holder, Option obj) {
                holder.setImageResource(R.id.img_more_icon, obj.getIcon());
                holder.setText(R.id.tv_more_name, obj.getName());
                holder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (obj.getAction() != null) {
                            if (obj.getAction().equals("UPDATE")) {
                                Toast.makeText(getContext(), "已是最新版本", Toast.LENGTH_SHORT).show();
                            } else if (obj.getAction().equals("INVITE")) {
                                Intent intent = new Intent(Intent.ACTION_SEND);
                                intent.putExtra(Intent.EXTRA_TEXT, "https://www.cljy.com/download");
                                intent.setType("text/plain");
                                startActivity(Intent.createChooser(intent, "Share to..."));
                            } else {
                                Intent intent = new Intent("com.ujs.driving.app." + obj.getAction());
                                startActivity(intent);
                            }
                        }
                    }
                });
            }
        });

        TwinklingRefreshLayout twinklingRefreshLayout = requireView().findViewById(R.id.refreshLayout); //找到控件
        twinklingRefreshLayout.setPureScrollModeOn();  //设置回弹效果
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timerUtil.destroy();
    }
}
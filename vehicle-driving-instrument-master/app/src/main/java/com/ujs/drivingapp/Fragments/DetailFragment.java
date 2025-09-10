package com.ujs.drivingapp.Fragments;

/**
 * @author HWH.
 * @email huangwenhuan2019@outlook.com
 * @create time 1/27/2022 8:38 PM.
 * @description 汇总部分 详情 界面
 */

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.ujs.drivingapp.Adapters.UniversalAdapter;
import com.ujs.drivingapp.Pojo.DetailItem;
import com.ujs.drivingapp.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment {


    private static final String TAG = "DetailFragment";
    private String statisticId;

    public DetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailFragment newInstance() {
        DetailFragment fragment = new DetailFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        Bundle b = getArguments();
        if (null != b) {
            statisticId = b.getString("statisticId");
            Log.e(TAG, "onStart: " + statisticId);
        }
        GridView gridView = requireView().findViewById(R.id.grid_detail);

        ArrayList<DetailItem> detailItems = new ArrayList<>();
        detailItems.add(new DetailItem("驾驶时间", "00:08:01", R.drawable.ic_drive_time, ""));
        detailItems.add(new DetailItem("平均速率", "14.65", R.drawable.ic_speed_avg, "公里/小时"));
        detailItems.add(new DetailItem("心率", "74", R.drawable.ic_heart_rate, "bpm"));
        detailItems.add(new DetailItem("收缩压", "102", R.drawable.ic_bp_high, "mmHg"));
        detailItems.add(new DetailItem("舒张压", "75", R.drawable.ic_bp_low, "mmHg"));
        detailItems.add(new DetailItem("异常次数", "62", R.drawable.ic_exception, ""));
        gridView.setAdapter(new UniversalAdapter<DetailItem>(detailItems, R.layout.detail_item) {
            @Override
            public void bindView(ViewHolder holder, DetailItem obj) {
                holder.setImageResource(R.id.img_detail_icon, obj.getIcon());
                holder.setText(R.id.tv_detail_key, obj.getKey());
                holder.setText(R.id.tv_detail_value, obj.getValue());
                holder.setText(R.id.tv_detail_unit, obj.getUnit());

            }
        });

        TwinklingRefreshLayout twinklingRefreshLayout = requireView().findViewById(R.id.refreshLayout); //找到控件
        twinklingRefreshLayout.setPureScrollModeOn();  //设置回弹效果
    }
}
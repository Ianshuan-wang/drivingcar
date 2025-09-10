package com.ujs.drivingapp.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.ujs.drivingapp.Adapters.UniversalRecyclerViewAdapter;
import com.ujs.drivingapp.Component.ExpandLayout;
import com.ujs.drivingapp.Pojo.StatisticGroup;
import com.ujs.drivingapp.Pojo.StatisticItem;
import com.ujs.drivingapp.Pojo.SummaryItem;
import com.ujs.drivingapp.R;
import com.ujs.drivingapp.Retrofit.CarappService;
import com.ujs.drivingapp.Utils.JsonUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatisticsFragment#  newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatisticsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "StatisticsFragment";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private List<String> categories = new ArrayList<>();
    private Retrofit retrofit;
    private CarappService carappService;
    private List<SummaryItem> summaryItems = new ArrayList<>();
    private List<StatisticGroup> statisticGroups = new ArrayList<>();
    private List<StatisticItem> statisticItems = new ArrayList<>();
    private int current = 0; // 当前类别
    private UniversalRecyclerViewAdapter<SummaryItem> summariesAdapter;
    private UniversalRecyclerViewAdapter<StatisticGroup> statisticGroupAdapter;
    private UniversalRecyclerViewAdapter<StatisticItem> statisticItemAdapter;
    private RecyclerView summary = null;
    private RecyclerView statistic_group = null;
    private RecyclerView statistic_item = null;


    public StatisticsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StatisticsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StatisticsFragment newInstance(String param1, String param2) {
        StatisticsFragment fragment = new StatisticsFragment();
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
        retrofit = new Retrofit.Builder().baseUrl("http://47.96.138.120:8082/api/").build();
        carappService = retrofit.create(CarappService.class);
        Calendar cal = Calendar.getInstance();

        categories.add(cal.get(Calendar.YEAR) - 1 + "年行车概览");
        categories.add(cal.get(Calendar.YEAR) + "年行车概览");
        categories.add("总览");
        current = categories.size() - 1;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_statistics, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        ImageView to_left = requireView().findViewById(R.id.img_to_left);
        ImageView to_right = requireView().findViewById(R.id.img_to_right);
        TextView category = requireView().findViewById(R.id.tv_category);

        to_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                current = current - 1 < 0 ? categories.size() - current - 1 : (current - 1) % categories.size();
                category.setText(categories.get(current));
                refreshData();
            }
        });

        to_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "onClick: " + current);
                current = (current + 1) % categories.size();
                Log.e(TAG, "onClick: " + current);
                category.setText(categories.get(current));
                refreshData();
            }
        });

        if (summary == null || statistic_group == null || statistic_item == null) loadData();

        TwinklingRefreshLayout twinklingRefreshLayout = requireView().findViewById(R.id.refreshLayout); //找到控件
        twinklingRefreshLayout.setPureScrollModeOn();  //设置回弹效果
    }

    private void loadData() {
        Call<ResponseBody> call = carappService.getStatistics(categories.get(current));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful()) {
                    Log.e(TAG, "onResponse: " + response.message());
                } else {
                    Log.e(TAG, "onResponse: 数据加载成功");
                    try {
                        Map<String, Object> map = JsonUtil.toMap(response.body().string());
                        Map<String, Object> data = JsonUtil.getMap(map, "data");
                        List<Map<String, Object>> summaries = JsonUtil.getListMap(data, "summaries");
                        List<Map<String, Object>> statistics = JsonUtil.getListMap(data, "statistics");

                        for (Map<String, Object> summary : summaries) {
                            SummaryItem summaryItem = JsonUtil.toBean(summary.toString(), SummaryItem.class);
                            summaryItems.add(summaryItem);
                        }

                        for (Map<String, Object> statistic : statistics) {
                            StatisticGroup statisticGroup = JsonUtil.toBean(statistic.toString(), StatisticGroup.class);
                            statisticGroups.add(statisticGroup);
                        }

                        summary = requireView().findViewById(R.id.rv_summary);
                        summary.setLayoutManager(new GridLayoutManager(getContext(), 4));
                        summariesAdapter = new UniversalRecyclerViewAdapter<SummaryItem>(summaryItems, R.layout.summary_item) {

                            @Override
                            public void bindView(RecyclerViewHolder holder, SummaryItem obj) {
                                ImageView imageView = holder.getView(R.id.img_divide_line_1);
                                imageView.setSelected(!obj.getData().equals(summaryItems.get(0).getData()));
                                if (obj.getData().length() > 3) {
                                    TextView data = holder.getView(R.id.tv_summary_data);
                                    data.setTextSize(23 - obj.getData().length());
                                }
                                String data = obj.getData();
                                if (obj.getData().equals(summaryItems.get(2).getData()) ||
                                        obj.getData().equals(summaryItems.get(3).getData())) {
                                    data = Math.round(Float.parseFloat(data)) + "";
                                }
                                holder.setText(R.id.tv_summary_data, data);
                                holder.setText(R.id.tv_summary_unit, obj.getUnit());
                            }
                        };
                        summary.setAdapter(summariesAdapter);


                        statistic_group = requireView().findViewById(R.id.rv_statistics);
                        statistic_group.setLayoutManager(new LinearLayoutManager(requireContext()));
                        statisticGroupAdapter = new UniversalRecyclerViewAdapter<StatisticGroup>(statisticGroups, R.layout.statistic_group) {

                            @Override
                            public void bindView(RecyclerViewHolder holder, StatisticGroup obj) {

                                holder.setText(R.id.tv_date, obj.getDate());
                                holder.setText(R.id.tv_mileage, obj.getMileage() + "公里");
                                holder.setText(R.id.tv_time, obj.getTime() + " 分钟");
                                holder.setText(R.id.tv_count, obj.getDriveCount() + " 次");
                                holder.setText(R.id.tv_abnormal, "异常 " + obj.getAbnormalCount() + " 次");
                                statistic_item = holder.getView(R.id.rv_statistic_item);
                                statistic_item.setLayoutManager(new LinearLayoutManager(requireContext()));
                                statisticItems = obj.getStatisticItems();
                                statisticItemAdapter = new UniversalRecyclerViewAdapter<StatisticItem>(statisticItems, R.layout.statistic_item) {

                                    @Override
                                    public void bindView(RecyclerViewHolder holder, StatisticItem obj) {
                                        holder.setText(R.id.tv_date, obj.getDate());
                                        holder.setText(R.id.tv_time_detail, obj.getTime().replaceAll("：", ":"));
                                        holder.setText(R.id.tv_mileage_current, String.valueOf(obj.getMileage()));
                                        holder.setText(R.id.tv_duration, obj.getDuration().replaceAll("：", ":"));
                                        holder.setText(R.id.tv_speed, String.valueOf(obj.getSpeed()));
                                        ImageView imageView = holder.getView(R.id.img_divide_line);
                                        imageView.setSelected(!obj.getTime().equals(statisticItems.get(0).getTime()));
                                        holder.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Intent intent = new Intent("com.ujs.driving.app.SUMMARY");
                                                Log.e(TAG, "onClick: " + obj.getId());
                                                intent.putExtra("statisticId", String.valueOf(obj.getId()));
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                requireContext().startActivity(intent);
                                            }
                                        });
                                    }
                                };
                                statistic_item.setAdapter(statisticItemAdapter);
                                holder.setOnClickListener(R.id.rlayout_group, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        ExpandLayout mExpandLayout = holder.getView(R.id.expandLayout);
                                        mExpandLayout.toggleExpand();
                                        ImageView imageView = holder.getView(R.id.img_expand);
                                        imageView.setSelected(mExpandLayout.isExpand());
                                    }
                                });
                            }
                        };
                        statistic_group.setAdapter(statisticGroupAdapter);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void refreshData() {
        Call<ResponseBody> call = carappService.getStatistics(categories.get(current));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful()) {
                    Log.e(TAG, "onResponse: " + response.message());
                } else {
                    Log.e(TAG, "onResponse: 数据加载成功");
                    try {
                        Map<String, Object> map = JsonUtil.toMap(response.body().string());
                        Map<String, Object> data = JsonUtil.getMap(map, "data");
                        List<Map<String, Object>> summaries = JsonUtil.getListMap(data, "summaries");
                        List<Map<String, Object>> statistics = JsonUtil.getListMap(data, "statistics");
                        summaryItems.clear();
                        for (Map<String, Object> summary : summaries) {
                            SummaryItem summaryItem = JsonUtil.toBean(summary.toString(), SummaryItem.class);
                            summaryItems.add(summaryItem);
                        }
                        statisticGroups.clear();
                        for (Map<String, Object> statistic : statistics) {
                            StatisticGroup statisticGroup = JsonUtil.toBean(statistic.toString(), StatisticGroup.class);

                            statisticGroups.add(statisticGroup);
                        }

                        summariesAdapter.notifyDataSetChanged();
                        statisticGroupAdapter.notifyDataSetChanged();
                        statisticItemAdapter.notifyDataSetChanged();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

}
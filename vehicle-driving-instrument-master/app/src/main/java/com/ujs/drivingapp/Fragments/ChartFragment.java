package com.ujs.drivingapp.Fragments;

/**
 * @author HWH.
 * @email huangwenhuan2019@outlook.com
 * @create time 1/27/2022 8:38 PM.
 * @description 汇总部分 图表 界面
 */

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.ujs.drivingapp.Component.CustomizedPieChart;
import com.ujs.drivingapp.R;
import com.ujs.drivingapp.Retrofit.CarappService;
import com.ujs.drivingapp.Utils.BarChartUtil;
import com.ujs.drivingapp.Utils.JsonUtil;
import com.ujs.drivingapp.Utils.LineChartUtil;
import com.ujs.drivingapp.Utils.PieChartUtil;
import com.ujs.drivingapp.Utils.UserStatusUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChartFragment extends Fragment {


    private static final String TAG = "ChartFragment";
    private String statisticId;

    private List<List<BarEntry>> barEntries = new ArrayList<>(4);   // 柱状图数据集
    private List<PieEntry> pieEntries = new ArrayList<>();  // 饼图数据集

    private List<Entry> driveEntries = new ArrayList<>();  // 驾驶情况数据集
    private List<String> driveStatus = new ArrayList<>();   // 驾驶情况标签

    private List<String> fatigueLabels = new ArrayList<>(); // 疲劳标签
    private List<String> distractionLabels = new ArrayList<>(); // 分心标签
    private List<String> sentimentLabels = new ArrayList<>(); // 情绪标签
    private List<Integer> barColors = new ArrayList<>(); // 颜色


    // 网络请求处理
    private Retrofit retrofit;
    private CarappService carappService;

    // 图表
    private LineChart line;

    private CustomizedPieChart pie_chart_fatigue;
    private BarChart bar_chart_fatigue;

    private CustomizedPieChart pie_chart_distraction;
    private BarChart bar_chart_distraction;

    private CustomizedPieChart pie_chart_sentiment;
    private BarChart bar_chart_sentiment;


    public ChartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ChartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChartFragment newInstance() {
        ChartFragment fragment = new ChartFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        retrofit = new Retrofit.Builder().baseUrl("http://47.96.138.120:8082/api/").build();
        carappService = retrofit.create(CarappService.class);

        driveStatus.add(UserStatusUtil.NORMAL);
        driveStatus.add(UserStatusUtil.FATIGUE);
        driveStatus.add(UserStatusUtil.DISTRACTION);
        driveStatus.add(UserStatusUtil.SENTIMENT);

        fatigueLabels.add("非疲劳驾驶");
        fatigueLabels.add("眨眼");
        fatigueLabels.add("哈欠");
        fatigueLabels.add("瞌睡");

        distractionLabels.add("非分心驾驶");
        distractionLabels.add("操作手机");
        distractionLabels.add("接听电话");
        distractionLabels.add("饮用饮品");

        sentimentLabels.add("非情绪驾驶");
        sentimentLabels.add("高兴");
        sentimentLabels.add("伤心");
        sentimentLabels.add("恐惧");
        sentimentLabels.add("愤怒");


        barColors.add(Color.parseColor("#f1f1dd"));
        barColors.add(Color.parseColor("#91cc75"));
        barColors.add(Color.parseColor("#5470c6"));
        barColors.add(Color.parseColor("#fc8452"));
        barColors.add(Color.parseColor("#ee6666"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chart, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle b = getArguments();
        if (null != b) {
            statisticId = b.getString("statisticId");
            Log.e(TAG, "onStart: " + statisticId);
        }

        line = requireView().findViewById(R.id.line);
        pie_chart_fatigue = requireView().findViewById(R.id.pie_chart_fatigue);
        bar_chart_fatigue = requireView().findViewById(R.id.bar_chart_fatigue);

        pie_chart_distraction = requireView().findViewById(R.id.pie_chart_distraction);
        bar_chart_distraction = requireView().findViewById(R.id.bar_chart_distraction);

        pie_chart_sentiment = requireView().findViewById(R.id.pie_chart_sentiment);
        bar_chart_sentiment = requireView().findViewById(R.id.bar_chart_sentiment);


        Call<ResponseBody> drive = carappService.getDriveStatus(statisticId);
        drive.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful()) {
                    Log.e(TAG, "onResponse: " + response.message());
                } else {
                    try {
                        Map<String, Object> map = JsonUtil.toMap(response.body().string());
                        Map<String, Object> data = JsonUtil.getMap(map, "data");
                        List<Double> driveData = (List<Double>) data.get("driveEntries");
                        Log.e(TAG, "onResponse: " + driveData);
                        for (int i = 0; i < driveData.size(); i++) {
                            driveEntries.add(new Entry(i, driveData.get(i).floatValue()));
                        }
                        Log.e(TAG, "onResponse: " + driveEntries);
                        LineChartUtil.initLineChart(line, driveStatus);
                        LineChartUtil.setLineData(line, driveEntries);
                        line.invalidate();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });


        Call<ResponseBody> fatigue = carappService.getFatigue(statisticId);
        fatigue.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful()) {
                    Log.e(TAG, "onResponse: " + response.message());
                } else {
                    operation(response, bar_chart_fatigue, pie_chart_fatigue, fatigueLabels, barColors);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });


        Call<ResponseBody> distraction = carappService.getDistraction(statisticId);
        distraction.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful()) {
                    Log.e(TAG, "onResponse: " + response.message());
                } else {
                    operation(response, bar_chart_distraction, pie_chart_distraction, distractionLabels, barColors);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });


        Call<ResponseBody> sentiment = carappService.getSentiment(statisticId);
        sentiment.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful()) {
                    Log.e(TAG, "onResponse: " + response.message());
                } else {
                    operation(response, bar_chart_sentiment, pie_chart_sentiment, sentimentLabels, barColors);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

        TwinklingRefreshLayout twinklingRefreshLayout = requireView().findViewById(R.id.refreshLayout); //找到控件
        twinklingRefreshLayout.setPureScrollModeOn();  //设置回弹效果

    }


    /**
     * 请求成功操作，包括图表数据处理即绑定
     *
     * @param response 请求结果
     * @param barChart 柱状图对象
     * @param pieChart 饼图对象
     * @param labels   标签集合
     * @param colors   颜色集合
     */
    private void operation(Response<ResponseBody> response, BarChart barChart, CustomizedPieChart pieChart, List<String> labels, List<Integer> colors) {
        try {
            List<List<BarEntry>> barEntries = new ArrayList<>(4);   // 柱状图数据集
            List<PieEntry> pieEntries = new ArrayList<>();  // 饼图数据集
            Map<String, Object> map = JsonUtil.toMap(response.body().string());
            Map<String, Object> data = JsonUtil.getMap(map, "data");
            Set<String> strings = data.keySet();
            String listName = "";
            for (String string : strings) {
                listName = string;
            }
            Log.e(TAG, "operation: " + listName);
            List<List<Double>> barDataList = JsonUtil.getList(data, listName, Double.class);
            int dataCount = 0;
            for (int i = 0; i < barDataList.size(); i++) {
                dataCount += barDataList.get(i).size();
                barEntries.add(new ArrayList<>());
                for (Double d : barDataList.get(i)) {
                    barEntries.get(i).add(new BarEntry(d.floatValue(), 1));
                }
            }

            for (int i = 0; i < barDataList.size(); i++) {
                pieEntries.add(new PieEntry(barDataList.get(i).size() / (float) dataCount, labels.get(i)));
            }

            BarChartUtil.initBarChart(barChart);
            BarChartUtil.setBarDatas(barChart, barEntries, labels, colors);

            PieChartUtil.initPieChart(pieChart);
            PieChartUtil.setPieData(pieChart, pieEntries, colors);// 设置饼图数据

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
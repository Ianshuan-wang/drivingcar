package com.ujs.drivingapp.Utils;

import android.graphics.Color;
import android.util.Log;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.List;

/**
 * @author HWH.
 * @email huangwenhuan2019@outlook.com
 * @create time 2/11/2022 2:06 PM.
 * @description 柱状图配置工具类$
 */
public class BarChartUtil {

    private static final String TAG = "BarChartUtil";

    /**
     * 初始化BarChart图表
     */
    public static void initBarChart(BarChart barChart) {
        /***图表设置***/
        barChart.setExtraOffsets(24, 24, 24, 12);
        //背景颜色
        barChart.setBackgroundColor(Color.WHITE);
        //不显示图表网格
        barChart.setDrawGridBackground(false);
        //背景阴影
        barChart.setDrawBarShadow(false);
        barChart.setHighlightFullBarEnabled(false);
        //不显示边框
        barChart.setDrawBorders(false);
        //设置动画效果
        barChart.animateY(1000, Easing.Linear);
        barChart.animateX(1000, Easing.Linear);

        /***XY轴的设置***/
        //X轴设置显示位置在底部
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);
        xAxis.setDrawAxisLine(false);
        //不显示X轴网格线
        xAxis.setDrawGridLines(false);

        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                int v = (int) value;
                int hour = v / 3600;
                int minute = (v - hour * 3600) / 60;
                int second = (v - hour * 3600 - minute * 60);
                return (hour >= 10 ? hour + "" : "0" + hour) + ":" +
                        (minute >= 10 ? minute + "" : "0" + minute) + ":" +
                        (second >= 10 ? second + "" : "0" + second);
            }
        });

        YAxis leftAxis = barChart.getAxisLeft();
        YAxis rightAxis = barChart.getAxisRight();
        leftAxis.setAxisMaximum(1f);
        leftAxis.setGranularity(1f);// 设置刻度步长
        leftAxis.setDrawGridLines(false);
        //保证Y轴从0开始，不然会上移一点
        leftAxis.setAxisMinimum(0f);
        rightAxis.setAxisMinimum(0f);
        // 左边Y轴不绘制
        leftAxis.setEnabled(false);
        // 取消右边Y轴
        rightAxis.setEnabled(false);

        //不显示右下角内容
        Description description = new Description();
        description.setEnabled(false);
        barChart.setDescription(description);

        /***折线图例 标签 设置***/
        Legend legend = barChart.getLegend();
        legend.setForm(Legend.LegendForm.LINE);
        legend.setTextSize(11f);
        //显示位置
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        //是否绘制在图表里面
        legend.setDrawInside(false);
    }


    /**
     * 设置图表数据
     *
     * @param barChart   柱状图对象
     * @param barEntries 数据集合 List<List<BarEntry>> 类型
     * @param labels     数据对应标签集合 List<String> 类型
     * @param colors     数据对应颜色集合 List<String> 类型
     */
    public static void setBarDatas(BarChart barChart, List<List<BarEntry>> barEntries, List<String> labels, List<Integer> colors) {
        BarData barData = new BarData();
        for (int i = 0; i < barEntries.size(); i++) {
            BarDataSet barDataSet = new BarDataSet(barEntries.get(i), labels.get(i));
            barDataSet.setColor(colors.get(i));
            barDataSet.setFormLineWidth(1f);
            barDataSet.setFormSize(15.f);
            //不显示柱状图顶部值
            barDataSet.setDrawValues(false);
            barData.addDataSet(barDataSet);
        }
        //设置宽度为100%
        barData.setBarWidth(1f);
        barChart.setData(barData);
    }


    public static void setBarData(BarChart barChart, List<BarEntry> barEntries, String label, int color) {
        BarDataSet barDataSet = new BarDataSet(barEntries, label);
        barDataSet.setColor(color);
        barDataSet.setFormLineWidth(1f);
        barDataSet.setFormSize(15.f);
        //不显示柱状图顶部值
        barDataSet.setDrawValues(false);
        BarData barData = new BarData(barDataSet);
        //设置宽度为100%
        barData.setBarWidth(1f);
        barChart.setData(barData);
    }

    public static void addBarData(BarChart barChart, List<BarEntry> barEntries, String label, int color) {
        BarDataSet barDataSet = new BarDataSet(barEntries, label);
        barDataSet.setColor(color);
        barDataSet.setFormLineWidth(1f);
        barDataSet.setFormSize(15.f);
        //不显示柱状图顶部值
        barDataSet.setDrawValues(false);
        BarData barData = barChart.getBarData();
        barData.addDataSet(barDataSet);
    }

}

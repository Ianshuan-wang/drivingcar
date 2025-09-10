package com.ujs.drivingapp.Utils;

/**
 * @author HWH.
 * @email huangwenhuan2019@outlook.com
 * @create time 1/27/2022 8:32 PM.
 * @description 折线图配置工具类
 */

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.List;

public class LineChartUtil {

    public static void initLineChart(LineChart line, List<String> status) {
        line.setExtraOffsets(12, 0, 24, 12);


        XAxis xAxis = line.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return status.get((int) value);
            }
        });
        xAxis.setAxisMaximum(3.2f);
        xAxis.setAxisMinimum(-0.2f);
        xAxis.setGranularity(1f);

        // 设置左右坐标Y轴不显示
        // line.getAxisLeft().setEnabled(false);
        line.getAxisRight().setEnabled(false);
        line.getDescription().setEnabled(false);//设置右下角description

        YAxis yAxisLeft = line.getAxisLeft();
        yAxisLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        yAxisLeft.setDrawAxisLine(false);
        yAxisLeft.setDrawZeroLine(false);

        yAxisLeft.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return ((int) (value * 100)) + "%";
            }
        });
        yAxisLeft.setAxisMaximum(1f);
        yAxisLeft.setAxisMinimum(0f);

        line.setScaleEnabled(false);   // 取消缩放
        line.setDragEnabled(false); // 取消拖拽
        Legend legend = line.getLegend();
        legend.setYOffset(12);


    }

    public static void setLineData(LineChart line, List<Entry> list) {
        //list是你这条线的数据
        LineDataSet lineDataSet = new LineDataSet(list, "驾驶情况");
        // 设置曲线模式
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        // 填充颜色(渐变色)
        lineDataSet.setDrawFilled(true);
        lineDataSet.setFillDrawable(new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{Color.parseColor("#31FF5A00"), Color.parseColor("#00FA5544")}));
        lineDataSet.setLineWidth(2f);
        LineData lineData = new LineData();
        lineData.addDataSet(lineDataSet);
        // 点击折点显示高亮
        lineDataSet.setDrawHighlightIndicators(false);
        // 坐标显示值
        lineDataSet.setDrawValues(true);
        // 坐标显示圆点
        lineDataSet.setDrawCircles(true);
        lineData.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return ((int) (value * 100)) + "%";
            }
        });

        line.setData(lineData);

    }
}

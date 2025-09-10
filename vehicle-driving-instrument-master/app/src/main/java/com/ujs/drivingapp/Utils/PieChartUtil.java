package com.ujs.drivingapp.Utils;

/**
 * @author HWH.
 * @email huangwenhuan2019@outlook.com
 * @create time 1/27/2022 8:33 PM.
 * @description 饼图配置工具类
 */

import android.graphics.Color;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.ujs.drivingapp.Component.CustomizedPieChart;

import java.util.ArrayList;
import java.util.List;

public class PieChartUtil {

    /**
     * @param chart CustomizedPieChart 组件对象
     *              初始化图表
     */
    static public void initPieChart(CustomizedPieChart chart) {
        chart.setUsePercentValues(true);
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(0.f, 10.f, 0.f, 10.f);

        chart.setDragDecelerationFrictionCoef(0.95f);
        chart.setCenterText("");

        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.WHITE); //中间圆颜色
        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(110);

        chart.setHoleRadius(40f);  //圆半径
        chart.setTransparentCircleRadius(44f);
        chart.setDrawCenterText(true);

        // 设置允许旋转
        chart.setRotationEnabled(false);
        chart.setRotationAngle(-90);

        chart.setHighlightPerTapEnabled(true);

        chart.animateY(1200, Easing.EaseInQuad); //设置饼图动画

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setTextColor(Color.BLUE);
        l.setEnabled(false);
    }

    /**
     * @param chart   CustomizedPieChart 组件对象
     * @param entries 饼图每一块的数据, 为 ArrayList<PieEntry> 类型
     * @param colors  饼图每一块对应的颜色, 为ArrayList<Integer> 类型
     *                设置饼图数据
     */
    static public void setPieData(CustomizedPieChart chart, List<PieEntry> entries, List<Integer> colors) {

        PieDataSet dataSet = new PieDataSet(entries, "Election Results");
        dataSet.setSliceSpace(3f);  //不同块之间的间距
        dataSet.setSelectionShift(7f);//选中时候突出的间距

        dataSet.setColors(colors);// 设置块区域颜色

        dataSet.setValueLineWidth(2);// 设置指示线宽度
        dataSet.setValueLinePart1Length(0.6f);// 设置线1长度
        dataSet.setValueLinePart2Length(0.4f);// 设置线2长度(水平线)
        dataSet.setValueLinePart1OffsetPercentage(100f);// 设置线1偏移,范围(0~100f)

        dataSet.setHighlightEnabled(true);// 设置允许高亮
        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);//标签显示在外面，关闭显示在饼图里面

        dataSet.setValueLineColor(0xff000000);  //设置指示线条颜色,必须设置成这样，才能和饼图区域颜色一致

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(12f);
        data.setHighlightEnabled(true);

        chart.setData(data);
        chart.highlightValues(null);
        chart.invalidate();
    }
}

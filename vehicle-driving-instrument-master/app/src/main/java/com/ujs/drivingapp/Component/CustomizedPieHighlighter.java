package com.ujs.drivingapp.Component;

/**
 * @author HWH.
 * @email huangwenhuan2019@outlook.com
 * @create time 1/27/2022 8:26 PM.
 * @description 自定义饼图高亮类
 */

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.highlight.PieRadarHighlighter;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;

public class CustomizedPieHighlighter extends PieRadarHighlighter<CustomizedPieChart> {

    public CustomizedPieHighlighter(CustomizedPieChart chart) {
        super(chart);
    }

    @Override
    protected Highlight getClosestHighlight(int index, float x, float y) {

        IPieDataSet set = mChart.getData().getDataSet();

        final Entry entry = set.getEntryForIndex(index);

        return new Highlight(index, entry.getY(), x, y, 0, set.getAxisDependency());
    }
}

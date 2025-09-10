package com.ujs.drivingapp.Pojo;

/**
 * @author HWH.
 * @email huangwenhuan2019@outlook.com
 * @create time 2/4/2022 3:34 PM.
 * @description 汇总项实体类$
 */
public class SummaryItem {
    private String data;
    private String unit;

    public SummaryItem(String data, String unit) {
        this.data = data;
        this.unit = unit;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}

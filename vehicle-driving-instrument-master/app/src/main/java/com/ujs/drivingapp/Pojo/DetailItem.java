package com.ujs.drivingapp.Pojo;

/**
 * @author HWH.
 * @email huangwenhuan2019@outlook.com
 * @create time 1/28/2022 2:47 PM.
 * @description 详情项实体类$
 */
public class DetailItem {
    private String key;
    private String value;
    private int icon; // 图标资源id
    private String unit;

    public DetailItem(String key, String value, int icon, String unit) {
        this.key = key;
        this.value = value;
        this.icon = icon;
        this.unit = unit;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "DetailItem{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", icon=" + icon +
                ", unit='" + unit + '\'' +
                '}';
    }
}

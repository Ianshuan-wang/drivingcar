package com.ujs.drivingapp.Pojo;

/**
 * @author HWH.
 * @email huangwenhuan2019@outlook.com
 * @create time 2/15/2022 9:45 PM.
 * @description 设置项实体类$
 */
public class SettingItem {
    private String name;

    public SettingItem(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "SettingItem{" +
                "name='" + name + '\'' +
                '}';
    }
}

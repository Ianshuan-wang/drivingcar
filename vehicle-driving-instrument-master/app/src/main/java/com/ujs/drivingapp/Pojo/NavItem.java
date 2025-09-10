package com.ujs.drivingapp.Pojo;

/**
 * @author HWH.
 * @email huangwenhuan2019@outlook.com
 * @create time 1/27/2022 8:40 PM.
 * @description 主界面导航按钮实体类
 */

public class NavItem {


    private int ic_nav;// 导航栏图标
    private String name_nav;// 导航栏名称


    public NavItem(int ic_nav, String name_nav) {
        this.ic_nav = ic_nav;
        this.name_nav = name_nav;
    }

    public int getIc_nav() {
        return ic_nav;
    }

    public void setIc_nav(int ic_nav) {
        this.ic_nav = ic_nav;
    }

    public String getName_nav() {
        return name_nav;
    }

    public void setName_nav(String name_nav) {
        this.name_nav = name_nav;
    }

    @Override
    public String toString() {
        return "NavItem{" +
                "ic_nav=" + ic_nav +
                ", name_nav='" + name_nav + '\'' +
                '}';
    }
}

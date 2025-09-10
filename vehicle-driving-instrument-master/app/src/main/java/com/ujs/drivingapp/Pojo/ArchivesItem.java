package com.ujs.drivingapp.Pojo;

/**
 * @author HWH.
 * @email huangwenhuan2019@outlook.com
 * @create time 2/16/2022 9:27 PM.
 * @description 资料设置项实体类$
 */
public class ArchivesItem {
    private String name;
    private String tip;

    public ArchivesItem(String name, String tip) {
        this.name = name;
        this.tip = tip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    @Override
    public String toString() {
        return "ArchivesItem{" +
                "name='" + name + '\'' +
                ", tip='" + tip + '\'' +
                '}';
    }
}

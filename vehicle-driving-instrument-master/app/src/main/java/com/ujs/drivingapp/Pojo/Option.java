package com.ujs.drivingapp.Pojo;

/**
 * @author HWH.
 * @email huangwenhuan2019@outlook.com
 * @create time 1/29/2022 4:08 PM.
 * @description 选项实体类$
 */
public class Option {

    private String name;
    private int icon;
    private String action;

    public Option(String name, int icon) {
        this.name = name;
        this.icon = icon;
    }

    public Option(String name, int icon, String action) {
        this.name = name;
        this.icon = icon;
        this.action = action;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return "Option{" +
                "name='" + name + '\'' +
                ", icon=" + icon +
                ", action='" + action + '\'' +
                '}';
    }
}

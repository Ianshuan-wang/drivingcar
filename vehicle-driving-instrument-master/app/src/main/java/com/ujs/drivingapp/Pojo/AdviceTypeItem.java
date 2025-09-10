package com.ujs.drivingapp.Pojo;

/**
 * @author HWH.
 * @email huangwenhuan2019@outlook.com
 * @create time 2/9/2022 5:56 PM.
 * @description 问题类型$
 */
public class AdviceTypeItem {

    private String type;
    private boolean choose;

    public AdviceTypeItem(String type, boolean choose) {
        this.type = type;
        this.choose = choose;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isChoose() {
        return choose;
    }

    public void setChoose(boolean choose) {
        this.choose = choose;
    }

    @Override
    public String toString() {
        return "AdviceTypeItem{" +
                "type='" + type + '\'' +
                ", choose=" + choose +
                '}';
    }
}

package com.ujs.drivingapp.Pojo;

/**
 * @author HWH.
 * @email huangwenhuan2019@outlook.com
 * @create time 1/27/2022 8:41 PM.
 * @description 行车贴士实体类
 */

// TODO
public class Tip {
    String context;
    String image;

    public Tip(String context) {
        this.context = context;
    }

    public Tip(String context, String image) {
        this.context = context;
        this.image = image;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

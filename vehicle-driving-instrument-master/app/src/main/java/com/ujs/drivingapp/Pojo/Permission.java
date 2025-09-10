package com.ujs.drivingapp.Pojo;

/**
 * @author HWH.
 * @email huangwenhuan2019@outlook.com
 * @create time 1/30/2022 7:40 PM.
 * @description 权限实体类$
 */
public class Permission {
    private String name;
    private String detail;

    public Permission(String name, String detail) {
        this.name = name;
        this.detail = detail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    @Override
    public String toString() {
        return "Permission{" +
                "name='" + name + '\'' +
                ", detail='" + detail + '\'' +
                '}';
    }
}

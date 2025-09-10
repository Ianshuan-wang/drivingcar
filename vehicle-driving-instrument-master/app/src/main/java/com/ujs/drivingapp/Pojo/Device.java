package com.ujs.drivingapp.Pojo;

/**
 * @author HWH.
 * @email huangwenhuan2019@outlook.com
 * @create time 1/29/2022 8:31 PM.
 * @description 设备实体类$
 */
public class Device {
    private int _id;
    private String name;

    public Device() {
    }

    public Device(int _id, String name) {
        this._id = _id;
        this.name = name;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    @Override
    public String toString() {
        return "Device{" +
                "_id=" + _id +
                ", name='" + name + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

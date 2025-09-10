package com.ujs.drivingapp.Pojo;

/**
 * @author HWH.
 * @email huangwenhuan2019@outlook.com
 * @create time 2/4/2022 1:40 PM.
 * @description 统计数据项$
 */
public class StatisticItem {
    private int id; //id
    private String date; //日期
    private String time; //时间点
    private String duration; //时间段
    private double mileage; //里程
    private double speed; //平均速度

    public StatisticItem() {
    }

    public StatisticItem(String date, String time, String duration, double mileage, double speed) {
        this.date = date;
        this.time = time;
        this.duration = duration;
        this.mileage = mileage;
        this.speed = speed;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public double getMileage() {
        return mileage;
    }

    public void setMileage(double mileage) {
        this.mileage = mileage;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "StatisticItem{" +
                "id='" + id + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", duration='" + duration + '\'' +
                ", mileage=" + mileage +
                ", speed=" + speed +
                '}';
    }
}

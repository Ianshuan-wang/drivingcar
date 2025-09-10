package com.ujs.drivingapp.Pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author HWH.
 * @email huangwenhuan2019@outlook.com
 * @create time 2/4/2022 1:12 PM.
 * @description 统计数据组$
 */


public class StatisticGroup {
    private String date; //日期
    private double time; //总时间
    private double mileage; //总里程
    private int driveCount; //驾驶总数
    private int abnormalCount;  //异常总数
    private List<StatisticItem> statisticItems = new ArrayList<>();


    public StatisticGroup() {
    }

    public StatisticGroup(double time, double mileage, String date, int driveCount, int abnormalCount) {
        this.time = time;
        this.mileage = mileage;
        this.date = date;
        this.driveCount = driveCount;
        this.abnormalCount = abnormalCount;
    }

    public StatisticGroup(String date, double time, double mileage, int driveCount, int abnormalCount, List<StatisticItem> statisticItems) {
        this.date = date;
        this.time = time;
        this.mileage = mileage;
        this.driveCount = driveCount;
        this.abnormalCount = abnormalCount;
        this.statisticItems = statisticItems;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public double getMileage() {
        return mileage;
    }

    public void setMileage(double mileage) {
        this.mileage = mileage;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getDriveCount() {
        return driveCount;
    }

    public void setDriveCount(int driveCount) {
        this.driveCount = driveCount;
    }

    public int getAbnormalCount() {
        return abnormalCount;
    }

    public void setAbnormalCount(int abnormalCount) {
        this.abnormalCount = abnormalCount;
    }

    public List<StatisticItem> getStatisticItems() {
        return statisticItems;
    }

    public void setStatisticItems(List<StatisticItem> statisticItems) {
        this.statisticItems = statisticItems;
    }

    @Override
    public String toString() {
        return "StatisticGroup{" +
                "time=" + time +
                ", mileage=" + mileage +
                ", date='" + date + '\'' +
                ", driveCount=" + driveCount +
                ", abnormalCount=" + abnormalCount +
                '}';
    }
}

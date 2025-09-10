package com.ujs.drivingapp.Pojo;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * @author huanqiu
 * @email 2364521714@qq.com
 * @create time 05/03/2022 下午 8:51
 * @description
 */
@Entity(tableName = "path")
public class Path {

    @PrimaryKey(autoGenerate = true)
    private Integer id;

    /**
     * 路径点集合
     */
    @ColumnInfo(name = "path")
    private String path;

    /**
     * 开始时间
     */
    @ColumnInfo(name = "start_time")
    private String startTime;

    /**
     * 持续时间
     */
    @ColumnInfo(name = "duration")
    private Long duration;

    /**
     * 里程
     */
    @ColumnInfo(name = "mileage")
    private Double mileage;

    /**
     * 最大速度
     */
    @ColumnInfo(name = "max_speed")
    private Double maxSpeed;

    /**
     * 最小速度
     */
    @ColumnInfo(name = "min_speed")
    private Double minSpeed;

    /**
     * 平均速度
     */
    @ColumnInfo(name = "average_speed")
    private Double averageSpeed;

    public Path(Integer id, String path, String startTime, Long duration, Double mileage, Double maxSpeed, Double minSpeed, Double averageSpeed) {
        this.id = id;
        this.path = path;
        this.startTime = startTime;
        this.duration = duration;
        this.mileage = mileage;
        this.maxSpeed = maxSpeed;
        this.minSpeed = minSpeed;
        this.averageSpeed = averageSpeed;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Double getMileage() {
        return mileage;
    }

    public void setMileage(Double mileage) {
        this.mileage = mileage;
    }

    public Double getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(Double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public Double getMinSpeed() {
        return minSpeed;
    }

    public void setMinSpeed(Double minSpeed) {
        this.minSpeed = minSpeed;
    }

    public Double getAverageSpeed() {
        return averageSpeed;
    }

    public void setAverageSpeed(Double averageSpeed) {
        this.averageSpeed = averageSpeed;
    }

    @Override
    public String toString() {
        return "Path{" +
                "id=" + id +
                ", path='" + path + '\'' +
                ", startTime='" + startTime + '\'' +
                ", duration=" + duration +
                ", mileage=" + mileage +
                ", maxSpeed=" + maxSpeed +
                ", minSpeed=" + minSpeed +
                ", averageSpeed=" + averageSpeed +
                '}';
    }
}

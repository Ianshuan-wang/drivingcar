package com.ujs.drivingapp.Pojo;

/**
 * @author HWH.
 * @email huangwenhuan2019@outlook.com
 * @create time 2/8/2022 9:19 PM.
 * @description 签到项实体类$
 */
public class PunchItem {
    private String date;
    private String info;
    private boolean status;

    public PunchItem(String date, String info, boolean status) {
        this.date = date;
        this.info = info;
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "PunchItem{" +
                "date='" + date + '\'' +
                ", info='" + info + '\'' +
                ", status=" + status +
                '}';
    }
}

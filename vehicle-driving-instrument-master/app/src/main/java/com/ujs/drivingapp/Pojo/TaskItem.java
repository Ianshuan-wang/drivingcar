package com.ujs.drivingapp.Pojo;

/**
 * @author HWH.
 * @email huangwenhuan2019@outlook.com
 * @create time 2/8/2022 11:06 PM.
 * @description 新手任务实体类$
 */
public class TaskItem {
    private String name;
    private String detail;
    private String exp;
    private boolean status;

    public TaskItem(String name, String detail, String exp, boolean status) {
        this.name = name;
        this.detail = detail;
        this.exp = exp;
        this.status = status;
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

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "TaskItem{" +
                "name='" + name + '\'' +
                ", detail='" + detail + '\'' +
                ", exp='" + exp + '\'' +
                ", status=" + status +
                '}';
    }
}

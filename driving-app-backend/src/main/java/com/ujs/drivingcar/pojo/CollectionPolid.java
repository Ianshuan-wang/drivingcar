package com.ujs.drivingcar.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class CollectionPolid {
    private int id;
    private String userid;
    private String poi_id;

    public CollectionPolid() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPoi_id() {
        return poi_id;
    }

    public void setPoi_id(String poi_id) {
        this.poi_id = poi_id;
    }

    @Override
    public String toString() {
        return "CollectionPolid{" +
                "id=" + id +
                ", userid='" + userid + '\'' +
                ", poi_id='" + poi_id + '\'' +
                '}';
    }
}

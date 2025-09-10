package com.ujs.drivingapp.Pojo;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.ujs.drivingapp.Commen.Constants;

/**
 * @author huanqiu
 * @email 2364521714@qq.com
 * @create time 16/02/2022 下午 8:25
 * @description
 */
@Entity(tableName = "poiSearchHistory")
public class PoiSearchHistory {
    @PrimaryKey(autoGenerate = true)
    private Integer id;

    @ColumnInfo(name = "content")
    private String content;

    @ColumnInfo(name = "type")
    private String type;

    @ColumnInfo(name = "poi_id")
    private String poiId;

    @Ignore
    public PoiSearchHistory() {
    }

    @Ignore
    public PoiSearchHistory(Integer id, String content) {
        this.id = id;
        this.content = content;
        this.type = Constants.PoiSearchHistory.TYPE_CONTENT;
        this.poiId = null;
    }

    public PoiSearchHistory(Integer id, String content, String type, String poiId) {
        this.id = id;
        this.content = content;
        this.type = type;
        this.poiId = poiId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPoiId() {
        return poiId;
    }

    public void setPoiId(String poiId) {
        this.poiId = poiId;
    }

    @Override
    public String toString() {
        return "PoiSearchHistory{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", type='" + type + '\'' +
                ", poiId='" + poiId + '\'' +
                '}';
    }
}

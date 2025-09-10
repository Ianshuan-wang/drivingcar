package com.ujs.drivingapp.Room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.ujs.drivingapp.Pojo.PoiSearchHistory;

import java.util.List;

/**
 * @author huanqiu
 * @email 2364521714@qq.com
 * @create time 16/02/2022 下午 8:22
 * @description
 */
@Dao
public interface PoiSearchHistoryDao {

    @Insert
    void insert(PoiSearchHistory poiSearchHistory);

    @Query("delete from poiSearchHistory")
    void deleteAll();

    @Query("delete from poiSearchHistory where id=:id")
    void delete(Integer id);

    @Query("select * from poiSearchHistory")
    List<PoiSearchHistory> select();

    @Query("select * from poiSearchHistory where content=:content")
    List<PoiSearchHistory> selectByContent(String content);
}

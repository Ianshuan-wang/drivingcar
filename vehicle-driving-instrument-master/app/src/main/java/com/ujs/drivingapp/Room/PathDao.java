package com.ujs.drivingapp.Room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.ujs.drivingapp.Pojo.Path;

/**
 * @author huanqiu
 * @email 2364521714@qq.com
 * @create time 05/03/2022 下午 8:58
 * @description
 */
@Dao
public interface PathDao {

    @Query("select * from path")
    public Path selectOne();

    @Query("delete from path")
    public void deleteAll();

    @Insert
    public void insert(Path path);
}

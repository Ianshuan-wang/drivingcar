package com.ujs.drivingapp.Room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.ujs.drivingapp.Pojo.PoiSearchHistory;

/**
 * @author huanqiu
 * @email 2364521714@qq.com
 * @create time 16/02/2022 下午 8:20
 * @description
 */
@Database(entities = {PoiSearchHistory.class}, version = 2,exportSchema = false)
public abstract class PoiSearchHistoryDataBase extends RoomDatabase {

    private static PoiSearchHistoryDataBase instance;

    public abstract PoiSearchHistoryDao poiSearchHistoryDao();

    public static PoiSearchHistoryDataBase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    PoiSearchHistoryDataBase.class,
                    "poiSearchHistory.db")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}

package com.ujs.drivingapp.Room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.ujs.drivingapp.Pojo.Path;

/**
 * @author huanqiu
 * @email 2364521714@qq.com
 * @create time 05/03/2022 下午 8:58
 * @description
 */
@Database(entities = {Path.class}, version = 1, exportSchema = false)
public abstract class PathDataBase extends RoomDatabase {
    private static PathDataBase instance;

    public abstract PathDao pathDao();

    public static PathDataBase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    PathDataBase.class,
                    "pathDataBase.db")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}

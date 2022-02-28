package com.example.bookeep;

import android.app.Application;

import com.example.bookeep.db.DBManger;

//表示全局应用的类
public class UniteApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化数据库
        DBManger.initDB(getApplicationContext());
    }
}

package com.wlm.wanandroid2.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.wlm.baselib.BaseApp

@Database(entities = [History::class], version = 1)
abstract class AppDataBase : RoomDatabase() {

    abstract fun getHistoryDao(): HistoryDao

    companion object {
        val instance by lazy {
            Room.databaseBuilder(BaseApp.instance, AppDataBase::class.java, "wan_android_db")
                .allowMainThreadQueries().build()
        }
    }
}
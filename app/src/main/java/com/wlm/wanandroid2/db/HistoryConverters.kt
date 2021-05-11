package com.wlm.wanandroid2.db

import androidx.room.TypeConverter
import java.util.*

class HistoryConverters {
    @TypeConverter
    fun boolean2Int(flag: Boolean): Int {
        return if (flag) 1 else 0
    }

    fun int2Boolean(i: Int): Boolean {
        return i == 1
    }


}
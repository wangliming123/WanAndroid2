package com.wlm.wanandroid2.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.TypeConverters
import java.io.Serializable
import java.util.*

@Entity(primaryKeys = ["visit_user_id", "id"])
@TypeConverters(HistoryConverters::class)
data class History (
    @ColumnInfo(name = "visit_user_id")
    var visitUserId: Int,
    @ColumnInfo
    val id: Int,
    @ColumnInfo
    val title: String,
    @ColumnInfo
    val url: String,
    @ColumnInfo
    val desc: String?,
    @ColumnInfo(name = "image_path", typeAffinity = ColumnInfo.TEXT)
    val imagePath: String?,
    @ColumnInfo
    val type: Int,
    @ColumnInfo
    var collect: Boolean,
    @ColumnInfo(name = "history_time")
    var historyTime: Long
): Serializable
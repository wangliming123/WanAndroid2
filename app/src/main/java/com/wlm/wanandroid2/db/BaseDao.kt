package com.wlm.wanandroid2.db

import androidx.room.*

@Dao
interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(element: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg elements: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(list: List<T>)

    @Delete
    fun delete(element: T)

    @Delete
    fun delete(vararg element: T)

    @Delete
    fun delete(list: List<T>)

    @Update
    fun update(element: T)


}
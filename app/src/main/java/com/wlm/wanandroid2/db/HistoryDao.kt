package com.wlm.wanandroid2.db

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update

@Dao
interface HistoryDao : BaseDao<History> {

    @Query("select * from History where visit_user_id = :visitUserId order by history_time desc")
    fun getAllHistory(visitUserId: Int): DataSource.Factory<Int, History>

    @Query("update History set visit_user_id = :visitUserId where visit_user_id = -1")
    fun updateHistoryVisitUser(visitUserId: Int)
    @Query("select count(*) from History where visit_user_id = :visitUserId and id = :id" )
    fun checkExist(visitUserId: Int, id: Int): Int

}
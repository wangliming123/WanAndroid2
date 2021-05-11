package com.wlm.wanandroid2.repository

import com.wlm.wanandroid2.db.AppDataBase

object DataBaseRepository {

    val historyDao by lazy { AppDataBase.instance.getHistoryDao() }
}
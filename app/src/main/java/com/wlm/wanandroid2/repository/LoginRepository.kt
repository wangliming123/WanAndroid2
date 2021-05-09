package com.wlm.wanandroid2.repository

import com.wlm.wanandroid2.bean.User
import com.wlm.wanandroid2.bean.HttpResponse
import com.wlm.wanandroid2.common.RetrofitManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object LoginRepository  {
    suspend fun login(username: String, password: String): HttpResponse<User> =
        withContext(Dispatchers.IO) {
            RetrofitManager.service.login(username, password)
        }


    suspend fun register(username: String, password: String, rePassword: String): HttpResponse<User> {
        return withContext(Dispatchers.Default) {
            RetrofitManager.service.register(username, password, rePassword)
        }
    }

    suspend fun logout(): HttpResponse<Any> {
        return withContext(Dispatchers.Default) {
            RetrofitManager.service.logout()
        }
    }

}
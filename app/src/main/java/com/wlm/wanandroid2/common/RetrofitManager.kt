package com.wlm.wanandroid2.common

import com.wlm.baselib.utils.OkHttpManager
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * retrofit网络请求管理类
 */
object RetrofitManager {
    val service by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        getRetrofit().create(ApiService::class.java)
    }

    fun getRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(ApiService.BASE_URL)
            .client(OkHttpManager.getOkHttpClient(needLog = true, saveCookie = true))
            .addConverterFactory(GsonConverterFactory.create())
            .build()


}
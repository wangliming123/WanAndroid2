package com.wlm.baselib.utils

import com.wlm.baselib.BaseApp
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import java.util.concurrent.TimeUnit

object OkHttpManager {

    /**
     * 获取okHttpClient
     * @param cacheSize 缓存大小，默认50M
     * @param needLog 是否打印log
     * @param saveCookie 是否持久化cookie
     * @param token token过滤
     * @param logLevel log级别，needLog为true生效，默认body级别
     *
     */
    fun getOkHttpClient(
        cacheSize: Long = 1024 * 1024 * 50,
        needLog: Boolean = false,
        logLevel: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BODY,
        saveCookie: Boolean = false,
        token: Boolean = false,
        interceptors: List<Interceptor> = emptyList()
    ): OkHttpClient {
        return OkHttpClient.Builder().apply {
            val cacheFile = File(BaseApp.instance.cacheDir, "cache")
            val cache = Cache(cacheFile, cacheSize)//50Mb 缓存的大小
            if (needLog) {
                //添加一个log拦截器,打印所有的log
                val httpLoggingInterceptor = HttpLoggingInterceptor()
                //可以设置请求过滤的水平,body,basic,headers
                httpLoggingInterceptor.level = logLevel
                addInterceptor(httpLoggingInterceptor)
            }
            cache(cache)
            //持久化cookie
            if (saveCookie) {
                addInterceptor(receivedCookieInterceptor())
                    .addInterceptor(addCookieInterceptor())
            }
            //token过滤
            if (token) {
                addInterceptor(addHeaderTokenInterceptor())
            }
            interceptors.forEach {
                addInterceptor(it)
            }
        }.connectTimeout(60L, TimeUnit.SECONDS)
            .readTimeout(60L, TimeUnit.SECONDS)
            .writeTimeout(60L, TimeUnit.SECONDS)
            .build()

    }

    private var cookie by SharedPrefs("cookie", "")
    private fun receivedCookieInterceptor(): Interceptor {
        return Interceptor { chain ->
            val response = chain.proceed(chain.request())
            val cookies = response.headers("Set-Cookie")
            if (cookies.isNotEmpty()) {
                val sb = StringBuilder()
                cookies.forEach {
                    sb.append("$it,")
                }
                cookie = sb.toString()
            }
            response
        }
    }

    private fun addCookieInterceptor(): Interceptor {
        return Interceptor { chain ->
            val originRequest = chain.request()
            val requestBuilder = originRequest.newBuilder()
                .method(originRequest.method, originRequest.body)
            val cookies = cookie.split(",")
            cookies.forEach {
                if (it.isNotEmpty()) {
                    requestBuilder.addHeader("Cookie", it)
                }
            }
            val request = requestBuilder.build()
            chain.proceed(request)
        }
    }

    private val token: String by SharedPrefs("token", "")

    private fun addHeaderTokenInterceptor(): Interceptor {
        return Interceptor { chain ->
            val originRequest = chain.request()
            val requestBuilder = originRequest.newBuilder()
                .header("token", token)
                .method(originRequest.method, originRequest.body)
            val request = requestBuilder.build()
            chain.proceed(request)
        }
    }

    private fun addQueryParameterInterceptor(keys: List<String>, values: List<String>): Interceptor {
        return Interceptor { chain ->
            val originalRequest = chain.request()
            val modifiedUrl = originalRequest.url.newBuilder().apply {
                // Provide your custom parameter here
                for (i in keys.indices) {
                    addQueryParameter(keys[i], values[i])
                }
            }.build()
            val request = originalRequest.newBuilder().url(modifiedUrl).build()
            chain.proceed(request)
        }
    }

}
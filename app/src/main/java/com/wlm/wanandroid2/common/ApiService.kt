package com.wlm.wanandroid2.common

import com.wlm.wanandroid2.bean.*
import retrofit2.http.*

interface ApiService {

    companion object {
        const val BASE_URL = "https://www.wanandroid.com/"
//        const val TOOL_URL = "http://www.wanandroid.com/tools"
    }


    @FormUrlEncoded
    @POST("user/login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): HttpResponse<User>

    @FormUrlEncoded
    @POST("user/register")
    suspend fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("repassword") rePassword: String
    ): HttpResponse<User>


    @GET("user/logout/json")
    suspend fun logout(): HttpResponse<Any>

    ///page 页码，从1开始
    @GET("article/list/{page}/json")
    suspend fun getArticles(@Path("page") page: Int): HttpResponse<ArticleList>

    @GET("banner/json")
    suspend fun getBanners(): HttpResponse<List<BannerData>>

    @GET("article/top/json")
    suspend fun getTop(): HttpResponse<List<Article>>


    @POST("lg/collect/{id}/json")
    suspend fun collect(@Path("id") id: Int): HttpResponse<Any>


    @POST("lg/uncollect_originId/{origin_id}/json")
    suspend fun unCollect(@Path("origin_id") id: Int): HttpResponse<Any>


    @GET("navi/json")
    suspend fun getNavigation(): HttpResponse<List<Navigation>>

    @GET("project/tree/json")
    suspend fun getProjectTree(): HttpResponse<List<Knowledge>>

    @GET("wxarticle/chapters/json")
    suspend fun getWxList(): HttpResponse<List<Knowledge>>

    ///page 页码，从1开始
    @GET("wxarticle/list/{id}/{page}/json")
    suspend fun getWxArticles(
        @Path("id") id: Int,
        @Path("page") page: Int
    ): HttpResponse<ArticleList>


    ///page 页码，从1开始
    @GET("project/list/{page}/json")
    suspend fun getProjectItem(
        @Path("page") page: Int,
        @Query("cid") cid: Int
    ): HttpResponse<ArticleList>

    ///page 页码，从0开始
    @GET("article/list/{page}/json")
    suspend fun getKnowledgeItem(
        @Path("page") page: Int,
        @Query("cid") cid: Int
    ): HttpResponse<ArticleList>

    @GET("tree/json")
    suspend fun getKnowledgeTree(): HttpResponse<List<Knowledge>>

    @GET("/user_article/list/{page}/json")
    suspend fun getSquareArticle(@Path("page") page: Int): HttpResponse<ArticleList>

    @FormUrlEncoded
    @POST("lg/user_article/add/json")
    suspend fun shareArticle(
        @Field("title") title: String,
        @Field("link") link: String
    ): HttpResponse<Any>

    @GET("lg/collect/list/{page}/json")
    suspend fun getCollectArticles(@Path("page") page: Int): HttpResponse<ArticleList>


    @GET("lg/todo/v2/list/{page}/json")
    suspend fun getTodoList(
        @Path("page") page: Int,
        @Query("status") status: Int
    ): HttpResponse<TodoList>

    @FormUrlEncoded
    @POST("lg/todo/done/{id}/json")
    suspend fun finishTodo(
        @Path("id") id: Int,
        @Field("status") status: Int
    ): HttpResponse<Any>

    @POST("lg/todo/delete/{id}/json")
    suspend fun deleteTodo(@Path("id") id: Int): HttpResponse<Any>

    @FormUrlEncoded
    @POST("lg/todo/add/json")
    suspend fun addTodo(
        @Field("title") title: String,
        @Field("content") content: String,
        @Field("date") date: String
    ): HttpResponse<Any>

    @FormUrlEncoded
    @POST("lg/todo/update/{id}/json")
    suspend fun updateTodo(
        @Path("id") id: Int,
        @Field("title") title: String,
        @Field("content") content: String,
        @Field("date") date: String,
        @Field("status") status: Int
    ): HttpResponse<Any>


}
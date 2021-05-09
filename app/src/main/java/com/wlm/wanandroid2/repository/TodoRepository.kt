package com.wlm.wanandroid2.repository

import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.wlm.baselib.common.Listing
import com.wlm.wanandroid2.bean.HttpResponse
import com.wlm.wanandroid2.bean.Todo
import com.wlm.wanandroid2.bean.TodoList
import com.wlm.wanandroid2.common.RetrofitManager
import com.wlm.wanandroid2.datasource.TodoDataSourceFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object TodoRepository {


    fun getTodoListing(
        pageSize: Int,
        sourceFactory: TodoDataSourceFactory
    ): Listing<Todo> {
        val pagedList = LivePagedListBuilder(
            sourceFactory, PagedList.Config.Builder()
                .setEnablePlaceholders(true)
                .setInitialLoadSizeHint(pageSize * 2)
                .setPageSize(pageSize).build()
        ).build()
        return Listing(pagedList, refresh = { sourceFactory.source.value?.invalidate() })
    }

    suspend fun getTodoList(page: Int, status: Int): HttpResponse<TodoList> {
        return withContext(Dispatchers.IO) {
            RetrofitManager.service.getTodoList(page, status)
        }
    }

    suspend fun finishTodo(id: Int, status: Int): HttpResponse<Any> {
        return withContext(Dispatchers.Default) {
            RetrofitManager.service.finishTodo(id, status)
        }
    }

    suspend fun deleteTodo(id: Int): HttpResponse<Any> {
        return withContext(Dispatchers.Default) {
            RetrofitManager.service.deleteTodo(id)
        }
    }

    suspend fun addTodo(title: String, content: String, dateStr: String): HttpResponse<Any> {
        return withContext(Dispatchers.Default) {
            RetrofitManager.service.addTodo(title, content, dateStr)
        }
    }

    suspend fun updateTodo(id: Int, title: String, content: String, dateStr: String, status: Int): HttpResponse<Any>  {
        return withContext(Dispatchers.Default) {
            RetrofitManager.service.updateTodo(id, title, content, dateStr, status)
        }
    }
}
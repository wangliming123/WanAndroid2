package com.wlm.wanandroid2.datasource

import androidx.lifecycle.viewModelScope
import androidx.paging.ItemKeyedDataSource
import com.wlm.baselib.common.UiState
import com.wlm.baselib.datasource.BaseDataSourceFactory
import com.wlm.wanandroid2.bean.Todo
import com.wlm.wanandroid2.common.executeResponse
import com.wlm.wanandroid2.repository.TodoRepository
import com.wlm.wanandroid2.viewmodel.TodoViewModel
import kotlinx.coroutines.launch

class TodoDataSourceFactory(private val viewModel: TodoViewModel) : BaseDataSourceFactory<TodoDataSource, Int, Todo>() {
    override fun createDataSource(): TodoDataSource = TodoDataSource(viewModel)
}

class TodoDataSource(private val viewModel: TodoViewModel) : ItemKeyedDataSource<Int, Todo>() {
    private var page = 1
    private var pageCount = 0
    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Todo>) {
        viewModel.run {
            viewModelScope.launch {
                uiState.value = UiState(true, null, null)
                tryCatch(
                    tryBlock = {
                        val result = TodoRepository.getTodoList(page, status)
                        executeResponse(result, {
                            result.data?.let {
                                pageCount = it.pageCount
                                page++
                                uiState.value = UiState(false, null, it)
                                callback.onResult(it.datas)
                            }
                        }, { msg ->
                            uiState.value = UiState(false, msg, null)
                        })
                    },
                    catchBlock = { t ->
                        uiState.value = UiState(false, t.message, null)
                    },
                    handleCancellationExceptionManually = true
                )
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Todo>) {
        if (page > pageCount) return
        viewModel.run {
            viewModelScope.launch {
                tryCatch(
                    tryBlock = {
                        val result = TodoRepository.getTodoList(page, status)
                        executeResponse(result, {
                            result.data?.let {
                                page++
                                callback.onResult(it.datas)
                            }
                        }, {})
                    },
                    handleCancellationExceptionManually = true
                )
            }

        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Todo>) {

    }

    override fun getKey(item: Todo): Int = item.id

}
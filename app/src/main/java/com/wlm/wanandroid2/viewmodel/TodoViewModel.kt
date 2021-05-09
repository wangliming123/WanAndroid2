package com.wlm.wanandroid2.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.wlm.baselib.BaseViewModel
import com.wlm.baselib.common.UiState
import com.wlm.wanandroid2.bean.Todo
import com.wlm.wanandroid2.bean.TodoList
import com.wlm.wanandroid2.common.Constant
import com.wlm.wanandroid2.common.executeResponse
import com.wlm.wanandroid2.datasource.TodoDataSourceFactory
import com.wlm.wanandroid2.repository.TodoRepository
import kotlinx.coroutines.launch

class TodoViewModel : BaseViewModel() {

    private val sourceFactory by lazy { TodoDataSourceFactory(this) }

    var status = Constant.TODO_STATUS_TODO

    private val pageSize = MutableLiveData<Int>()

    private val todoListing = Transformations.map(pageSize) {
        TodoRepository.getTodoListing(it, sourceFactory)
    }

    val pagedList = Transformations.switchMap(todoListing) {
        it.pagedList
    }

    val uiState = MutableLiveData<UiState<TodoList>>()

    val todoState = MutableLiveData<UiState<Int>>()

    fun initLoad(pageSize: Int = 10) {
        if (this.pageSize.value != pageSize) this.pageSize.value = pageSize
    }

    fun refresh() {
        todoListing.value?.refresh?.invoke()
    }


    fun finishTodo(todo: Todo, status: Int) {
        val successState =
            if (status == Constant.TODO_STATUS_TODO) Constant.TODO_FINISH else Constant.TODO_REVERT
        viewModelScope.launch {
            tryCatch(
                tryBlock = {
                    val result = TodoRepository.finishTodo(todo.id, status)
                    executeResponse(result, {
                        todoState.value = UiState(true, null, successState)
                    }, { msg ->
                        todoState.value = UiState(false, msg, successState)
                    })
                },
                catchBlock = { t ->
                    todoState.value = UiState(false, t.message, successState)
                },
                handleCancellationExceptionManually = true
            )
        }
    }

    fun deleteTodo(todo: Todo) {
        viewModelScope.launch {
            tryCatch(
                tryBlock = {
                    val result = TodoRepository.deleteTodo(todo.id)
                    executeResponse(result, {
                        todoState.value = UiState(true, null, Constant.TODO_DELETE)
                    }, { msg ->
                        todoState.value = UiState(false, msg, Constant.TODO_DELETE)
                    })
                },
                catchBlock = { t ->
                    todoState.value = UiState(false, t.message, Constant.TODO_DELETE)
                },
                handleCancellationExceptionManually = true
            )
        }
    }

    fun addTodo(title: String, content: String, dateStr: String) {
        viewModelScope.launch {
            tryCatch(
                tryBlock = {
                    val result = TodoRepository.addTodo(title, content, dateStr)
                    executeResponse(result, {
                        todoState.value = UiState(true, null, Constant.TODO_ADD)
                    }, { msg ->
                        todoState.value = UiState(false, msg, Constant.TODO_ADD)
                    })
                },
                catchBlock = { t ->
                    todoState.value = UiState(false, t.message, Constant.TODO_ADD)
                },
                handleCancellationExceptionManually = true
            )
        }
    }

    fun updateTodo(id: Int, title: String, content: String, dateStr: String, status: Int) {
        viewModelScope.launch {
            tryCatch(
                tryBlock = {
                    val result = TodoRepository.updateTodo(id, title, content, dateStr, status)
                    executeResponse(result, {
                        todoState.value = UiState(true, null, Constant.TODO_UPDATE)
                    }, { msg ->
                        todoState.value = UiState(false, msg, Constant.TODO_UPDATE)
                    })
                },
                catchBlock = { t ->
                    todoState.value = UiState(false, t.message, Constant.TODO_UPDATE)
                },
                handleCancellationExceptionManually = true
            )
        }
    }

}
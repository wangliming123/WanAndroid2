package com.wlm.wanandroid2.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.orhanobut.logger.Logger
import com.wlm.baselib.BaseViewModel
import com.wlm.wanandroid2.common.Constant
import com.wlm.wanandroid2.common.executeResponse
import com.wlm.wanandroid2.db.History
import com.wlm.wanandroid2.repository.ArticleRepository
import com.wlm.wanandroid2.repository.DataBaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoryViewModel : BaseViewModel() {
    companion object {
        private const val PAGE_SIZE = 10
        private const val ENABLE_PLACEHOLDERS = false
    }

    private fun getUserId(): Int {
        val userString = Constant.userString
        return if (userString.isNotEmpty()) {
            userString.split(",")[2].toInt()
        } else {
            -1
        }
    }

    val allHistory = LivePagedListBuilder(DataBaseRepository.historyDao.getAllHistory(getUserId()), PagedList.Config.Builder()
        .setPageSize(PAGE_SIZE)
        .setEnablePlaceholders(ENABLE_PLACEHOLDERS)
        .setInitialLoadSizeHint(PAGE_SIZE * 2)
        .build()).build()

    fun collect(history: History) {

        viewModelScope.launch {
            tryCatch(
                tryBlock = {
                    val result = ArticleRepository.collect(history.id)
                    executeResponse(result, {
                        Logger.d(result.toString())
                        DataBaseRepository.historyDao.update(history.copy(collect = true))
                    }, {
                        Logger.d(result.toString())
                    })
                },
                catchBlock = { t ->
                    Logger.d(t.message)
                }
            )
        }

    }

    fun unCollect(history: History) {
        viewModelScope.launch {
            tryCatch(
                tryBlock = {
                    val result = ArticleRepository.unCollect(history.id)
                    executeResponse(result, {
                        Logger.d(result.toString())
                        DataBaseRepository.historyDao.update(history.copy(collect = false))
                    }, {
                        Logger.d(result.toString())
                    })
                },
                catchBlock = { t ->
                    Logger.d(t.message)
                }
            )
        }
    }

    fun refresh() {
        allHistory.value?.dataSource?.invalidate()
    }
}
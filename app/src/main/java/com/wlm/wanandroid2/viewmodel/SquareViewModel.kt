package com.wlm.wanandroid2.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.orhanobut.logger.Logger
import com.wlm.baselib.BaseViewModel
import com.wlm.baselib.common.UiState
import com.wlm.wanandroid2.bean.ArticleList
import com.wlm.wanandroid2.common.executeResponse
import com.wlm.wanandroid2.datasource.SquareDataSourceFactory
import com.wlm.wanandroid2.repository.ArticleRepository
import kotlinx.coroutines.launch

class SquareViewModel : BaseViewModel() {

    private val sourceFactory by lazy { SquareDataSourceFactory(this) }

    private val pageSize = MutableLiveData<Int>()

    private val listing = Transformations.map(pageSize) {
        ArticleRepository.getSquareListing(it, sourceFactory)
    }

    val pagedList = Transformations.switchMap(listing){
        it.pagedList
    }

    val uiState = MutableLiveData<UiState<ArticleList>>()

    val shareState = MutableLiveData<String>()

    fun refresh() {
        listing.value?.refresh?.invoke()
    }

    fun initLoad(pageSize: Int = 10) {
        if (this.pageSize.value != pageSize) this.pageSize.value = pageSize
    }

    fun collect(id: Int) {
        viewModelScope.launch {
            tryCatch(
                tryBlock = {
                    val result = ArticleRepository.collect(id)
                    executeResponse(result, {
                        Logger.d(result.toString())
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

    fun unCollect(id: Int) {
        viewModelScope.launch {
            tryCatch(
                tryBlock = {
                    val result = ArticleRepository.unCollect(id)
                    executeResponse(result, {
                        Logger.d(result.toString())
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

    fun share(title: String, link: String) {
        viewModelScope.launch {
            tryCatch(
                tryBlock = {
                    val result = ArticleRepository.shareArticle(title, link)
                    executeResponse(result, {
                        shareState.value = "分享成功"
                    }, {
                        shareState.value = "分享失败"
                    })
                }
            )
        }
    }
}
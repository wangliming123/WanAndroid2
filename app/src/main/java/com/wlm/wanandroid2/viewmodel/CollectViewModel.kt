package com.wlm.wanandroid2.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.orhanobut.logger.Logger
import com.wlm.baselib.BaseViewModel
import com.wlm.baselib.common.UiState
import com.wlm.wanandroid2.bean.ArticleList
import com.wlm.wanandroid2.common.executeResponse
import com.wlm.wanandroid2.datasource.CollectDataSourceFactory
import com.wlm.wanandroid2.repository.ArticleRepository
import kotlinx.coroutines.launch

class CollectViewModel : BaseViewModel() {

    private val sourceFactory by lazy { CollectDataSourceFactory(this) }

    private val pageSize = MutableLiveData<Int>()

    private val collectListing = Transformations.map(pageSize) {
        ArticleRepository.getCollectListing(it, sourceFactory)!!
    }

    val pagedList = Transformations.switchMap(collectListing) {
        it.pagedList
    }

    val uiState = MutableLiveData<UiState<ArticleList>>()

    val unCollectId = MutableLiveData<Int>()

    fun initLoad(pageSize: Int = 10) {
        if (this.pageSize.value != pageSize) this.pageSize.value = pageSize
    }

    fun refresh() {
        collectListing.value?.refresh?.invoke()
    }

    fun unCollect(id: Int) {
        viewModelScope.launch {
            tryCatch(
                tryBlock = {
                    val result = ArticleRepository.unCollect(id)
                    executeResponse(result, {
                        Logger.d(result.toString())
                        unCollectId.value = id
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

}
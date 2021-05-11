package com.wlm.wanandroid2.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.wlm.baselib.BaseViewModel
import com.wlm.baselib.common.UiState
import com.wlm.baselib.utils.ToastUtils
import com.wlm.wanandroid2.bean.ArticleList
import com.wlm.wanandroid2.bean.ShareArticles
import com.wlm.wanandroid2.common.executeResponse
import com.wlm.wanandroid2.datasource.HomeDataSourceFactory
import com.wlm.wanandroid2.datasource.ShareDataSourceFactory
import com.wlm.wanandroid2.repository.ArticleRepository
import kotlinx.coroutines.launch

class MyShareViewModel : BaseViewModel() {

    private val sourceFactory by lazy { ShareDataSourceFactory(this) }

    private val pageSize = MutableLiveData<Int>()

    private val shareListing = Transformations.map(pageSize) {
        ArticleRepository.getShareListing(it, sourceFactory)
    }

    val pagedList = Transformations.switchMap(shareListing) {
        it.pagedList
    }

    val uiState = MutableLiveData<UiState<ShareArticles>>()
    val deleteState = MutableLiveData<UiState<Boolean>>()

    fun initLoad(pageSize: Int = 10) {
        if (this.pageSize.value != pageSize) this.pageSize.value = pageSize
    }

    fun refresh() {
        shareListing.value?.refresh?.invoke()
    }

    fun deleteShare(id: Int) {
        viewModelScope.launch {
            tryCatch({
                val result = ArticleRepository.deleteShare(id)
                executeResponse(result, {
                    deleteState.value = UiState(success = true)
                }, {
                    deleteState.value = UiState(success = false)
                })
            }, handleCancellationExceptionManually = true)
        }
    }


}
package com.wlm.wanandroid2.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.wlm.baselib.BaseViewModel
import com.wlm.baselib.common.UiState
import com.wlm.wanandroid2.bean.Navigation
import com.wlm.wanandroid2.datasource.NavigationDataSourceFactory
import com.wlm.wanandroid2.repository.ArticleRepository

class NavigationViewModel : BaseViewModel() {

    private val sourceFactory by lazy { NavigationDataSourceFactory(this) }

    private val pageSize = MutableLiveData<Int>()

    private val navigationListing = Transformations.map(pageSize) {
        ArticleRepository.getNavigationList(it, sourceFactory)
    }

    val pagedList = Transformations.switchMap(navigationListing) {
        it.pagedList
    }

    val uiState = MutableLiveData<UiState<List<Navigation>>>()


    fun initLoad(pageSize: Int = 10) {
        if (this.pageSize.value != pageSize) this.pageSize.value = pageSize
    }

    fun refresh() {
        navigationListing.value?.refresh?.invoke()
    }
}
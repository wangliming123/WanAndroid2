package com.wlm.wanandroid2.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.wlm.baselib.BaseViewModel
import com.wlm.baselib.common.UiState
import com.wlm.wanandroid2.bean.Knowledge
import com.wlm.wanandroid2.datasource.KnowledgeTreeDataSourceFactory
import com.wlm.wanandroid2.repository.ArticleRepository

class KnowledgeTreeViewModel : BaseViewModel() {

    private val sourceFactory by lazy { KnowledgeTreeDataSourceFactory(this) }

    private val pageSize = MutableLiveData<Int>()

    private val treeListing = Transformations.map(pageSize) {
        ArticleRepository.getKnowledgeTreeList(it, sourceFactory)
    }

    val pagedList = Transformations.switchMap(treeListing) {
        it.pagedList
    }

    val uiState = MutableLiveData<UiState<List<Knowledge>>>()

    fun initLoad(pageSize: Int = 10) {
        if (this.pageSize.value != pageSize) this.pageSize.value = pageSize
    }

    fun refresh() {
        treeListing.value?.refresh?.invoke()
    }

}
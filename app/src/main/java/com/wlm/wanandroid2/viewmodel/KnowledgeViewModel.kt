package com.wlm.wanandroid2.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.orhanobut.logger.Logger
import com.wlm.baselib.BaseViewModel
import com.wlm.baselib.common.UiState
import com.wlm.wanandroid2.bean.ArticleList
import com.wlm.wanandroid2.common.executeResponse
import com.wlm.wanandroid2.datasource.KnowledgeDataSourceFactory
import com.wlm.wanandroid2.repository.ArticleRepository
import com.wlm.wanandroid2.ui.fragment.KnowledgeFragment
import kotlinx.coroutines.launch

class KnowledgeViewModel : BaseViewModel() {

    private val knowledgeDataSourceFactory by lazy { KnowledgeDataSourceFactory(this) }

    var knowledgeId : Int = 0
    var type : Int = KnowledgeFragment.TYPE_KNOWLEDGE

    private val pageSize = MutableLiveData<Int>()
    private val knowledgeListing = Transformations.map(pageSize) {
        ArticleRepository.getKnowledgeList(it, knowledgeDataSourceFactory)
    }

    val pagedList = Transformations.switchMap(knowledgeListing) {
        it.pagedList
    }

    val uiState = MutableLiveData<UiState<ArticleList>>()

    fun refresh() {
        knowledgeListing.value?.refresh?.invoke()
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

}
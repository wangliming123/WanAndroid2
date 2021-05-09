package com.wlm.wanandroid2.datasource

import androidx.lifecycle.viewModelScope
import androidx.paging.ItemKeyedDataSource
import com.wlm.baselib.common.UiState
import com.wlm.baselib.datasource.BaseDataSourceFactory
import com.wlm.wanandroid2.bean.Knowledge
import com.wlm.wanandroid2.common.executeResponse
import com.wlm.wanandroid2.repository.ArticleRepository
import com.wlm.wanandroid2.viewmodel.KnowledgeTreeViewModel
import kotlinx.coroutines.launch

class KnowledgeTreeDataSourceFactory(private val viewModel: KnowledgeTreeViewModel) :
    BaseDataSourceFactory<KnowledgeTreeDataSource, Int, Knowledge>() {
    override fun createDataSource(): KnowledgeTreeDataSource = KnowledgeTreeDataSource(viewModel)
}

class KnowledgeTreeDataSource(private val viewModel: KnowledgeTreeViewModel) :
    ItemKeyedDataSource<Int, Knowledge>() {
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Knowledge>
    ) {
        viewModel.run {
            viewModelScope.launch {
                uiState.value = UiState(true, null, null)
                tryCatch(
                    tryBlock = {
                        val result = ArticleRepository.getKnowledgeTree()
                        executeResponse(result, {
                            if (result.data != null) {
                                uiState.value = UiState(false, null, result.data)
                                callback.onResult(result.data)
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

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Knowledge>) {

    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Knowledge>) {

    }

    override fun getKey(item: Knowledge): Int = item.id

}
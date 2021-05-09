package com.wlm.wanandroid2.datasource

import androidx.lifecycle.viewModelScope
import androidx.paging.ItemKeyedDataSource
import com.wlm.baselib.common.UiState
import com.wlm.baselib.datasource.BaseDataSourceFactory
import com.wlm.wanandroid2.bean.Article
import com.wlm.wanandroid2.common.executeResponse
import com.wlm.wanandroid2.repository.ArticleRepository
import com.wlm.wanandroid2.viewmodel.CollectViewModel
import kotlinx.coroutines.launch

class CollectDataSourceFactory(private val viewModel: CollectViewModel) : BaseDataSourceFactory<CollectDataSource, Int, Article>() {
    override fun createDataSource(): CollectDataSource = CollectDataSource(viewModel)
}
class CollectDataSource(private val viewModel: CollectViewModel) :
    ItemKeyedDataSource<Int, Article>() {
    private var page = 0
    private var pageCount = 0
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Article>
    ) {
        viewModel.run {
            viewModelScope.launch {
                uiState.value = UiState(true, null, null)
                tryCatch(
                    tryBlock = {
                        val result = ArticleRepository.getCollectArticles(page)
                        executeResponse(result, {
                            result.data?.let { articleList ->
                                pageCount = articleList.pageCount
                                page++
                                articleList.datas.forEach { it.collect = true }
                                uiState.value = UiState(false, null, articleList)
                                callback.onResult(articleList.datas)
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

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Article>) {
        if (page > pageCount) return
        viewModel.run {
            viewModelScope.launch {
                tryCatch({
                    val result = ArticleRepository.getCollectArticles(page)
                    executeResponse(result, {
                        result.data?.let {
                            page++
                            callback.onResult(it.datas)
                        }
                    }, {})
                }, handleCancellationExceptionManually = true)
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Article>) {

    }

    override fun getKey(item: Article): Int = item.id

}
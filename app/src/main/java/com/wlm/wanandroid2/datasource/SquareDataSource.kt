package com.wlm.wanandroid2.datasource

import androidx.lifecycle.viewModelScope
import androidx.paging.ItemKeyedDataSource
import com.wlm.baselib.common.UiState
import com.wlm.baselib.datasource.BaseDataSourceFactory
import com.wlm.wanandroid2.bean.Article
import com.wlm.wanandroid2.common.executeResponse
import com.wlm.wanandroid2.repository.ArticleRepository
import com.wlm.wanandroid2.viewmodel.SquareViewModel
import kotlinx.coroutines.launch

class SquareDataSourceFactory(private val viewModel: SquareViewModel) :
    BaseDataSourceFactory<SquareDataSource, Int, Article>() {
    override fun createDataSource(): SquareDataSource = SquareDataSource(viewModel)
}

class SquareDataSource(private val viewModel: SquareViewModel) :
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
                        val result = ArticleRepository.getSquareArticle(page)
                        executeResponse(result, {
                            result.data?.let {
                                pageCount = it.pageCount
                                page++
                                uiState.value = UiState(false, null, it)
                                callback.onResult(it.datas)
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
        viewModel.run {
            viewModelScope.launch {
                tryCatch(
                    tryBlock = {
                        val result = ArticleRepository.getSquareArticle(page)
                        executeResponse(result, {
                            result.data?.let {
                                page++
                                callback.onResult(it.datas)
                            }
                        }, {})
                    },
                    handleCancellationExceptionManually = true
                )
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Article>) {

    }

    override fun getKey(item: Article): Int = item.id

}
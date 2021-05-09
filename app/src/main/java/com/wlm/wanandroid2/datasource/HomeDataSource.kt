package com.wlm.wanandroid2.datasource

import androidx.lifecycle.viewModelScope
import androidx.paging.ItemKeyedDataSource
import com.wlm.baselib.common.UiState
import com.wlm.baselib.datasource.BaseDataSourceFactory
import com.wlm.wanandroid2.bean.Article
import com.wlm.wanandroid2.common.executeResponse
import com.wlm.wanandroid2.repository.ArticleRepository
import com.wlm.wanandroid2.viewmodel.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


class HomeDataSourceFactory(private val homeViewModel: HomeViewModel) : BaseDataSourceFactory<HomeDataSource, Int, Article>(), CoroutineScope by MainScope()  {
    override fun createDataSource(): HomeDataSource {
        return HomeDataSource(homeViewModel)
    }

}

class HomeDataSource(private val viewModel: HomeViewModel) : ItemKeyedDataSource<Int, Article>() {

    private var page = 0
    private var pageCount = 0
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Article>
    ) {
        page = 0
        viewModel.run {
            viewModelScope.launch {
                uiState.value = UiState(true, null, null)
                tryCatch(
                    tryBlock = {
                        val result = ArticleRepository.getArticleList(page)
                        val topResult = ArticleRepository.getTop()
                        val bannerResult = ArticleRepository.getBanners()
                        executeResponse(result, {
                            result.data?.let { articleList ->
                                pageCount = articleList.pageCount
                                page++

                                executeResponse(topResult, {
                                    topResult.data?.let {
                                        it.forEach { top ->
                                            top.isTop = true
                                        }
                                        articleList.datas.addAll(0, it)
                                    }
                                }, {})
                                //将banner数据放入article数据的的第一条(article.size + 1)
                                executeResponse(bannerResult, {
                                    val article = articleList.datas[0].copy()
                                    article.bannerList = bannerResult.data
                                    articleList.datas.add(0, article)
                                }, {})
                                uiState.value = UiState(false, null, result.data)
                                callback.onResult(result.data.datas)
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
                    val result = ArticleRepository.getArticleList(page)
                    executeResponse(result, {
                        result.data?.let {
                            page++
                            callback.onResult(result.data.datas)
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
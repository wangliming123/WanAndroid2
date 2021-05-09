package com.wlm.wanandroid2.datasource

import androidx.lifecycle.viewModelScope
import androidx.paging.ItemKeyedDataSource
import com.wlm.baselib.common.UiState
import com.wlm.baselib.datasource.BaseDataSourceFactory
import com.wlm.wanandroid2.bean.Navigation
import com.wlm.wanandroid2.common.executeResponse
import com.wlm.wanandroid2.repository.ArticleRepository
import com.wlm.wanandroid2.viewmodel.NavigationViewModel
import kotlinx.coroutines.launch

class NavigationDataSourceFactory(private val viewModel: NavigationViewModel) : BaseDataSourceFactory<NavigationDataSource, Int, Navigation>() {
    override fun createDataSource(): NavigationDataSource {
        return NavigationDataSource(viewModel)
    }

}

class NavigationDataSource(private val viewModel: NavigationViewModel) : ItemKeyedDataSource<Int, Navigation>() {
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Navigation>
    ) {
        viewModel.run {
            viewModelScope.launch {
                uiState.value = UiState(true, null, null)
                tryCatch(
                    tryBlock = {
                        val result = ArticleRepository.getNavigation()

                        executeResponse(result, {
                            result.data?.let {
                                uiState.value = UiState(false, null, it)
                                callback.onResult(it)
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

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Navigation>) {

    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Navigation>) {

    }

    override fun getKey(item: Navigation): Int = item.cid

}

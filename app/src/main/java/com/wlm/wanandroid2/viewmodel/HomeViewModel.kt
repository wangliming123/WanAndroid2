package com.wlm.wanandroid2.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.orhanobut.logger.Logger
import com.wlm.baselib.common.UiState
import com.wlm.baselib.BaseViewModel
import com.wlm.wanandroid2.bean.ArticleList
import com.wlm.wanandroid2.common.Constant
import com.wlm.wanandroid2.common.executeResponse
import com.wlm.wanandroid2.datasource.HomeDataSourceFactory
import com.wlm.wanandroid2.repository.ArticleRepository
import com.wlm.wanandroid2.repository.LoginRepository
import kotlinx.coroutines.launch

class HomeViewModel : BaseViewModel() {

    private val sourceFactory by lazy { HomeDataSourceFactory(this) }

    val loginState = MutableLiveData<Boolean>()

    private val pageSize = MutableLiveData<Int>()

    private val articleListing = Transformations.map(pageSize) {
        ArticleRepository.getArticleListing(it, sourceFactory)
    }

    val pagedList = Transformations.switchMap(articleListing) {
        it.pagedList
    }


    val uiState = MutableLiveData<UiState<ArticleList>>()

    fun login() {
        if (Constant.userString.isEmpty()) {
            loginState.value = false
        }
        val namePassword = Constant.userString.split(",")
        val username = namePassword[0]
        val password = namePassword[1]
        viewModelScope.launch {
            tryCatch(
                tryBlock = {
                    val result = LoginRepository.login(username, password)
                    executeResponse(result, {
                        loginState.value = true
                    }, {
                        loginState.value = false
                    })
                },
                catchBlock = {
                    loginState.value = false
                }
            )
        }
    }

    fun initLoad(pageSize: Int = 10) {
        if (this.pageSize.value != pageSize) this.pageSize.value = pageSize
    }

    fun refresh() {
        articleListing.value?.refresh?.invoke()
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
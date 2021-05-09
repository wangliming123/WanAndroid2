package com.wlm.wanandroid2.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wlm.baselib.BaseViewModel
import com.wlm.baselib.common.UiState
import com.wlm.wanandroid2.bean.Knowledge
import com.wlm.wanandroid2.common.executeResponse
import com.wlm.wanandroid2.repository.ArticleRepository
import kotlinx.coroutines.launch

class ProjectViewModel : BaseViewModel() {

    val projects = MutableLiveData<List<Knowledge>>()

    val uiState = MutableLiveData<UiState<List<Knowledge>>>()

    fun getProjectTree() {
        viewModelScope.launch {
            uiState.value = UiState(true, null, null)
            tryCatch(
                tryBlock = {
                    val result = ArticleRepository.getProjectTree()
                    executeResponse(result, {
                        result.data?.let {
                            uiState.value = UiState(false, null, it)
                            projects.value = it
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
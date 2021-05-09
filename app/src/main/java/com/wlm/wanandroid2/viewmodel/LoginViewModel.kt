package com.wlm.wanandroid2.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wlm.baselib.BaseViewModel
import com.wlm.baselib.common.UiState
import com.wlm.wanandroid2.common.executeResponse
import com.wlm.wanandroid2.repository.LoginRepository
import kotlinx.coroutines.launch

class LoginViewModel : BaseViewModel() {
    val uiState = MutableLiveData<UiState<String>>()

    fun login(username: String, password: String) {
        viewModelScope.launch {
            uiState.value = UiState(true, null, null)
            tryCatch(
                tryBlock = {
                    val result = LoginRepository.login(username, password)
                    executeResponse(result, {
                        result.data?.let {
                            uiState.value = UiState(false, null, "$username,$password")
                        }
                    }, {
                        uiState.value = UiState(false, "登录失败", null)
                    })
                },
                catchBlock = {
                    uiState.value = UiState(false, "登录失败", null)
                },
                handleCancellationExceptionManually = true
            )

        }
    }

    fun register(username: String, password: String) {
        viewModelScope.launch {
            uiState.value = UiState(true, null, null)
            tryCatch(
                tryBlock = {
                    val result = LoginRepository.register(username, password, password)
                    executeResponse(result, {
                        result.data?.let {
                            uiState.value = UiState(false, null, "$username,$password")
                            LoginRepository.login(username, password)
                        }
                    }, {
                        uiState.value = UiState(false, "注册失败", null)
                    })
                },
                catchBlock = {
                    uiState.value = UiState(false, "注册失败", null)
                },
                handleCancellationExceptionManually = true
            )

        }
    }
}
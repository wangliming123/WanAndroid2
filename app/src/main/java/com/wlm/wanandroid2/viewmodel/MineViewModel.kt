package com.wlm.wanandroid2.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wlm.baselib.BaseViewModel
import com.wlm.baselib.common.UiState
import com.wlm.wanandroid2.common.Constant
import com.wlm.wanandroid2.common.executeResponse
import com.wlm.wanandroid2.repository.LoginRepository
import kotlinx.coroutines.launch

class MineViewModel : BaseViewModel() {

    val logoutState = MutableLiveData<UiState<Boolean>>()
    fun logout() {
        viewModelScope.launch {
            tryCatch(
                tryBlock = {
                    val result = LoginRepository.logout()
                    executeResponse(result, {
                        Constant.isLogin = false
                        Constant.userString = ""
                        logoutState.value = UiState()
                    }, {})
                }
            )
        }
    }
}
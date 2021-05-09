package com.wlm.baselib.common

data class UiState<T>(
    val loading: Boolean = false,
    val error: String? = null,
    val success: T? = null
)
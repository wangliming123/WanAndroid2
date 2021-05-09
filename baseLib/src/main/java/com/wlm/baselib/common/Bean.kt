package com.wlm.baselib.common

import androidx.lifecycle.LiveData
import androidx.paging.PagedList

data class Listing<T>(
    val pagedList: LiveData<PagedList<T>>,
    val refresh: () -> Unit //刷新
)
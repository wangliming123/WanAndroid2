package com.wlm.baselib.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.ItemKeyedDataSource

abstract class BaseDataSourceFactory<DS : ItemKeyedDataSource<K, V>, K, V> : DataSource.Factory<K, V>() {

    val source = MutableLiveData<DS>()

    override fun create(): DataSource<K, V> {
        val dataSource = createDataSource()
        source.postValue(dataSource)
        return dataSource
    }

    abstract fun createDataSource(): DS

}
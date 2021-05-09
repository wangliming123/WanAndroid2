package com.wlm.baselib.ui

import android.util.SparseArray
import com.wlm.baselib.BaseViewModel

class DataBindingConfig<VM : BaseViewModel>(
    val layout: Int,
    val vmVariableId: Int,
    val viewModel: VM
) {

    private var bindingParams: SparseArray<Any> = SparseArray()

    fun getBindingParams(): SparseArray<Any> = bindingParams

    fun addBindingParams(variableId: Int, objezt: Any): DataBindingConfig<VM> {
        if (bindingParams.get(variableId) == null) {
            bindingParams.put(variableId, objezt)
        }
        return this
    }
}
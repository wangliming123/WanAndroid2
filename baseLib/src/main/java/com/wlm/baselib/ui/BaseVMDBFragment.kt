package com.wlm.baselib.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.forEach
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.classic.common.MultipleStatusView
import com.orhanobut.logger.Logger
import com.wlm.baselib.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

abstract class BaseVMDBFragment<VM : BaseViewModel, VDB : ViewDataBinding> : Fragment(), EasyPermissions.PermissionCallbacks {

    lateinit var mBinding: VDB

    lateinit var mViewModel: VM
    abstract val providerVMClass: Class<VM>


    protected var statusView: MultipleStatusView? = null

    protected var isRefreshFromPull = false

    abstract fun getDataBindingConfig() : DataBindingConfig<VM>

    /**
     * 初始化
     */
    abstract fun init(savedInstanceState: Bundle?)


    open fun childStatusView() : MultipleStatusView? = null

    open fun retry() {

    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        initVM()
        startObserve()
        mBinding =
                DataBindingUtil.inflate(inflater, getDataBindingConfig().layout, container, false)
        mBinding.lifecycleOwner = viewLifecycleOwner
        mBinding.setVariable(
                getDataBindingConfig().vmVariableId,
                getDataBindingConfig().viewModel
        )
        val bindingParams = getDataBindingConfig().getBindingParams()
        bindingParams.forEach { key, value ->
            mBinding.setVariable(key, value)
        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        statusView = childStatusView()
        statusView?.setOnClickListener {
            when (statusView?.viewStatus) {
                MultipleStatusView.STATUS_ERROR, MultipleStatusView.STATUS_EMPTY ->
                    retry()
            }
        }
        init(savedInstanceState)
    }


    open fun startObserve() {
        mViewModel.mException.observe(viewLifecycleOwner, Observer { onError(it) })
    }

    private fun initVM() {
//        mViewModel = ViewModelProviders.of(this).get(providerVMClass)
        mViewModel = ViewModelProvider(this).get(providerVMClass)
        lifecycle.addObserver(mViewModel)
    }

    override fun onDestroy() {
        lifecycle.removeObserver(mViewModel)
        super.onDestroy()
    }

    open fun onError(e: Throwable) {
        Logger.d("${providerVMClass.name} onError: ${e.message}")
    }



    /**
     * 重写要申请权限的Activity或者Fragment的onRequestPermissionsResult()方法，
     * 在里面调用EasyPermissions.onRequestPermissionsResult()，实现回调。
     *
     * @param requestCode  权限请求的识别码
     * @param permissions  申请的权限
     * @param grantResults 授权结果
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    /**
     * 当权限被成功申请的时候执行回调
     *
     * @param requestCode 权限请求的识别码
     * @param perms       申请的权限的名字
     */
    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        Logger.i("EasyPermission", "成功申请权限$perms")
    }

    /**
     * 当权限申请失败的时候执行的回调
     *
     * @param requestCode 权限请求的识别码
     * @param perms       申请的权限的名字
     */
    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        val sb = StringBuffer()
        for (str in perms) {
            sb.append(str)
            sb.append("\n")
        }
        sb.replace(sb.length - 2, sb.length, "")
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
//            ToastUtils.show("已拒绝权限${sb}并不在询问")
            AppSettingsDialog.Builder(this)
                .setRationale("此功能需要${sb}权限，否则无法正常使用，是否打开设置")
                .setPositiveButton("是")
                .setNegativeButton("否")
                .build()
                .show()
        }
    }
}
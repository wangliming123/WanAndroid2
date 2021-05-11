package com.wlm.wanandroid2.ui.activity

import android.graphics.Color
import android.os.Bundle
import androidx.lifecycle.Observer
import com.classic.common.MultipleStatusView
import com.orhanobut.logger.Logger
import com.wlm.baselib.ui.BaseVMDBActivity
import com.wlm.baselib.ui.DataBindingConfig
import com.wlm.baselib.utils.ToastUtils
import com.wlm.wanandroid2.BR
import com.wlm.wanandroid2.R
import com.wlm.wanandroid2.databinding.ActivityMyShareBinding
import com.wlm.wanandroid2.ui.adapter.MyShareAdapter
import com.wlm.wanandroid2.viewmodel.MyShareViewModel

class MyShareActivity : BaseVMDBActivity<MyShareViewModel, ActivityMyShareBinding>() {
    override val providerVMClass: Class<MyShareViewModel> = MyShareViewModel::class.java

    override fun getDataBindingConfig(): DataBindingConfig<MyShareViewModel> =
        DataBindingConfig(R.layout.activity_my_share, BR.MyShareViewModel, mViewModel)

    override fun childStatusView(): MultipleStatusView? = mBinding.statusView

    private val adapter by lazy { MyShareAdapter() }

    override fun init(savedInstanceState: Bundle?) {
        mBinding.shareToolbar.run {
            setSupportActionBar(this)
            title = getString(R.string.str_my_share)
            setNavigationIcon(R.drawable.arrow_back)
            setNavigationOnClickListener { onBackPressed() }
        }

        initRecycler()
        mBinding.layoutRefresh.run {
            setColorSchemeColors(Color.BLUE, Color.GREEN)
            setOnRefreshListener {
                isRefreshFromPull = true
                mViewModel.refresh()
            }
        }
    }

    private fun initRecycler() {
        mBinding.rvShare.adapter = adapter
        adapter.setOnDeleteListener {
            mViewModel.deleteShare(it.id)
        }
    }

    override fun startObserve() {
        super.startObserve()

        mViewModel.run {

            pagedList.observe(this@MyShareActivity, Observer {
                adapter.submitList(it)
            })
            uiState.observe(this@MyShareActivity, Observer { state ->
                mBinding.layoutRefresh.isRefreshing = state.loading

                if (state.loading) {
                    if (isRefreshFromPull) {
                        isRefreshFromPull = false
                    } else {
                        statusView?.showLoading()
                    }
                }

                state.success?.let {
                    if (it.datas.isEmpty()) {
                        statusView?.showEmpty()
                    } else {
                        statusView?.showContent()
                    }
                }

                state.error?.let {
                    statusView?.showError()
                    Logger.d("load_error", it)
                }
            })
            deleteState.observe(this@MyShareActivity, Observer { state ->
                state.success?.let {
                    if (it) {
                        ToastUtils.show("删除成功")
                        mViewModel.refresh()
                    } else {
                        ToastUtils.show("删除失败")
                    }
                }
            })
            initLoad()
        }
    }

    override fun retry() {
        mViewModel.refresh()
    }
}
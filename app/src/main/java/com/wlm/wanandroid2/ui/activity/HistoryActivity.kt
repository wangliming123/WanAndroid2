package com.wlm.wanandroid2.ui.activity

import android.graphics.Color
import android.os.Bundle
import androidx.lifecycle.Observer
import com.classic.common.MultipleStatusView
import com.wlm.baselib.common.startKtxActivity
import com.wlm.baselib.ui.BaseVMDBActivity
import com.wlm.baselib.ui.DataBindingConfig
import com.wlm.wanandroid2.BR
import com.wlm.wanandroid2.R
import com.wlm.wanandroid2.common.Constant
import com.wlm.wanandroid2.databinding.ActivityHistoryBinding
import com.wlm.wanandroid2.repository.DataBaseRepository
import com.wlm.wanandroid2.ui.adapter.HistoryAdapter
import com.wlm.wanandroid2.viewmodel.HistoryViewModel

class HistoryActivity : BaseVMDBActivity<HistoryViewModel, ActivityHistoryBinding>() {
    override val providerVMClass: Class<HistoryViewModel> = HistoryViewModel::class.java

    override fun getDataBindingConfig(): DataBindingConfig<HistoryViewModel> =
        DataBindingConfig(R.layout.activity_history, BR.HistoryViewModel, mViewModel)

    override fun childStatusView(): MultipleStatusView? = mBinding.multipleStatusView

    private val adapter by lazy { HistoryAdapter() }

    override fun init(savedInstanceState: Bundle?) {
        mBinding.historyHeader.let { toolbar ->
            toolbar.title = getString(R.string.str_history)
            setSupportActionBar(toolbar)
            toolbar.setNavigationIcon(R.drawable.arrow_back)
            toolbar.setNavigationOnClickListener { onBackPressed() }
        }

        mBinding.layoutRefresh.setColorSchemeColors(Color.GREEN, Color.BLUE)
        mBinding.layoutRefresh.setOnRefreshListener {
            isRefreshFromPull = true
            mViewModel.refresh()
            mBinding.layoutRefresh.isRefreshing = false
        }

        initRecyclerView()
    }

    private fun initRecyclerView() {
        mBinding.rvHistory.adapter = adapter
        adapter.setOnCollectListener {
            if (Constant.isLogin) {
                if (it.collect) {
                    mViewModel.unCollect(it)
                }else {
                    mViewModel.collect(it)
                }
            } else {
                startKtxActivity<LoginActivity>()
                finish()
            }
        }
    }

    override fun startObserve() {
        super.startObserve()
        mViewModel.allHistory.observe(this, Observer {
            adapter.submitList(it)
        })
    }
}
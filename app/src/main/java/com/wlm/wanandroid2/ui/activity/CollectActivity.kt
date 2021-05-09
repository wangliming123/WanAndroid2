package com.wlm.wanandroid2.ui.activity

import android.graphics.Color
import android.os.Bundle
import androidx.lifecycle.Observer
import com.classic.common.MultipleStatusView
import com.orhanobut.logger.Logger
import com.wlm.baselib.ui.BaseVMDBActivity
import com.wlm.baselib.ui.DataBindingConfig
import com.wlm.wanandroid2.BR
import com.wlm.wanandroid2.R
import com.wlm.wanandroid2.common.Constant
import com.wlm.wanandroid2.databinding.ActivityCollectBinding
import com.wlm.wanandroid2.ui.adapter.DefaultArticleAdapter
import com.wlm.wanandroid2.viewmodel.CollectViewModel

class CollectActivity : BaseVMDBActivity<CollectViewModel, ActivityCollectBinding>() {
    override val providerVMClass: Class<CollectViewModel> = CollectViewModel::class.java

    override fun childStatusView(): MultipleStatusView? = mBinding.multipleStatusView

    private val adapter by lazy { DefaultArticleAdapter() }
    private var isRefreshFromUnCollect = false


    override fun getDataBindingConfig(): DataBindingConfig<CollectViewModel> =
        DataBindingConfig(R.layout.activity_collect, BR.CollectViewModel, mViewModel)

    override fun init(savedInstanceState: Bundle?) {
        mBinding.collectHeader.let {  toolbar ->
            toolbar.title = getString(R.string.str_collect)
            setSupportActionBar(toolbar)
            toolbar.setNavigationIcon(R.drawable.arrow_back)
            toolbar.setNavigationOnClickListener { onBackPressed() }
        }


        mBinding.refreshCollect.setColorSchemeColors(Color.GREEN, Color.BLUE)
        mBinding.refreshCollect.setOnRefreshListener {
            isRefreshFromPull = true
            mViewModel.refresh()
        }

        initRecyclerView()
    }

    private fun initRecyclerView() {
        mBinding.rvCollect.adapter = adapter
        adapter.setOnCollectListener { data ->
            mViewModel.unCollect(data.originId)
        }
    }

    override fun startObserve() {
        super.startObserve()

        mViewModel.run {
            pagedList.observe(this@CollectActivity, Observer {
                adapter.submitList(it)
            })
            uiState.observe(this@CollectActivity, Observer { state ->
                if (!isRefreshFromUnCollect) {
                    mBinding.refreshCollect.isRefreshing = state.loading
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
                }else {
                    isRefreshFromUnCollect = true
                }
            })

            unCollectId.observe(this@CollectActivity, Observer {
                isRefreshFromUnCollect = true
                refresh()
            })

            initLoad()
        }
    }

    override fun retry() {
        mViewModel.refresh()
    }


}

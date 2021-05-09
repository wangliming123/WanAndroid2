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
import com.wlm.wanandroid2.databinding.ActivityKnowledgeTreeBinding
import com.wlm.wanandroid2.ui.adapter.KnowledgeTreeAdapter
import com.wlm.wanandroid2.viewmodel.KnowledgeTreeViewModel

class KnowledgeTreeActivity : BaseVMDBActivity<KnowledgeTreeViewModel, ActivityKnowledgeTreeBinding>() {
    override val providerVMClass: Class<KnowledgeTreeViewModel> = KnowledgeTreeViewModel::class.java

    override fun getDataBindingConfig(): DataBindingConfig<KnowledgeTreeViewModel> =
        DataBindingConfig(R.layout.activity_knowledge_tree, BR.KnowledgeTreeViewModel, mViewModel)


    private val adapter by lazy { KnowledgeTreeAdapter() }

    override fun childStatusView(): MultipleStatusView? = mBinding.multipleStatusView
    override fun init(savedInstanceState: Bundle?) {

        mBinding.knowledgeTreeToolbar.run {
            setSupportActionBar(this)
            title = getString(R.string.str_project)
            setNavigationIcon(R.drawable.arrow_back)
            setNavigationOnClickListener { finish() }
        }

        mBinding.rvRefresh.adapter = adapter

        mBinding.layoutRefresh.setColorSchemeColors(Color.GREEN, Color.BLUE)
        mBinding.layoutRefresh.setOnRefreshListener {
            isRefreshFromPull = true
            mViewModel.refresh()
        }
    }


    override fun startObserve() {
        super.startObserve()
        mViewModel.run {
            pagedList.observe(this@KnowledgeTreeActivity, Observer {
                adapter.submitList(it)
            })

            uiState.observe(this@KnowledgeTreeActivity, Observer { state->
                mBinding.layoutRefresh.isRefreshing = state.loading

                if (state.loading) {
                    if (isRefreshFromPull) {
                        isRefreshFromPull = false
                    } else {
                        statusView?.showLoading()
                    }
                }

                state.success?.let {
                    if (it.isEmpty()) {
                        statusView?.showEmpty()
                    } else {
                        statusView?.showContent()
                    }
                }

                state.error?.let {
                    statusView?.showError()
                    Logger.d("knowledge tree error", it)
                }
            })

            initLoad()
        }
    }


    override fun retry() {
        mViewModel.refresh()
    }
}
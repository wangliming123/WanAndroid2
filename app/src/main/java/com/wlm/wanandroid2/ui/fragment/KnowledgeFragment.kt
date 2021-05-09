package com.wlm.wanandroid2.ui.fragment

import android.graphics.Color
import android.os.Bundle
import androidx.lifecycle.Observer
import com.classic.common.MultipleStatusView
import com.orhanobut.logger.Logger
import com.wlm.baselib.common.startKtxActivity
import com.wlm.baselib.ui.BaseVMDBFragment
import com.wlm.baselib.ui.DataBindingConfig
import com.wlm.wanandroid2.BR
import com.wlm.wanandroid2.R
import com.wlm.wanandroid2.bean.Knowledge
import com.wlm.wanandroid2.common.Constant
import com.wlm.wanandroid2.databinding.FragmentKnowledgeBinding
import com.wlm.wanandroid2.ui.activity.LoginActivity
import com.wlm.wanandroid2.ui.adapter.DefaultArticleAdapter
import com.wlm.wanandroid2.viewmodel.KnowledgeViewModel

class KnowledgeFragment(private val knowledge: Knowledge, private val type: Int = TYPE_KNOWLEDGE) :
    BaseVMDBFragment<KnowledgeViewModel, FragmentKnowledgeBinding>(){

    companion object {
        const val TYPE_KNOWLEDGE = 0
        const val TYPE_PROJECT = 1
        const val TYPE_WX_ARTICLE = 2
    }
    override val providerVMClass: Class<KnowledgeViewModel> = KnowledgeViewModel::class.java


    override fun getDataBindingConfig(): DataBindingConfig<KnowledgeViewModel> =
        DataBindingConfig(R.layout.fragment_knowledge, BR.KnowledgeViewModel, mViewModel)

    private val adapter by lazy { DefaultArticleAdapter() }


    override fun childStatusView(): MultipleStatusView? = mBinding.multipleStatusView


    override fun init(savedInstanceState: Bundle?) {
        mViewModel.knowledgeId = knowledge.id
        mViewModel.type = type

        initRecyclerView()
        
        mBinding.layoutRefresh.setColorSchemeColors(Color.GREEN, Color.BLUE)
        mBinding.layoutRefresh.setOnRefreshListener {
            isRefreshFromPull = true
            mViewModel.refresh()
        }
    }

    private fun initRecyclerView() {
        mBinding.rvRefresh.adapter = adapter
        adapter.setOnCollectListener { data ->
            if (Constant.isLogin) {
                if (data.collect) {
                    data.collect = false
                    mViewModel.unCollect(data.id)
                }else {
                    data.collect = true
                    mViewModel.collect(data.id)
                }
                adapter.notifyDataSetChanged()
            } else {
                startKtxActivity<LoginActivity>()
            }
        }
    }

    override fun startObserve() {
        super.startObserve()
        mViewModel.run {
            pagedList.observe(this@KnowledgeFragment, Observer {
                adapter.submitList(it)
            })
            uiState.observe(this@KnowledgeFragment, Observer { state ->
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
                    }else {
                        statusView?.showContent()
                    }
                }

                state.error?.let {
                    statusView?.showError()
                    Logger.d("load_error", it)
                }
            })

            initLoad()
        }
    }

    override fun retry() {
        mViewModel.refresh()
    }

}
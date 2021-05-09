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
import com.wlm.wanandroid2.common.Constant
import com.wlm.wanandroid2.databinding.FragmentHomeBinding
import com.wlm.wanandroid2.ui.activity.LoginActivity
import com.wlm.wanandroid2.ui.adapter.HomeAdapter
import com.wlm.wanandroid2.viewmodel.HomeViewModel

class HomeFragment : BaseVMDBFragment<HomeViewModel, FragmentHomeBinding>() {
    override val providerVMClass: Class<HomeViewModel> = HomeViewModel::class.java

    override fun getDataBindingConfig(): DataBindingConfig<HomeViewModel> =
        DataBindingConfig(R.layout.fragment_home, BR.HomeViewModel, mViewModel)

    private val adapter by lazy { HomeAdapter() }

    override fun childStatusView(): MultipleStatusView? = mBinding.homeStatusView

    override fun init(savedInstanceState: Bundle?) {
        initUser()
        initRecycler()

        mBinding.homeRefresh.run {
            setColorSchemeColors(Color.BLUE, Color.GREEN)
            setOnRefreshListener {
                isRefreshFromPull = true
                mViewModel.refresh()
            }
        }
    }

    private fun initUser() {
        if (Constant.isLogin) {
            mViewModel.login()
        }
    }

    private fun initRecycler() {
        mBinding.rvHome.adapter = adapter
        adapter.setOnCollectListener {
            if (Constant.isLogin) {
                if (it.collect) {
                    it.collect = false
                    mViewModel.unCollect(it.id)
                }else {
                    it.collect = true
                    mViewModel.collect(it.id)
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
            loginState.observe(this@HomeFragment, Observer {
                Constant.isLogin = it
            })

            pagedList.observe(this@HomeFragment, Observer {
                adapter.submitList(it)
            })

            uiState.observe(this@HomeFragment, Observer { state->
                mBinding.homeRefresh.isRefreshing = state.loading

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
}
package com.wlm.wanandroid2.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import com.classic.common.MultipleStatusView
import com.google.android.material.tabs.TabLayout
import com.orhanobut.logger.Logger
import com.wlm.baselib.common.startKtxActivity
import com.wlm.baselib.ui.BaseVMDBActivity
import com.wlm.baselib.ui.DataBindingConfig
import com.wlm.baselib.utils.ToastUtils
import com.wlm.wanandroid2.BR
import com.wlm.wanandroid2.R
import com.wlm.wanandroid2.common.Constant
import com.wlm.wanandroid2.databinding.ActivitySquareBinding
import com.wlm.wanandroid2.ui.adapter.DefaultArticleAdapter
import com.wlm.wanandroid2.viewmodel.SquareViewModel

class SquareActivity : BaseVMDBActivity<SquareViewModel, ActivitySquareBinding>() {
    override val providerVMClass: Class<SquareViewModel> = SquareViewModel::class.java

    override fun getDataBindingConfig(): DataBindingConfig<SquareViewModel> =
        DataBindingConfig(R.layout.activity_square, BR.SquareViewModel, mViewModel)


    override fun childStatusView(): MultipleStatusView? = mBinding.multipleStatusView

    private val adapter by lazy { DefaultArticleAdapter(DefaultArticleAdapter.TYPE_SHARE) }

    override fun init(savedInstanceState: Bundle?) {
        mBinding.squareToolbar.run {
            setSupportActionBar(this)
            title = getString(R.string.str_square)
            setNavigationIcon(R.drawable.arrow_back)
            setNavigationOnClickListener { onBackPressed() }
        }
        initRecyclerView()
        mBinding.layoutRefresh.setColorSchemeColors(Color.GREEN, Color.BLUE)
        mBinding.layoutRefresh.setOnRefreshListener {
            isRefreshFromPull = true
            mViewModel.refresh()
        }
        initShare()
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

    private fun initShare() {
        mBinding.fabAddShare.setOnClickListener {
            statusView?.visibility = View.GONE
            mBinding.layoutShare.visibility = View.VISIBLE
        }
        mBinding.btnShare.setOnClickListener {
            val title = mBinding.etTitle.text.toString()
            val link = mBinding.etLink.text.toString()
            when{
                title.isEmpty() -> {
                    mBinding.tvTips.visibility = View.VISIBLE
                    mBinding.tvTips.text = getString(R.string.str_tip_title_not_empty)
                }
                link.isEmpty() -> {
                    mBinding.tvTips.visibility = View.VISIBLE
                    mBinding.tvTips.text = getString(R.string.str_tip_link_not_empty)
                }
                !link.startsWith("http://") && !link.startsWith("https://") -> {
                    mBinding.tvTips.visibility = View.VISIBLE
                    mBinding.tvTips.text = getString(R.string.str_tip_link_not_valid)
                }
                else -> {
                    mBinding.tvTips.visibility = View.GONE
                    mViewModel.share(title, link)
                }
            }
        }
        mBinding.btnCancel.setOnClickListener {
            statusView?.visibility = View.VISIBLE
            mBinding.layoutShare.visibility = View.GONE
        }
    }


    override fun startObserve() {
        super.startObserve()
        mViewModel.run {

            pagedList.observe(this@SquareActivity, Observer {
                adapter.submitList(it)
            })

            uiState.observe(this@SquareActivity, Observer { state ->
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
            shareState.observe(this@SquareActivity, Observer {
                ToastUtils.show(it)
                if ("分享成功" == it) {
                    statusView?.visibility = View.VISIBLE
                    mBinding.layoutShare.visibility = View.GONE
                }
            })

            initLoad()
        }
    }

    override fun retry() {
        mViewModel.refresh()
    }
}
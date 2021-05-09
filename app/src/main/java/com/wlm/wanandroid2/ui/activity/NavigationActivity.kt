package com.wlm.wanandroid2.ui.activity

import android.graphics.Color
import android.os.Bundle
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.classic.common.MultipleStatusView
import com.orhanobut.logger.Logger
import com.wlm.baselib.ui.BaseVMDBActivity
import com.wlm.baselib.ui.DataBindingConfig
import com.wlm.wanandroid2.BR
import com.wlm.wanandroid2.R
import com.wlm.wanandroid2.databinding.ActivityNavigationBinding
import com.wlm.wanandroid2.ui.adapter.NavigationAdapter
import com.wlm.wanandroid2.ui.adapter.NavigationArticleAdapter
import com.wlm.wanandroid2.viewmodel.NavigationViewModel

class NavigationActivity : BaseVMDBActivity<NavigationViewModel, ActivityNavigationBinding>() {


    override fun getDataBindingConfig(): DataBindingConfig<NavigationViewModel> =
        DataBindingConfig(R.layout.activity_navigation, BR.NavigationViewModel, mViewModel)

    override val providerVMClass: Class<NavigationViewModel> = NavigationViewModel::class.java
    private var checkedPosition: Int = 0
    private val adapterNavigation by lazy {
        NavigationAdapter {
            checkedPosition = it
            mBinding.rvNavigationArticle.scrollToPosition(it)
        }
    }

    private val adapterNavigationArticle by lazy { NavigationArticleAdapter() }

    override fun childStatusView(): MultipleStatusView? = mBinding.multipleStatusView

    override fun init(savedInstanceState: Bundle?) {

        mBinding.navigationHeader.let { toolbar ->

            toolbar.title = getString(R.string.str_navigation)
            setSupportActionBar(toolbar)
            toolbar.navigationIcon = ResourcesCompat.getDrawable(resources, R.drawable.arrow_back, null)
            toolbar.setNavigationOnClickListener { finish() }
        }

        initRecycler()

        mBinding.layoutRefresh.setColorSchemeColors(Color.GREEN, Color.BLUE)
        mBinding.layoutRefresh.setOnRefreshListener {
            isRefreshFromPull = true
            mViewModel.refresh()
            adapterNavigation.setChecked(0)
        }
    }


    private fun initRecycler() {
        mBinding.rvNavigation.adapter = adapterNavigation
        mBinding.rvNavigationArticle.adapter = adapterNavigationArticle
        mBinding.rvNavigationArticle.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val index = (recyclerView.layoutManager as LinearLayoutManager)
                        .findFirstVisibleItemPosition()
                    if (index != checkedPosition) {
                        mBinding.rvNavigation.scrollToPosition(index)
                        adapterNavigation.setChecked(index)
                        checkedPosition = index
                    }
                }
            }
        })
    }

    override fun startObserve() {
        super.startObserve()
        mViewModel.run {
            pagedList.observe(this@NavigationActivity, Observer {
                adapterNavigation.submitList(it)
                adapterNavigationArticle.submitList(it)
            })

            uiState.observe(this@NavigationActivity, Observer { state ->
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

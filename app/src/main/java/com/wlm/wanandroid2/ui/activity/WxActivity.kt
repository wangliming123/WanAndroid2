package com.wlm.wanandroid2.ui.activity

import android.os.Bundle
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import com.classic.common.MultipleStatusView
import com.google.android.material.tabs.TabLayout
import com.orhanobut.logger.Logger
import com.wlm.baselib.ui.BaseVMDBActivity
import com.wlm.baselib.ui.DataBindingConfig
import com.wlm.wanandroid2.BR
import com.wlm.wanandroid2.R
import com.wlm.wanandroid2.bean.Knowledge
import com.wlm.wanandroid2.databinding.ActivityWxBinding
import com.wlm.wanandroid2.ui.fragment.KnowledgeFragment
import com.wlm.wanandroid2.viewmodel.WxViewModel

class WxActivity : BaseVMDBActivity<WxViewModel, ActivityWxBinding>() {
    override val providerVMClass: Class<WxViewModel> = WxViewModel::class.java

    override fun getDataBindingConfig(): DataBindingConfig<WxViewModel> =
        DataBindingConfig(R.layout.activity_wx, BR.WxViewModel, mViewModel)

    override fun childStatusView(): MultipleStatusView? = mBinding.multipleStatusView

    private val fragments = mutableListOf<Fragment>()

    override fun init(savedInstanceState: Bundle?) {
        mBinding.wxHeader.run {
            setSupportActionBar(this)
            title = getString(R.string.str_wx_article)
            setNavigationIcon(R.drawable.arrow_back)
            setNavigationOnClickListener { onBackPressed() }
        }
        mViewModel.getWxList()

    }


    override fun startObserve() {
        super.startObserve()

        mViewModel.run {
            wxList.observe(this@WxActivity, Observer {
                initViewPager(it)
                initTabLayout()
            })

            uiState.observe(this@WxActivity, Observer { state ->

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
                    Logger.d("load_error", it)
                }
            })
        }
    }

    private fun initViewPager(wxList: List<Knowledge>) {
        fragments.clear()
        wxList.forEach {
            fragments.add(KnowledgeFragment(it, KnowledgeFragment.TYPE_WX_ARTICLE))
        }
        mBinding.wxArticleViewPager.adapter = object : FragmentPagerAdapter(
            supportFragmentManager,
            BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        ) {
            override fun getItem(position: Int): Fragment = fragments[position]
            override fun getCount(): Int = fragments.size
            override fun getPageTitle(position: Int): CharSequence? =
                HtmlCompat.fromHtml(wxList[position].name, HtmlCompat.FROM_HTML_MODE_LEGACY)
        }
    }

    private fun initTabLayout() {
        mBinding.wxTabLayout.run {
            setupWithViewPager(mBinding.wxArticleViewPager)
            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabReselected(tab: TabLayout.Tab?) {

                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab?.let {
                        mBinding.wxArticleViewPager.currentItem = it.position
                    }
                }
            })
        }
    }
}
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
import com.wlm.wanandroid2.databinding.ActivityProjectBinding
import com.wlm.wanandroid2.ui.fragment.KnowledgeFragment
import com.wlm.wanandroid2.viewmodel.ProjectViewModel

class ProjectActivity : BaseVMDBActivity<ProjectViewModel, ActivityProjectBinding>() {
    override val providerVMClass: Class<ProjectViewModel> = ProjectViewModel::class.java

    override fun getDataBindingConfig(): DataBindingConfig<ProjectViewModel> =
        DataBindingConfig(R.layout.activity_project, BR.ProjectViewModel, mViewModel)

    override fun childStatusView(): MultipleStatusView? = mBinding.multipleStatusView

    private val fragments = mutableListOf<Fragment>()

    override fun init(savedInstanceState: Bundle?) {

        mBinding.projectToolbar.run {
            setSupportActionBar(this)
            title = getString(R.string.str_knowledge_tree)
            setNavigationIcon(R.drawable.arrow_back)
            setNavigationOnClickListener { finish() }
        }
        mViewModel.getProjectTree()

    }


    private fun initViewPager(projects: List<Knowledge>) {
        fragments.clear()
        projects.forEach {
            fragments.add(KnowledgeFragment(it, KnowledgeFragment.TYPE_PROJECT))
        }
        mBinding.projectViewPager.adapter = object : FragmentPagerAdapter(
            supportFragmentManager,
            BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        ) {
            override fun getItem(position: Int): Fragment = fragments[position]
            override fun getCount(): Int = fragments.size
            override fun getPageTitle(position: Int): CharSequence? =
                HtmlCompat.fromHtml(projects[position].name, HtmlCompat.FROM_HTML_MODE_LEGACY)
        }
    }

    private fun initTabLayout() {
        mBinding.projectTabLayout.run {
            setupWithViewPager(mBinding.projectViewPager)
            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabReselected(tab: TabLayout.Tab?) {

                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab?.let {
                        mBinding.projectViewPager.currentItem = it.position
                    }
                }

            })
        }
    }

    override fun startObserve() {
        super.startObserve()
        mViewModel.run {
            projects.observe(this@ProjectActivity, Observer {
                initViewPager(it)
                initTabLayout()
            })

            uiState.observe(this@ProjectActivity, Observer { state ->

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
}
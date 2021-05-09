package com.wlm.wanandroid2.ui.activity

import android.os.Bundle
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.wlm.baselib.ui.BaseDBActivity
import com.wlm.wanandroid2.R
import com.wlm.wanandroid2.common.Constant
import com.wlm.wanandroid2.databinding.ActivityTodoBinding
import com.wlm.wanandroid2.ui.fragment.OnParentRefreshListener
import com.wlm.wanandroid2.ui.fragment.TodoFragment

class TodoActivity : BaseDBActivity<ActivityTodoBinding>() {

    override val layoutId: Int = R.layout.activity_todo

    private var titles = arrayOf(R.string.str_todo, R.string.str_finished)

    private val todoFragment by lazy { TodoFragment() }
    private val finishedTodoFragment by lazy { TodoFragment(Constant.TODO_STATUS_FINISHED, false) }




    override fun init(savedInstanceState: Bundle?) {
        initToolBar()
        initViewPager()
        initTabLayout()
        initFragment()
    }

    private fun initToolBar() {
        mBinding.todoToolbar.run {
            title = getString(R.string.str_todo_list)
            setSupportActionBar(this)
            navigationIcon = ResourcesCompat.getDrawable(resources, R.drawable.arrow_back, null)
            setNavigationOnClickListener { finish() }
        }
    }

    private fun initViewPager() {
        mBinding.todoViewPager.adapter = object : FragmentPagerAdapter(
            supportFragmentManager,
            BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        ) {
            override fun getItem(position: Int): Fragment = when(position) {
                0 -> todoFragment
                else -> finishedTodoFragment
            }

            override fun getCount(): Int = 2

            override fun getPageTitle(position: Int): CharSequence? = getString(titles[position])

        }
    }

    private fun initTabLayout() {
        mBinding.todoTabLayout.setupWithViewPager(mBinding.todoViewPager)
        mBinding.todoTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    mBinding.todoViewPager.currentItem = it.position
                }
            }

        })
    }

    private val onParentRefreshListener = object : OnParentRefreshListener {
        override fun onRefresh() {
            todoFragment.refreshWithNotLoading()
            finishedTodoFragment.refreshWithNotLoading()
        }
    }


    private fun initFragment() {
        todoFragment.listener = onParentRefreshListener
        finishedTodoFragment.listener = onParentRefreshListener
    }

}

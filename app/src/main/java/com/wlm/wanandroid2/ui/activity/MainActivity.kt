package com.wlm.wanandroid2.ui.activity

import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.wlm.baselib.ui.BaseVMDBActivity
import com.wlm.baselib.ui.DataBindingConfig
import com.wlm.baselib.utils.ToastUtils
import com.wlm.wanandroid2.BR
import com.wlm.wanandroid2.R
import com.wlm.wanandroid2.databinding.ActivityMainBinding
import com.wlm.wanandroid2.ui.fragment.DiscoverFragment
import com.wlm.wanandroid2.ui.fragment.HomeFragment
import com.wlm.wanandroid2.ui.fragment.MineFragment
import com.wlm.wanandroid2.viewmodel.MainViewModel

class MainActivity : BaseVMDBActivity<MainViewModel, ActivityMainBinding>() {
    override val providerVMClass: Class<MainViewModel> = MainViewModel::class.java

    override fun getDataBindingConfig(): DataBindingConfig<MainViewModel> =
        DataBindingConfig(R.layout.activity_main, BR.MainViewModel, mViewModel)

    private val homeFragment by lazy { HomeFragment() }
    private val discoverFragment by lazy { DiscoverFragment() }
    private val mineFragment by lazy { MineFragment() }

    override fun init(savedInstanceState: Bundle?) {
//        setSupportActionBar(mBinding.mainToolbar)

        initView()
    }

    private fun initView() {
        mBinding.mainViewPager.apply {
            offscreenPageLimit = 2
            adapter = object : FragmentStateAdapter(
                supportFragmentManager,
                lifecycle
            ) {
                override fun getItemCount(): Int = 3

                override fun createFragment(position: Int): Fragment {
                    return when (position) {
                        0 -> homeFragment
                        1 -> discoverFragment
                        else -> mineFragment
                    }
                }

            }
        }.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                mBinding.bottomNavigation.selectedItemId =
                    mBinding.bottomNavigation.menu.getItem(position).itemId
//                mBinding.mainToolbar.title =
//                    if (position == 0) getString(R.string.app_name) else resources.getStringArray(R.array.titles)[position]
            }
        })
        mBinding.bottomNavigation.apply {
            labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_LABELED
        }.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> mBinding.mainViewPager.currentItem = 0
                R.id.discover -> mBinding.mainViewPager.currentItem = 1
                R.id.mine -> mBinding.mainViewPager.currentItem = 2
            }
            true
        }
    }

    private var lastExitTime = 0L
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        val current = System.currentTimeMillis()
        if (current - lastExitTime > 2000) {
            ToastUtils.show(getString(R.string.str_exit_hint))
            lastExitTime = current
            return true
        } else {
            finish()
        }
        return super.onKeyDown(keyCode, event)
    }

}
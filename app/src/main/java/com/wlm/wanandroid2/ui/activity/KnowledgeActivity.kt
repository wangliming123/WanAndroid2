package com.wlm.wanandroid2.ui.activity

import android.os.Bundle
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.wlm.baselib.ui.BaseDBActivity
import com.wlm.wanandroid2.R
import com.wlm.wanandroid2.bean.Knowledge
import com.wlm.wanandroid2.databinding.ActivityKnowledgeBinding
import com.wlm.wanandroid2.ui.fragment.KnowledgeFragment

class KnowledgeActivity : BaseDBActivity<ActivityKnowledgeBinding>() {

    companion object {
        const val KEY_KNOWLEDGE = "KEY_KNOWLEDGE"
    }


    override val layoutId: Int = R.layout.activity_knowledge

    private val knowledge by lazy { intent?.extras?.get(KEY_KNOWLEDGE) as Knowledge }

    private val fragments = mutableListOf<Fragment>()



    override fun init(savedInstanceState: Bundle?) {

        mBinding.knowledgeToolbar.run {
            setSupportActionBar(this)

            title = knowledge.name
            setNavigationIcon(R.drawable.arrow_back)
            setNavigationOnClickListener { onBackPressed() }
        }
        initViewPager()

        initTabLayout()
    }

    private fun initViewPager() {
        fragments.clear()
        knowledge.children.forEach {
            fragments.add(KnowledgeFragment(it))
        }
        mBinding.knowledgeViewPager.run {
            adapter = object : FragmentPagerAdapter(
                supportFragmentManager,
                BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
            ) {
                override fun getItem(position: Int): Fragment = fragments[position]

                override fun getCount(): Int = fragments.size

                override fun getPageTitle(position: Int): CharSequence? =
                    HtmlCompat.fromHtml(
                        knowledge.children[position].name,
                        HtmlCompat.FROM_HTML_MODE_LEGACY
                    )


            }
        }
    }

    private fun initTabLayout() {
        mBinding.knowledgeTabLayout.run {
            setupWithViewPager(mBinding.knowledgeViewPager)
            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabReselected(tab: TabLayout.Tab?) {
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab?.let {
                        mBinding.knowledgeViewPager.currentItem = it.position
                    }
                }

            })
        }
    }


}

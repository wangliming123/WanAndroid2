package com.wlm.wanandroid2.ui.fragment

import android.os.Bundle
import com.wlm.baselib.common.startKtxActivity
import com.wlm.baselib.ui.BaseVMDBFragment
import com.wlm.baselib.ui.DataBindingConfig
import com.wlm.wanandroid2.BR
import com.wlm.wanandroid2.R
import com.wlm.wanandroid2.databinding.FragmentDiscoverBinding
import com.wlm.wanandroid2.ui.activity.*
import com.wlm.wanandroid2.viewmodel.DiscoverViewModel

class DiscoverFragment : BaseVMDBFragment<DiscoverViewModel, FragmentDiscoverBinding>() {
    override val providerVMClass: Class<DiscoverViewModel> = DiscoverViewModel::class.java

    override fun getDataBindingConfig(): DataBindingConfig<DiscoverViewModel> =
        DataBindingConfig(R.layout.fragment_discover, BR.DiscoverViewModel, mViewModel)

    override fun init(savedInstanceState: Bundle?) {

        mBinding.setVariable(BR.DiscoverClick, Click())

    }


    inner class Click {

        fun square() {
            startKtxActivity<SquareActivity>()
        }

        fun knowledgeTree() {
            startKtxActivity<KnowledgeTreeActivity>()
        }

        fun project() {
            startKtxActivity<ProjectActivity>()
        }

        fun weChat() {
            startKtxActivity<WxActivity>()
        }

        fun navigation() {
            startKtxActivity<NavigationActivity>()
        }
    }
}
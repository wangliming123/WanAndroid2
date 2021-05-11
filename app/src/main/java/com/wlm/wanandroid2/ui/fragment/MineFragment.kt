package com.wlm.wanandroid2.ui.fragment

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.wlm.baselib.common.startKtxActivity
import com.wlm.baselib.ui.BaseVMDBFragment
import com.wlm.baselib.ui.DataBindingConfig
import com.wlm.baselib.utils.ToastUtils
import com.wlm.wanandroid2.BR
import com.wlm.wanandroid2.R
import com.wlm.wanandroid2.common.Constant
import com.wlm.wanandroid2.databinding.FragmentMineBinding
import com.wlm.wanandroid2.ui.activity.*
import com.wlm.wanandroid2.viewmodel.MineViewModel

class MineFragment : BaseVMDBFragment<MineViewModel, FragmentMineBinding>() {
    override val providerVMClass: Class<MineViewModel> = MineViewModel::class.java

    override fun getDataBindingConfig(): DataBindingConfig<MineViewModel> =
        DataBindingConfig(R.layout.fragment_mine, BR.MineViewModel, mViewModel)

    override fun init(savedInstanceState: Bundle?) {

        mBinding.setVariable(BR.MineClick, Click())
    }

    override fun startObserve() {
        super.startObserve()
        mViewModel.logoutState.observe(this, Observer {
            ToastUtils.show("退出成功")
        })
    }

    inner class Click {
        fun history() {
            startKtxActivity<HistoryActivity>()
        }

        fun myCollect() {
            if (Constant.isLogin) {
                startKtxActivity<CollectActivity>()
            } else {
                startKtxActivity<LoginActivity>()
            }
        }

        fun myShare() {
            if (Constant.isLogin) {
                startKtxActivity<MyShareActivity>()
            } else {
                startKtxActivity<LoginActivity>()
            }
        }

        fun todo() {
            if (Constant.isLogin) {
                startKtxActivity<TodoActivity>()
            } else {
                startKtxActivity<LoginActivity>()
            }
        }

        fun about() {
            AlertDialog.Builder(requireContext()).setTitle(R.string.app_name)
                .setMessage(
                    getString(
                        R.string.str_source_code,
                        "https://github.com/wangliming123/WanAndroid2"
                    )
                )
                .setPositiveButton(R.string.str_confirm, null)
                .create()
                .show()
        }

        fun logout() {
            if (Constant.isLogin) {
                mViewModel.logout()
            }
        }
    }
}
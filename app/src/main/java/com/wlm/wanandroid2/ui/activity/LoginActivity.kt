package com.wlm.wanandroid2.ui.activity

import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.orhanobut.logger.Logger
import com.wlm.baselib.ui.BaseVMDBActivity
import com.wlm.baselib.ui.DataBindingConfig
import com.wlm.baselib.utils.ToastUtils
import com.wlm.wanandroid2.BR
import com.wlm.wanandroid2.R
import com.wlm.wanandroid2.common.Constant
import com.wlm.wanandroid2.databinding.ActivityLoginBinding
import com.wlm.wanandroid2.repository.DataBaseRepository
import com.wlm.wanandroid2.viewmodel.LoginViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : BaseVMDBActivity<LoginViewModel, ActivityLoginBinding>() {
    override val providerVMClass: Class<LoginViewModel> = LoginViewModel::class.java

    override fun getDataBindingConfig(): DataBindingConfig<LoginViewModel> =
        DataBindingConfig(R.layout.activity_login, BR.loginViewModel, mViewModel)

    override fun init(savedInstanceState: Bundle?) {
        initView()
        setSupportActionBar(mBinding.loginToolbar)
        mBinding.loginToolbar.navigationIcon = ResourcesCompat.getDrawable(resources, R.drawable.arrow_back, null)
        mBinding.loginToolbar.setNavigationOnClickListener { finish() }
    }


    private fun initView() {
        val username = Constant.userString.split(",")[0]
        mBinding.etUsername.setText(username)
        mBinding.btnLogin.setOnClickListener { login() }
        mBinding.btnRegister.setOnClickListener { register() }
    }

    private fun login() {
        if (check()) {
            mViewModel.login(mBinding.etUsername.text.toString(), mBinding.etPassword.text.toString())
        }
    }

    private fun register() {
        if (check()) {
            mViewModel.register(mBinding.etUsername.text.toString(), mBinding.etPassword.text.toString())
        }
    }

    private fun check(): Boolean {
        if (mBinding.etUsername.text.toString().length < 3) {
            mBinding.tvTips.visibility = View.VISIBLE
            mBinding.tvTips.text = getString(R.string.str_username_invalid)
            return false
        }
        if (mBinding.etPassword.text.toString().length < 6) {
            mBinding.tvTips.visibility = View.VISIBLE
            mBinding.tvTips.text = getString(R.string.str_password_invalid)
            return false
        }
        mBinding.tvTips.visibility = View.GONE
        return true
    }

    override fun startObserve() {
        super.startObserve()
        mViewModel.run {
            uiState.observe(this@LoginActivity, Observer { state ->
                showLoading(state.loading)

                state.error?.let {
                    ToastUtils.show(it)
                }

                state.success?.let {
                    Constant.isLogin = true
                    Constant.userString = it
                    lifecycleScope.launch(Dispatchers.IO) {
                        DataBaseRepository.historyDao.updateHistoryVisitUser(it.split(",")[2].toInt())
                    }
                    finish()
                }
            })
        }
    }
    private fun showLoading(loading: Boolean) {
        if (loading) mBinding.loginProgress.visibility = View.VISIBLE else mBinding.loginProgress.visibility = View.GONE
    }

    override fun onError(e: Throwable) {
        Logger.d("login or register error", e.message)
    }
}
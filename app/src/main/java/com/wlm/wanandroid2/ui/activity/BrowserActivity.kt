package com.wlm.wanandroid2.ui.activity

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.*
import androidx.core.content.res.ResourcesCompat
import com.orhanobut.logger.Logger
import com.wlm.baselib.ui.BaseDBActivity
import com.wlm.wanandroid2.R
import com.wlm.wanandroid2.databinding.ActivityBrowserBinding

class BrowserActivity : BaseDBActivity<ActivityBrowserBinding>() {

    companion object {
        const val TAG = "BrowserActivity"
        const val KEY_URL = "url"
    }

    override val layoutId = R.layout.activity_browser

    override fun init(savedInstanceState: Bundle?) {
        mBinding.browserHeader.run {
            title = getString(R.string.str_is_loading)
            setNavigationIcon(R.drawable.arrow_back)
            setNavigationOnClickListener { onBackPressed() }
        }
        mBinding.progressBar.progressDrawable = ResourcesCompat.getDrawable(resources, R.drawable.color_progressbar, null)
        initWebView()

        loadWebView()
    }

    private fun initWebView() {
        mBinding.webView.run {
            webViewClient = object : WebViewClient() {
                //防止加载网页时调起系统浏览器
                override fun shouldOverrideUrlLoading(view: WebView, url: String?): Boolean {
                    Logger.d("load_url: $url")
                    url?.let {
                        if (url.startsWith("http:") || it.startsWith("https:")) {
                            view.loadUrl(url)
                            return false
                        }
                    }
                    return true
                }

                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    mBinding.progressBar.visibility = View.VISIBLE
                    Logger.d("onPageStarted")
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    mBinding.progressBar.visibility = View.GONE
                    Logger.d("onPageFinished")
                }
            }

            webChromeClient = object : WebChromeClient() {

                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    super.onProgressChanged(view, newProgress)
                    mBinding.progressBar.progress = newProgress
                    Logger.d(newProgress.toString())
                }

                override fun onReceivedTitle(view: WebView?, title: String?) {
                    super.onReceivedTitle(view, title)
                    Logger.d("onReceivedTitle")
                    title?.let { mBinding.browserHeader.title = it }
                }
            }
        }
    }

    private fun loadWebView() {
        intent?.extras?.getString(KEY_URL)?.let {
            Logger.d("URL: $it")
            mBinding.webView.loadUrl(it)
//            webView.loadUrl("https://www.baidu.com")
        }
    }

    override fun onBackPressed() {
        if (mBinding.webView.canGoBack()) mBinding.webView.goBack()
        else super.onBackPressed()
    }


}

package com.wlm.wanandroid2.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.wlm.baselib.common.startKtxActivity
import com.wlm.wanandroid2.GlideApp
import com.wlm.wanandroid2.R
import com.wlm.wanandroid2.bean.BannerData
import com.wlm.wanandroid2.ui.activity.BrowserActivity
import com.youth.banner.Banner
import com.youth.banner.BannerConfig
import com.youth.banner.loader.ImageLoader

class BannerViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.layout_banner, parent, false)) {

    private val banner = itemView.findViewById<Banner>(R.id.banner)

    private var bannerDatas: List<BannerData>? = null
    private val bannerImages = mutableListOf<String>()
    private val bannerTitles = mutableListOf<String>()
    private val bannerUrls = mutableListOf<String>()

    fun bindBanner(banners: List<BannerData>?) {
        bannerDatas = banners
        bannerImages.clear()
        bannerTitles.clear()
        bannerUrls.clear()
        banners?.forEach {
            it.run {
                bannerImages.add(imagePath)
                bannerTitles.add(title)
                bannerUrls.add(url)
            }
        }
        banner.run {
            setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE)
            setImageLoader(GlideImageLoader())
            setImages(bannerImages)
            setBannerTitles(bannerTitles)
            setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE)
            setDelayTime(3000)
            setOnBannerListener{ position ->
                context.startKtxActivity<BrowserActivity>(value = BrowserActivity.KEY_URL to bannerUrls[position])
            }
            start()
        }
    }

    class GlideImageLoader : ImageLoader(){
        override fun displayImage(context: Context, path: Any?, imageView: ImageView) {
            GlideApp.with(context).load(path).into(imageView)
        }

    }
}



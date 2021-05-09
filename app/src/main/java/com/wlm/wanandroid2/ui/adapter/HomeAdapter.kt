package com.wlm.wanandroid2.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wlm.wanandroid2.R
import com.wlm.wanandroid2.bean.Article
import com.wlm.wanandroid2.databinding.ItemArticleBinding

class HomeAdapter : PagedListAdapter<Article, RecyclerView.ViewHolder>(diffCallback) {

//    private var listener: OnItemChildClickListener<Article>? = null
//
//    fun setOnItemChildClickListener(listener: OnItemChildClickListener<Article>) {
//        this.listener = listener
//    }

    private var collectListener: ((data: Article) -> Unit)? = null
    fun setOnCollectListener(listener: (data: Article) -> Unit) {
        this.collectListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            ITEM_TYPE_HEADER -> BannerViewHolder(parent)
//            ITEM_TYPE_FOOTER -> FooterViewHolder(parent)
            else -> ArticleViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_article, parent, false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ArticleViewHolder -> {
                holder.bind(getItem(position))
                holder.mBinding.ivCollect.setOnClickListener {
                    getItem(position)?.let{
                        collectListener?.invoke(it)
                    }
                }
            }
//            is FooterViewHolder -> holder.bind()
            is BannerViewHolder -> holder.bindBanner(getItem(position)?.bannerList)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> ITEM_TYPE_HEADER
            itemCount - 1 -> ITEM_TYPE_FOOTER
            else -> super.getItemViewType(position)
        }
    }

    companion object {
        private const val ITEM_TYPE_HEADER = 99
        private const val ITEM_TYPE_FOOTER = 100

        private val diffCallback = object : DiffUtil.ItemCallback<Article>() {
            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean =
                oldItem == newItem

        }
    }
}
package com.wlm.wanandroid2.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.wlm.wanandroid2.R
import com.wlm.wanandroid2.bean.Article

class DefaultArticleAdapter(private val articleType: Int = TYPE_AUTHOR) :
    PagedListAdapter<Article, ArticleViewHolder>(diffCallback) {
    companion object {
        const val TYPE_AUTHOR = 0
        const val TYPE_SHARE = 1

        private val diffCallback = object : DiffUtil.ItemCallback<Article>() {
            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean =
                oldItem == newItem

        }
    }

    private var collectListener: ((data: Article) -> Unit)? = null
    fun setOnCollectListener(listener: (data: Article) -> Unit) {
        this.collectListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder =
        ArticleViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_article, parent, false
            )
        )

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bind(getItem(position), articleType)
        holder.mBinding.ivCollect.setOnClickListener {
            getItem(position)?.let {
                collectListener?.invoke(it)
            }
        }
    }


}
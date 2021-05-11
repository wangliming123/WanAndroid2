package com.wlm.wanandroid2.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wlm.baselib.common.startKtxActivity
import com.wlm.wanandroid2.R
import com.wlm.wanandroid2.bean.Article
import com.wlm.wanandroid2.databinding.ItemShareBinding
import com.wlm.wanandroid2.ui.activity.BrowserActivity

class MyShareAdapter : PagedListAdapter<Article, ShareViewHolder>(diffCallback) {
    companion object {

        private val diffCallback = object : DiffUtil.ItemCallback<Article>() {
            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean =
                oldItem == newItem

        }
    }

    private var deleteListener: ((data: Article) -> Unit)? = null
    fun setOnDeleteListener(listener: (data: Article) -> Unit) {
        this.deleteListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShareViewHolder =
        ShareViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_share, parent, false
            )
        )

    override fun onBindViewHolder(holder: ShareViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.mBinding.ivDelete.setOnClickListener {
            getItem(position)?.let {
                deleteListener?.invoke(it)
            }
        }
    }

}


class ShareViewHolder(val mBinding: ItemShareBinding) : RecyclerView.ViewHolder(
    mBinding.root
) {

    private val context = mBinding.root.context

    fun bind(article: Article?) {
        article?.run {
            mBinding.tvTitle.text = article.title
            mBinding.tvLink.text = article.link
            itemView.setOnClickListener {
                context.startKtxActivity<BrowserActivity>(value = BrowserActivity.KEY_HISTORY to createHistory())
            }
        }
    }

}
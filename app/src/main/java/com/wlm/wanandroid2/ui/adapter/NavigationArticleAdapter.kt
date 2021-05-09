package com.wlm.wanandroid2.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wlm.baselib.common.startKtxActivity
import com.wlm.wanandroid2.R
import com.wlm.wanandroid2.bean.Article
import com.wlm.wanandroid2.bean.Navigation
import com.wlm.wanandroid2.databinding.LayoutNavigationArticleBinding
import com.wlm.wanandroid2.ui.activity.BrowserActivity
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter

class NavigationArticleAdapter :
    PagedListAdapter<Navigation, NavigationArticleViewHolder>(diffCallback) {
    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Navigation>() {
            override fun areItemsTheSame(oldItem: Navigation, newItem: Navigation): Boolean =
                oldItem.cid == newItem.cid

            override fun areContentsTheSame(oldItem: Navigation, newItem: Navigation): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NavigationArticleViewHolder {
        return NavigationArticleViewHolder(
            DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.layout_navigation_article, parent, false
        ))
    }

    override fun onBindViewHolder(holder: NavigationArticleViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class NavigationArticleViewHolder(val mBinding: LayoutNavigationArticleBinding) : RecyclerView.ViewHolder(
    mBinding.root
) {


    fun bind(navigation: Navigation?) {
        navigation?.run {
            mBinding.tvNavigationName.text = name
            mBinding.flowLayout.run {
                adapter = object : TagAdapter<Article>(articles) {
                    override fun getView(parent: FlowLayout, position: Int, article: Article?): View {
                        val tvArticle = LayoutInflater.from(parent.context).inflate(
                            R.layout.item_navigation_article, parent, false
                        ) as TextView
                        article?.let {
                            tvArticle.text = it.title
                        }
                        return tvArticle
                    }

                }

                setOnTagClickListener { _, position, parent ->
                    val article = articles[position]
                    parent.context.startKtxActivity<BrowserActivity>(value = BrowserActivity.KEY_URL to article.link)

                    true
                }
            }
        }
    }

}
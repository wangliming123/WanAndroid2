package com.wlm.wanandroid2.ui.adapter

import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.wlm.baselib.common.startKtxActivity
import com.wlm.wanandroid2.GlideApp
import com.wlm.wanandroid2.R
import com.wlm.wanandroid2.bean.Article
import com.wlm.wanandroid2.common.Constant
import com.wlm.wanandroid2.databinding.ItemArticleBinding
import com.wlm.wanandroid2.ui.activity.BrowserActivity

class ArticleViewHolder(val mBinding: ItemArticleBinding) : RecyclerView.ViewHolder(
    mBinding.root
) {

    private var article: Article? = null
    private val context = mBinding.root.context

    fun bind(article: Article?, articleType: Int = Constant.TYPE_AUTHOR) {
        this.article = article
        article?.run {
            if (articleType == Constant.TYPE_SHARE) {
                mBinding.tvAuthor.text = shareUser
            } else {
                mBinding.tvAuthor.text = author
            }
            mBinding.tvTop.visibility = if (isTop) View.VISIBLE else View.GONE
            mBinding.tvDate.text = niceDate
            mBinding.tvTitle.text = HtmlCompat.fromHtml(title, HtmlCompat.FROM_HTML_MODE_LEGACY)
            if (desc.isNullOrBlank()) mBinding.tvDesc.visibility = View.GONE
            else mBinding.tvDesc.text = HtmlCompat.fromHtml(desc, HtmlCompat.FROM_HTML_MODE_LEGACY)
            mBinding.tvChapter.text = when {
                (superChapterName != null && superChapterName.isNotBlank()) and chapterName.isNotBlank() ->
                    "${superChapterName}/ $chapterName"
                (superChapterName != null && superChapterName.isNotBlank()) -> superChapterName
                chapterName.isNotBlank() -> chapterName
                else -> ""
            }
            mBinding.ivPic.visibility = View.GONE
            if (envelopePic == null || envelopePic.isBlank()) {
                mBinding.ivPic.visibility = View.GONE
            } else {
                mBinding.ivPic.visibility = View.VISIBLE
                GlideApp.with(context).load(envelopePic).into(mBinding.ivPic)
            }
            mBinding.ivCollect.setImageDrawable(ResourcesCompat.getDrawable(context.resources,
                if (collect) R.drawable.ic_like else R.drawable.ic_like_normal, null))
            itemView.setOnClickListener {
                context.startKtxActivity<BrowserActivity>(value = BrowserActivity.KEY_URL to link)
            }
        }
    }

}
package com.wlm.wanandroid2.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.HtmlCompat
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wlm.baselib.common.startKtxActivity
import com.wlm.baselib.utils.DateUtils
import com.wlm.wanandroid2.GlideApp
import com.wlm.wanandroid2.R
import com.wlm.wanandroid2.databinding.ItemHistoryBinding
import com.wlm.wanandroid2.db.History
import com.wlm.wanandroid2.repository.DataBaseRepository
import com.wlm.wanandroid2.ui.activity.BrowserActivity

class HistoryAdapter : PagedListAdapter<History, HistoryViewHolder>(diffCallback) {

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<History>() {
            override fun areItemsTheSame(oldItem: History, newItem: History): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: History, newItem: History): Boolean =
                oldItem == newItem

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder =
        HistoryViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_history, parent, false
            )
        )

    private var collectListener: ((data: History) -> Unit)? = null
    fun setOnCollectListener(listener: (data: History) -> Unit) {
        this.collectListener = listener
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.mBinding.ivCollect.setOnClickListener {
            getItem(position)?.let {
                collectListener?.invoke(it)
            }
        }
    }

}


class HistoryViewHolder(val mBinding: ItemHistoryBinding) : RecyclerView.ViewHolder(
    mBinding.root
) {

    private val context = mBinding.root.context

    fun bind(history: History?) {
        history?.run {

            mBinding.tvTitle.text = HtmlCompat.fromHtml(title, HtmlCompat.FROM_HTML_MODE_LEGACY)
            if (desc.isNullOrBlank()) mBinding.tvDesc.visibility = View.GONE
            else mBinding.tvDesc.text = HtmlCompat.fromHtml(desc, HtmlCompat.FROM_HTML_MODE_LEGACY)

            mBinding.ivPic.visibility = View.GONE
            if (imagePath == null || imagePath.isBlank()) {
                mBinding.ivPic.visibility = View.GONE
            } else {
                mBinding.ivPic.visibility = View.VISIBLE
                GlideApp.with(context).load(imagePath).into(mBinding.ivPic)
            }
            mBinding.tvHistoryTime.text = DateUtils.format(historyTime)
            mBinding.ivCollect.setImageDrawable(
                ResourcesCompat.getDrawable(
                    context.resources,
                    if (collect) R.drawable.ic_like else R.drawable.ic_like_normal, null
                )
            )
            itemView.setOnClickListener {
//                DataBaseRepository.historyDao.update(history.apply { historyTime = System.currentTimeMillis() })
                context.startKtxActivity<BrowserActivity>(value = BrowserActivity.KEY_HISTORY to history)
            }
        }
    }

}
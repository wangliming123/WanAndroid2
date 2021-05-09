package com.wlm.wanandroid2.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wlm.baselib.common.startKtxActivity
import com.wlm.wanandroid2.R
import com.wlm.wanandroid2.bean.Knowledge
import com.wlm.wanandroid2.databinding.ItemKnowledgeBinding
import com.wlm.wanandroid2.ui.activity.KnowledgeActivity

class KnowledgeTreeAdapter : PagedListAdapter<Knowledge, KnowledgeTreeViewHolder>(
    diffCallback
) {


    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Knowledge>() {
            override fun areItemsTheSame(oldItem: Knowledge, newItem: Knowledge): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Knowledge, newItem: Knowledge): Boolean =
                oldItem == newItem

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KnowledgeTreeViewHolder {
        return KnowledgeTreeViewHolder(
            DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_knowledge, parent, false
        ))
    }

    override fun onBindViewHolder(holder: KnowledgeTreeViewHolder, position: Int) {
        val knowledge = getItem(position)
        holder.bind(knowledge)
        holder.itemView.setOnClickListener { view ->
            knowledge?.let {
                view.context.startKtxActivity<KnowledgeActivity>(value = KnowledgeActivity.KEY_KNOWLEDGE to it)
            }

        }
    }
}

class KnowledgeTreeViewHolder(val mBinding: ItemKnowledgeBinding) : RecyclerView.ViewHolder(
    mBinding.root
) {

    fun bind(knowledge: Knowledge?) {
        knowledge?.run {
            mBinding.knowledgeTitle.text = name
            mBinding.knowledgeChildren.text = children.joinToString("    "){
                it.name
            }
        }
    }
}
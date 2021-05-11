package com.wlm.wanandroid2.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wlm.wanandroid2.R
import com.wlm.wanandroid2.bean.Todo
import com.wlm.wanandroid2.common.Constant
import com.wlm.wanandroid2.databinding.ItemTodoBinding

class TodoAdapter(private val status: Int = Constant.TODO_STATUS_TODO) :
    PagedListAdapter<Todo, TodoViewHolder>(diffCallback) {
    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Todo>() {
            override fun areItemsTheSame(oldItem: Todo, newItem: Todo): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Todo, newItem: Todo): Boolean =
                oldItem == newItem

        }
    }

    private var finishOrRevertListener: ((Todo)-> Unit)? = null
    private var deleteListener: ((Todo)-> Unit)? = null
    private var itemListener: ((Todo)-> Unit)? = null

    fun setListener(finishOrRevertListener: ((Todo)-> Unit), deleteListener: ((Todo)-> Unit), itemListener: ((Todo)-> Unit)) {
        this.finishOrRevertListener = finishOrRevertListener
        this.deleteListener = deleteListener
        this.itemListener = itemListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder =
        TodoViewHolder(
            DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_todo, parent, false
        ), status)


    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.mBinding.ivFinishOrRevert.setOnClickListener {
            getItem(position)?.let {
                finishOrRevertListener?.invoke(it)
            }
        }

        holder.mBinding.ivDelete.setOnClickListener {
            getItem(position)?.let {
                deleteListener?.invoke(it)
            }
        }

        holder.itemView.setOnClickListener {
            getItem(position)?.let {
                itemListener?.invoke(it)
            }
        }
    }


}


class TodoViewHolder(val mBinding: ItemTodoBinding, private val status: Int) : RecyclerView.ViewHolder(
    mBinding.root
) {
    private val context = mBinding.root.context

    fun bind(todo: Todo?) {
        if (status == Constant.TODO_STATUS_FINISHED) {
            mBinding.ivFinishOrRevert.setImageResource(R.mipmap.ic_revert)
            mBinding.tvTodoDateTime.visibility = View.GONE
        } else {
            mBinding.ivFinishOrRevert.setImageResource(R.mipmap.ic_finish)
            mBinding.tvTodoCompleteTime.visibility = View.GONE
        }
        todo?.run {
            mBinding.tvTodoTitle.text = HtmlCompat.fromHtml(title, HtmlCompat.FROM_HTML_MODE_LEGACY)
            mBinding.tvTodoContent.text = HtmlCompat.fromHtml(content, HtmlCompat.FROM_HTML_MODE_LEGACY)
            mBinding.tvTodoDateTime.text = context.getString(R.string.str_date_time, dateStr)
            mBinding.tvTodoCompleteTime.text = context.getString(R.string.str_complete_time, completeDateStr)
        }
    }

}
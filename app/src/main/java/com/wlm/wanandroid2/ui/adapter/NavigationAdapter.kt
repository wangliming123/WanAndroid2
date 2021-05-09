package com.wlm.wanandroid2.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wlm.wanandroid2.R
import com.wlm.wanandroid2.bean.Navigation
import com.wlm.wanandroid2.databinding.ItemNavigationBinding

class NavigationAdapter(private val listener: ((Int) -> Unit)? = null) :
    PagedListAdapter<Navigation, NavigationViewHolder>(diffCallback) {
    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Navigation>() {
            override fun areItemsTheSame(oldItem: Navigation, newItem: Navigation): Boolean =
                oldItem.cid == newItem.cid

            override fun areContentsTheSame(oldItem: Navigation, newItem: Navigation): Boolean =
                oldItem == newItem
        }
    }

    private var positionChecked = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NavigationViewHolder {
        return NavigationViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_navigation, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: NavigationViewHolder, position: Int) {
        holder.bind(getItem(position))

        holder.itemView.setOnClickListener {
            setChecked(position)
            listener?.invoke(position)
        }
        holder.mBinding.tvNavigation.setTextColor(
            ContextCompat.getColor(
                holder.itemView.context,
                if (positionChecked == position) R.color.colorPrimary else R.color.textColorSecondary
            )
        )
    }

    fun setChecked(position: Int) {
        positionChecked = position
        notifyDataSetChanged()

    }
}


class NavigationViewHolder(val mBinding: ItemNavigationBinding) : RecyclerView.ViewHolder(
    mBinding.root
) {

    fun bind(navigation: Navigation?) {
        mBinding.tvNavigation.text = navigation?.name
    }
}
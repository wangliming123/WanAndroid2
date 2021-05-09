package com.wlm.wanandroid2.ui.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Observer
import com.classic.common.MultipleStatusView
import com.orhanobut.logger.Logger
import com.wlm.baselib.ui.BaseVMDBFragment
import com.wlm.baselib.ui.DataBindingConfig
import com.wlm.baselib.utils.ToastUtils
import com.wlm.wanandroid2.BR
import com.wlm.wanandroid2.R
import com.wlm.wanandroid2.bean.Todo
import com.wlm.wanandroid2.common.Constant
import com.wlm.wanandroid2.databinding.FragmentTodoBinding
import com.wlm.wanandroid2.ui.adapter.TodoAdapter
import com.wlm.wanandroid2.viewmodel.TodoViewModel
import java.text.SimpleDateFormat
import java.util.*

class TodoFragment(private val status: Int = Constant.TODO_STATUS_TODO,
                   private val showAddBtn: Boolean = true) :
    BaseVMDBFragment<TodoViewModel, FragmentTodoBinding>() {

    override val providerVMClass: Class<TodoViewModel> = TodoViewModel::class.java
    override fun childStatusView(): MultipleStatusView? = mBinding.layoutTodoList

    private val adapter by lazy { TodoAdapter(status) }

    private var isRefreshFromUpdateOrDelete = false

    private var isUpdate = false
    private lateinit var todo: Todo


    override fun getDataBindingConfig(): DataBindingConfig<TodoViewModel> =
        DataBindingConfig(R.layout.fragment_todo, BR.TodoViewModel, mViewModel)

    override fun init(savedInstanceState: Bundle?) {
        initRecyclerView()
        mBinding.layoutRefresh.setColorSchemeColors(Color.GREEN, Color.BLUE)
        mBinding.layoutRefresh.setOnRefreshListener {
            isRefreshFromPull = true
            mViewModel.refresh()
        }

        initTodo()
    }

    private fun initRecyclerView() {
        mBinding.rvRefresh.adapter = adapter
        adapter.setListener({
            mViewModel.finishTodo(it, if (status == Constant.TODO_STATUS_TODO) Constant.TODO_STATUS_FINISHED else Constant.TODO_STATUS_TODO)
        }, {
            mViewModel.deleteTodo(it)
        }, {
            mBinding.layoutAddTodo.visibility = View.VISIBLE
            mBinding.layoutTodoList.visibility = View.GONE
            todo = it
            isUpdate = true
            mBinding.etTitle.setText(HtmlCompat.fromHtml(todo.title, HtmlCompat.FROM_HTML_MODE_LEGACY))
            mBinding.etContent.setText(HtmlCompat.fromHtml(todo.content, HtmlCompat.FROM_HTML_MODE_LEGACY))
            mBinding.etDateTime.setText(todo.dateStr)
        })
    }

    @SuppressLint("SimpleDateFormat")
    private fun initTodo() {
        if (!showAddBtn)  mBinding.fabAddTodo.hide()
        val exampleDateStr = SimpleDateFormat("yyyy-MM-dd").format(Date())
        mBinding.fabAddTodo.setOnClickListener {
            mBinding.layoutAddTodo.visibility = View.VISIBLE
            mBinding.layoutTodoList.visibility = View.GONE
            mBinding.etDateTime.setText(exampleDateStr)
            mBinding.etTitle.setText("")
            mBinding.etContent.setText("")
        }
        mBinding.btnAddTodo.setOnClickListener {
            val title = mBinding.etTitle.text.toString()
            val content = mBinding.etContent.text.toString()
            val dateStr = mBinding.etDateTime.text.toString()
            if (title.isEmpty()) {
                mBinding.tvTips.visibility = View.VISIBLE
                mBinding.tvTips.text = getString(R.string.str_tip_todo_title_not_empty)
            }else if (dateStr.isEmpty()) {
                mBinding.tvTips.visibility = View.VISIBLE
                mBinding.tvTips.text = getString(R.string.str_tip_date_not_empty)
            }else if (!checkDate(dateStr)) {
                mBinding.tvTips.visibility = View.VISIBLE
                mBinding.tvTips.text = getString(R.string.str_tip_date_not_valid, exampleDateStr)
            } else  {
                mBinding.tvTips.visibility = View.GONE
                mBinding.layoutAddTodo.visibility = View.GONE
                mBinding.layoutTodoList.visibility = View.VISIBLE

                if (isUpdate) {
                    isUpdate = false
                    mViewModel.updateTodo(todo.id, title, content, dateStr, todo.status)
                }else {
                    mViewModel.addTodo(title, content, dateStr)
                }
            }
        }
        mBinding.btnCancel.setOnClickListener {
            mBinding.layoutAddTodo.visibility = View.GONE
            mBinding.layoutTodoList.visibility = View.VISIBLE
        }
    }

    private fun checkDate(dateStr: String): Boolean {
        val nums = dateStr.split("-")
        if (nums.size == 3 && nums[0].length == 4 && nums[1].length == 2 && nums[2].length == 2) {
            return true
        }
        return false
    }

    override fun startObserve() {
        super.startObserve()
        mViewModel.status = this.status
        mViewModel.run {

            pagedList.observe(this@TodoFragment, Observer {
                adapter.submitList(it)
            })

            uiState.observe(this@TodoFragment, Observer { state ->
                if (!isRefreshFromUpdateOrDelete) {
                    mBinding.layoutRefresh.isRefreshing = state.loading

                    if (state.loading) {
                        if (isRefreshFromPull) {
                            isRefreshFromPull = false
                        } else {
                            statusView?.showLoading()
                        }
                    }

                    state.success?.let {
                        if (it.datas.isEmpty()) {
                            statusView?.showEmpty()
                        } else {
                            statusView?.showContent()
                        }
                    }

                    state.error?.let {
                        statusView?.showError()
                        Logger.d("load_error", it)
                    }
                } else {
                    isRefreshFromUpdateOrDelete = false
                }
            })

            todoState.observe(this@TodoFragment, Observer { state ->
                if (state.loading) {
                    when (state.success) {
                        Constant.TODO_FINISH, Constant.TODO_REVERT -> {
                            listener?.onRefresh()
                        }
                        Constant.TODO_DELETE, Constant.TODO_UPDATE, Constant.TODO_ADD -> {
                            refreshWithNotLoading()
                        }
                    }
                } else {
                    ToastUtils.show(
                        when (state.success) {
                            Constant.TODO_FINISH -> "完成失败"
                            Constant.TODO_DELETE -> "删除失败"
                            Constant.TODO_UPDATE -> "更新失败"
                            Constant.TODO_ADD -> "添加失败"
                            else -> "操作失败"
                        }
                    )
                }
            })

            initLoad()
        }
    }

    override fun retry() {
        mViewModel.refresh()
    }

    fun refreshWithNotLoading() {
        isRefreshFromUpdateOrDelete = true
        mViewModel.refresh()
    }

    var listener: OnParentRefreshListener? = null

}

interface OnParentRefreshListener {
    fun onRefresh()
}
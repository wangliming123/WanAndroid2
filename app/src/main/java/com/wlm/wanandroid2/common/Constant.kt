package com.wlm.wanandroid2.common

import com.wlm.baselib.utils.SharedPrefs

class Constant {
    companion object {
        private const val IS_LOGIN = "is_login"
        private const val USER_STRING = "USER_STRING"

        const val TYPE_AUTHOR = 0
        const val TYPE_SHARE = 1


        var isLogin by SharedPrefs(IS_LOGIN, false)
        var userString by SharedPrefs(USER_STRING, "")


        const val TODO_STATUS_TODO = 0//未完成
        const val TODO_STATUS_FINISHED = 1//已完成

        const val TODO_FINISH = 101
        const val TODO_DELETE = 102
        const val TODO_UPDATE = 103
        const val TODO_REVERT = 104
        const val TODO_ADD = 105
    }
}
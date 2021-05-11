package com.wlm.baselib.utils

import java.text.SimpleDateFormat
import java.util.*

class DateUtils {
    companion object {
        fun format(date: Long): String {
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
            return simpleDateFormat.format(Date(date))
        }
    }

}
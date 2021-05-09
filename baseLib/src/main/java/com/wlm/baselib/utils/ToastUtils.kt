package com.wlm.baselib.utils

import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.wlm.baselib.BaseApp
import com.wlm.baselib.R

class ToastUtils {
    companion object {
        private var toast: Toast? = null

        fun showWithColors(content: String, duration: Int = Toast.LENGTH_SHORT, bgColor: Int? = null, textColor: Int? = null) {
            val inflater = LayoutInflater.from(BaseApp.instance)
            val view = inflater.inflate(R.layout.layout_toast, null)
            val tvToast = view.findViewById<TextView>(R.id.tv_toast)
            bgColor?.let {
                val layout = view.findViewById<LinearLayout>(R.id.layout_toast)
                layout.setBackgroundColor(it)
            }
            textColor?.let {
                tvToast.setTextColor(it)
            }
            tvToast.text = content
            toast = Toast(BaseApp.instance)
            toast?.run {
                setGravity(Gravity.CENTER, 0, 0)
                this.duration = duration
                this.view = view
                show()
            }
        }
        fun show(content: String, duration: Int = Toast.LENGTH_SHORT) {
            Toast.makeText(BaseApp.instance, content, duration).show()
        }
    }
}
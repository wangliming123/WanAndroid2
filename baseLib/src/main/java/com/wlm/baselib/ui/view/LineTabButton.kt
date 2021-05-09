package com.wlm.baselib.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.databinding.DataBindingUtil
import com.wlm.baselib.R
import com.wlm.baselib.databinding.ButtonLineTabBinding

class LineTabButton(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
    RelativeLayout(context, attrs, defStyleAttr) {
    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    private val mBinding = ButtonLineTabBinding.inflate(LayoutInflater.from(context),this, true)

    init {

        val typedArray = context?.obtainStyledAttributes(attrs, R.styleable.LineTabButton)
        typedArray?.run {
            val iconRes = getResourceId(R.styleable.LineTabButton_button_icon, R.drawable.arrow_right)
            val buttonText = typedArray.getString(R.styleable.LineTabButton_button_text)

            mBinding.buttonIcon.setImageResource(iconRes)
            mBinding.buttonText.text = buttonText
            recycle()
        }
    }

}
package com.hae.haessentials.utility

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.Nullable
import com.google.android.material.textfield.TextInputLayout


class HAECustomTextInputLayout : TextInputLayout {
    constructor(context: Context?) : super(context!!) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context!!, attrs, defStyleAttr) {}

    override fun drawableStateChanged() {
        super.drawableStateChanged()
        clearETColorFilter()
    }

    override fun setError(@Nullable error: CharSequence?) {
        super.setError(error)
        clearETColorFilter()
    }

    private fun clearETColorFilter() {
        val editText = editText
        if (editText != null) {
            val background = editText.background
            background?.clearColorFilter()
        }
    }
}
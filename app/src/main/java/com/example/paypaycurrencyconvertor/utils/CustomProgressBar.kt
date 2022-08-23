package com.example.paypaycurrencyconvertor.utils

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import com.example.paypaycurrencyconvertor.R
import com.example.paypaycurrencyconvertor.databinding.ViewProgressBinding

class CustomProgressBar : ConstraintLayout {
    private lateinit var mProgressBarBinding: ViewProgressBinding

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context)
    }

    private fun init(context: Context) {
        mProgressBarBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.view_progress,
            this,
            true
        )
    }
}
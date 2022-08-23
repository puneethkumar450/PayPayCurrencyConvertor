package com.example.paypaycurrencyconvertor.ui.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel<N : IBaseNavigator> : ViewModel() {

    var mIsLoading = MutableLiveData(false)
    private lateinit var mBaseVMListener: N

    /**
     * Get Listener
     */
    protected val mVMListener: N
        get() {
            return mBaseVMListener
        }

    /**
     * Set Listener
     */
    fun setNavigator(navigator: N) {
        this.mBaseVMListener = navigator
    }
}
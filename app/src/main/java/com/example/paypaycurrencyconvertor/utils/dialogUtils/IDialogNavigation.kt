package com.example.paypaycurrencyconvertor.utils.dialogUtils

import com.example.paypaycurrencyconvertor.ui.base.IBaseNavigator

interface IDialogNavigation : IBaseNavigator {
    fun onNext()
    fun onPositive()
    fun onCancel()
}
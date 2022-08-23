package com.example.paypaycurrencyconvertor.utils.dialogUtils

import androidx.databinding.ObservableField
import com.example.paypaycurrencyconvertor.ui.base.BaseViewModel

class CustomDialogViewModel : BaseViewModel<IDialogNavigation>() {

    var dialogTitle = ObservableField<String>()
    var dialogMessage = ObservableField<String>()
    var positiveBtnText = ObservableField<String>()
    var closeBtnText = ObservableField<String>()

    fun onNext() {
        mVMListener.onNext()
    }

    fun onPositive() {
        mVMListener.onPositive()
    }

    fun onCancel() {
        mVMListener.onCancel()
    }


}
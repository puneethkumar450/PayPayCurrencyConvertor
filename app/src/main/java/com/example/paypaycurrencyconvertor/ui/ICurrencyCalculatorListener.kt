package com.example.paypaycurrencyconvertor.ui

import com.example.paypaycurrencyconvertor.model.ConvertedCurrencyModel
import com.example.paypaycurrencyconvertor.ui.base.IBaseNavigator

interface ICurrencyCalculatorListener : IBaseNavigator {
    fun onShowDialog(aMessage: String)
    fun onShowList(aConvertedList : List<ConvertedCurrencyModel>)
    fun setupObservers()
}
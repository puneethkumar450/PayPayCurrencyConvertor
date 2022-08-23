package com.example.paypaycurrencyconvertor.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.paypaycurrencyconvertor.model.ConvertedCurrencyModel

class CurrencyItemViewModel(aCurrencyRateItemModel: ConvertedCurrencyModel) {
    var currencyName: MutableLiveData<String> = MutableLiveData(aCurrencyRateItemModel.aCurrencyName)
    var currencyValue: MutableLiveData<String> = MutableLiveData(aCurrencyRateItemModel.aCurrencyValue)

}
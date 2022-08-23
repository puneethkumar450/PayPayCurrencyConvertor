package com.example.paypaycurrencyconvertor.ui.viewmodel

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.example.paypaycurrencyconvertor.data.repository.AppRepository
import com.example.paypaycurrencyconvertor.data.source.local.database.entity.CurrencyRate
import com.example.paypaycurrencyconvertor.model.ConvertedCurrencyModel
import com.example.paypaycurrencyconvertor.ui.ICurrencyCalculatorListener
import com.example.paypaycurrencyconvertor.ui.base.BaseViewModel
import com.example.paypaycurrencyconvertor.utils.AppEnum
import com.example.paypaycurrencyconvertor.utils.Resource
import com.example.paypaycurrencyconvertor.utils.StringUtils.isDigitsOnly
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@HiltViewModel
class CurrencyCalculatorViewModel @Inject constructor(aRepository: AppRepository) :
    BaseViewModel<ICurrencyCalculatorListener>()
{
    var mIsDataFetching = ObservableField(false)

    var mCurrencyResponse = aRepository.getLiveCurrency()

    var mCurrencyList: ArrayList<CurrencyRate> = arrayListOf()

    var mConversionAmountLiveData = MutableLiveData<String>()
    var mCurrencyNameLiveData = MutableLiveData<String>()
    var mJob: Job? = null


    /*
     * Get latest currency data
     * */
    fun getCurrencyResponse(aResponse: Resource<List<CurrencyRate>>) {
        mIsLoading.value = true
        when (aResponse.aStatus.name) {
            AppEnum.ApiCallStatus.SUCCESS.name -> {
                aResponse.aData?.let {
                    if (mCurrencyList.isNotEmpty()) {
                        mCurrencyList.clear()
                    }
                    mCurrencyList.addAll(it)
                }
                mIsLoading.value = false
            }
            AppEnum.ApiCallStatus.ERROR.name -> {
                mIsLoading.value = false
                aResponse.aMessage?.let { mVMListener.onShowDialog(it) }
            }
            AppEnum.ApiCallStatus.LOADING.name -> {
                mIsLoading.value = true
            }
        }
    }

    /*
     * Getting all country codes
     * */
    fun prepareCurrencyNameList(): List<String> {
        val lTempList = arrayListOf<String>()
        for (item in mCurrencyList) {
            item.currency?.let { lTempList.add(it) }
        }
        return lTempList
    }


    /*
    * Calculate the currency of user input
    * */
    fun onClickCalculateCurrency() = if (!validateText(mConversionAmountLiveData.value.toString())) {
        mVMListener.onShowDialog("Please Enter Valid Amount")
    } else {
        mIsLoading.value = true
        val lSelectedCurrency = mCurrencyList.filter {
            mCurrencyNameLiveData.value?.let { it1 -> it.currency?.contains(it1) } == true
        }
        var lCalculatedCurrency = 0.0
        if (lSelectedCurrency.isNotEmpty()) {
            mConversionAmountLiveData.value?.let { name ->
                lSelectedCurrency[0].currencyValue?.toDouble()?.let {
                    name.toDouble().div(it)
                }
            }?.let {
                lCalculatedCurrency = it
            }
        }

        val lConvertedList = arrayListOf<ConvertedCurrencyModel>()
        for (lItem in mCurrencyList) {
            val lFinalConvertedOutput =
                lItem.currencyValue?.let(fun(it: String): Double {
                    return lCalculatedCurrency.times(it.toDouble())
                })
            lConvertedList.add(ConvertedCurrencyModel(lItem.currency, lFinalConvertedOutput.toString()))
        }
        mVMListener.onShowList(lConvertedList)
        mIsLoading.value = false
    }

    /*
     * startUpdates
     * Getting updates for every 30Min's
     * */
    fun startUpdates() {
        stopUpdates()
        mJob = MainScope().launch {
            while(true) {
                Log.d("CCViewModel","Inside get new update" )
                mVMListener.setupObservers()
                delay(TimeUnit.MINUTES.toMillis(30))
            }
        }
    }


    /*
     * stopUpdates
     * stopping the job
     * */
    private fun stopUpdates() {
        Log.d("CCViewModel","Inside stopUpdates" )
        mJob?.cancel()
        mJob = null
    }

    /*
     * Validates input currency value
     * */
    fun validateText(s: String): Boolean
    {
        if(s.isNotEmpty() && s.isDigitsOnly())
        {
            return true
        }
        return false
    }

}


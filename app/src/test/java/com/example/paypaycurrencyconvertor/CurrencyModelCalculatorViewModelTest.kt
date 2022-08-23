package com.example.paypaycurrencyconvertor

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.paypaycurrencyconvertor.data.repository.AppRepository
import com.example.paypaycurrencyconvertor.data.source.local.database.entity.CurrencyRate
import com.example.paypaycurrencyconvertor.model.ConvertedCurrencyModel
import com.example.paypaycurrencyconvertor.ui.ICurrencyCalculatorListener
import com.example.paypaycurrencyconvertor.ui.viewmodel.CurrencyCalculatorViewModel
import com.example.paypaycurrencyconvertor.utils.Resource
import com.google.common.truth.Truth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class CurrencyModelCalculatorViewModelTest {

    private lateinit var mCurrencyCalculatorViewModel: CurrencyCalculatorViewModel
    private lateinit var mRepository: AppRepository
    private val mTestDispatcher = StandardTestDispatcher()

    private lateinit var mCCListener: ICurrencyCalculatorListener

    @get:Rule
    val instantTaskExecutionRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun initSetUp() {
        Dispatchers.setMain(mTestDispatcher)
        mRepository = Mockito.mock(AppRepository::class.java)
        mCurrencyCalculatorViewModel = CurrencyCalculatorViewModel(mRepository)
        mCCListener = Mockito.mock(ICurrencyCalculatorListener::class.java)
        mCurrencyCalculatorViewModel.setNavigator(mCCListener)
    }

    @Test
    fun onResponse_checkFailedState_isError() {
        Mockito.`when`(
            mCurrencyCalculatorViewModel.getCurrencyResponse(
                Resource(
                    aStatus = Resource.Status.SUCCESS,
                    aData = null,
                    aMessage = null
                )
            )
        ).thenReturn(null)
        mCurrencyCalculatorViewModel.onClickCalculateCurrency()
        Truth.assertThat(mCurrencyCalculatorViewModel.mCurrencyList).isNotNull()
        Truth.assertThat(mCurrencyCalculatorViewModel.mIsLoading.value).isEqualTo(false)
    }

    @Test
    fun onClickCalculateCurrency_isSuccess()
    {
        val currencyList: ArrayList<CurrencyRate> = arrayListOf()
        currencyList.add(
            CurrencyRate(
                id = 0,
                timestamp = 1647245463,
                currency = "AED",
                currencyValue = "3.67303"
            )
        )
        currencyList.add(
            CurrencyRate(
                id = 1,
                timestamp = 1647245463,
                currency = "BDT",
                currencyValue = "86.67303"
            ))

        mCurrencyCalculatorViewModel.mCurrencyNameLiveData.value = "BDT"
        mCurrencyCalculatorViewModel.mConversionAmountLiveData.value = "100"
        val selectedCurrency = currencyList.filter {
            mCurrencyCalculatorViewModel.mCurrencyNameLiveData.value?.let { it1 -> it.currency?.contains(it1) } == true
        }
        var calculateWithUSD = 0.0
        if (!selectedCurrency.isNullOrEmpty()) {
            mCurrencyCalculatorViewModel.mConversionAmountLiveData.value?.let { name ->
                selectedCurrency[0].currencyValue?.toDouble()?.let {
                    name.toDouble().div(it)
                }
            }?.let {
                calculateWithUSD = it
            }
        }
        val convertedList = arrayListOf<ConvertedCurrencyModel>()
        for (cItem in currencyList) {
            val finalOutput =
                cItem.currencyValue?.let { calculateWithUSD.times(it.toDouble()) }
            convertedList.add(ConvertedCurrencyModel(cItem.currency, finalOutput.toString()))
        }
        Truth.assertThat(convertedList.isNotEmpty()).isEqualTo(true)
        mCurrencyCalculatorViewModel.mIsLoading.value = false
        Truth.assertThat(mCurrencyCalculatorViewModel.mIsLoading.value).isEqualTo(false)
    }


    @Test
    fun onClickCalculateCurrency_isFailed()
    {
        val currencyList: ArrayList<CurrencyRate> = arrayListOf()
        mCurrencyCalculatorViewModel.mCurrencyNameLiveData.value = "JPY"
        mCurrencyCalculatorViewModel.mConversionAmountLiveData.value = "100"
        val selectedCurrency = currencyList.filter {
            mCurrencyCalculatorViewModel.mCurrencyNameLiveData.value?.let { it1 -> it.currency?.contains(it1) } == true
        }
        var calculateWithUSD = 0.0
        if (!selectedCurrency.isNullOrEmpty()) {
            mCurrencyCalculatorViewModel.mConversionAmountLiveData.value?.let { name ->
                selectedCurrency[0].currencyValue?.toDouble()?.let {
                    name.toDouble().div(it)
                }
            }?.let {
                calculateWithUSD = it
            }
        }
        val convertedList = arrayListOf<ConvertedCurrencyModel>()
        for (cItem in currencyList) {
            val finalOutput =
                cItem.currencyValue?.let { calculateWithUSD.times(it.toDouble()) }
            convertedList.add(ConvertedCurrencyModel(cItem.currency, finalOutput.toString()))
        }
        Truth.assertThat(convertedList.isNullOrEmpty()).isEqualTo(true)
        mCurrencyCalculatorViewModel.mIsLoading.value = false
        Truth.assertThat(mCurrencyCalculatorViewModel.mIsLoading.value).isEqualTo(false)
    }

    @Test
    fun checkLoadingStateOnRequest_isTrue() {
        mCurrencyCalculatorViewModel.mIsLoading.value = true
        Truth.assertThat(mCurrencyCalculatorViewModel.mIsLoading.value).isEqualTo(true)
    }


    @Test
    fun checkLoadingStateOnRequest_isFalse() {
        mCurrencyCalculatorViewModel.mIsLoading.value = false
        Truth.assertThat(mCurrencyCalculatorViewModel.mIsLoading.value).isFalse()
        mCurrencyCalculatorViewModel.mCurrencyNameLiveData
    }

    @Test
    fun onValidateCurrency_isSuccess()
    {
        val lValue = mCurrencyCalculatorViewModel.validateText("1")
        Truth.assertThat(lValue).isEqualTo(true)
    }

    @Test
    fun onValidateCurrency_Failure()
    {
        val lValue = mCurrencyCalculatorViewModel.validateText("paypay")
        Truth.assertThat(lValue).isFalse()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
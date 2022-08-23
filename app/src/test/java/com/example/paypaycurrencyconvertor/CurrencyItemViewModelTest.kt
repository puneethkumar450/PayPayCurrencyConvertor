package com.example.paypaycurrencyconvertor

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.paypaycurrencyconvertor.model.ConvertedCurrencyModel
import com.example.paypaycurrencyconvertor.ui.viewmodel.CurrencyItemViewModel
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class CurrencyItemViewModelTest {

    private lateinit var mCurrencyItemViewModel: CurrencyItemViewModel

    @get:Rule
    val instantTaskExecutionRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun initSetUp() {
        mCurrencyItemViewModel = CurrencyItemViewModel( ConvertedCurrencyModel("USD","1"))
    }

    @Test
    fun onCheckConvertedCurrency_nonNullValues()
    {
        Truth.assertThat(mCurrencyItemViewModel.currencyValue.value).isNotNull()
        Truth.assertThat(mCurrencyItemViewModel.currencyName.value).isNotNull()
    }


    @Test
    fun onCheckConvertedCurrency_null_Values()
    {
        mCurrencyItemViewModel.currencyValue.value = null
        mCurrencyItemViewModel.currencyName.value = null
        Truth.assertThat(mCurrencyItemViewModel.currencyValue.value).isNull()
        Truth.assertThat(mCurrencyItemViewModel.currencyName.value).isNull()
    }

}
package com.example.paypaycurrencyconvertor.data.source.remote.apiService

import com.example.paypaycurrencyconvertor.data.source.remote.ApiEndPoint.Companion.LIVE_CURRENCY_API
import com.example.paypaycurrencyconvertor.data.source.remote.ApiEndPoint.Companion.QUERY_APP_ID
import com.example.paypaycurrencyconvertor.model.CurrencyModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ILiveCurrencyService {

    @GET(LIVE_CURRENCY_API)
    suspend fun getAllLiveCurrency(@Query(QUERY_APP_ID) accessKey : String): Response<CurrencyModel>
}
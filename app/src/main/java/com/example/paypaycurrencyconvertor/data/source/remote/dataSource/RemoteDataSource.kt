package com.example.paypaycurrencyconvertor.data.source.remote.dataSource

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.example.paypaycurrencyconvertor.data.source.remote.ApiEndPoint.Companion.ACCESS_KEY
import com.example.paypaycurrencyconvertor.data.source.remote.apiService.ILiveCurrencyService
import com.example.paypaycurrencyconvertor.data.source.remote.dataSource.baseDataSource.BaseDataSource
import com.example.paypaycurrencyconvertor.utils.Resource
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val aLiveCurrencyService: ILiveCurrencyService
) : BaseDataSource()
{
    fun <T, A> performGetOperation(
        aDatabaseQuery: () -> LiveData<T>,
        aNetworkCall: suspend () -> Resource<A>,
        aSaveCallResult: suspend (A) -> Unit
    ): LiveData<Resource<T>> =
        liveData(Dispatchers.IO) {
            emit(Resource.loading())
            val source = aDatabaseQuery.invoke().map { Resource.success(it) }
            emitSource(source)

            val responseStatus = aNetworkCall.invoke()
            if (responseStatus.aStatus == Resource.Status.SUCCESS) {
                responseStatus.aData?.let { aSaveCallResult(it) }

            } else if (responseStatus.aStatus == Resource.Status.ERROR) {
                emit(Resource.error(responseStatus.aMessage.toString()))
                emitSource(source)
            }
        }

    suspend fun getAllLiveCurrency() = getResult {
        aLiveCurrencyService.getAllLiveCurrency(ACCESS_KEY)
    }
}
package com.example.paypaycurrencyconvertor.data.repository

import com.example.paypaycurrencyconvertor.data.source.local.database.dao.CurrencyDao
import com.example.paypaycurrencyconvertor.data.source.local.database.entity.CurrencyRate
import com.example.paypaycurrencyconvertor.data.source.remote.dataSource.RemoteDataSource
import com.example.paypaycurrencyconvertor.data.source.remote.dataSource.baseDataSource.BaseDataSource
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class AppRepository @Inject constructor(
    private var aDataSource: RemoteDataSource,
    private var aLocalDataSource: CurrencyDao,
) : BaseDataSource()
{
    fun getLiveCurrency() = aDataSource.performGetOperation(
        aDatabaseQuery = { aLocalDataSource.getAllCurrency() },
        aNetworkCall = { aDataSource.getAllLiveCurrency() },
        aSaveCallResult = { liveCurrency ->
            val lLiveDataArray = liveCurrency.rates
            val lCurrencyList = ArrayList<CurrencyRate>()
            var lRowCount = 0
            lLiveDataArray.keys.sorted().forEach {
                val lValues = lLiveDataArray.getValue(it)
                lCurrencyList.add(
                    CurrencyRate(
                        lRowCount,
                        liveCurrency.timestamp,
                        it,
                        lValues.toString()
                    ))
                lRowCount += 1
            }
            aLocalDataSource.insertAllCurrencyRate(lCurrencyList)
        }
    )
}
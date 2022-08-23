package com.example.paypaycurrencyconvertor.data.source.local.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.paypaycurrencyconvertor.data.source.local.database.entity.CurrencyRate

@Dao
interface CurrencyDao {

    @Query("SELECT * FROM PPCurrencyTable")
    fun getAllCurrency(): LiveData<List<CurrencyRate>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllCurrencyRate(currency: List<CurrencyRate>)

    @Query("DELETE FROM PPCurrencyTable")
    fun removeAll()
}

package com.example.paypaycurrencyconvertor.data.source.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.paypaycurrencyconvertor.data.source.local.database.dao.CurrencyDao
import com.example.paypaycurrencyconvertor.data.source.local.database.entity.CurrencyRate

/**
 * SQLite Database for storing the logs.
 */
@Database(entities = [CurrencyRate::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun currencyDao(): CurrencyDao

    companion object {
        const val DATABASE_NAME: String = "PayPayCurrency.db"
    }
}
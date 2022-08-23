package com.example.paypaycurrencyconvertor.data.source.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.paypaycurrencyconvertor.utils.Constants

@Entity(tableName = Constants.DB_NAME)
data class CurrencyRate(

    @PrimaryKey
    val id: Int,

    @ColumnInfo(name = "timestamp")
    var timestamp: Int? = null,

    @ColumnInfo(name = "currency_name")
    var currency: String? = null,

    @ColumnInfo(name = "currency_value")
    var currencyValue: String? = null,

)

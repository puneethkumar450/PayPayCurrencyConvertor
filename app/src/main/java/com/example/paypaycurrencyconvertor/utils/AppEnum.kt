package com.example.paypaycurrencyconvertor.utils

object AppEnum {

    enum class ApiCallStatus(val aData: String) {
        SUCCESS("SUCCESS"),
        ERROR("ERROR"),
        LOADING("LOADING");

        companion object {
            fun fromString(value: String): ApiCallStatus {
                return values().first { it.aData == value }
            }
        }
    }
}
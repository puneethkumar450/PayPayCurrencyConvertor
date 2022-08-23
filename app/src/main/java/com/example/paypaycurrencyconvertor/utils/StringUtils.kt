package com.example.paypaycurrencyconvertor.utils

object StringUtils {
    fun String.isDigitsOnly() = all(Char::isDigit) && isNotEmpty()
}
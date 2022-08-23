package com.example.paypaycurrencyconvertor.model

data class CurrencyModel (
  var disclaimer     : String?  = null,
  var license   : String?  = null,
  var timestamp : Int?     = null,
  var base    : String?  = null,
  var rates    : HashMap<String, Double> = HashMap()
)
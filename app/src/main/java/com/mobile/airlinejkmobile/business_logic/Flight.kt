package com.mobile.airlinejkmobile.business_logic

data class Flight(
    val id: Int,
    var start: String,
    var end: String,
    var duration: String,
    var price: Double,
    var discount: Double,
    var availableSeats: Int,
    var date: String,
    var hour: String
)
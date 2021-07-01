package com.mobile.airlinejkmobile.business_logic

data class Schedule(var departureTime : String)

data class Route(
    var id : String,
    var durationhours : Int,
    var durationminutes : Int,
    var schedule: Schedule
)

data class Flight(
    var id: Int,
    var price: Double,
    var discount: Double,
    var availableSeats: Int,
    var departureDate: String,
    var route : Route
)
package com.mobile.airlinejkmobile.business_logic

data class Reservation(
    var id: Int,
    var flight: Flight,
    var seatQ: Int,
    var checkInQ: Int,
    var user: User,
    var totalPrice: Double
)

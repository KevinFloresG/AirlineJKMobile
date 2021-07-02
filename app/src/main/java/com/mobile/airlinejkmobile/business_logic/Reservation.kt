package com.mobile.airlinejkmobile.business_logic

data class Reservation(
    var id: Int,
    var flight: Flight,
    var seatQ: Int,
    var checkInQ: Int,
    var user: User,
    var totalPrice: Double
)
/*
data class Reservation(
    var id: Int,
    var flightId: Flight,
    var seatQuantity: Int,
    var checkInQuantity: Int,
    var flightInfo: String,
    var user: User,
    var totalPrice: Double,
    var airplane : String
)

/*
    private Integer id, seatQuantity, checkedInQuantity, flightId;
    private String flightInfo;
    private Userss user;
    private double totalPrice;
    private String airplane;
    private Paymenttypes typeOfPayment;
*/
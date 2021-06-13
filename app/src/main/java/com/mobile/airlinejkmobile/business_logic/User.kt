package com.mobile.airlinejkmobile.business_logic

import java.io.Serializable

class User : Serializable{

    var username:String = ""
    var password:String = ""
    var name:String = ""
    var lastName:String = ""
    var lastName2:String = ""
    var email:String = ""
    var dateOfBirth:String = "" 
    var address:String = ""
    var workphone:String = ""
    var cellphone:String = ""
    var isAdmin:Int = 0
    
    internal constructor(username:String, password:String, name:String, lastName:String, lastName2:String,
                         email:String, dateOfBirth:String, address:String, workphone:String, cellphone:String, isAdmin:Int){

        this.username = username
        this.password = password
        this.name = name
        this.lastName = lastName
        this.lastName2 = lastName2
        this.email = email
        this.dateOfBirth = dateOfBirth
        this.address = address
        this.workphone = workphone
        this.cellphone = cellphone
        this.isAdmin = isAdmin
    }

    internal constructor(){
    }
}
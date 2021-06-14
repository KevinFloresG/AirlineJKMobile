package com.mobile.airlinejkmobile.business_logic

import java.util.ArrayList

object Model {

    var usersList = ArrayList<User>()

    var flightsList = ArrayList<Flight>()
    var currentUser:User? = null
    init {
        flightsList.add(Flight("A","","2 horas", 3300.0))
        flightsList.add(Flight("B","","2 horas", 3300.0))
        flightsList.add(Flight("C","","2 horas", 3300.0))
        flightsList.add(Flight("ABC","","2 horas", 3300.0))

        usersList.add(User("1", "1", "Javier","Amador","Delgado",
            "java64@gmail.com", "2000-05-30","San Jose","2222-2222","8611-7062",0))

        usersList.add(User("java123", "123", "Javier","Amador","Delgado",
            "java64@gmail.com", "2000-05-30","San Jose","2222-2222","8611-7062",0))

        usersList.add(User("kevfg", "321", "Kevin","Flores","Garcia",
            "kevfg@gmail.com", "1999-02-19","Puntarenas","2154-2465","8312-7534",0))

        usersList.add(User("albert1", "111111", "Alberto","Achio","Morales",
            "aacmora@gmail.com", "1990-04-24","Guanacaste","2356-0142","6242-2634",0))

        usersList.add(User("ngray", "00000", "Nolan","Grayson","N/A",
            "naminmo@gmail.com", "1970-07-03","","4356-0142","6242-2634",0))

    }

    fun getUsers(): ArrayList<User> {
        return usersList
    }

    fun addUser(s : User){
        usersList.add(s)
    }

    fun login(username: String?, password: String?): User? {
        for(u: User in usersList!!){
            if(u.username == username && u.password == password && u.isAdmin == 0){
                return u
            }
        }
        return null
    }

    fun getUserByUsername(username: String?): User? {
        for(u: User in usersList!!){
            if(u.username == username){
                return u
            }
        }
        return null
    }

    fun updateUserInfo(user: User){
        for(u: User in usersList!!){
            if(u.username == user.username){
                u.email = user.email
                u.address = user.address
                u.workphone = user.workphone
                u.cellphone = user.cellphone

            }
        }
    }



}
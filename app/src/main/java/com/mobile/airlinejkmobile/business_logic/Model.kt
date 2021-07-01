package com.mobile.airlinejkmobile.business_logic

import org.json.JSONObject
import java.util.ArrayList

object Model {

    var usersList = ArrayList<User>()
    //var flightsList = ArrayList<Flight>()
    var flights = HashMap<Int, Flight>()
    //var reservations = HashMap<Int, Reservation>()
    var reservationId = 0
    var reservations = ArrayList<Reservation>()


    var flightsList = ArrayList<Flight>()
    var currentUser:User? = null
    init {
        flights[0] = Flight(0,"CRC","USA","3 horas", 700.0, 0.0, 35, "13/06/2021", "13:00")
        flights[1] = Flight(1,"USA","CRC","3 horas", 700.0, 0.0, 35, "21/06/2021", "14:00")
        flights[2] = Flight(2,"ARG","CAN","4 horas", 300.0, 0.0, 35, "13/06/2021", "10:00")
        flights[3] = Flight(3,"CAN","CHL","4 horas", 400.0, 0.0, 35, "22/06/2021", "13:00")
        flights[4] = Flight(4,"CHL","CRC","2 horas", 200.0, 0.0, 35, "13/06/2021", "09:00")
        flights[5] = Flight(5,"CRC","CHN","26 horas", 2000.0, 0.0, 35, "13/06/2021", "16:00")
        flights[6] = Flight(6,"CHN","PER","25 horas", 2000.0, 0.0, 35, "14/06/2021", "17:00")
        flights[7] = Flight(7,"PER","POL","8 horas", 600.0, 0.0, 35,"15/06/2021", "13:00")
        flights[8] = Flight(8,"POL","PRI","8 horas", 600.0, 0.0, 35, "16/06/2021", "10:00")
        flights[9] = Flight(9,"PRI","RUS","10 horas", 1000.0, 0.0, 35, "17/06/2021", "13:00")
        flights[10] = Flight(10,"QAT","RUS","1 horas", 100.0, 0.0, 35, "13/06/2021", "21:00")
        flights[11] = Flight(11,"RUS","CRC","10 horas", 1000.0, 0.0, 35, "18/06/2021", "13:00")
        flights[12] = Flight(12,"SRB","QAT","4 horas", 500.0, 0.0, 35, "19/06/2021", "22:00")
        flights[13] = Flight(13,"SGP","ARG","5 horas", 500.0, 0.0, 35, "25/06/2021", "13:00")

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
        for(u: User in usersList){
            if(u.username == username && u.password == password && u.isAdmin == 0){
                return u
            }
        }
        return null
    }

    fun getUserByUsername(username: String?): User? {
        for(u: User in usersList){
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

    fun setCurrentUserFromJSON(uJ: JSONObject){

        currentUser = User(uJ.getString("username"),uJ.getString("name"),
            uJ.getString("lastname"),uJ.getString("email"),
            uJ.getString("dateOfBirth"),uJ.getString("address"),
            uJ.getString("workphone"), uJ.getString("cellphone")
        )
    }



}
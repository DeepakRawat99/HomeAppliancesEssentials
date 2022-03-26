package com.hae.haessentials.backend

import com.squareup.moshi.Json

class UserData {

    @field:Json(name = "fullName")
    var fullName:String?= null

    @field:Json(name = "mobileNumber")
    var mobileNumber:String?= null

    @field:Json(name = "fullAddress")
    var fullAddress:String?= null

    @field:Json(name = "pinCode")
    var pinCode:String?= null

}
package com.hae.haessentials.backend

import com.squareup.moshi.Json

class OtpRequest {
    @field:Json(name = "mobileNumber")
    var mobileNumber:String?= null
}
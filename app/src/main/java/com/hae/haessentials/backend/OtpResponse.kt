package com.hae.haessentials.backend

import com.squareup.moshi.Json

class OtpResponse:BaseResponse() {

    @field:Json(name = "alreadyUser")
    var alreadyUser:String?= null
}
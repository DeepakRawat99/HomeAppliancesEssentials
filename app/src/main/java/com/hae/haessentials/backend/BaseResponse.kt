package com.hae.haessentials.backend

import com.squareup.moshi.Json

open class BaseResponse {

    @field:Json(name = "status")
    var status:String?= null
}
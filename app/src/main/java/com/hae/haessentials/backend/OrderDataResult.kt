package com.hae.haessentials.backend

import com.squareup.moshi.Json

class OrderDataResult {

    @field:Json(name = "orderID")
    var orderID:String?= null

    @field:Json(name = "orderedDate")
    var orderedDate:String?= null

    @field:Json(name = "totalAmount")
    var totalAmount:Int?= null

    @field:Json(name = "status")
    var status:String?= null

    @field:Json(name = "items")
    var items:ArrayList<ItemList>?= null

    @field:Json(name = "genOrderId")
    var genOrderId:Long?= null

}
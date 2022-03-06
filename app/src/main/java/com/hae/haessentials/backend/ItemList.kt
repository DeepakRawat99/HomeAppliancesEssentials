package com.hae.haessentials.backend

import com.squareup.moshi.Json

class ItemList {

    @field:Json(name = "itemId")
    var itemId:String?= null

    @field:Json(name = "itemName")
    var itemName:String?= null

    @field:Json(name = "itemDesc")
    var itemDesc:String?= null

    @field:Json(name = "itemCount")
    var itemCount:String?= null

    @field:Json(name = "itemPrice")
    var itemPrice:String?= null

}
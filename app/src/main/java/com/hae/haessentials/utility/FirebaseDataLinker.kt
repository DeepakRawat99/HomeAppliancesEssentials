package com.hae.haessentials.utility

import com.google.firebase.remoteconfig.FirebaseRemoteConfig


object FirebaseDataLinker {
    const val ENCRYPTION_KEY = "Ax6ut0Odmvs67Bmwmcmsfkcskrvsm2uz"
    const val FIREBASE_DB_URL = "firebase_db_url"
    const val USER_CART = "usercart"
    const val ITEM_DETAILS = "item_details"
    const val USER_DETAIL = "userdetail"
    const val USER_ORDERS = "userorders"
    const val FIREBASE_DB = "https://ha-essentials-default-rtdb.asia-southeast1.firebasedatabase.app/"

    @JvmStatic
    fun getString(key:String):String{
        return FirebaseRemoteConfig.getInstance().getString(key)
    }
}
package com.hae.haessentials.utility

import com.google.firebase.remoteconfig.FirebaseRemoteConfig


object FirebaseDataLinker {
    const val ENCRYPTION_KEY = "encryption_key"
    const val FIREBASE_DB_URL = "firebase_db_url"
    const val USER_CART = "usercart"

    @JvmStatic
    fun getString(key:String):String{
        return FirebaseRemoteConfig.getInstance().getString(key)
    }
}
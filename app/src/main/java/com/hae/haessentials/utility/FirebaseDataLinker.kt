package com.hae.haessentials.utility

import com.google.firebase.remoteconfig.FirebaseRemoteConfig


object FirebaseDataLinker {
    const val ENCRYPTION_KEY = "encryption_key"

    @JvmStatic
    fun getString(key:String):String{
        return FirebaseRemoteConfig.getInstance().getString(key)
    }
}
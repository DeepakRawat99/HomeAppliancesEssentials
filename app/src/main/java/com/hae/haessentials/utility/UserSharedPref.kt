package com.hae.haessentials.utility

import android.content.Context
import android.content.SharedPreferences

object UserSharedPref {

    private lateinit var prefs: SharedPreferences
    private const val PREFS_NAME = "user_shared_pref"
    private const val LOGIN = "is_logged_in"
    private const val MOBILE_NUMBER = "mobile_number"


    fun init(context: Context){
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    private fun setBoolean(key:String, value: Boolean){
        val editor = prefs.edit()
        editor?.putBoolean(key, value)
        editor?.apply()
    }

    private fun getBoolean(key: String, value: Boolean):Boolean{
        return prefs.getBoolean(key,value)
    }

    private fun setString(key:String, value: String?){
        val editor = prefs.edit()
        editor?.putString(key, value)
        editor?.apply()
    }

    private fun getString(key: String, value: String?): String? {
        return prefs.getString(key,value)
    }

    fun isLoggedIn(): Boolean{
        return getBoolean(LOGIN,false)
    }

    fun setLogin(value: Boolean){
        setBoolean(LOGIN, value)
    }

    fun getMobileNumber(): String?{
        return getString(MOBILE_NUMBER,null)
    }

    fun setMobile(value: String?){
        setString(MOBILE_NUMBER, encrypt(value))
    }

    private fun encrypt(value: String?): String? {
       return if(value.isNullOrBlank()) value
        else value
    }

}
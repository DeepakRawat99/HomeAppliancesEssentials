package com.hae.haessentials.utility

import android.content.Context
import android.content.SharedPreferences

object UserSharedPref {

    private lateinit var prefs: SharedPreferences
    private const val PREFS_NAME = "user_shared_pref"
    private const val LOGIN = "is_logged_in"
    private const val MOBILE_NUMBER = "mobile_number"
    private const val FIRST_NAME = "first_name"


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
        return decrypt(getString(MOBILE_NUMBER,null))
    }

    fun setMobile(value: String?){
        setString(MOBILE_NUMBER, encrypt(value))
    }
    fun getFirstName(): String?{
        return getString(FIRST_NAME,null)
    }

    fun setFirstName(value: String?){
        setString(FIRST_NAME, value)
    }

    private fun encrypt(value: String?): String? {
         if(value.isNullOrBlank()) {
            val key = FirebaseDataLinker.getString(FirebaseDataLinker.ENCRYPTION_KEY)
             return EncryptDecryptData().encryptUserData(value, key, "")
        }
        return null
    }
    private fun decrypt(value: String?): String? {
         if(value.isNullOrBlank()) {
            val key = FirebaseDataLinker.getString(FirebaseDataLinker.ENCRYPTION_KEY)
             return EncryptDecryptData().decryptUserData(value, key, "")
        }
        return null
    }

}
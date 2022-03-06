package com.hae.haessentials.utility

import android.util.Log
import androidx.core.text.isDigitsOnly
import java.lang.Exception

const val VERIFICATION_ID = "verification_id"
const val TOKEN = "token"
const val MOBILE_NUMBER = "mobile_number"

const val ITEM_JSON = "{\n" +
        "\t\"items\": [{\n" +
        "\t\t\t\"item_name\": \"surf\",\n" +
        "\t\t\t\"item_desc\": \"this is item description\",\n" +
        "\t\t\t\"item_price\": 200\n" +
        "\t\t},\n" +
        "\t\t{\n" +
        "\t\t\t\"item_name\": \"surf\",\n" +
        "\t\t\t\"item_desc\": \"this is item description\",\n" +
        "\t\t\t\"item_price\": 200\n" +
        "\t\t}\n" +
        "\t]\n" +
        "}"
const val PINCODE_JSON = ""
fun validMobileNumber(number:String?):Boolean{
    return try {
            !number.isNullOrEmpty() && number.length ==10 && number.isDigitsOnly()
                    && ( number.startsWith("9") ||
                    number.startsWith("8") ||
                    number.startsWith("7") ||
                    number.startsWith("6"))
        //can use regex also but I'm not a fan
    //&& !("^([0-9])\\1*$".toRegex().containsMatchIn(number))
    }
    catch (e:Exception){
        Log.d("Exception_with_mobile", "Can't Validate mobile Exception: $e")
        false
    }
}

fun validText(text:String):Boolean{
    return !(text.isEmpty() || text.length< 3)
}
fun validPinCode(text:String):Boolean{
    return !(text.isEmpty() || text.length< 6)
}
fun servicePinCode(text:String):Boolean{
    return true
}
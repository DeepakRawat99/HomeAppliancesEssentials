package com.hae.haessentials.utility

import android.content.Context
import android.util.Log
import androidx.core.text.isDigitsOnly
import java.lang.Exception
import androidx.core.content.ContextCompat.startActivity

import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity

import android.content.ClipDescription
import androidx.core.content.ContextCompat


const val VERIFICATION_ID = "verification_id"
const val TOKEN = "token"
const val MOBILE_NUMBER = "mobile_number"

const val ITEM_JSON = "{\n" +
        "\t\"items\": [{\n" +
        "\t\t\"item_id\": \"1\",\n" +
        "\t\t\"item_name\": \"Front load Fluff\",\n" +
        "\t\t\"item_desc\": \"Liquid detergent\",\n" +
        "\t\t\"item_image\": \"front_load_fluff\",\n" +
        "\t\t\"item_price\": \"299\",\n" +
        "\t\t\"item_mrp\": \"335\",\n" +
        "\t\t\"item_disc\": \"10\"\n" +
        "\t}, {\n" +
        "\t\t\"item_id\": \"2\",\n" +
        "\t\t\"item_name\": \"Top load Fluff\",\n" +
        "\t\t\"item_desc\": \"Liquid detergent\",\n" +
        "\t\t\"item_image\": \"top_load_fluff\",\n" +
        "\t\t\"item_price\": \"260\",\n" +
        "\t\t\"item_mrp\": \"290\",\n" +
        "\t\t\"item_disc\": \"10\"\n" +
        "\t}, {\n" +
        "\t\t\"item_id\": \"3\",\n" +
        "\t\t\"item_name\": \"Descale\",\n" +
        "\t\t\"item_desc\": \"Cleaning Powder\",\n" +
        "\t\t\"item_image\": \"descale\",\n" +
        "\t\t\"item_price\": \"165\",\n" +
        "\t\t\"item_mrp\": \"185\",\n" +
        "\t\t\"item_disc\": \"10\"\n" +
        "\t}, {\n" +
        "\t\t\"item_id\": \"4\",\n" +
        "\t\t\"item_name\": \"Limo\",\n" +
        "\t\t\"item_desc\": \"Fabric Brightener\",\n" +
        "\t\t\"item_image\": \"limo\",\n" +
        "\t\t\"item_price\": \"135\",\n" +
        "\t\t\"item_mrp\": \"150\",\n" +
        "\t\t\"item_disc\": \"10\"\n" +
        "\t}, {\n" +
        "\t\t\"item_id\": \"5\",\n" +
        "\t\t\"item_name\": \"Fabo\",\n" +
        "\t\t\"item_desc\": \"Stain Remover\",\n" +
        "\t\t\"item_image\": \"fabo\",\n" +
        "\t\t\"item_price\": \"180\",\n" +
        "\t\t\"item_mrp\": \"200\",\n" +
        "\t\t\"item_disc\": \"10\"\n" +
        "\t}, {\n" +
        "\t\t\"item_id\": \"6\",\n" +
        "\t\t\"item_name\": \"AutoDish Rinse Aid\",\n" +
        "\t\t\"item_desc\": \"Shining & Spotless Clean\",\n" +
        "\t\t\"item_image\": \"atd_rinse_aid\",\n" +
        "\t\t\"item_price\": \"130\",\n" +
        "\t\t\"item_mrp\": \"150\",\n" +
        "\t\t\"item_disc\": \"10\"\n" +
        "\t}, {\n" +
        "\t\t\"item_id\": \"7\",\n" +
        "\t\t\"item_name\": \"Autodish Detergent\",\n" +
        "\t\t\"item_desc\": \"keeps utensils hygienic & Stain Free\",\n" +
        "\t\t\"item_image\": \"autodish_detergent\",\n" +
        "\t\t\"item_price\": \"310\",\n" +
        "\t\t\"item_mrp\": \"330\",\n" +
        "\t\t\"item_disc\": \"6\"\n" +
        "\t}, {\n" +
        "\t\t\"item_id\": \"8\",\n" +
        "\t\t\"item_name\": \"Autodish Salt\",\n" +
        "\t\t\"item_desc\": \"Protect from Corrosion\",\n" +
        "\t\t\"item_image\": \"autodish_salt\",\n" +
        "\t\t\"item_price\": \"130\"\n" +
        "\t\t\"item_mrp\": \"150\",\n" +
        "\t\t\"item_disc\": \"13\"\n" +
        "\t}, {\n" +
        "\t\t\"item_id\": \"9\",\n" +
        "\t\t\"item_name\": \"Colour & Dirt Catcher\",\n" +
        "\t\t\"item_desc\": \"Prevent Colour Run & Wash Mixed Loads\",\n" +
        "\t\t\"item_image\": \"colour_dirt_catcher\",\n" +
        "\t\t\"item_price\": \"220\",\n" +
        "\t\t\"item_mrp\": \"250\",\n" +
        "\t\t\"item_disc\": \"12\"\n" +
        "\t}, {\n" +
        "\t\t\"item_id\": \"10\",\n" +
        "\t\t\"item_name\": \"Microclean\",\n" +
        "\t\t\"item_desc\": \"Removes Stains\",\n" +
        "\t\t\"item_image\": \"microclean\",\n" +
        "\t\t\"item_price\": \"200\",\n" +
        "\t\t\"item_mrp\": \"220\",\n" +
        "\t\t\"item_disc\": \"11\"\n" +
        "\t}, {\n" +
        "\t\t\"item_id\": \"11\",\n" +
        "\t\t\"item_name\": \"Colour Care\",\n" +
        "\t\t\"item_desc\": \"Liquid detergent\",\n" +
        "\t\t\"item_image\": \"colour_care\",\n" +
        "\t\t\"item_price\": \"380\",\n" +
        "\t\t\"item_mrp\": \"400\",\n" +
        "\t\t\"item_disc\": \"5\"\n" +
        "\t}, {\n" +
        "\t\t\"item_id\": \"12\",\n" +
        "\t\t\"item_name\": \"Woollens Fluff\",\n" +
        "\t\t\"item_desc\": \"Liquid detergent\",\n" +
        "\t\t\"item_image\": \"woolens_fluff\",\n" +
        "\t\t\"item_price\": \"380\",\n" +
        "\t\t\"item_mrp\": \"380\",\n" +
        "\t  \t\"item_disc\": \"0\"\n" +
        "\t}]\n" +
        "}"
val PINCODE_JSON = arrayOf("110010",
    "110045",
    "110046",
    "110077",
    "110043",
    "110058",
    "110059",
    "110027",
    "110018")
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

fun checkNullString(text: String?):String{
    return if(!text.isNullOrEmpty()){
        "$text,"
    }else ""
}
fun validPinCode(text:String):Boolean{
    return !(text.isEmpty() || text.length< 6)
}
fun servicePinCode(text:String):Boolean{
    for(i in PINCODE_JSON){
        if(text.equals(i))
            return true
    }
    return false
}

fun openPhone(number:String,context: Context){
    val intent = Intent(Intent.ACTION_DIAL)
    intent.data = Uri.parse("tel:$number")
    context.startActivity(intent)
}
fun openEmail(email: String,context: Context){
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = ClipDescription.MIMETYPE_TEXT_PLAIN
    intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
    context.startActivity(Intent.createChooser(intent, "Send Email"))
}
fun CheckNumberNull(number: String?):Boolean{

return number!=null && number.trim().isNotEmpty() && !number.equals("null")
}
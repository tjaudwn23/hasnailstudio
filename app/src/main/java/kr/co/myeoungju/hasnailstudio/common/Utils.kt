package kr.co.myeoungju.hasnailstudio.common

import android.graphics.Bitmap
import android.util.Base64
import com.google.gson.GsonBuilder
import java.io.ByteArrayOutputStream

class Utils {

    companion object{
        fun parseHashMapToObject(map: Map<String?, Any?>?, cls: Class<*>?): Any? {
            val gsonBuilder = GsonBuilder()
            val gson = gsonBuilder.create()
            val jsonString = gson.toJson(map)
            return gson.fromJson<Any>(jsonString, cls)
        }
        fun BitMapToString(bitmap: Bitmap): String {
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
            val b = baos.toByteArray()
            return Base64.encodeToString(b, Base64.DEFAULT)
        }
    }

}
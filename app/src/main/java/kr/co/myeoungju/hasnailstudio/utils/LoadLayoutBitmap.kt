package kr.co.myeoungju.hasnailstudio.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Log
import android.view.View
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream

class LoadLayoutBitmap {

    fun GetBitmapByteArry(v: View, width: Int, height: Int):ByteArray{
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        v.draw(canvas)
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos)
        val data = baos.toByteArray()
        return data
    }


    fun LoadBitmap(v: View, width: Int, height: Int, operation: (String) -> Unit) {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        v.draw(canvas)
        val storage = FirebaseStorage.getInstance()
        // Create a storage reference from our app
        val storageRef = storage.reference

// Create a reference to "mountains.jpg"
        val mountainsRef = storageRef.child("mountains.jpg")
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        val uploadTask = mountainsRef.putBytes(data)
        uploadTask.addOnFailureListener { exception ->
            Log.e("LoadBitmap", exception.toString())
            operation("")
            // Handle unsuccessful uploads
        }.addOnSuccessListener {
            Log.d("LoadBitmap", "Success")
            operation(it.metadata?.path ?: "")
            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
            // ...
        }
    }
}
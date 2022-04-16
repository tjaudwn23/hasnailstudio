package kr.co.myeoungju.hasnailstudio

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.surgery_activity.*
import kr.co.myeoungju.hasnailstudio.entity.GuestInfo
import kr.co.myeoungju.hasnailstudio.popup.Popup_Nomal
import java.text.SimpleDateFormat
import java.util.*

class SurgeryActivity:AppCompatActivity() {


    var guestInfo:GuestInfo? = null
    var byteArray:ByteArray? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.surgery_activity)


        guestInfo = intent.getParcelableExtra("guest")
        byteArray = intent.getByteArrayExtra("byteArray")
        startsurgery_btn.setOnClickListener {
            startSurgery()
        }
    }

    fun startSurgery(){


        var dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val dateString = dateFormat.format(Date())

        dateFormat = SimpleDateFormat("yyyy")
        val year = dateFormat.format(Date())
        dateFormat = SimpleDateFormat("MM")
        val month = dateFormat.format(Date())
        dateFormat = SimpleDateFormat("dd")
        val date = dateFormat.format(Date())


        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        val mountainsRef = storageRef.child(guestInfo?.name!! + "_" + guestInfo?.birth).child(year).child(month).child(date).child("agree_" + dateString)

        val uploadTask = mountainsRef.putBytes(byteArray!!)
        uploadTask.addOnFailureListener { exception ->
            Log.e("LoadBitmap", exception.toString())
            val popup = Popup_Nomal(this)
            popup.showDialog("동의서 업로드를 실패했습니다."){
                popup.dismiss()
            }
            // Handle unsuccessful uploads
        }.addOnSuccessListener {
            Log.d("LoadBitmap", "Success")

            mountainsRef.downloadUrl.addOnFailureListener {
                val popup = Popup_Nomal(this)
                popup.showDialog("동의서 업로드를 실패했습니다."){
                    popup.dismiss()
                }
            }.addOnSuccessListener {
                updateInfo(it.toString())
            }


            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
            // ...
        }



    }

    fun updateInfo(agreeUrl: String){
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val dateString = dateFormat.format(Date())

        var param = HashMap<String, Any>()
        param.apply {
            put("date",dateString)
            put("agree_url",agreeUrl)
        }
        val firestore = FirebaseFirestore.getInstance()

        firestore.collection("guest").document(guestInfo?.key!!).collection("surgery")
            .add(param)
            .addOnSuccessListener {
                val popup = Popup_Nomal(this)
                popup.showDialog("성공했습니다!"){
                    popup.dismiss()
                    intent.putExtra("data",guestInfo)
                    setResult(RESULT_OK,intent)
                    finish()
                }
            }.addOnFailureListener {
                val popup = Popup_Nomal(this)
                popup.showDialog("시술등록을 실패 했습니다.\n잠시후 시도해주세요."){
                    popup.dismiss()
                }
            }
    }

}
package kr.co.myeoungju.hasnailstudio

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.accept_the_terms_activity.*
import kr.co.myeoungju.hasnailstudio.common.Utils
import kr.co.myeoungju.hasnailstudio.entity.GuestInfo
import kr.co.myeoungju.hasnailstudio.popup.Popup_Nomal
import kr.co.myeoungju.hasnailstudio.utils.LoadLayoutBitmap
import java.text.SimpleDateFormat
import java.util.*
import android.util.Base64
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import kotlinx.android.synthetic.main.main_activity.*

class AcceptTheTermsActivity : AppCompatActivity() {

    var guestInfo:GuestInfo? = null
    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.accept_the_terms_activity)

        guestInfo = intent.getParcelableExtra<GuestInfo>("guest")

        if (guestInfo != null){
            name_edit.setText(guestInfo!!.name)
            var split = guestInfo!!.phone_num.split("-")
            phoneNum_f_edit.setText(split[0])
            phoneNum_m_edit.setText(split[1])
            phoneNum_e_edit.setText(split[2])

            split = guestInfo!!.birth.split("-")
            year_edit.setText(split[0])
            month_edit.setText(split[1])
            date_edit.setText(split[2])

            val decodedString: ByteArray = Base64.decode(guestInfo!!.sign_url, Base64.DEFAULT)
            val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)

            signView.visibility = View.GONE
            sign_imgView.visibility = View.VISIBLE

            sign_imgView.setImageBitmap(decodedByte)

        }

        bind()
        attribute()
    }

    fun attribute(){

        val builder = VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

        setTextSpan()
        signView.scrollView = scrollview
    }

    fun bind(){

        next_btn.setOnClickListener {
            if(checker()){
                setData()
               /* val cp = CreatePDF()
                cp.LoadBitmap(scrollview_linear,scrollview_linear.width,scrollview_linear.height)*/
            }

        }

        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if(it.resultCode == RESULT_OK){
                val intent = it.data
                val data = intent!!.getParcelableExtra<GuestInfo>("data") ?: GuestInfo()
                intent.putExtra("data",data)
                setResult(RESULT_OK,intent)
                finish()
            }else{
                finish()
            }
        }
    }

    fun setData() {

        if(guestInfo == null){
            val name = name_edit.text.toString()
            val phoneNum = phoneNum_f_edit.text.toString() + "-" + phoneNum_m_edit.text.toString() + "-" + phoneNum_e_edit.text.toString()
            val year = year_edit.text.toString()
            val month = month_edit.text.toString()
            val date = date_edit.text.toString()

            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val dateString = dateFormat.format(Date())



            val firestore = FirebaseFirestore.getInstance()

            var param = HashMap<String, Any>()
            param.apply {
                put("name",name)
                put("phone_num",phoneNum)
                put("sign_url",Utils.BitMapToString(signView.mBitmap!!))
                put("birth",year + "-" + month + "-" + date)
                put("register_date",dateString)
            }
            guestInfo = GuestInfo()
            guestInfo?.name = name
            guestInfo?.phone_num = phoneNum
            guestInfo?.sign_url = Utils.BitMapToString(signView.mBitmap!!)
            guestInfo?.birth = year + "-" + month + "-" + date
            guestInfo?.register_date = dateString



            firestore.collection("guest")
                .add(param)
                .addOnSuccessListener {
                    val popup = Popup_Nomal(this)

                    guestInfo?.key = it.id
                    gotoSurgery(guestInfo!!)
                }.addOnFailureListener {
                    val popup = Popup_Nomal(this)
                    popup.showDialog("??????????????? ?????? ????????????.\n????????? ??????????????????."){
                        popup.dismiss()
                    }
                }
        }else{
            gotoSurgery(guestInfo!!)

        }


    }

    fun gotoSurgery(info:GuestInfo){
        val loadLayoutBitmap = LoadLayoutBitmap()
        val array = loadLayoutBitmap.GetBitmapByteArry(scrollview_linear,scrollview_linear.width,scrollview_linear.height)

        val intent = Intent(this,SurgeryActivity::class.java)
        intent.putExtra("guest",info)
        intent.putExtra("byteArray",array)
        activityResultLauncher.launch(intent)

    }

    fun  checker():Boolean {
        val name = name_edit.text.toString()
        val phoneNum = phoneNum_f_edit.text.toString() + phoneNum_m_edit.text.toString() + phoneNum_e_edit.text.toString()
        val isSign = signView.mBitmap != null
        val year = year_edit.text.toString()
        val month = month_edit.text.toString()
        val date = date_edit.text.toString()

        if (name.equals("")){
            val popup = Popup_Nomal(this)
            popup.showDialog("????????? ????????? ?????????."){
                popup.dismiss()
            }
            return false
        }

        if(phoneNum.length != 11){
            val popup = Popup_Nomal(this)
            popup.showDialog("?????????????????? ????????? ?????????."){
                popup.dismiss()
            }
            return false
        }
        if(guestInfo == null){
            if (!isSign){
                val popup = Popup_Nomal(this)
                popup.showDialog("????????? ????????????."){
                    popup.dismiss()
                }
                return false
            }
        }


        if(year.equals("") || month.equals("") || date.equals("")){
            val popup = Popup_Nomal(this)
            popup.showDialog("????????? ????????? ?????????."){
                popup.dismiss()
            }
            return false
        }

        if((!agree_checker.isChecked && !nonagree_checker.isChecked) || (agree_checker.isChecked && nonagree_checker.isChecked)){
            val popup = Popup_Nomal(this)
            popup.showDialog("?????? ????????? ????????? ?????????."){
                popup.dismiss()
            }
            return false
        }

        return true
    }

    fun setTextSpan(){
        var word = ArrayList<String>()
        word.add("???????????? ???????????? ?????? (???/????????? ????????? ?????? ?????? ??????)??? ??????")
        note_2_1.text = setSapn(note_2_1.text.toString(),word)

        word = ArrayList<String>()
        word.add("?????? ????????? ?????? ??? ???????????? ????????? ????????? ???????????? ????????????.")
        note_2_3.text = setSapn(note_2_3.text.toString(),word)

        word = ArrayList<String>()
        word.add("?????? ??? ????????? ??????")
        note_3_1.text = setSapn(note_3_1.text.toString(),word)

        word = ArrayList<String>()
        word.add("????????????")
        word.add("????????? ??????")
        note_3_2.text = setSapn(note_3_2.text.toString(),word)

        word = ArrayList<String>()
        word.add("3??? ~ 4???")
        word.add("2???")
        note_4_1.text = setSapn(note_4_1.text.toString(),word)

        word = ArrayList<String>()
        word.add("?????? ??????")
        word.add("???????????? ???????????? ??????")
        note_4_3.text = setSapn(note_4_3.text.toString(),word)

        word = ArrayList<String>()
        word.add("????????? ????????? ??????????????? ??? ??????, ??? ?????????")
        note_4_4.text = setSapn(note_4_4.text.toString(),word)

        word = ArrayList<String>()
        word.add("?????????")
        note_4_5.text = setSapn(note_4_5.text.toString(),word)

        word = ArrayList<String>()
        word.add("?????????")
        note_4_6.text = setSapn(note_4_6.text.toString(),word)

        word = ArrayList<String>()
        word.add("3??? ???")
        note_5_1.text = setSapn(note_5_1.text.toString(),word)

        word = ArrayList<String>()
        word.add("7??? ???")
        note_5_2.text = setSapn(note_5_2.text.toString(),word)

        word = ArrayList<String>()
        word.add("?????? ???????????? ?????? ?????????, ?????? ??????, ???????????? ???????????? 1??? ?????? ?????? ??????")
        note_5_3.text = setSapn(note_5_3.text.toString(),word)

        word = ArrayList<String>()
        word.add("????????? ?????????")
        note_5_4.text = setSapn(note_5_4.text.toString(),word)

        var dateForamt = SimpleDateFormat("yyyy???")
        year_txt.text = dateForamt.format(Date())

        dateForamt = SimpleDateFormat("MM???")
        month_txt.text = dateForamt.format(Date())

        dateForamt = SimpleDateFormat("dd???")
        date_txt.text = dateForamt.format(Date())
    }



    fun setSapn(source:String,word:ArrayList<String>): SpannableString{

        val span = SpannableString(source)
        val bold = StyleSpan(Typeface.BOLD)

        for(i:Int in 0 until word.size){
            val start = source.indexOf(word[i])
            val end = start + word[i].length

            span.setSpan(bold,start,end,Spannable.SPAN_INCLUSIVE_INCLUSIVE)
            span.setSpan(ForegroundColorSpan(Color.BLACK), start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        return span
    }

}
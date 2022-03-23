package kr.co.myeoungju.hasnailstudio

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
import kotlinx.android.synthetic.main.accept_the_terms_activity.*
import kr.co.myeoungju.hasnailstudio.utils.CreatePDF
import java.text.SimpleDateFormat
import java.util.*

class AcceptTheTermsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.accept_the_terms_activity)
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
            val cp = CreatePDF()
            cp.LoadBitmap(scrollview_linear,scrollview_linear.width,scrollview_linear.height)
        }
    }

    fun setTextSpan(){
        var word = ArrayList<String>()
        word.add("소비자의 미확인된 질환 (젤/아세톤 알러지 등의 기타 질환)이 발현")
        note_2_1.text = setSapn(note_2_1.text.toString(),word)

        word = ArrayList<String>()
        word.add("이와 관련해 시술 후 발생하는 문제에 대해선 책임지지 않습니다.")
        note_2_3.text = setSapn(note_2_3.text.toString(),word)

        word = ArrayList<String>()
        word.add("통증 및 소량의 출혈")
        note_3_1.text = setSapn(note_3_1.text.toString(),word)

        word = ArrayList<String>()
        word.add("거스러미")
        word.add("충분한 보습")
        note_3_2.text = setSapn(note_3_2.text.toString(),word)

        word = ArrayList<String>()
        word.add("3주 ~ 4주")
        word.add("2주")
        note_4_1.text = setSapn(note_4_1.text.toString(),word)

        word = ArrayList<String>()
        word.add("크게 손상")
        word.add("네일샵에 방문하여 제거")
        note_4_3.text = setSapn(note_4_3.text.toString(),word)

        word = ArrayList<String>()
        word.add("부착된 파츠가 떨어지거나 끝 까짐, 젤 리프팅")
        note_4_4.text = setSapn(note_4_4.text.toString(),word)

        word = ArrayList<String>()
        word.add("곰팡이")
        note_4_5.text = setSapn(note_4_5.text.toString(),word)

        word = ArrayList<String>()
        word.add("곰팡이")
        note_4_6.text = setSapn(note_4_6.text.toString(),word)

        word = ArrayList<String>()
        word.add("3일 내")
        note_5_1.text = setSapn(note_5_1.text.toString(),word)

        word = ArrayList<String>()
        word.add("7일 내")
        note_5_2.text = setSapn(note_5_2.text.toString(),word)

        word = ArrayList<String>()
        word.add("고객 부주의로 인한 리프팅, 단순 변심, 시술받은 일로부터 1주 이상 지난 경우")
        note_5_3.text = setSapn(note_5_3.text.toString(),word)

        word = ArrayList<String>()
        word.add("환불이 불가능")
        note_5_4.text = setSapn(note_5_4.text.toString(),word)

        var dateForamt = SimpleDateFormat("yyyy년")
        year_txt.text = dateForamt.format(Date())

        dateForamt = SimpleDateFormat("MM월")
        month_txt.text = dateForamt.format(Date())

        dateForamt = SimpleDateFormat("dd일")
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
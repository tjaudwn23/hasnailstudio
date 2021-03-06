package kr.co.myeoungju.hasnailstudio

import android.content.Intent
import kr.co.myeoungju.hasnailstudio.helper.SwipeHelperCallback
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.accept_the_terms_activity.*
import kotlinx.android.synthetic.main.main_activity.*
import kr.co.myeoungju.hasnailstudio.adapter.DateRecyclerAdapter
import kr.co.myeoungju.hasnailstudio.adapter.UserInfoRecyclerAdapter
import kr.co.myeoungju.hasnailstudio.common.Utils
import kr.co.myeoungju.hasnailstudio.entity.DateInfo
import kr.co.myeoungju.hasnailstudio.entity.GuestInfo
import kr.co.myeoungju.hasnailstudio.helper.DateSwipeHelperCallback
import kr.co.myeoungju.hasnailstudio.search.SearchActivity

class MainActivity:AppCompatActivity() {

    lateinit var activityResultLauncher:ActivityResultLauncher<Intent>
    var guestAdapter:UserInfoRecyclerAdapter? = null
    var dateAdapter:DateRecyclerAdapter? = null


    val NAME_REQUESTCODE = 10000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        bind()
        attribute()

        //getGest()
    }

    fun bind(){
        nameBind()
        dateBind()
        infoBind()

    }

    fun getGest(){

        val searchText = search_name_textField.text.toString()

        val firestore = FirebaseFirestore.getInstance()
        val docRef = firestore.collection("guest")
            .whereEqualTo("name",searchText)

        docRef.get()
            .addOnSuccessListener {
                val datas = ArrayList<GuestInfo>()
                for (doc in it!!) {
                    val dn: GuestInfo = Utils.parseHashMapToObject(
                        doc.data,
                        GuestInfo::class.java
                    ) as GuestInfo
                    dn.key = doc.id
                    datas.add(dn)
                }
                guestAdapter?.setData(datas)
            }.addOnFailureListener {
                guestAdapter?.setData(ArrayList<GuestInfo>())
            }
       /* val firestore = FirebaseFirestore.getInstance()
        val docRef = firestore.collection("guest")
        docRef.addSnapshotListener(EventListener { value, error ->
            if (error != null) {
                Log.w("listener", "Listen failed.", error)
                return@EventListener
            }
            val datas = ArrayList<GuestInfo>()
            for (doc in value!!) {
                val dn: GuestInfo = Utils.parseHashMapToObject(
                    doc.data,
                    GuestInfo::class.java
                ) as GuestInfo
                dn.key = doc.id
                datas.add(dn)

                guestAdapter?.setData(datas)
            }
        })*/
    }

    fun getDate(guestInfo:GuestInfo){
        val firestore = FirebaseFirestore.getInstance()
        val docRef = firestore.collection("guest").document(guestInfo.key).collection("surgery")
        docRef.get()
            .addOnSuccessListener {
                val datas = ArrayList<DateInfo>()
                for (doc in it!!) {
                    val dn: DateInfo = Utils.parseHashMapToObject(
                        doc.data,
                        DateInfo::class.java
                    ) as DateInfo
                    dn.key = doc.id
                    datas.add(dn)


                }
                con_date.visibility = View.VISIBLE
                dateAdapter?.setData(datas,guestInfo)
            }.addOnFailureListener {
                Log.w("listener", "Listen failed.", it)
            }
    }

    fun searchDate(guestInfo:GuestInfo){
        val searchDate = search_date_textField.text.toString()
        val firestore = FirebaseFirestore.getInstance()
        val docRef = firestore.collection("guest").document(guestInfo.key).collection("surgery")
            .whereGreaterThanOrEqualTo("date",searchDate)
        docRef.get()
            .addOnSuccessListener {
                val datas = ArrayList<DateInfo>()
                for (doc in it!!) {
                    val dn: DateInfo = Utils.parseHashMapToObject(
                        doc.data,
                        DateInfo::class.java
                    ) as DateInfo
                    dn.key = doc.id
                    datas.add(dn)


                }
                con_date.visibility = View.VISIBLE
                dateAdapter?.setData(datas,guestInfo)
            }.addOnFailureListener {
                Log.w("listener", "Listen failed.", it)
            }
    }

    fun nameBind(){
        register_btn.setOnClickListener {
            val intent = Intent(this,AcceptTheTermsActivity::class.java)
            activityResultLauncher.launch(intent)
        }

        search_name_textField.setOnEditorActionListener { textView, i, keyEvent ->
            if(i == EditorInfo.IME_ACTION_SEARCH){
                getGest()
            }
            true
        }

        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if(it.resultCode == RESULT_OK){
                val intent = it.data
                val data = intent!!.getParcelableExtra<GuestInfo>("data") ?: GuestInfo()
                search_name_textField.setText(data.name)
                getGest()

            }
        }
    }

    fun dateBind(){
        date_back_btn.setOnClickListener {
            con_name.visibility = View.VISIBLE
            con_treatment.visibility = View.GONE
            con_date.visibility = View.GONE
        }

        new_btn.setOnClickListener {
            val intent = Intent(this,AcceptTheTermsActivity::class.java)
            intent.putExtra("guest",dateAdapter?.selectGuest)
            startActivity(intent)
        }

        search_date_textField.setOnEditorActionListener { textView, i, keyEvent ->
            if(i == EditorInfo.IME_ACTION_SEARCH){
                searchDate(dateAdapter!!.selectGuest)
            }
            true
        }
    }

    fun infoBind(){
        back_btn.setOnClickListener {
            con_treatment.visibility = View.GONE
        }

    }

    fun attribute(){
        setUserInfo()
        setDateInfo()
    }

    fun  setDateInfo(){
        // ?????????????????? ????????? ??????
        dateAdapter = DateRecyclerAdapter(this)
        date_recyclerview.adapter = dateAdapter
        date_recyclerview.layoutManager = LinearLayoutManager(this)

        // ????????????????????? ????????????, ????????? ?????? ??????
        val swipeHelperCallback = DateSwipeHelperCallback(dateAdapter!!).apply {
            // ??????????????? ??? ???????????? ?????? ??????
            setClamp(resources.displayMetrics.density * 100)    // 1080 / 4 = 270
        }
        ItemTouchHelper(swipeHelperCallback).attachToRecyclerView(date_recyclerview)

        // ????????? ??????
        date_recyclerview.addItemDecoration(DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL))

        // ?????? ??? ?????? ??? ?????? ???????????? ??? ??????
        date_recyclerview.setOnTouchListener { _, _ ->
            swipeHelperCallback.removePreviousClamp(date_recyclerview)
            false
        }
    }

    fun setUserInfo(){
        // ?????????????????? ????????? ??????
        guestAdapter = UserInfoRecyclerAdapter(this)
        name_recyclerview.adapter = guestAdapter
        name_recyclerview.layoutManager = LinearLayoutManager(this)

        // ????????????????????? ????????????, ????????? ?????? ??????
        val swipeHelperCallback = SwipeHelperCallback(guestAdapter!!).apply {
            // ??????????????? ??? ???????????? ?????? ??????
            setClamp(resources.displayMetrics.density * 100)    // 1080 / 4 = 270
        }
        ItemTouchHelper(swipeHelperCallback).attachToRecyclerView(name_recyclerview)

        // ????????? ??????
        name_recyclerview.addItemDecoration(DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL))

        // ?????? ??? ?????? ??? ?????? ???????????? ??? ??????
        name_recyclerview.setOnTouchListener { _, _ ->
            swipeHelperCallback.removePreviousClamp(name_recyclerview)
            false
        }
    }
}
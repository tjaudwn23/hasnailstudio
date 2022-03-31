package kr.co.myeoungju.hasnailstudio

import kr.co.myeoungju.hasnailstudio.helper.SwipeHelperCallback
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.main_activity.*
import kr.co.myeoungju.hasnailstudio.adapter.DateRecyclerAdapter
import kr.co.myeoungju.hasnailstudio.adapter.UserInfoRecyclerAdapter
import kr.co.myeoungju.hasnailstudio.helper.DateSwipeHelperCallback

class MainActivity:AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        bind()
        attribute()
    }

    fun bind(){


    }

    fun attribute(){
        setUserInfo()
        setDateInfo()
    }

    fun  setDateInfo(){
        // 리사이클러뷰 아이템 생성
        val items = arrayListOf("안녕 - 조이", "Je T'aime - 조이", "Day by Day - 조이", "좋을텐데(If Only)(Feat. 폴킴) - 조이",
            "Happy Birthday To You - 조이", "그럴때마다(Be There For You) - 조이", "매일 그대와 - 아이유", "너의 의미 - 아이유",
            "여우야 - 조이", "요즘 너 말야 - 조이", "러브레터 - 정승환")

        // 리사이클러뷰 어댑터 달기
        val recyclerViewAdapter = DateRecyclerAdapter(this)
        date_recyclerview.adapter = recyclerViewAdapter
        date_recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerViewAdapter.setData(items)

        // 리사이클러뷰에 스와이프, 드래그 기능 달기
        val swipeHelperCallback = DateSwipeHelperCallback(recyclerViewAdapter).apply {
            // 스와이프한 뒤 고정시킬 위치 지정
            setClamp(resources.displayMetrics.density * 100)    // 1080 / 4 = 270
        }
        ItemTouchHelper(swipeHelperCallback).attachToRecyclerView(date_recyclerview)

        // 구분선 추가
        date_recyclerview.addItemDecoration(DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL))

        // 다른 곳 터치 시 기존 선택했던 뷰 닫기
        date_recyclerview.setOnTouchListener { _, _ ->
            swipeHelperCallback.removePreviousClamp(date_recyclerview)
            false
        }
    }

    fun setUserInfo(){
        // 리사이클러뷰 아이템 생성
        val items = arrayListOf("안녕 - 조이", "Je T'aime - 조이", "Day by Day - 조이", "좋을텐데(If Only)(Feat. 폴킴) - 조이",
            "Happy Birthday To You - 조이", "그럴때마다(Be There For You) - 조이", "매일 그대와 - 아이유", "너의 의미 - 아이유",
            "여우야 - 조이", "요즘 너 말야 - 조이", "러브레터 - 정승환")

        // 리사이클러뷰 어댑터 달기
        val recyclerViewAdapter = UserInfoRecyclerAdapter(this)
        name_recyclerview.adapter = recyclerViewAdapter
        name_recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerViewAdapter.setData(items)

        // 리사이클러뷰에 스와이프, 드래그 기능 달기
        val swipeHelperCallback = SwipeHelperCallback(recyclerViewAdapter).apply {
            // 스와이프한 뒤 고정시킬 위치 지정
            setClamp(resources.displayMetrics.density * 100)    // 1080 / 4 = 270
        }
        ItemTouchHelper(swipeHelperCallback).attachToRecyclerView(name_recyclerview)

        // 구분선 추가
        name_recyclerview.addItemDecoration(DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL))

        // 다른 곳 터치 시 기존 선택했던 뷰 닫기
        name_recyclerview.setOnTouchListener { _, _ ->
            swipeHelperCallback.removePreviousClamp(name_recyclerview)
            false
        }
    }
}
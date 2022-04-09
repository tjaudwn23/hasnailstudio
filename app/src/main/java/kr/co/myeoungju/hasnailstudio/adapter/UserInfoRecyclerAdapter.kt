package kr.co.myeoungju.hasnailstudio.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.userinfo_recyclercell.view.*
import kr.co.myeoungju.hasnailstudio.MainActivity
import kr.co.myeoungju.hasnailstudio.R
import kr.co.myeoungju.hasnailstudio.entity.GuestInfo
import java.util.*
import kotlin.collections.ArrayList

class UserInfoRecyclerAdapter(val superCon: MainActivity) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){


    var lstDatas = ArrayList<GuestInfo>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.userinfo_recyclercell,parent,false)

        return ViewHolder(view,superCon)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val mHolder = holder as ViewHolder
        mHolder.initView(lstDatas,position)
    }

    override fun getItemCount(): Int {
        return lstDatas.size
    }


    class ViewHolder(itemView: View,val superCon: MainActivity) : RecyclerView.ViewHolder(itemView){

        fun initView(datas:ArrayList<GuestInfo>,position: Int )
        {
            val info = datas[position]
            itemView.name_txt.text = "이름 : " + info.name
            itemView.register_txt.text = "등록일자 : " +  info.register_date
            itemView.phoneNum_txt.text = "휴대폰 번호 : " + info.phone_num
            itemView.birth_txt.text = "생년월일 : " + info.birth


            itemView.setOnClickListener {

                superCon.getDate(info)
            }

        }

    }

    // position 위치의 데이터를 삭제 후 어댑터 갱신
    fun removeData(position: Int) {
        lstDatas.removeAt(position)
        notifyItemRemoved(position)
    }

    // 현재 선택된 데이터와 드래그한 위치에 있는 데이터를 교환
    fun swapData(fromPos: Int, toPos: Int) {
        Collections.swap(lstDatas, fromPos, toPos)
        notifyItemMoved(fromPos, toPos)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data:ArrayList<GuestInfo>){
        lstDatas = data
        notifyDataSetChanged()
    }
}
package kr.co.myeoungju.hasnailstudio.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.date_recyclercell.view.*
import kotlinx.android.synthetic.main.main_activity.*
import kr.co.myeoungju.hasnailstudio.MainActivity
import kr.co.myeoungju.hasnailstudio.R
import kr.co.myeoungju.hasnailstudio.entity.DateInfo
import kr.co.myeoungju.hasnailstudio.entity.GuestInfo
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DateRecyclerAdapter(val superCon: MainActivity) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){


    var lstDatas = ArrayList<DateInfo>()
    var selectGuest:GuestInfo = GuestInfo()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.date_recyclercell,parent,false)

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

        fun initView(datas:ArrayList<DateInfo>,position: Int )
        {
            val info = datas[position]
            itemView.date_txt.text = info.date

            itemView.setOnClickListener {
                superCon.con_name.visibility = View.GONE
                superCon.con_treatment.visibility = View.VISIBLE
                Glide.with(itemView.context).load(info.agree_url).into(superCon.info_imageView)
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
    fun setData(data:ArrayList<DateInfo>, info:GuestInfo){
        selectGuest = info
        lstDatas = data
        notifyDataSetChanged()
    }
}
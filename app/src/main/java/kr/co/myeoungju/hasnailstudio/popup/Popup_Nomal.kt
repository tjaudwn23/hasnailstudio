package kr.co.myeoungju.hasnailstudio.popup

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.WindowManager
import kotlinx.android.synthetic.main.popup_nomal.*
import kr.co.myeoungju.hasnailstudio.R

class Popup_Nomal(context: Context)
{
    private val dialog = Dialog(context)

    fun showDialog(note:String, operation: () -> Unit)
    {
        dialog.setContentView(R.layout.popup_nomal)
        dialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.show()

        dialog.txt_note.text = note
        dialog.btn_confirem.setOnClickListener {
            operation()
        }
    }

    fun dismiss(){
        dialog.dismiss()
    }



}
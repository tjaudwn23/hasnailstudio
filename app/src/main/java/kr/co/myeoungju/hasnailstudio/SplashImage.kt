package kr.co.myeoungju.hasnailstudio

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class SplashImage:AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_img)
        bind()
        attribute()
    }

    fun bind(){
        openTabBarActivity()
    }

    fun attribute(){

    }
    fun openTabBarActivity(){
        Handler().postDelayed({
            val mIntent = Intent(this,MainActivity::class.java)
            startActivity(mIntent)
            finish()
        },2000)
    }
}
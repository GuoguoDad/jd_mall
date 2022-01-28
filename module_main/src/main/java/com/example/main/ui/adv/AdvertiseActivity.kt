package com.example.main.ui.adv

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import com.example.main.MainActivity
import com.example.main.R
import com.example.main.ui.base.BaseActivity
import kotlinx.android.synthetic.main.layout_advertisement.*

class AdvertiseActivity: BaseActivity() {
    private var count: Int = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_advertisement)
        initView()
    }

    private fun initView() {
        btn_skip.setOnClickListener {
            toMainActivity()
        }
        skipSecondsHandler.sendEmptyMessageDelayed(0, 1000)
    }

    private var skipSecondsHandler: Handler = object : Handler(Looper.getMainLooper()){
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when(msg.what){
                0 -> {
                    btn_skip.text = "跳过${getTextCount()}s"
                    this.sendEmptyMessageDelayed(0, 1000)
                }
            }
        }
    }


    fun getTextCount(): Int {
        count--
        if(count === 0) {
            toMainActivity()
        }
        return count
    }

    private fun toMainActivity() {
        val toMain = Intent(this, MainActivity::class.java)
        startActivity(toMain)
        finish()
    }
}

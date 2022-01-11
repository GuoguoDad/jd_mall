package com.example.news.splash

import android.content.Intent
import android.os.Bundle
import com.example.news.MainActivity
import com.example.news.adv.AdvertiseActivity
import com.example.news.base.BaseActivity

class StartPageActivity: BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Thread{
            Thread.sleep(1000)
            val toAd = Intent(this, MainActivity::class.java)
            startActivity(toAd)
            finish()
        }.start()
    }
}



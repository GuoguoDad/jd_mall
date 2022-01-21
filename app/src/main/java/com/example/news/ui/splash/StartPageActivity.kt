package com.example.news.ui.splash

import android.content.Intent
import android.os.Bundle
import com.example.news.MainActivity
import com.example.news.ui.base.BaseActivity

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



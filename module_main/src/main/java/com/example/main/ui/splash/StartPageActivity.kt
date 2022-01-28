package com.example.main.ui.splash

import android.content.Intent
import android.os.Bundle
import com.example.main.MainActivity
import com.example.main.ui.base.BaseActivity

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



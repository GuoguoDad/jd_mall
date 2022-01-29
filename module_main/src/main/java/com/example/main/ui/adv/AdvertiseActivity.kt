package com.example.main.ui.adv

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.example.common.constants.RouterPaths
import com.example.main.R
import com.example.main.ui.base.BaseActivity
import kotlinx.android.synthetic.main.layout_advertisement.*

@Route(path = RouterPaths.ADV_ACTIVITY)
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
        ARouter.getInstance().build(RouterPaths.MAIN_ACTIVITY).navigation()
        finish()
    }
}

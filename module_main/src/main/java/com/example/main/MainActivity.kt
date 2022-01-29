package com.example.main

import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.common.constants.RouterPaths
import com.example.common.ui.BaseActivity
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import kotlinx.android.synthetic.main.layout_main.*

@Route(path = RouterPaths.MAIN_ACTIVITY)
class MainActivity : BaseActivity(R.layout.layout_main) {

    override fun initView() {
        val navController = findNavController(R.id.layout_fragment_activity_main)
        nav_view.labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_LABELED
        nav_view.setupWithNavController(navController)
    }

    override fun initData() {}
}
package com.example.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import kotlinx.android.synthetic.main.layout_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_main)

        val navController = findNavController(R.id.layout_fragment_activity_main)
        nav_view.labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_LABELED
        nav_view.setupWithNavController(navController)
    }
}
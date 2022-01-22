package com.example.news.kit.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder

open class GsonUtil private constructor(){
    companion object{
        val instance: Gson by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").setPrettyPrinting().create()
        }
    }
}
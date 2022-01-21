package com.example.news.common.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder

open class GsonUtil {
    companion object{
        open var instance: Gson? = null

        init {
            if (instance == null) {
                instance = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create()
            }
        }
    }
}
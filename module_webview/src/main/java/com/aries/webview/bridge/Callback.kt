package com.aries.webview.bridge

interface Callback {
    fun call(map: HashMap<String, Any>?)
}
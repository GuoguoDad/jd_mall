package com.aries.webview.bridge

import com.aries.webview.bridge.Callback

interface Handler {
    fun handler(map: HashMap<String, Any>?, callback: Callback)
}
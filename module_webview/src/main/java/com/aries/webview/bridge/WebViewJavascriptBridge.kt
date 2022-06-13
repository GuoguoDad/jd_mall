package com.aries.webview.bridge

import android.annotation.SuppressLint
import android.content.Context
import android.webkit.JavascriptInterface
import android.webkit.WebView
import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader

class WebViewJavascriptBridge(_context: Context?, _webView: WebView?) {
    private var context: Context? = _context
    private var webView: WebView? = _webView
    var consolePipe: ConsolePipe? = null
    private var responseCallbacks: HashMap<String, Callback> = java.util.HashMap()
    private var messageHandlers: HashMap<String, Handler> = java.util.HashMap()
    private var uniqueId = 0
    init {
        setupBridge()
    }
    @SuppressLint("SetJavaScriptEnabled", "JavascriptInterface")
    fun setupBridge() {
        println("setupBridge")
        val webSettings = webView!!.settings
        webSettings.javaScriptEnabled = true
        // 开启js支持
        webView!!.addJavascriptInterface(this, "normalPipe")
        webView!!.addJavascriptInterface(this, "consolePipe")
    }
    @JavascriptInterface
    fun postMessage(data: String?) {
        flush(data)
    }
    @JavascriptInterface
    fun receiveConsole(data: String?) {
        if (consolePipe != null) {
            consolePipe!!.post(data!!)
        }
    }
    fun injectJavascript() {
        val script = getFromAssets(context!!, "bridge.js")
        webView!!.loadUrl("javascript:$script")
        val script1 = getFromAssets(context!!, "hookConsole.js")
        webView!!.loadUrl("javascript:$script1")
    }
    fun register(handlerName: String?, handler: Handler?) {
        messageHandlers[handlerName!!] = handler!!
    }
    fun remove(handlerName: String?) {
        messageHandlers.remove(handlerName!!)
    }
    fun call(
        handlerName: String?,
        data: java.util.HashMap<String, Any>?,
        callback: Callback?
    ) {
        val message = java.util.HashMap<String?, Any?>()
        message["handlerName"] = handlerName
        if (data != null) {
            message["data"] = data
        }
        if (callback != null) {
            uniqueId += 1
            val callbackId = "native_cb_$uniqueId"
            responseCallbacks[callbackId] = callback
            message["callbackId"] = callbackId
        }
        dispatch(message)
    }
    private fun flush(messageString: String?) {
        if (messageString == null) {
            println("Javascript give data is null")
            return
        }
        val gson = Gson()
        val message = gson.fromJson(
            messageString,
            java.util.HashMap::class.java
        )
        val responseId = message["responseId"] as String?
        if (responseId != null) {
            val callback = responseCallbacks[responseId]
            val responseData = message["responseData"] as LinkedTreeMap<String, Any>
            val response = HashMap<String, Any>()
            for ((key, value) in responseData) {
                response[key] = value
            }
            callback!!.call(response)
            responseCallbacks.remove(responseId)
        } else {
            val callback: Callback
            val callbackID = message["callbackId"] as String?
            if (callbackID != null) {
                callback = object : Callback {
                    override fun call(map: HashMap<String, Any>?) {
                        val msg: java.util.HashMap<String?, Any?> = java.util.HashMap<String?, Any?>()
                        msg["responseId"] = callbackID
                        msg["responseData"] = map
                        dispatch(msg)
                    }
                }
            } else {
                callback = object : Callback {
                    override fun call(map: HashMap<String, Any>?) {
                        println("no logic")
                    }
                }
            }
            val handlerName = message["handlerName"] as String?
            val handler = messageHandlers[handlerName!!]
            if (handler == null) {
                val error = String.format(
                    "NoHandlerException, No handler for message from JS:%s",
                    handlerName
                )
                println(error)
                return
            }
            val treeMap = message["data"] as LinkedTreeMap<String, Any>
            val response = HashMap<String, Any>()
            for ((key, value) in treeMap) {
                response[key] = value
            }
            handler.handler(response, callback)
        }
    }
    private fun dispatch(message: java.util.HashMap<String?, Any?>) {
        val jsonObject = JSONObject(message)
        var messageString = jsonObject.toString()
        messageString = messageString.replace("\\", "\\\\")
        messageString = messageString.replace("\"", "\\\"")
        messageString = messageString.replace("\'", "\\\'")
        messageString = messageString.replace("\n", "\\n")
        messageString = messageString.replace("\r", "\\r")
        messageString = messageString.replace("\u000C", "\\u000C")
        messageString = messageString.replace("\u2028", "\\u2028")
        messageString = messageString.replace("\u2029", "\\u2029")
        val javascriptCommand = String.format("WebViewJavascriptBridge.handleMessageFromNative('%s');", messageString)
        webView!!.post { webView!!.evaluateJavascript(javascriptCommand, null) }
    }

    private fun getFromAssets(context: Context, fileName: String): String? {
        try {
            val inputReader = InputStreamReader(context.resources.assets.open(fileName))
            val bufReader = BufferedReader(inputReader)
            var line: String?
            var result: String? = ""
            while (bufReader.readLine().also { line = it } != null) result += line
            return result
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }
}
package com.aries.webview.ui

import android.os.Bundle
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.aries.common.constants.RouterPaths
import com.aries.common.util.StatusBarUtil
import com.aries.webview.R
import kotlinx.android.synthetic.main.activity_webview.*

@Route(path = RouterPaths.WebView_ACTIVITY)
class WebViewActivity: AppCompatActivity() {
    @Autowired
    @JvmField
    var isDarkTheme: Boolean = false

    @Autowired
    @JvmField
    var url: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ARouter.getInstance().inject(this)
        setContentView(R.layout.activity_webview)
        initView()
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            finish()
        }
    }

    override fun onDestroy() {
        webView.destroy()
        super.onDestroy()
    }

    private fun initView() {
        StatusBarUtil.setBarTextModal(this, isDarkTheme)
        webView.settings.run {
            true.also { javaScriptEnabled = it }
            useWideViewPort = true
            loadWithOverviewMode = true
            displayZoomControls = false
            javaScriptCanOpenWindowsAutomatically = false
            loadsImagesAutomatically = true
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            defaultTextEncodingName = "utf-8"
        }

        webView.loadUrl(url)

        webView.webViewClient = object: WebViewClient(){
            @Deprecated("Deprecated in Java")
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                webView.loadUrl(url!!)
                return true
            }

            override fun onReceivedHttpError(
                view: WebView?,
                request: WebResourceRequest?,
                errorResponse: WebResourceResponse?,
            ) {
//                super.onReceivedHttpError(view, request, errorResponse)
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?,
            ) {
//                super.onReceivedError(view, request, error)
            }
        }
    }
}
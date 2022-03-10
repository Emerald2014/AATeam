package ru.kudesnik.aateam

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import ru.kudesnik.aateam.webview.MyWebViewClient


class MainActivity : AppCompatActivity() {
    private lateinit var webView: WebView
    private lateinit var containerButton: Button

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_main)
        val urlMain = "https://socloseslots.ru/y6D1QQSR"
        val urlTest = "https://yandex.ru/"

        containerButton = findViewById(R.id.buttonStart)
        webView = findViewById(R.id.webView)

        val buttonStart = findViewById<Button>(R.id.buttonStart)
        webView.webViewClient = MyWebViewClient()

        loadUrlInWebView(urlMain)

        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView, url: String) {
                webView.evaluateJavascript(
                    "(function() { return ('<html>'+document.getElementsByTagName('body')[0].innerHTML+'</html>'); })();"
                ) { html ->
                    if (html.contains("body")) {
                        containerButton.visibility = View.GONE
                        webView.visibility = View.VISIBLE
                    } else {
                        containerButton.visibility = View.VISIBLE
                        webView.visibility = View.GONE
                    }
                }
            }
        }

        buttonStart.setOnClickListener {
            containerButton.visibility = View.GONE
            supportFragmentManager.beginTransaction()
                .replace(R.id.containerFragment, SpinFragment.newInstance())
                .commit()
        }
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    private fun loadUrlInWebView(url: String) {
        webView.settings.apply {
            builtInZoomControls = false
            javaScriptEnabled = true
            useWideViewPort = true
            loadWithOverviewMode = true
            setSupportMultipleWindows(false)
        }
        CookieManager.getInstance().apply {
            setAcceptThirdPartyCookies(webView, true) // My minSdkVersion is 21
            removeAllCookies { value ->
                Log.d("Cookies", "Removed all cookies from CookieManager")
            }
        }

        webView.apply {
            isVerticalScrollBarEnabled = true
            isHorizontalScrollBarEnabled = true
            loadUrl(
                url,
                mutableMapOf(
                    "Cookie" to "ThisIsMyCookieWithMyValues",
                    "Accept" to "*/*",
                    "Accept-Encoding" to "gzip, deflate",
                    "Cache-Control" to "no-cache",
                    "Content-type" to "application/x-www-form-urlencoded"
                )
            )
        }
    }
}


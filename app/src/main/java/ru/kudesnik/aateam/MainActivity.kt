package ru.kudesnik.aateam

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
import org.jsoup.Jsoup
import org.jsoup.nodes.Document


private const val URL_MAIN = "https://socloseslots.ru/y6D1QQSR"

class MainActivity : AppCompatActivity() {
    private lateinit var webView: WebView
    private lateinit var containerButton: FrameLayout
    private lateinit var bodyUrl: String
    private lateinit var myString: String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val urlTest = "https://yandex.ru/"   //Для теста

        doCheckUrl(URL_MAIN)
        containerButton = findViewById(R.id.containerButton)
//        loadUrlInWebView(URL_MAIN)

        val buttonStart = findViewById<Button>(R.id.buttonStart)

        buttonStart.setOnClickListener {
            containerButton.visibility = View.GONE
            supportFragmentManager.beginTransaction()
                .replace(R.id.containerFragment, SpinFragment.newInstance())
                .commit()
        }
    }

    private fun doCheckUrl(url: String) {
        Thread {
            val doc: Document = Jsoup.connect(url)
                .userAgent("Chrome/79.0.3945.130 Safari/537.36")
                .get()
            Log.d("testingMy", "  ${doc.body()}")

//            bodyUrl = "<body ...><bold>https://www.mos.ru</bold></body>" //Для теста
            bodyUrl = doc.body().toString()

            if (bodyUrl.contains("http")) {
                myString = bodyUrl
                    .substringAfter("<body")
                    .substringBefore("</body")
                myString = myString.substring(myString.lastIndexOf("http")).substringBefore('<')
                Log.d("testingMy", "Тело содержит ссылку.  $myString")
                runOnUiThread {
                    loadUrlInWebView(myString)
                }
            } else {
                Log.d("myTesting", "Тело НЕ содержит ссылку.  $bodyUrl")
                runOnUiThread {
                    containerButton.visibility = View.VISIBLE
                    webView.visibility = View.GONE
                }
            }
        }.start()
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    private fun loadUrlInWebView(url: String) {
        webView = findViewById(R.id.webViewL)
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                return false
            }
        }

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
            Log.d("testingMy", "Заупская ссылку через метод с куки")
        }
    }
}

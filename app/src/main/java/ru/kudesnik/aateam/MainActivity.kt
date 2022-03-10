package ru.kudesnik.aateam

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isEmpty
import ru.kudesnik.aateam.webview.MyWebViewClient


class MainActivity : AppCompatActivity() {
    private lateinit var webView: WebView
    private lateinit var containerButton: FrameLayout

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        val url = "https://socloseslots.ru/y6D1QQSR"
        val url2 = "https://yandex.ru/"
        containerButton = findViewById(R.id.containerButton)
        webView = findViewById(R.id.webView)
        val buttonStart = findViewById<Button>(R.id.buttonStart)
        webView.webViewClient = MyWebViewClient()
        // включаем поддержку JavaScript
        webView.getSettings().setJavaScriptEnabled(true)
        // указываем страницу загрузки

        webView.loadUrl(url)

        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                Toast.makeText(this@MainActivity, "Loading", Toast.LENGTH_SHORT).show()

            }

            override fun onPageFinished(view: WebView, url: String) {
                if (webView.isEmpty()) {
                    Toast.makeText(this@MainActivity, "Empty", Toast.LENGTH_SHORT).show()
                    containerButton.visibility = View.VISIBLE
                    webView.visibility = View.GONE
                } else {
                    Toast.makeText(this@MainActivity, "NOT Empty", Toast.LENGTH_SHORT).show()

                    containerButton.visibility = View.GONE
                    webView.visibility = View.VISIBLE
                }
            }

//                println("Ух ты как просто!")
//                Toast.makeText(context, "Спасибо гуглу за это", Toast.LENGTH_LONG).show()
        }


        val web = webView.webViewClient.onPageFinished(webView, url)
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
}


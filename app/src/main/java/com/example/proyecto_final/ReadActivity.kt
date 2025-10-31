package com.example.proyecto_final

import android.os.Bundle
import android.webkit.WebView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ReadActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val webView = WebView(this)
        setContentView(webView)
        val url = intent.getStringExtra("url")
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(url ?: "https://www.gutenberg.org/")
    }
}
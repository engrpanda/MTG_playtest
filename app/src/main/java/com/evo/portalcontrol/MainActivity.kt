package com.evo.portalcontrol

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.evo.portalcontrol.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("login_prefs", Context.MODE_PRIVATE)

        initViews()
        loadUrl("https://global-robotmp.orionstar.com/web/portal/#/frame/hmag-rmc/hmag-rmc.rmc/")
    }

    private fun initViews() {
        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun loadUrl(url: String) {
        with(binding.webView.settings) {
            javaScriptEnabled = true
            domStorageEnabled = true
            loadWithOverviewMode = true
            useWideViewPort = true
            builtInZoomControls = true
            displayZoomControls = false
            userAgentString = "Mozilla/5.0 (Linux; Android 10; Mobile) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Mobile Safari/537.36"
        }
        binding.webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                if (url == "https://global-robotmp.orionstar.com/web/portal/#/frame/hmag-rmc/hmag-rmc.rmc/") {
                    autoFillLoginDetails()
                }
            }

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: android.webkit.WebResourceError?) {
                showToast("Failed to load URL")
            }
        }
        binding.webView.loadUrl(url)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun saveLoginDetails(username: String, password: String) {
        val editor = sharedPreferences.edit()
        editor.putString("username", username)
        editor.putString("password", password)
        editor.apply()
    }

    private fun getSavedUsername(): String {
        return sharedPreferences.getString("username", "") ?: ""
    }

    private fun getSavedPassword(): String {
        return sharedPreferences.getString("password", "") ?: ""
    }

    private fun autoFillLoginDetails() {
        val username = getSavedUsername()
        val password = getSavedPassword()

        if (username.isNotEmpty() && password.isNotEmpty()) {
            binding.webView.evaluateJavascript(
                "document.getElementById('username').value = '$username';" +
                        "document.getElementById('password').value = '$password';" +
                        "document.forms[0].submit();", null)
        }
    }

    // Example function to be called after successful login
    private fun onLoginSuccess(username: String, password: String) {
        saveLoginDetails(username, password)
        showToast("Login details saved")
    }
}

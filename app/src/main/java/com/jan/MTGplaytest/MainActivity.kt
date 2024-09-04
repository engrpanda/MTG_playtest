package com.jan.MTGplaytest

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jan.MTGplaytest.databinding.ActivityMainBinding
import android.view.View

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("login_prefs", Context.MODE_PRIVATE)

        initViews()
    }

    private fun initViews() {
        binding.buttonWebview1.setOnClickListener {
            supportActionBar?.hide()
            hideButtons()
            loadUrl("https://www.moxfield.com/decks/Le7BrEI170qgsZTCv8Gw0w/goldfish")
        }

        binding.buttonWebview2.setOnClickListener {
            supportActionBar?.hide()
            hideButtons()
            loadUrl("https://www.moxfield.com/decks/3Hn4JcETtkutfuYtjXvyCw/goldfish")
        }

        binding.buttonWebview3.setOnClickListener {
            supportActionBar?.hide()
            hideButtons()
            loadUrl("https://www.moxfield.com/decks/-cZamv84tE22y_aSgsEPhw/goldfish")
        }

        // Add listener for the Back button
        binding.buttonBack.setOnClickListener {
            if (binding.webView.visibility == View.VISIBLE) {
                supportActionBar?.show()
                showButtons()
            } else {
                onBackPressed()  // This will handle the default back button action
            }
        }
    }

    private fun hideButtons() {
        binding.buttonWebview1.visibility = View.GONE
        binding.buttonWebview2.visibility = View.GONE
        binding.buttonWebview3.visibility = View.GONE
        binding.buttonBack.visibility = View.VISIBLE
        binding.webView.visibility = View.VISIBLE
    }

    private fun showButtons() {
        binding.buttonWebview1.visibility = View.VISIBLE
        binding.buttonWebview2.visibility = View.VISIBLE
        binding.buttonWebview3.visibility = View.VISIBLE
        binding.buttonBack.visibility = View.VISIBLE
        binding.webView.visibility = View.GONE
    }

    private fun loadUrl(url: String) {
        binding.webView.settings.apply {
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
//                if (url == "https://global-robotmp.orionstar.com/web/portal/#/frame/hmag-person/hmag-person.person_corp") {
//                    autoFillLoginDetails()
//                }
            }

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: android.webkit.WebResourceError?) {
                showToast("Failed to load URL")
            }
        }
        binding.webView.loadUrl(url)
    }

    override fun onBackPressed() {
        if (binding.webView.canGoBack()) {
            binding.webView.goBack()
        } else {
            if (binding.webView.visibility == View.VISIBLE) {
                supportActionBar?.show()
                showButtons()
            } else {
                super.onBackPressed()
            }
        }
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

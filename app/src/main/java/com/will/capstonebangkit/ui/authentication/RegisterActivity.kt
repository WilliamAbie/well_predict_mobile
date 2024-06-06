package com.will.capstonebangkit.ui.authentication

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Html
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.will.capstonebangkit.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.tvLoginRedirect.text =
            Html.fromHtml("<font color=${Color.WHITE}>Already have an account? </font>" + "<font color=#5AB2FF>Log In</font>", HtmlCompat.FROM_HTML_MODE_LEGACY)

        binding.tvLoginRedirect.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
package com.maratangsoft.mykaraokebook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        loadPref()

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }

    private fun loadPref(){
        val pref = getSharedPreferences("setting", MODE_PRIVATE)
        brand = pref.getString("brand", BRAND_TJ).toString()
        rowCount = pref.getInt("rowCount", 50)
    }
}
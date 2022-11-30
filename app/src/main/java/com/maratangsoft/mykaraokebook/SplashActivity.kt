package com.maratangsoft.mykaraokebook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadPref()
        goToMain()
    }
/////////////////////////////////////////////////////////////////////////////////////////////////

    private fun loadPref(){
        val pref = getSharedPreferences("setting", MODE_PRIVATE)
        brand = pref.getString("brand", BRAND_TJ).toString()
        rowCount = pref.getInt("rowCount", 50)
    }

    private fun goToMain(){
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 1500)
    }
}
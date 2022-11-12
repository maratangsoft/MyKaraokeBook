package com.maratangsoft.mykaraokebook

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts

class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

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
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }
}
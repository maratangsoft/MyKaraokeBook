package com.maratangsoft.mykaraokebook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.maratangsoft.mykaraokebook.databinding.ActivitySettingBinding

var brand = "tj"
var rowCount = 50

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onNavigateUp(): Boolean {
        finish()
        return super.onNavigateUp()
    }
}
package com.maratangsoft.mykaraokebook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.maratangsoft.mykaraokebook.databinding.ActivitySettingBinding

var brand = "tj"
var rowCount = 50

class SettingActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySettingBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

//        when (brand){
//            "tj" -> binding.radioTj.isChecked
//            "kumyoung" -> binding.radioKumyoung.isChecked
//        }

        binding.radioGroup.setOnCheckedChangeListener { group, checkedId -> checkRadio(checkedId) }
    }

    override fun onNavigateUp(): Boolean {
        finish()
        return super.onNavigateUp()
    }

    private fun checkRadio(checkedId:Int){
        when (checkedId){
            R.id.radio_tj -> brand = "tj"
            R.id.radio_kumyoung -> brand = "kumyoung"
        }
    }
}
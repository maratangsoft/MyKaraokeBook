package com.maratangsoft.mykaraokebook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.maratangsoft.mykaraokebook.databinding.ActivitySettingBinding
import kotlin.system.exitProcess

class SettingActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySettingBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.radioGroup.setOnCheckedChangeListener { _, checkedId -> checkRadio(checkedId) }
        binding.btnResetFavorite.setOnClickListener { resetFavorite() }
        binding.btnExportFavorite.setOnClickListener { exportFavorite() }
    }

    override fun onResume() {
        super.onResume()
        if (brand==BRAND_TJ) binding.radioTj.isChecked = true
        else if (brand==BRAND_KY) binding.radioKumyoung.isChecked = true
    }

/////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    private fun checkRadio(checkedId:Int){
        when (checkedId){
            R.id.radio_tj -> {
                brand = BRAND_TJ
                savePref(BRAND_TJ)
            }
            R.id.radio_kumyoung -> {
                brand = BRAND_KY
                savePref(BRAND_KY)
            }
        }
    }

    private fun savePref(value:String){
        val editor = getSharedPreferences("setting", MODE_PRIVATE).edit()
        when (value){
            BRAND_TJ -> editor.putString("brand", BRAND_TJ)
            BRAND_KY -> editor.putString("brand", BRAND_KY)
        }
        editor.apply()
    }

    private fun resetFavorite(){
        AlertDialog.Builder(this)
            .setMessage(R.string.msg_clearDB)
            .setPositiveButton(R.string.confirm) { _, _ ->
                SQLiteDB(this@SettingActivity).clearDB()
                val intent = packageManager.getLaunchIntentForPackage(packageName)
                val mainIntent = Intent.makeRestartActivityTask(intent?.component)
                startActivity(mainIntent)
                exitProcess(0)
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun exportFavorite(){
        Toast.makeText(this, "준비중입니다.", Toast.LENGTH_SHORT).show()
    }
}
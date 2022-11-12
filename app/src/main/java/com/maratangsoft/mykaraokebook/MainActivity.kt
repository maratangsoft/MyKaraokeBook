package com.maratangsoft.mykaraokebook

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.maratangsoft.mykaraokebook.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    var fragments: Array<Fragment?> = arrayOf(FavoriteFragment(), null, null, null, null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if(checkSelfPermission(permissions[0]) != PackageManager.PERMISSION_GRANTED){
            permissionLauncher.launch(permissions)
        }

        supportFragmentManager.beginTransaction().add(R.id.container, fragments[0]!!).commit()
        binding.bnv.setOnItemSelectedListener { clickBnv(it) }
    }
//////////////////////////////////////////////////////////////////////////////////////////////

    private fun clickBnv(menuItem: MenuItem): Boolean {
        val transaction = supportFragmentManager.beginTransaction()
        fragments[0]?.let { transaction.remove(it) }
        for (index in 1..4) fragments[index]?.let { transaction.hide(it) }

        when (menuItem.itemId){
            R.id.bnv_favorite -> {
                fragments[0]?.let { transaction.add(R.id.container, it) }
            }
            R.id.bnv_search -> {
                if (fragments[1] == null){
                    fragments[1] = SearchFragment()
                    fragments[1]?.let { transaction.add(R.id.container, it) }
                }
                transaction.show(fragments[1]!!)
            }
            R.id.bnv_newSong -> {
                if (fragments[2] == null){
                    fragments[2] = NewSongFragment()
                    fragments[2]?.let { transaction.add(R.id.container, it) }
                }
                transaction.show(fragments[2]!!)
            }
            R.id.bnv_popular -> {
                if (fragments[3] == null){
                    fragments[3] = PopularFragment()
                    fragments[3]?.let { transaction.add(R.id.container, it) }
                }
                transaction.show(fragments[3]!!)
            }
            R.id.bnv_location -> {
                if (fragments[4] == null){
                    fragments[4] = LocationFragment()
                    fragments[4]?.let { transaction.add(R.id.container, it) }
                }
                transaction.show(fragments[4]!!)
            }
        }
        transaction.commit()
        return true
    }

    private val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
        if (it.all { permission -> permission.value }) {
            Log.d("aaa_MainActivity", "퍼미션 허가됨")
            isPermitted = true
        }else{
            Log.d("aaa_MainActivity", "퍼미션 거부됨")
            Toast.makeText(this@MainActivity, "위치정보를 사용할 수 없습니다.", Toast.LENGTH_SHORT).show()
            isPermitted = false
        }
    }
}

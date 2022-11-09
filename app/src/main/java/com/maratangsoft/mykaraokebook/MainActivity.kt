package com.maratangsoft.mykaraokebook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.maratangsoft.mykaraokebook.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    var fragments: Array<Fragment?> = arrayOf(FavoriteFragment(), null, null, null, null)
    val manager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        manager.beginTransaction().add(R.id.container, fragments[0]!!).commit()
        binding.bnv.setOnItemSelectedListener { clickBnv(it) }
    }

    private fun clickBnv(menuItem: MenuItem): Boolean {
        val transaction = manager.beginTransaction()
        for (i in fragments) if(i != null) transaction.hide(i)

        when (menuItem.itemId){
            R.id.favorite_fragment -> {
                transaction.show(fragments[0]!!)

            }
            R.id.search_fragment -> {
                if (fragments[1] == null){
                    fragments[1] = SearchFragment()
                    transaction.add(R.id.container, fragments[1]!!)
                }
                transaction.show(fragments[1]!!)

            }
            R.id.new_song_fragment -> {
                if (fragments[2] == null){
                    fragments[2] = NewSongFragment()
                    transaction.add(R.id.container, fragments[2]!!)
                }
                transaction.show(fragments[2]!!)

            }
            R.id.popular_fragment -> {
                if (fragments[3] == null){
                    fragments[3] = PopularFragment()
                    transaction.add(R.id.container, fragments[3]!!)
                }
                transaction.show(fragments[3]!!)
            }
            R.id.map_fragment -> {
                if (fragments[4] == null){
                    fragments[4] = MapFragment()
                    transaction.add(R.id.container, fragments[4]!!)
                }
                transaction.show(fragments[4]!!)
            }
        }
        transaction.commit()
        return true
    }
}

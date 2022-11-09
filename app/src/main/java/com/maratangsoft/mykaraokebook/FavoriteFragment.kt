package com.maratangsoft.mykaraokebook

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.maratangsoft.mykaraokebook.databinding.FragmentFavoriteBinding

class FavoriteFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteBinding
    private var items:MutableList<Item> = mutableListOf()
    //private val db = SQLiteDB(requireActivity())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSort.setOnClickListener { openPopup() }
        binding.btnSetting.setOnClickListener { startActivity(Intent(requireActivity(), SettingActivity::class.java)) }
        binding.recycler.adapter = FavoriteAdapter(requireActivity(), items)

        //db.loadDB(items, binding.recycler.adapter)
    }

///////////////////////////////////////////////////////////////////////////////////////////////////

    private fun openPopup(){
        val popup = PopupMenu(requireActivity(), binding.btnSort)
        popup.menuInflater.inflate(R.menu.popup_favorite, popup.menu)
        popup.setOnMenuItemClickListener {
            val testText = when (it.itemId){
                R.id.title -> "bb"
                R.id.singer -> "cc"
                else -> "dd"
            }
            Toast.makeText(requireActivity(), testText, Toast.LENGTH_SHORT).show()
            true
        }
        popup.show()
    }
}
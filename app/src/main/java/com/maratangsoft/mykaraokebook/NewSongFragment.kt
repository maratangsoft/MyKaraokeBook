package com.maratangsoft.mykaraokebook

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.maratangsoft.mykaraokebook.databinding.FragmentNewSongBinding
import java.text.SimpleDateFormat
import java.util.*

class NewSongFragment : Fragment() {
    lateinit var binding: FragmentNewSongBinding
    var items:MutableList<Item> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentNewSongBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recycler.adapter = SearchAdapter(requireActivity(), items)
        binding.btnMonth.setOnClickListener { openPopup() }

        loadData()
    }

    private fun openPopup(){
        val popup = PopupMenu(requireActivity(), binding.btnMonth)
        popup.menuInflater.inflate(R.menu.popup_new_song, popup.menu)

        val calendar = Calendar.getInstance()
        for (i in 0..4){
            popup.menu.getItem(i).title = SimpleDateFormat("yyyy년 MM월").format(calendar.time)
            calendar.add(Calendar.MONTH, -1)
        }

        popup.setOnMenuItemClickListener {
            val testText = when (it.itemId){
                R.id.this_month -> "bb"
                R.id.before_1_month -> "cc"
                R.id.before_2_month -> "dd"
                R.id.before_3_month -> "ee"
                else -> "ff"
            }
            Toast.makeText(requireActivity(), testText, Toast.LENGTH_SHORT).show()
            true
        }
        popup.show()
    }

    private fun loadData(){
        items.add(Item("dd", 1, "dd", "sfsf", "gfwsefe", null))
        items.add(Item("dd", 1, "dd", "sfsf", "gfwsefe", null))
        items.add(Item("dd", 1, "dd", "sfsf", "gfwsefe", null))
    }
}
package com.maratangsoft.mykaraokebook

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.maratangsoft.mykaraokebook.databinding.FragmentFavoriteBinding
import com.maratangsoft.mykaraokebook.databinding.FragmentPopularBinding

class PopularFragment : Fragment() {
    lateinit var binding: FragmentPopularBinding
    var items:MutableList<Item> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPopularBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recycler.adapter = SearchAdapter(requireActivity(), items)
        binding.btnNation.setOnClickListener { openPopup() }

        loadData()
    }

    private fun openPopup(){
        val popup = PopupMenu(requireActivity(), binding.btnNation)
        popup.menuInflater.inflate(R.menu.popup_popular, popup.menu)
        popup.setOnMenuItemClickListener {
            val testText = when (it.itemId){
                R.id.korean -> "bb"
                R.id.english -> "cc"
                else -> "dd"
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
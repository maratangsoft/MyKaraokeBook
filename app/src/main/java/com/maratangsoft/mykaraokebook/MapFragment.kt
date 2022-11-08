package com.maratangsoft.mykaraokebook

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.maratangsoft.mykaraokebook.databinding.FragmentFavoriteBinding
import com.maratangsoft.mykaraokebook.databinding.FragmentMapBinding

class MapFragment : Fragment() {
    private lateinit var binding: FragmentMapBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSetting.setOnClickListener { startActivity(Intent(requireActivity(), SettingActivity::class.java)) }
    }
}
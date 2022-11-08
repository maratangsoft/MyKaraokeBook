package com.maratangsoft.mykaraokebook

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.appcompat.app.AlertDialog
import com.maratangsoft.mykaraokebook.databinding.FragmentNewSongBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.min

class NewSongFragment : Fragment() {
    private lateinit var binding: FragmentNewSongBinding
    private var items: MutableList<Item> = mutableListOf()
    private lateinit var result: MutableList<Item>
    private var targetMonth = SimpleDateFormat("yyyyMM").format(Date())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentNewSongBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnMonth.setOnClickListener { openPopup() }
        binding.btnMonth.text = SimpleDateFormat("yyyy년 MM월").format(Date())
        binding.btnSetting.setOnClickListener { startActivity(Intent(requireActivity(), SettingActivity::class.java)) }
        binding.recycler.adapter = SearchAdapter(requireActivity(), items)

        initData()
    }

///////////////////////////////////////////////////////////////////////////////////////////////////

    private fun openPopup(){
        val popup = PopupMenu(requireActivity(), binding.btnMonth)
        popup.menuInflater.inflate(R.menu.popup_new_song, popup.menu)

        for (i in 0..4){
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.MONTH, -i)
            popup.menu.getItem(i).title = SimpleDateFormat("yyyy년 M월").format(calendar.time)
        }

        popup.setOnMenuItemClickListener {
            var monthBefore = 0
            when (it.itemId){
                R.id.this_month -> monthBefore = 0
                R.id.before_1_month -> monthBefore = -1
                R.id.before_2_month -> monthBefore = -2
                R.id.before_3_month -> monthBefore = -3
                R.id.before_4_month -> monthBefore = -4
            }
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.MONTH, monthBefore)
            binding.btnMonth.text = it.title
            targetMonth = SimpleDateFormat("yyyyMM").format(calendar.time)
            initData()
            true
        }
        popup.show()
    }

    private fun initData(){
        items.clear()
        binding.recycler.adapter?.notifyDataSetChanged()

        RetrofitHelper.getInstance().create(RetrofitService::class.java)
        .loadNewSongData(targetMonth, brand).enqueue(object : Callback<MutableList<Item>> {

                override fun onResponse(call: Call<MutableList<Item>>, response: Response<MutableList<Item>>) {
                    response.body()?.let { result = it }
                    for (i in 0 until min(rowCount, result.size)) {
                        items.add(result[i])
                    }
                    binding.recycler.adapter?.notifyDataSetChanged()
                }

                override fun onFailure(call: Call<MutableList<Item>>, t: Throwable) {
                    AlertDialog.Builder(requireActivity()).setMessage(t.message).show()
                }

            })
    }
}
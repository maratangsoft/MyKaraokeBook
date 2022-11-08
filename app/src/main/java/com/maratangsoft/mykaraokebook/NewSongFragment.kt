package com.maratangsoft.mykaraokebook

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.maratangsoft.mykaraokebook.databinding.FragmentNewSongBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class NewSongFragment : Fragment() {
    private lateinit var binding: FragmentNewSongBinding
    private var items: MutableList<Item> = mutableListOf()
    private var targetMonth = SimpleDateFormat("yyyy-MM").format(Date())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentNewSongBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recycler.adapter = SearchAdapter(requireActivity(), items)
        binding.btnMonth.setOnClickListener { openPopup() }
        binding.btnMonth.text = SimpleDateFormat("yyyy년 MM월").format(Date())
        loadData()
    }

    private fun openPopup(){
        val popup = PopupMenu(requireActivity(), binding.btnMonth)
        popup.menuInflater.inflate(R.menu.popup_new_song, popup.menu)

        for (i in 0..4){
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.MONTH, -i)
            popup.menu.getItem(i).title = SimpleDateFormat("yyyy년 MM월").format(calendar.time)
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
            binding.btnMonth.text = "▼ "+SimpleDateFormat("yyyy년 MM월").format(calendar.time)
            targetMonth = SimpleDateFormat("yyyy-MM").format(calendar.time)
            loadData()
            true
        }
        popup.show()
    }

    private fun loadData(){
        items.clear()
        binding.recycler.adapter?.notifyDataSetChanged()

        val retrofitService = RetrofitHelper.getInstance().create(RetrofitService::class.java)
        retrofitService.loadNewSongData(brand).enqueue(object : Callback<MutableList<Item>> {
                override fun onResponse(call: Call<MutableList<Item>>, response: Response<MutableList<Item>>) {
                    response.body()?.let {
                        for (item in it){
                            //목표월에 등록된 곡인지 확인해서 맞으면 리사이클러뷰에 올리기
                            if (item.release.substring(0,6).equals(targetMonth)){
                                items.add(item)
                            }
                        }
                    }
                    binding.recycler.adapter?.notifyDataSetChanged()
                }

                override fun onFailure(call: Call<MutableList<Item>>, t: Throwable) {
                    AlertDialog.Builder(requireActivity()).setMessage(t.message).show()
                }

            })
    }
}
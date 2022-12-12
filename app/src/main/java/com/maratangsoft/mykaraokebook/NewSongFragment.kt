package com.maratangsoft.mykaraokebook

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.maratangsoft.mykaraokebook.databinding.FragmentNewSongBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.min

class NewSongFragment : Fragment() {
    private lateinit var binding: FragmentNewSongBinding
    private var items: MutableList<SongItem> = mutableListOf()
    private lateinit var result: MutableList<SongItem>
    private var targetMonth = SimpleDateFormat("yyyyMM").format(Date())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentNewSongBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.spinMonth.setOnClickListener { openPopup() }
        binding.spinMonth.text = SimpleDateFormat("yyyy년 MM월").format(Date())
        binding.btnSetting.setOnClickListener { startActivity(Intent(requireActivity(), SettingActivity::class.java)) }
        binding.recycler.adapter = SearchAdapter(requireActivity(), items)
        binding.recycler.addOnScrollListener(OnScrollListener())

        initData()
    }

///////////////////////////////////////////////////////////////////////////////////////////////////

    private fun openPopup(){
        val popup = PopupMenu(requireActivity(), binding.spinMonth)
        popup.menuInflater.inflate(R.menu.popup_new_song, popup.menu)

        for (i in 0..4){
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.MONTH, -i)
            popup.menu.getItem(i).title = SimpleDateFormat("yyyy년 M월").format(calendar.time)
        }

        popup.setOnMenuItemClickListener {
            val monthBefore = when (it.itemId){
                R.id.this_month -> 0
                R.id.before_1_month -> -1
                R.id.before_2_month -> -2
                R.id.before_3_month -> -3
                else -> -4
            }
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.MONTH, monthBefore)
            binding.spinMonth.text = it.title
            targetMonth = SimpleDateFormat("yyyyMM").format(calendar.time)
            initData()
            true
        }
        popup.show()
    }

    private fun initData(){
        items.clear()
        binding.recycler.adapter?.notifyDataSetChanged()

        RetrofitHelper.getInstance("https://api.manana.kr/")
            .create(RetrofitService::class.java)
            .loadNewSongData(targetMonth, brand)
            .enqueue(object : Callback<MutableList<SongItem>> {
                override fun onResponse(call: Call<MutableList<SongItem>>, response: Response<MutableList<SongItem>>) {
                    response.body()?.let { result = it }
                    for (i in 0 until min(rowCount, result.size)) {
                        items.add(result[i])
                        binding.recycler.adapter?.notifyItemInserted(i)
                    }
                    items.add(SongItem("","","","",null,null))
                    binding.recycler.adapter?.notifyItemInserted(items.lastIndex)
                    Log.d("ttt-initlastindex", "${items.lastIndex}")
                }
                override fun onFailure(call: Call<MutableList<SongItem>>, t: Throwable) {
                    AlertDialog.Builder(requireActivity()).setMessage(t.message).show()
                }
            })
    }

    inner class OnScrollListener: RecyclerView.OnScrollListener(){
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val lastVisibleItemPosition = (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()

            if (!binding.recycler.canScrollVertically(1) && lastVisibleItemPosition == items.lastIndex && items.lastIndex != -1){
                Log.d("ttt-listenerlastindex", "${items.lastIndex}")
                items.removeAt(items.lastIndex)
                binding.recycler.adapter?.notifyItemRemoved(items.lastIndex + 1)
                for (i in items.size until min(items.size+rowCount, result.size)){
                    items.add(result[i])
                    binding.recycler.adapter?.notifyItemInserted(i)
                }
            }
        }
    }
}
package com.maratangsoft.mykaraokebook

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.maratangsoft.mykaraokebook.databinding.FragmentSearchBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.min

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private var items:MutableList<SongItem> = mutableListOf()
    lateinit var result:MutableList<SongItem>
    private var query = QUERY_TITLE

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnQuery.setOnClickListener { openPopup() }
        binding.btnSetting.setOnClickListener { startActivity(Intent(requireActivity(), SettingActivity::class.java)) }
        binding.recycler.adapter = SearchAdapter(requireActivity(), items)
        binding.et.setOnEditorActionListener { _, actionId, _ -> editorAction(actionId) }
        binding.recycler.addOnScrollListener(OnScrollListener())
    }

///////////////////////////////////////////////////////////////////////////////////////////////////

    private fun openPopup(){
        val popup = PopupMenu(requireActivity(), binding.btnQuery)
        popup.menuInflater.inflate(R.menu.popup_search, popup.menu)
        popup.setOnMenuItemClickListener {
            query = when (it.itemId){
                R.id.title -> QUERY_TITLE
                R.id.singer -> QUERY_SINGER
                else -> QUERY_NO
            }
            binding.btnQuery.text = it.title
            true
        }
        popup.show()
    }

    private fun editorAction(actionId:Int): Boolean{
        return when (actionId){
            EditorInfo.IME_ACTION_SEARCH -> {
                if (binding.et.text.isNullOrEmpty() || binding.et.text.toString() == " "){
                    Toast.makeText(requireActivity(), "검색어를 입력해 주세요.", Toast.LENGTH_SHORT).show()
                    false
                } else {
                    initData()
                    val inputMethodManager = requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
                    true
                }
            }
            else -> false
        }
    }

    private fun initData(){
        items.clear()
        binding.recycler.adapter?.notifyDataSetChanged()

        val word = binding.et.text.toString().replace(" ", "")
        RetrofitHelper.getInstance("https://api.manana.kr/")
            .create(RetrofitService::class.java)
            .loadSearchData(query, word, brand)
            .enqueue(object: Callback<MutableList<SongItem>>{
                override fun onResponse(call: Call<MutableList<SongItem>>, response: Response<MutableList<SongItem>>) {
                    if (response.body().isNullOrEmpty()) {
                        Toast.makeText(requireActivity(), "검색 결과가 없습니다!", Toast.LENGTH_SHORT).show()
                    } else {
                        response.body()?.let { result = it }
                        for (i in 0 until min(rowCount, result.size)) {
                            items.add(result[i])
                            binding.recycler.adapter?.notifyItemInserted(i)
                        }
                        items.add(SongItem("","","","",null,null))
                        binding.recycler.adapter?.notifyItemInserted(items.lastIndex)
                    }
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
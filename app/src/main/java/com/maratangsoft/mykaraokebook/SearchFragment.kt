package com.maratangsoft.mykaraokebook

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.maratangsoft.mykaraokebook.databinding.FragmentSearchBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private var items:MutableList<Item> = mutableListOf()
    private lateinit var result: MutableList<Item>

    private var query = "song"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnQuery.setOnClickListener { openPopup() }
        binding.recycler.adapter = SearchAdapter(requireActivity(), items)
        binding.et.setOnEditorActionListener { v, actionId, event ->
            when (actionId){
                EditorInfo.IME_ACTION_SEARCH -> {
                    if (binding.et.text.isNotEmpty() && binding.et.text.toString() != " "){
                        initData()
                        true
                    } else {
                        Toast.makeText(requireActivity(), "검색어를 입력해 주세요.", Toast.LENGTH_SHORT).show()
                        false
                    }
                }
                else -> false
            }
        }
    }

    private fun openPopup(){
        val popup = PopupMenu(requireActivity(), binding.btnQuery)
        popup.menuInflater.inflate(R.menu.popup_search, popup.menu)
        popup.setOnMenuItemClickListener {
            when (it.itemId){
                R.id.title -> {
                    query = "song"
                    binding.btnQuery.text = "▼ 제목"
                    true
                }

                R.id.singer -> {
                    query = "singer"
                    binding.btnQuery.text = "▼ 가수"
                    true
                }

                R.id.no -> {
                    query = "no"
                    binding.btnQuery.text = "▼ 번호"
                    true
                }

                else -> false
            }
        }
        popup.show()
    }

    private fun initData(){
        val word = binding.et.text.toString().trim()
        RetrofitHelper.getInstance().create(RetrofitService::class.java)
            .loadSearchData(query, word, brand).enqueue(object: Callback<MutableList<Item>>{
                override fun onResponse(call: Call<MutableList<Item>>, response: Response<MutableList<Item>>) {
                    if (response.body().isNullOrEmpty()) {
                        Toast.makeText(requireActivity(), "검색 결과가 없습니다!", Toast.LENGTH_SHORT).show()
                    } else {
                        response.body()?.let { result = it }
                        //검색결과가 30개 이상이면 30개까지만 리사이클러에 담기
                        if (result.size >= rowCount)
                            for (i in 0..29) items.add(result[i])
                        else
                            items = result

                        binding.recycler.adapter?.notifyDataSetChanged()
                    }
                }

                override fun onFailure(call: Call<MutableList<Item>>, t: Throwable) {
                    AlertDialog.Builder(requireActivity()).setMessage(t.message).show()
                }
            })
    }
}
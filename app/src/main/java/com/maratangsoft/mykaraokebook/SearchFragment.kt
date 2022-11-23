package com.maratangsoft.mykaraokebook

import android.content.Intent
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
import kotlin.math.min

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private var items:MutableList<SongItem> = mutableListOf()
    private lateinit var result: MutableList<SongItem>
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
        binding.et.setOnEditorActionListener { v, actionId, event -> editorAction(actionId) }
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
        RetrofitHelper.getInstance("https://api.manana.kr/").create(RetrofitService::class.java)
            .loadSearchData(query, word, brand).enqueue(object: Callback<MutableList<SongItem>>{

                override fun onResponse(call: Call<MutableList<SongItem>>, response: Response<MutableList<SongItem>>) {
                    if (response.body().isNullOrEmpty()) {
                        Toast.makeText(requireActivity(), "검색 결과가 없습니다!", Toast.LENGTH_SHORT).show()
                    } else {
                        response.body()?.let { result = it }
                        for (i in 0 until min(rowCount, result.size)) {
                            items.add(result[i])
                        }
                        binding.recycler.adapter?.notifyDataSetChanged()
                    }
                }

                override fun onFailure(call: Call<MutableList<SongItem>>, t: Throwable) {
                    AlertDialog.Builder(requireActivity()).setMessage(t.message).show()
                }
            })
    }
}
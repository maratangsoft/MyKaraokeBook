package com.maratangsoft.mykaraokebook

import android.content.Intent
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.*
import android.widget.PopupMenu
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import com.maratangsoft.mykaraokebook.databinding.FragmentFavoriteBinding

class FavoriteFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteBinding
    private var items:MutableList<SongItem> = mutableListOf()
    private lateinit var db:SQLiteDB
    private var sort = SORT_TITLE

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSort.setOnClickListener { openPopup() }
        binding.btnSetting.setOnClickListener { startActivity(Intent(requireActivity(), SettingActivity::class.java)) }
        binding.recycler.adapter = FavoriteAdapter(requireActivity(), this, items)

        db = SQLiteDB(requireActivity())
        db.loadDB(items, binding.recycler.adapter, sort)
    }

///////////////////////////////////////////////////////////////////////////////////////////////////

    private fun openPopup(){
        val popup = PopupMenu(requireActivity(), binding.btnSort)
        popup.menuInflater.inflate(R.menu.popup_favorite, popup.menu)
        popup.setOnMenuItemClickListener {
            sort = when (it.itemId){
                R.id.title -> SORT_TITLE
                R.id.singer -> SORT_SINGER
                else -> SORT_NO
            }
            db.loadDB(items, binding.recycler.adapter, sort)
            true
        }
        popup.show()
    }

    fun clickItem(position:Int){
        val item = items[position]
        val dialog = AlertDialog.Builder(requireActivity()).setView(R.layout.dialog_favorite).create()
        dialog.window?.attributes?.height = 160
        dialog.show()

        val tvTitle = dialog.findViewById<AppCompatTextView>(R.id.tv_title)
        val tvBrand = dialog.findViewById<AppCompatTextView>(R.id.tv_brand)
        val etMemo = dialog.findViewById<AppCompatEditText>(R.id.et_memo)
        val btnSaveMemo = dialog.findViewById<AppCompatTextView>(R.id.btn_save_memo)
        val btnDeleteFav = dialog.findViewById<AppCompatTextView>(R.id.btn_delete_fav)

        tvTitle?.text = item.title
        tvBrand?.text = item.brand
        etMemo?.text = SpannableStringBuilder(item.memo.toString())
        btnSaveMemo?.setOnClickListener {
            item.memo = etMemo?.text.toString()
            db.updateMemo(item.no, item.memo)
            dialog.dismiss()
        }
        btnDeleteFav?.setOnClickListener {
            db.deleteDB(item.no)
            dialog.dismiss()
        }
    }
}
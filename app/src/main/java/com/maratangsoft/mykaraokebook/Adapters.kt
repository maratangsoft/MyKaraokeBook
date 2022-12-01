package com.maratangsoft.mykaraokebook

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.maratangsoft.mykaraokebook.databinding.ItemFavoriteBinding
import com.maratangsoft.mykaraokebook.databinding.ItemLoadingBinding
import com.maratangsoft.mykaraokebook.databinding.ItemSearchBinding
class FavoriteAdapter(val context: Context, val hostFragment: FavoriteFragment, var items:MutableList<SongItem>) : Adapter<FavoriteAdapter.FavoriteViewHolder>() {
    inner class FavoriteViewHolder(val binding:ItemFavoriteBinding) : ViewHolder(binding.root){
        init {
            binding.root.setOnClickListener { hostFragment.clickItem(adapterPosition) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_favorite, parent, false)
        return FavoriteViewHolder(ItemFavoriteBinding.bind(itemView))
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.binding.tvNo.text = items[position].no
        holder.binding.tvTitle.text = items[position].title
        holder.binding.tvSinger.text = items[position].singer
        holder.binding.tvMemo.text = items[position].memo
    }

    override fun getItemCount() = items.size
}
class SearchAdapter(val context: Context, var items:MutableList<SongItem>) : Adapter<ViewHolder>() {

    private val db = SQLiteDB(context)
    private val ITEM = 1
    private val LOADING = 0

    inner class SearchViewHolder(val binding:ItemSearchBinding) : ViewHolder(binding.root){
        init {
            binding.root.setOnClickListener { clickItem(binding, adapterPosition) }
        }
    }
    inner class LoadingViewHolder(private val binding:ItemLoadingBinding) : ViewHolder(binding.root)

    override fun getItemViewType(position: Int): Int {
        return if (items[position].no != "") ITEM else LOADING
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewHolder:ViewHolder = if (viewType == ITEM){
            val itemView = LayoutInflater.from(context).inflate(R.layout.item_search, parent, false)
            SearchViewHolder(ItemSearchBinding.bind(itemView))
        }else{
            val itemView = LayoutInflater.from(context).inflate(R.layout.item_loading, parent, false)
            LoadingViewHolder(ItemLoadingBinding.bind(itemView))
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is SearchViewHolder){
            holder.binding.tvNo.text = items[position].no
            holder.binding.tvTitle.text = items[position].title
            holder.binding.tvSinger.text = items[position].singer
            if (db.isFavorited(items[position].no)) holder.binding.btnFavorite.setImageResource(R.drawable.ic_favorite_filled)
            else holder.binding.btnFavorite.setImageResource(R.drawable.ic_favorite_border)
        }
    }

    override fun getItemCount() = items.size

    fun clickItem(binding:ItemSearchBinding, position:Int){
        val item = items[position]

        //DB에서 중복검사해서 없으면 삽입, 있으면 삭제
        if (!db.isFavorited(item.no)){
            db.insertDB(item.brand, item.no, item.title, item.singer, item.release)
            binding.btnFavorite.setImageResource(R.drawable.ic_favorite_filled)
        }else{
            db.deleteDB(item.no)
            binding.btnFavorite.setImageResource(R.drawable.ic_favorite_border)
        }
    }
}


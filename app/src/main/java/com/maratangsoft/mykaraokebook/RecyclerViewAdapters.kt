package com.maratangsoft.mykaraokebook

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.maratangsoft.mykaraokebook.databinding.ItemFavoriteBinding
import com.maratangsoft.mykaraokebook.databinding.ItemSearchBinding

class FavoriteAdapter(val context: Context, var items:MutableList<Item>) : Adapter<FavoriteAdapter.FavoriteViewHolder>() {
    inner class FavoriteViewHolder(val binding:ItemFavoriteBinding) : ViewHolder(binding.root){
        init {
            binding.root.setOnClickListener { clickItem(it) }
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

    fun clickItem(view:View){

    }
}

class SearchAdapter(val context: Context, var items:MutableList<Item>) : Adapter<SearchAdapter.SearchViewHolder>() {
    inner class SearchViewHolder(val binding:ItemSearchBinding) : ViewHolder(binding.root){
        init {
            binding.btnFavorite.setOnClickListener { clickItem(it) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_search, parent, false)
        return SearchViewHolder(ItemSearchBinding.bind(itemView))
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.binding.tvNo.text = items[position].no
        holder.binding.tvTitle.text = items[position].title
        holder.binding.tvSinger.text = items[position].singer
    }

    override fun getItemCount() = items.size

    fun clickItem(view:View){

    }
}


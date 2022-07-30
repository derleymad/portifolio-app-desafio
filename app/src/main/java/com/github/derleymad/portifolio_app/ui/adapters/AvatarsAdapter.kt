package com.github.derleymad.portifolio_app.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.github.derleymad.portifolio_app.R
import com.github.derleymad.portifolio_app.model.SearchBio
import com.squareup.picasso.Picasso

class AvatarsAdapter(
    private val list: MutableList<SearchBio>
    ) : RecyclerView.Adapter<AvatarsAdapter.AvatarsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvatarsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.avatar_item,parent,false)
        return AvatarsViewHolder(view)
    }

    override fun onBindViewHolder(holder: AvatarsViewHolder, position: Int) {
        val itemCurent = list[position]
        holder.bind(itemCurent)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class AvatarsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(currentItem:SearchBio){
            val avatar = itemView.findViewById<ImageView>(R.id.img_avatar_repo)
            Picasso.get()
                .load(currentItem.avatar_url)
                .error(R.drawable.avatar_placeholder)
                .placeholder(R.drawable.placeholder_repo)
                .into(avatar)
        }
    }

}
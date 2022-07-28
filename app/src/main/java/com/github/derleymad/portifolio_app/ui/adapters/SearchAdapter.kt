package com.github.derleymad.portifolio_app.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.derleymad.portifolio_app.R
import com.github.derleymad.portifolio_app.model.SearchBio
import com.google.android.material.card.MaterialCardView
import com.squareup.picasso.Picasso

class SearchAdapter(
    private val search: List<SearchBio>,
    private var onItemClickListener : (String) -> Unit
) : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_item, parent, false)
        return SearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val currentItem = search[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return search.size
    }

    inner class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(currentItem: SearchBio) {
            val imageAvatar = itemView.findViewById<ImageView>(R.id.img_avatar_repo)
            val cardRepoItem = itemView.findViewById<MaterialCardView>(R.id.cardview_repo_item)
            val name = itemView.findViewById<TextView>(R.id.tv_name)

            name.text = currentItem.login

            Picasso.get()
                .load(currentItem.avatar_url)
                .placeholder(R.drawable.avatar_placeholder)
                .error(R.drawable.avatar_placeholder)
                .into(imageAvatar)

            cardRepoItem.setOnClickListener{
                onItemClickListener?.invoke(currentItem.login)
            }
        }
    }
}
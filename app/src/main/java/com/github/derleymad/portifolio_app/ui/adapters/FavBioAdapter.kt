package com.github.derleymad.portifolio_app.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.derleymad.portifolio_app.R
import com.github.derleymad.portifolio_app.model.BioFav
import com.google.android.material.card.MaterialCardView
import com.squareup.picasso.Picasso
import org.w3c.dom.Text

class FavBioAdapter(
    private val repos:List<BioFav>,
    private var onRepoClickListener : (String) -> Unit

): RecyclerView.Adapter<FavBioAdapter.RepoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavBioAdapter.RepoViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.repo_item_favoritos,parent,false)
        return RepoViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavBioAdapter.RepoViewHolder, position: Int) {
        val itemCurrent = repos[position]
        holder.bind(itemCurrent)
    }

    override fun getItemCount(): Int {
        return repos.size
    }

    inner class RepoViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun bind (itemCurrent:BioFav){
            val container = itemView.findViewById<MaterialCardView>(R.id.container)
            val tvTitleRepo = itemView.findViewById<TextView>(R.id.tv_title)
            val avatarImage = itemView.findViewById<ImageView>(R.id.img_avatar_repo)
            val repoNumbers = itemView.findViewById<TextView>(R.id.tv_repo)

            repoNumbers.text = itemView.context.getString(R.string.repo_numbers,itemCurrent.publicRepos.toString())
            tvTitleRepo.text = itemCurrent.login
            Picasso.get()
                .load(itemCurrent.avatarUrl)
                .placeholder(R.drawable.placeholder_repo)
                .error(R.drawable.avatar_placeholder)
                .into(avatarImage)

            container.setOnClickListener {
                onRepoClickListener?.invoke(itemCurrent.login)
            }
        }
    }

}
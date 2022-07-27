package com.github.derleymad.portifolio_app.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.derleymad.portifolio_app.R
import com.github.derleymad.portifolio_app.model.Repos
import com.google.android.material.card.MaterialCardView
import com.squareup.picasso.Picasso

class RepoAdapter(
    private val repos:List<Repos>,
    private var onRepoClickListener : ((Repos) -> Unit)? = null

    ): RecyclerView.Adapter<RepoAdapter.RepoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoAdapter.RepoViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.repo_item,parent,false)
        return RepoViewHolder(view)
    }

    override fun onBindViewHolder(holder: RepoAdapter.RepoViewHolder, position: Int) {
        val itemCurrent = repos[position]
        holder.bind(itemCurrent)
    }

    override fun getItemCount(): Int {
        return repos.size
    }

    inner class RepoViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun bind (itemCurrent:Repos){

            val link = "https://ui-avatars.com/api/?background=random&name=${itemCurrent.name}&size=100"
            val tvTitleRepo = itemView.findViewById<TextView>(R.id.tv_title_repo)
            val carViewContainer = itemView.findViewById<MaterialCardView>(R.id.cardview_repo_item)
            val tvDescRepo = itemView.findViewById<TextView>(R.id.tv_desc_repo)
            val tvLaguageRepo = itemView.findViewById<TextView>(R.id.tv_language_repo)
            val avatarImage = itemView.findViewById<ImageView>(R.id.img_avatar_repo)

            tvTitleRepo.text = itemCurrent.name
            tvLaguageRepo.text = itemCurrent.language
            if(itemCurrent.description == "null"){
                tvDescRepo.text = itemView.context.getString(R.string.no_description)
            }else{
                tvDescRepo.text = itemCurrent.description
            }

            Picasso.get()
                .load(link)
                .placeholder(R.drawable.placeholder_repo)
                .error(R.drawable.avatar_placeholder)
                .into(avatarImage)

            carViewContainer.setOnClickListener{
                onRepoClickListener?.invoke(itemCurrent)
            }
        }
    }
}
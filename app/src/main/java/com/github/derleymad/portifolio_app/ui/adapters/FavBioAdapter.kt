package com.github.derleymad.portifolio_app.ui.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.github.derleymad.portifolio_app.R
import com.github.derleymad.portifolio_app.model.BioFav
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.squareup.picasso.Picasso
import org.w3c.dom.Text

class FavBioAdapter(
    private val repos:List<BioFav>,
    private var onBioClickListener : (String) -> Unit,
    private var onCloseClickListener : (Int,Int) -> Unit

): RecyclerView.Adapter<FavBioAdapter.RepoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavBioAdapter.RepoViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.repo_item_favoritos,parent,false)
        return RepoViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavBioAdapter.RepoViewHolder, position: Int) {
        val itemCurrent = repos[position]
        holder.bind(itemCurrent,position)
    }

    override fun getItemCount(): Int {
        return repos.size
    }

    inner class RepoViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        fun bind (itemCurrent:BioFav,position: Int){

            val container = itemView.findViewById<MaterialCardView>(R.id.container)
            val btnSee = itemView.findViewById<MaterialButton>(R.id.btnSee)
            val btnClose= itemView.findViewById<ImageButton>(R.id.btnClose)
            val tvTitleRepo = itemView.findViewById<TextView>(R.id.tv_title)
            val avatarImage = itemView.findViewById<ImageView>(R.id.img_avatar_repo)
            val repoNumbers = itemView.findViewById<TextView>(R.id.tv_repo)

            val mint = ResourcesCompat.getColor(itemView.resources,R.color.mint_color,null)
            val lpurple = ResourcesCompat.getColor(itemView.resources,R.color.purple_light_color,null)
            val sand = ResourcesCompat.getColor(itemView.resources,R.color.sand_color,null)

            container.setCardBackgroundColor(listOf(mint,lpurple,sand).random())

            repoNumbers.text = itemView.context.getString(R.string.repo_numbers,itemCurrent.publicRepos.toString())
            tvTitleRepo.text = itemCurrent.login

            Picasso.get()
                .load(itemCurrent.avatarUrl)
                .placeholder(R.drawable.placeholder_repo)
                .error(R.drawable.avatar_placeholder)
                .into(avatarImage)

            btnSee.setOnClickListener {
                onBioClickListener?.invoke(itemCurrent.login)
            }
            btnClose.setOnClickListener {
                onCloseClickListener?.invoke(itemCurrent.id,position)
            }
        }
    }

}
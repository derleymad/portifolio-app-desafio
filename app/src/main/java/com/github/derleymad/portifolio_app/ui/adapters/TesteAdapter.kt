package com.github.derleymad.portifolio_app.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.derleymad.portifolio_app.R
import com.github.derleymad.portifolio_app.model.Bio
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup

class TesteAdapter(
    private val list : List<Bio>,
    private val function : ((Int) -> Any)? = null
) : RecyclerView.Adapter<TesteAdapter.TesteViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TesteViewHolder {
        val view =  LayoutInflater.from(parent.context).inflate(R.layout.avatar_item,parent,false)
        return TesteViewHolder(view)
    }

    override fun onBindViewHolder(holder: TesteViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class TesteViewHolder(view:View) : RecyclerView.ViewHolder(view){
        fun bind(int:Int){
            itemView.findViewById<MaterialButton>(R.id.btnClose)


        }
    }
}
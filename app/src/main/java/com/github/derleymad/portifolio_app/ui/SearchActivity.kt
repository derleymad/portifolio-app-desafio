package com.github.derleymad.portifolio_app.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.derleymad.portifolio_app.databinding.ActivitySearchBinding
import com.github.derleymad.portifolio_app.model.Repos
import com.github.derleymad.portifolio_app.ui.adapters.RepoAdapter
import com.github.derleymad.portifolio_app.utils.RepoRequest

class SearchActivity : AppCompatActivity(){
    private lateinit var binding: ActivitySearchBinding
    private lateinit var adapter: RepoAdapter
    private var repos = mutableListOf<Repos>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.searchBtn.setOnClickListener {
            val intent = Intent(this@SearchActivity,MainActivity::class.java)
            intent.putExtra("name",binding.editSearchEdit.text.toString())
            startActivity(intent)
        }

        for (i in 0..5){
            repos.add(Repos(i,"Projeto $i","Projeto/$i","urlfalsa.com","kotlin","null"))
        }

        adapter = RepoAdapter(repos)
        binding.rvFavoritos.adapter = adapter
        binding.rvFavoritos.layoutManager = LinearLayoutManager(this@SearchActivity)

    }

//    override fun onPreExecute() {
//        Log.i("Teste","Pssou no preExecute")
//    }
//
//    override fun onResult(repos: List<Repos>) {
//        this.repos.clear()
//        this.repos.addAll(repos)
//        adapter.notifyDataSetChanged()
//    }
//
//    override fun onFailure(message: String) {
//        Toast.makeText(this@SearchActivity,"Falha ao buscar repositório $message",Toast.LENGTH_SHORT)
//    }
}
package com.github.derleymad.portifolio_app.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.derleymad.portifolio_app.App
import com.github.derleymad.portifolio_app.databinding.ActivitySearchBinding
import com.github.derleymad.portifolio_app.model.FavRepos
import com.github.derleymad.portifolio_app.ui.adapters.FavRepoAdapter

class SearchActivity : AppCompatActivity(){
    private lateinit var binding: ActivitySearchBinding
    private lateinit var adapter: FavRepoAdapter
    private var repos = mutableListOf<FavRepos>()
    private lateinit var linearlayout : LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.searchBtn.setOnClickListener {
            val intent = Intent(this@SearchActivity,MainActivity::class.java)
            intent.putExtra("name",binding.editSearchEdit.text.toString())
            startActivity(intent)
        }

        adapter = FavRepoAdapter(repos)
        binding.rvFavoritos.adapter = adapter
        linearlayout = LinearLayoutManager(this@SearchActivity)
        linearlayout.stackFromEnd = true
        linearlayout.reverseLayout = true
        binding.rvFavoritos.layoutManager = linearlayout
    }

    override fun onResume() {
        Thread{
            val app = application as App
            val dao = app.db.favDao()
            val response = dao.getRegisterByType("repo")
            Log.i("teste",response.toString())
            runOnUiThread {
                //atualizando adapter
                repos.clear()
                repos.addAll(response)
                adapter.notifyDataSetChanged()
            }
        }.start()
        adapter.notifyDataSetChanged()
        super.onResume()
    }
}
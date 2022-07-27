package com.github.derleymad.portifolio_app.ui

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.GridView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.derleymad.portifolio_app.App
import com.github.derleymad.portifolio_app.R
import com.github.derleymad.portifolio_app.databinding.ActivitySearchBinding
import com.github.derleymad.portifolio_app.model.FavRepos
import com.github.derleymad.portifolio_app.model.SearchBio
import com.github.derleymad.portifolio_app.ui.adapters.FavRepoAdapter
import com.github.derleymad.portifolio_app.ui.adapters.SearchAdapter
import com.github.derleymad.portifolio_app.utils.BioSearchRequest

const val MAX_PER_SEARCH = 10

class SearchActivity : AppCompatActivity(), BioSearchRequest.Callback {
    private lateinit var binding: ActivitySearchBinding
    private lateinit var adapter: FavRepoAdapter
    private lateinit var adapterSearch: SearchAdapter

    private var repos = mutableListOf<FavRepos>()
    private var search = mutableListOf<SearchBio>()
    private lateinit var linearlayout: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo

        if (networkInfo != null && (networkInfo.isAvailable || networkInfo.isConnected)) {
            binding.searchBtn.setOnClickListener {
                BioSearchRequest(this@SearchActivity)
                    .execute("https://api.github.com/search/users?q=${binding.editSearchEdit.text.toString()}&$MAX_PER_SEARCH=5")
            }
        } else {
            binding.searchBtn.setOnClickListener {
                Toast.makeText(this@SearchActivity, "Network Not Available", Toast.LENGTH_LONG).show()
            }
            binding.imgSearch.setImageResource(R.drawable.ic_undraw_page_not_found)
        }

        //SEARCH ADAPTER AND RECYCLERVIEW
        adapterSearch = SearchAdapter(search)
        binding.rvSearch.adapter = adapterSearch
        binding.rvSearch.layoutManager = LinearLayoutManager(this@SearchActivity)


        //FAVORITOS ADAPTER AND RECYCLER VIEW
        adapter = FavRepoAdapter(repos)
        binding.rvFavoritos.adapter = adapter
        linearlayout = LinearLayoutManager(this@SearchActivity)
        linearlayout.stackFromEnd = true
        linearlayout.reverseLayout = true
        binding.rvFavoritos.layoutManager = linearlayout
    }

    override fun onResume() {
        Thread {
            val app = application as App
            val dao = app.db.favDao()
            val response = dao.getRegisterByType("repo")
            Log.i("teste", response.toString())
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

    override fun onPreExecute() {
        binding.imgSearch.visibility = View.INVISIBLE
    }

    override fun onResult(search: List<SearchBio>) {
        this.search.clear()
        this.search.addAll(search)
        binding.rvSearch.visibility = View.VISIBLE
        adapterSearch.notifyDataSetChanged()
    }

    override fun onFailure(message: String) {
        binding.imgSearch.visibility = View.VISIBLE
        binding.rvSearch.visibility = View.GONE
        Toast.makeText(this@SearchActivity,"Não foi possivel encontrar o perfil",Toast.LENGTH_SHORT).show()
    }
}
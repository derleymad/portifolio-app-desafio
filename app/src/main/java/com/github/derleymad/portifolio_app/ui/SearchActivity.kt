package com.github.derleymad.portifolio_app.ui

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.derleymad.portifolio_app.App
import com.github.derleymad.portifolio_app.R
import com.github.derleymad.portifolio_app.databinding.ActivitySearchBinding
import com.github.derleymad.portifolio_app.model.BioFav
import com.github.derleymad.portifolio_app.model.SearchBio
import com.github.derleymad.portifolio_app.ui.adapters.AvatarsAdapter
import com.github.derleymad.portifolio_app.ui.adapters.FavBioAdapter
import com.github.derleymad.portifolio_app.ui.adapters.SearchAdapter
import com.github.derleymad.portifolio_app.utils.BioSearchBestRequest
import com.github.derleymad.portifolio_app.utils.BioSearchRequest
import com.squareup.picasso.Picasso

class SearchActivity : AppCompatActivity(), BioSearchRequest.Callback, BioSearchBestRequest.Callback {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var adapter: FavBioAdapter
    private lateinit var adapterSearch: SearchAdapter
    private lateinit var adapterAvatars: AvatarsAdapter

    private var bio = mutableListOf<BioFav>()
    private var search = mutableListOf<SearchBio>()
    private var avatars = mutableListOf<SearchBio>()

    private lateinit var linearlayout: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCloseRvSearch.setOnClickListener {
            binding.containerRvSearch.visibility = View.GONE
            binding.linearContainerBest.visibility = View.VISIBLE
        }

        if (checkNetwork()) {
            binding.btnSeee.setOnClickListener {
                //TODO IR PARA REPO DO MAIS POPULAR
            }

            BioSearchBestRequest(this@SearchActivity).execute("https://api.github.com/search/users?q=a&sort=followers")
            binding.searchBtn.setOnClickListener {
                if (!validate()) {
                    Toast.makeText(this, R.string.fields_messages, Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                hideKeyBoard()
                BioSearchRequest(this@SearchActivity)
                    .execute("https://api.github.com/search/users?q=${binding.editSearchEdit.text.toString()}&per_page=10")
            }
        } else {
            binding.linearContainerBest.visibility = View.INVISIBLE
            binding.linearContainerConection.visibility = View.VISIBLE
            binding.searchBtn.setOnClickListener {
                Toast.makeText(this@SearchActivity, getString(R.string.no_conection), Toast.LENGTH_LONG).show()
            }
        }

        //SEARCH ADAPTER AND RECYCLERVIEW
        adapterSearch = SearchAdapter(search){
            val intent = Intent(this@SearchActivity,MainActivity::class.java)
            intent.putExtra("name",it)
            startActivity(intent)
        }
        binding.rvSearch.adapter = adapterSearch
        binding.rvSearch.layoutManager = LinearLayoutManager(this@SearchActivity)


        //CARVIEW BEST PROFILES RECYCLERVIEW
        adapterAvatars = AvatarsAdapter(avatars)
        binding.rvAvatars.adapter =  adapterAvatars
        binding.rvAvatars.layoutManager = LinearLayoutManager(this@SearchActivity,LinearLayoutManager.HORIZONTAL,false)

        //FAVORITOS ADAPTER AND RECYCLER VIE
        adapter = FavBioAdapter(bio,
            {id ->
                val intent = Intent(this@SearchActivity,MainActivity::class.java)
                intent.putExtra("name",id)
                startActivity(intent)
        },{id,position -> openDialogAndRemoveIntoDB(id,position)}
        )

        binding.rvFavoritos.adapter = adapter
        linearlayout = LinearLayoutManager(this@SearchActivity,LinearLayoutManager.HORIZONTAL,false)
        binding.rvFavoritos.layoutManager = linearlayout

    }

    private fun searchIntoDBandUpdateAdapter(){
        Thread {
            val app = application as App
            val dao = app.db.favDao()
            val response = dao.getRegisterByType("bio")
                Log.i("teste", response.toString())
                runOnUiThread {
                    //atualizando adapter
                    bio.clear()
                    bio.addAll(response)
                    adapter.notifyDataSetChanged()
                }
        }.start()
    }

    private fun openDialogAndRemoveIntoDB(id:Int,position:Int) {
        //TODO Remover bug de deteletar todos is favoritados
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.remove_db))
            .setMessage(getString(R.string.remove_request_description))
            .setPositiveButton(getString(R.string.remove)) { _, _ ->

                val app = (application as App)
                val dao = app.db.favDao()

                Thread {
                    val response = dao.getRegisterByid(id)
                    try {
                        dao.delete(response)
                        bio.removeAt(position)

                    } finally {
                        runOnUiThread {

                            //TODO Fazer adapter.notifyItemRemoved() para apenas o item removido
                            searchIntoDBandUpdateAdapter()
                            Toast.makeText(
                                applicationContext,
                                getString(R.string.removed_sucess),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }.start()
            }
            .setNegativeButton(android.R.string.cancel) { _, _ ->
            }
            .create()
            .show()
    }

    private fun checkNetwork() : Boolean{
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && (networkInfo.isAvailable || networkInfo.isConnected)
    }

    private fun validate(): Boolean {
        return (binding.editSearchEdit.text.toString().isNotEmpty()
                && binding.editSearchEdit.text.toString().isNotEmpty())
    }

    private fun hideKeyBoard(){
        val service = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        service.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

    override fun onStart() {
        Log.i("testing","onStart")
        super.onStart()
    }

    override fun onResume() {
        Log.i("testing","onResume")
        searchIntoDBandUpdateAdapter()
        super.onResume()
    }

    override fun onPreExecute() {
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun onResult(search: List<SearchBio>) {
        this.search.clear()
        this.search.addAll(search)
        binding.progressBar.visibility = View.INVISIBLE
        binding.linearContainerBest.visibility = View.INVISIBLE
        binding.containerRvSearch.visibility = View.VISIBLE
        adapterSearch.notifyDataSetChanged()
    }

    override fun onFailure(message: String) {
        binding.progressBar.visibility = View.INVISIBLE
        binding.rvSearch.visibility = View.GONE
        Toast.makeText(this@SearchActivity,getString(R.string.bio_not_found),Toast.LENGTH_SHORT).show()
    }

    override fun onPreExecuteBest() {
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun onResultBest(best: List<SearchBio>) {
        Log.i("result",best.toString())
        this.avatars.clear()
        this.avatars.addAll(best)
        adapterAvatars.notifyDataSetChanged()
        binding.nameBest.text = best[0].login
        Picasso.get()
            .load(best[0].avatar_url)
            .error(R.drawable.avatar_placeholder)
            .placeholder(R.drawable.avatar_placeholder)
            .into(binding.imgAvatarBest)
        binding.progressBar.visibility = View.GONE
    }

    override fun onFailureBest(message: String) {
        Toast.makeText(this@SearchActivity,getString(R.string.bio_not_found),Toast.LENGTH_SHORT).show()
    }
}
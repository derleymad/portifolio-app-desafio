package com.github.derleymad.portifolio_app.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.derleymad.portifolio_app.R
import com.github.derleymad.portifolio_app.databinding.ActivityMainBinding
import com.github.derleymad.portifolio_app.model.Bio
import com.github.derleymad.portifolio_app.model.Repos
import com.github.derleymad.portifolio_app.ui.adapters.RepoAdapter
import com.github.derleymad.portifolio_app.utils.BioRequest
import com.github.derleymad.portifolio_app.utils.RepoRequest
import com.squareup.picasso.Picasso
import java.util.*

class MainActivity : AppCompatActivity(), BioRequest.Callback, RepoRequest.Callback {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter : RepoAdapter
    private var repos = mutableListOf<Repos>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mainContainer.visibility = View.GONE

        adapter = RepoAdapter(repos){
            val intent = Intent(this@MainActivity,RepoActivity::class.java)
            intent.putExtra("full_name",it)
            startActivity(intent)
        }
        binding.rvRepos.adapter = adapter
        binding.rvRepos.layoutManager = LinearLayoutManager(this@MainActivity)

        val name = intent?.extras?.getString("name","derleymad") ?: throw IllegalStateException("Não devia está aqui!")

        BioRequest(this@MainActivity).execute("https://api.github.com/users/$name")
        RepoRequest(this@MainActivity).execute("https://api.github.com/users/$name/repos")

        val toolBar: Toolbar = findViewById(R.id.bio_toolbar)
        setSupportActionBar(toolBar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = null


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPreExecute() {
        binding.progressBarBio.visibility = View.VISIBLE
    }

    //REQUEST FROM BIO

    override fun onResult(bio: Bio) {
        binding.tvName.text = bio.login.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
        binding.tvDesc.text = bio.bio
        binding.tvStatus.text = getString(R.string.status,bio.followers.toString(),bio.following.toString())
        binding.tvLocation.text = bio.location
        binding.tvCompany.text = bio.company

        if(bio.company == "null"){binding.tvCompany.visibility = View.GONE}
        if(bio.location == "null"){binding.tvLocation.visibility = View.GONE}
        if(bio.bio == "null"){binding.tvDesc.text = "Perfil sem descrição"}
        if(bio.public_repos == 0){
            binding.imgEmptyRepo.visibility = View.VISIBLE
            binding.tvEmptyRepo.visibility = View.VISIBLE
        }

        Picasso.get()
            .load(bio.avatar_url)
            .placeholder(R.drawable.avatar_placeholder)
            .error(R.drawable.avatar_placeholder)
            .resize(200,200)
            .into(binding.imgAvatar)

        binding.progressBarBio.visibility = View.GONE
        binding.mainContainer.visibility = View.VISIBLE

    }

    override fun onFailure(message: String) {
        Toast.makeText(this@MainActivity,"Não foi possível encontrar o perfil!",Toast.LENGTH_SHORT).show()
        binding.progressBarBio.visibility = View.GONE
        finish()
    }


    //RESQUEST FROM REPO

    override fun onPreExecuteRepo() {
    }

    override fun onResultRepo(repos: List<Repos>) {
        this.repos.clear()
        this.repos.addAll(repos)
        adapter.notifyDataSetChanged()
    }

    override fun onFailureRepo(message: String) {
        binding.imgNotFound.visibility = View.VISIBLE
    }
}
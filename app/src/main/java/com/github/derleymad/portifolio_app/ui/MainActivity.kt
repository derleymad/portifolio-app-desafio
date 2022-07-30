package com.github.derleymad.portifolio_app.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.derleymad.portifolio_app.App
import com.github.derleymad.portifolio_app.R
import com.github.derleymad.portifolio_app.databinding.ActivityMainBinding
import com.github.derleymad.portifolio_app.model.Bio
import com.github.derleymad.portifolio_app.model.BioFav
import com.github.derleymad.portifolio_app.model.Repos
import com.github.derleymad.portifolio_app.ui.adapters.RepoAdapter
import com.github.derleymad.portifolio_app.utils.BioRequest
import com.github.derleymad.portifolio_app.utils.RepoRequest
import com.squareup.picasso.Picasso
import java.util.*

class MainActivity : AppCompatActivity(), BioRequest.Callback, RepoRequest.Callback {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: RepoAdapter
    private var repos = mutableListOf<Repos>()
    private var bioFav = mutableListOf<BioFav>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mainContainer.visibility = View.GONE

        adapter = RepoAdapter(repos) {
            val intent = Intent(this@MainActivity, RepoActivity::class.java)
            intent.putExtra("full_name", it.fullName)
            startActivity(intent)
        }

        binding.rvRepos.adapter = adapter
        binding.rvRepos.layoutManager = LinearLayoutManager(this@MainActivity)

        val name = intent?.extras?.getString("name", "derleymad")
            ?: throw IllegalStateException("Não devia está aqui!")

        BioRequest(this@MainActivity).execute("https://api.github.com/users/$name")
        RepoRequest(this@MainActivity).execute("https://api.github.com/users/$name/repos")

        val toolBar: Toolbar = findViewById(R.id.bio_toolbar)
        setSupportActionBar(toolBar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = null
    }

    private fun openDialogAndSaveIntoDB() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.save_request))
            .setMessage(getString(R.string.save_request_description))
            .setPositiveButton(getString(R.string.save)) { _, _ ->
                val app = (application as App)
                val dao = app.db.favDao()
                Thread {
                    try {
                        dao.insert(bioFav[0])
                    } finally {
                        runOnUiThread {
                            Toast.makeText(
                                applicationContext,
                                getString(R.string.save_sucess),
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        if(item.itemId == R.id.menu_favoritos){
            openDialogAndSaveIntoDB()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPreExecute() {
        binding.progressBarBio.visibility = View.VISIBLE
    }

    //REQUEST FROM BIO
    override fun onResult(bio: Bio) {
        bioFav.clear()
        bioFav.add(BioFav(
            login = bio.login,
            avatarUrl = bio.avatar_url,
            publicRepos = bio.public_repos
        ))
        binding.tvName.text =
            bio.login.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
        binding.tvDesc.text = bio.bio
        binding.tvStatus.text =
            getString(R.string.status, bio.followers.toString(), bio.following.toString())
        binding.tvLocation.text = bio.location
        binding.tvCompany.text = bio.company

        if (bio.company == "null") {
            binding.tvCompany.visibility = View.GONE
        }
        if (bio.location == "null") {
            binding.tvLocation.visibility = View.GONE
        }
        if (bio.bio == "null") {
            binding.tvDesc.text = getString(R.string.no_description)
        }
        if (bio.public_repos == 0) {
            binding.imgEmptyRepo.visibility = View.VISIBLE
            binding.tvEmptyRepo.visibility = View.VISIBLE
        }

        Picasso.get()
            .load(bio.avatar_url)
            .placeholder(R.drawable.avatar_placeholder)
            .error(R.drawable.avatar_placeholder)
            .resize(200, 200)
            .into(binding.imgAvatar)

        binding.progressBarBio.visibility = View.GONE
        binding.mainContainer.visibility = View.VISIBLE

    }

    override fun onFailure(message: String) {
        Toast.makeText(this@MainActivity, getString(R.string.bio_not_found), Toast.LENGTH_SHORT)
            .show()
        binding.progressBarBio.visibility = View.GONE
        finish()
    }

    //RESQUEST FROM REPO
    override fun onPreExecuteRepo() {
        binding.progressBarRepo.visibility = View.VISIBLE
    }

    override fun onResultRepo(repos: List<Repos>) {
        this.repos.clear()
        this.repos.addAll(repos)
        adapter.notifyDataSetChanged()
        binding.progressBarRepo.visibility = View.GONE
    }

    override fun onFailureRepo(message: String) {
        binding.imgNotFound.visibility = View.VISIBLE
        binding.progressBarRepo.visibility = View.GONE
    }
}
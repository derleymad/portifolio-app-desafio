package com.github.derleymad.portifolio_app.ui

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.github.derleymad.portifolio_app.App
import com.github.derleymad.portifolio_app.R
import com.github.derleymad.portifolio_app.databinding.ActivityRepoBinding
import com.github.derleymad.portifolio_app.model.FavRepos

class RepoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRepoBinding
    private lateinit var fullName: String
    private lateinit var avatarLink: String
    private lateinit var language: String
    private lateinit var description: String
    private lateinit var name: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRepoBinding.inflate(layoutInflater)

        setContentView(binding.root)
        val wrongPlace = getString(R.string.wrong_place)
        fullName = intent?.extras?.getString("full_name", "derleymad/portifolio-app-desafio")
            ?: throw IllegalStateException(wrongPlace)
        avatarLink = intent?.extras?.getString("avatar_link", "null")
            ?: throw IllegalStateException()
        language = intent?.extras?.getString("language", "")
            ?: throw IllegalStateException(wrongPlace)
        description = intent?.extras?.getString("description", getString(R.string.no_description))
            ?: throw IllegalStateException(wrongPlace)
        name = intent?.extras?.getString("name", "")
            ?: throw IllegalStateException(wrongPlace)

        binding.wvRepo.loadUrl("https://github.com/$fullName")

        val toolBar: Toolbar = findViewById(R.id.wb_toolbar)
        setSupportActionBar(toolBar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = fullName
    }

    private fun openDialogAndSaveIntoDB() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.save_request))
            .setMessage(getString(R.string.save_request_description))
            .setPositiveButton(getString(R.string.save)) { _, _ ->
                Log.i("fullname = $fullName", fullName)
                val app = (application as App)
                val dao = app.db.favDao()
                Thread {
                    try {
                        dao.insert(
                            FavRepos(
                                type = "repo",
                                name = name,
                                description = description,
                                avatarLink = avatarLink,
                                full_name = fullName,
                                language = language
                            )
                        )
                    } finally {
                        runOnUiThread {
                            Toast.makeText(
                                applicationContext,
                                "Salvo com sucesso",
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
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        if (item.itemId == R.id.menu_favoritos) {
            openDialogAndSaveIntoDB()
        }
        return super.onOptionsItemSelected(item)
    }

}


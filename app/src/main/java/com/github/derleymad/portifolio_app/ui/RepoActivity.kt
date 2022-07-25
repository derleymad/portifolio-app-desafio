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
import com.github.derleymad.portifolio_app.model.Repos

class RepoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRepoBinding
    private lateinit var fullName : String
    private lateinit var avatarLink : String
    private lateinit var language : String
    private lateinit var description : String
    private lateinit var name : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRepoBinding.inflate(layoutInflater)

        setContentView(binding.root)

        fullName = intent?.extras?.getString("full_name", "derleymad/portifolio-app-desafio")
            ?: throw IllegalStateException("Não deveria estar aqui!")
        avatarLink = intent?.extras?.getString("avatar_link", "null")
            ?: throw IllegalStateException("Não deveria estar aqui!")
        language = intent?.extras?.getString("language", "")
            ?: throw IllegalStateException("Não deveria estar aqui!")
        description = intent?.extras?.getString("description", "Sem descrição")
            ?: throw IllegalStateException("Não deveria estar aqui!")
        name = intent?.extras?.getString("name", "")
            ?: throw IllegalStateException("Não deveria estar aqui!")

        binding.wvRepo.loadUrl("https://github.com/$fullName")

        val toolBar: Toolbar = findViewById(R.id.wb_toolbar)
        setSupportActionBar(toolBar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = fullName
    }

    private fun openDialogAndSaveIntoDB(){
        AlertDialog.Builder(this)
            .setTitle("Deseja salvar nos favoritos?")
            .setMessage("teste")
            .setPositiveButton(android.R.string.cancel) { _, which ->
                // aqui vai rodar depois do click
            }
            .setNegativeButton("Salvar"){ _, which ->
                Log.i("fullname = $fullName",fullName)
                val app = (application as App)
                val dao = app.db.favDao()
                Thread{
                    try{
                        dao.insert(FavRepos(
                            type = "repo",
                            name = name,
                            description = description,
                            avatarLink = avatarLink,
                            full_name = fullName,
                            language = language
                        )
                        )
                    }finally {
                        runOnUiThread{
                                Toast.makeText(applicationContext, "Salvo com sucesso", Toast.LENGTH_SHORT).show()
                        }
                    }
                }.start()
            }
            .create()
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            finish()
        }
        if(item.itemId == R.id.menu_favoritos){
            openDialogAndSaveIntoDB()
        }
        return super.onOptionsItemSelected(item)
    }

}


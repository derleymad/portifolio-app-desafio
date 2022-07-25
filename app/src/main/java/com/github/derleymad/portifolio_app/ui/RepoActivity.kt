package com.github.derleymad.portifolio_app.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.github.derleymad.portifolio_app.R
import com.github.derleymad.portifolio_app.databinding.ActivityRepoBinding

class RepoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRepoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRepoBinding.inflate(layoutInflater)

        setContentView(binding.root)
        val fullName = intent?.extras?.getString("full_name", "derleymad/portifolio-app-desafio")
            ?: throw IllegalStateException("Não deveria estar aqui!")

        binding.wvRepo.settings.javaScriptEnabled = true

        binding.wvRepo.loadUrl("https://github.com/$fullName")

        val toolBar: Toolbar = findViewById(R.id.wb_toolbar)
        setSupportActionBar(toolBar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = fullName

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            finish()
        }
        if(item.itemId == R.id.menu_history){
            Toast.makeText(this@RepoActivity,"Adicionado aos favoritos",Toast.LENGTH_SHORT)
        }
        return super.onOptionsItemSelected(item)
    }

}


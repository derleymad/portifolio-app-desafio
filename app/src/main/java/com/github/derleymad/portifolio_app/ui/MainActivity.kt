package com.github.derleymad.portifolio_app.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.github.derleymad.portifolio_app.R
import com.github.derleymad.portifolio_app.databinding.ActivityMainBinding
import com.github.derleymad.portifolio_app.model.Bio
import com.github.derleymad.portifolio_app.utils.BioRequest
import com.squareup.picasso.Picasso
import java.util.*

class MainActivity : AppCompatActivity(), BioRequest.Callback {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.mainContainer.visibility = View.GONE
        BioRequest(this@MainActivity).execute("https://api.github.com/users/rmagalhaesds")

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

    override fun onResult(bio: Bio) {
        binding.tvName.text = bio.login.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
        binding.tvDesc.text = bio.bio
        binding.tvFollower.text = bio.followers.toString()
        binding.tvFollowing.text = bio.following.toString()
        binding.tvRepo.text = bio.public_repos.toString()

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
        Toast.makeText(this@MainActivity,"Não foi possível encontrar o perfil! $message",Toast.LENGTH_SHORT).show()
        binding.progressBarBio.visibility = View.GONE
    }
}
package com.github.derleymad.portifolio_app.model

data class SearchBio(
    val id:Int,
    val login:String,
    val avatar_url:String = "",
    val url:String
)

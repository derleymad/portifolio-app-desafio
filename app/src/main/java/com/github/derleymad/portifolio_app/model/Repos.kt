package com.github.derleymad.portifolio_app.model

data class Repos(
    val id:Int,
    val name: String,
    val fullName: String,
    val avatarUrl: String,
    val language: String,
    var description: String
)



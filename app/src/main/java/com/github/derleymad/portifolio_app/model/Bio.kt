package com.github.derleymad.portifolio_app.model

data class Bio(
    val login:String,
    val id:Int,
    val avatar_url:String = "",
    val repos_url:String,
    val company:String = "",
    val location:String = "",
    val bio:String = "",
    val public_repos:Int = 0,
    val followers:Int = 0,
    val following:Int = 0
)

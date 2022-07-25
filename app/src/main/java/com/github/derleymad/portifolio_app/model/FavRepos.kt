package com.github.derleymad.portifolio_app.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavRepos (
    @PrimaryKey (autoGenerate = true) val id : Int = 0,
    @ColumnInfo(name = "type") val type : String = "repo",
    @ColumnInfo(name = "name") val name : String,
    @ColumnInfo(name = "full_name") val full_name: String,
    @ColumnInfo(name = "avatar_link") val avatarLink: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "language") val language: String

)
package com.github.derleymad.portifolio_app.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BioFav(
    @PrimaryKey(autoGenerate = true) val id : Int = 0,
    @ColumnInfo(name = "type") val type: String = "bio",
    @ColumnInfo(name = "login") val login: String,
    @ColumnInfo(name = "avatarUrl") val avatarUrl: String = "",
    @ColumnInfo(name = "publicRepos") val publicRepos: Int = 0,
)

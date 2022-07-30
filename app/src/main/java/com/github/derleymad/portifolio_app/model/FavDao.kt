package com.github.derleymad.portifolio_app.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavDao {
    @Insert
    fun insert(fav: BioFav)

    @Delete
    fun delete(fav: BioFav)

    @Query("SELECT * FROM BioFav WHERE type = :type")
    fun getRegisterByType(type:String) : List<BioFav>

    @Query("SELECT * FROM BioFav WHERE id = :id")
    fun getRegisterByid(id: Int) : BioFav
}
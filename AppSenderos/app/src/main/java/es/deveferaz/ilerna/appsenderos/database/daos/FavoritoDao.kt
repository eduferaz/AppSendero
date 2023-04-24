package es.deveferaz.ilerna.appsenderos.database.daos

import androidx.room.Dao
import androidx.room.Query
import es.deveferaz.ilerna.appsenderos.database.entities.Favorito

@Dao
interface FavoritoDao : BaseDao<Favorito> {

    @Query("DELETE FROM favoritos where id_sendero=:id")
    fun deleteBySenderoId(id: Long)
}
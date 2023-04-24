package es.deveferaz.ilerna.appsenderos.database.daos

import androidx.room.Dao
import androidx.room.Query
import es.deveferaz.ilerna.appsenderos.database.entities.Dificultad

@Dao
interface DificultadDao : BaseDao<Dificultad> {
    @Query("DELETE FROM dificultades where id_sendero=:id")
    fun deleteBySenderoId(id: Long)

}
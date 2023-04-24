package es.deveferaz.ilerna.appsenderos.database.daos

import androidx.room.Dao
import androidx.room.Query
import es.deveferaz.ilerna.appsenderos.database.entities.Completado

@Dao
interface CompletadoDao : BaseDao<Completado> {
    @Query("DELETE FROM completados where id_sendero=:id")
    fun deleteBySenderoId(id: Long)
}
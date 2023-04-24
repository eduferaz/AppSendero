package es.deveferaz.ilerna.appsenderos.database.daos

import androidx.room.*
import es.deveferaz.ilerna.appsenderos.database.entities.MunicipioEntidad
import kotlinx.coroutines.flow.Flow

@Dao
interface MunicipioDao : BaseDao<MunicipioEntidad> {

    @Query("SELECT * from municipios")
    fun findAll(): Flow<List<MunicipioEntidad>>
}


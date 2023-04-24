package es.deveferaz.ilerna.appsenderos.database.daos

import androidx.room.Dao
import androidx.room.Query
import es.deveferaz.ilerna.appsenderos.database.entities.Usuario

@Dao
interface UsuarioDao : BaseDao<Usuario>{

    @Query("SELECT * from usuarios where nombre = :nombre")
    fun findOneByName(nombre: String): Usuario?
}
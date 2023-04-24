package es.deveferaz.ilerna.appsenderos.database.daos

import androidx.room.Dao
import androidx.room.Query
import es.deveferaz.ilerna.appsenderos.database.entities.SenderoEntidad
import es.deveferaz.ilerna.appsenderos.database.relations.DetalleSendero
import kotlinx.coroutines.flow.Flow

@Dao
interface SenderoDao : BaseDao<SenderoEntidad>{

    @Query(
        "SELECT s.*, m.nombre as municipio, " +
                "EXISTS (SELECT * from favoritos where id_usuario=:id and id_sendero = s.id) as favorito, " +
                "EXISTS (SELECT * from completados where id_usuario=:id and id_sendero = s.id) as completado, " +
                "EXISTS (SELECT * from dificultades where id_sendero=s.id and id_selectDificultad=1) as bajadificultad, " +
                "EXISTS (SELECT * from dificultades where id_sendero=s.id and id_selectDificultad=2) as mediadificultad, " +
                "EXISTS (SELECT * from dificultades where id_sendero=s.id and id_selectDificultad=3) as altadificultad " +
                "FROM senderos s " +
                "JOIN municipios m ON m.id = s.id_municipio "
    )
    //
    //Devuelve los resultados de las consultas como un flujo para actualizar los resultados en tiempo real
    fun findAll(id: Long): Flow<List<DetalleSendero>>


    @Query(
        "SELECT s.*, m.nombre as municipio, " +
                "EXISTS (SELECT * from favoritos where id_usuario=:id and id_sendero = s.id) as favorito, " +
                "EXISTS (SELECT * from completados where id_usuario=:id and id_sendero = s.id) as completado, " +
                "EXISTS (SELECT * from dificultades where id_sendero=s.id and id_selectDificultad=1) as bajadificultad, " +
                "EXISTS (SELECT * from dificultades where id_sendero=s.id and id_selectDificultad=2) as mediadificultad, " +
                "EXISTS (SELECT * from dificultades where id_sendero=s.id and id_selectDificultad=3) as altadificultad " +
                "FROM senderos s " +
                "JOIN municipios m ON m.id = s.id_municipio " +
                "JOIN favoritos f on f.id_sendero=s.id " +
                "WHERE f.id_usuario = :id"
    )
    fun findAllFavs(id: Long): Flow<List<DetalleSendero>>

    @Query(
        "SELECT s.*, m.nombre as municipio, " +
                "EXISTS (SELECT * from favoritos where id_usuario=:id and id_sendero = s.id) as favorito, " +
                "EXISTS (SELECT * from completados where id_usuario=:id and id_sendero = s.id) as completado, " +
                "EXISTS (SELECT * from dificultades where id_sendero=s.id and id_selectDificultad=1) as bajadificultad, " +
                "EXISTS (SELECT * from dificultades where id_sendero=s.id and id_selectDificultad=2) as mediadificultad, " +
                "EXISTS (SELECT * from dificultades where id_sendero=s.id and id_selectDificultad=3) as altadificultad " +
                "FROM senderos s " +
                "JOIN municipios m ON m.id = s.id_municipio " +
                "JOIN completados c on c.id_sendero=s.id " +
                "WHERE c.id_usuario = :id"
    )
    fun findAllCompletado(id: Long): Flow<List<DetalleSendero>>
}
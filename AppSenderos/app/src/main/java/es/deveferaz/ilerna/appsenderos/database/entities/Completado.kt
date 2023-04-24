package es.deveferaz.ilerna.appsenderos.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = Completado.TABLE_NAME,
    primaryKeys = ["id_usuario", "id_sendero"],
    indices = [Index(value = ["id_usuario"]), Index(value = ["id_sendero"])],
    foreignKeys = [ForeignKey(
        entity = SenderoEntidad::class,
        parentColumns = ["id"],
        childColumns = ["id_sendero"],
        onUpdate = ForeignKey.CASCADE, onDelete = ForeignKey.CASCADE
    ),ForeignKey(
        entity = Usuario::class,
        parentColumns = ["id"],
        childColumns = ["id_usuario"],
        onUpdate = ForeignKey.CASCADE, onDelete = ForeignKey.CASCADE
    )]
)

data class Completado(
    @ColumnInfo(name = "id_sendero") val senderoId: Long,
    @ColumnInfo(name = "id_usuario") val usuarioId: Long
){
    companion object {
        const val TABLE_NAME = "completados"
    }
}

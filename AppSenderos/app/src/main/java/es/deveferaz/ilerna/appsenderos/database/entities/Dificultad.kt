package es.deveferaz.ilerna.appsenderos.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = Dificultad.TABLE_NAME,
    primaryKeys = ["id_selectDificultad", "id_sendero"],
    indices = [Index(value = ["id_selectDificultad"]), Index(value = ["id_sendero"])],
    foreignKeys = [ForeignKey(
        entity = SenderoEntidad::class,
        parentColumns = ["id"],
        childColumns = ["id_sendero"],
        onUpdate = ForeignKey.CASCADE, onDelete = ForeignKey.CASCADE
    ),  ForeignKey(
        entity = Usuario::class,
        parentColumns = ["id"],
        childColumns = ["id_selectDificultad"],
        onUpdate = ForeignKey.CASCADE, onDelete = ForeignKey.CASCADE
    )]
)

data class Dificultad(
    @ColumnInfo(name = "id_selectDificultad") val selectDificultadId: Long,
    @ColumnInfo(name = "id_sendero") val senderoId: Long
){
    companion object{
        const val TABLE_NAME = "dificultades"
    }
}
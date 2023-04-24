package es.deveferaz.ilerna.appsenderos.database.entities

import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = SelectDificultad.TABLE_NAME,
    indices = [
        Index(value = ["nombre"], unique = true)
    ]
)


data class SelectDificultad(val nombre: String) : BaseEntity() {

    companion object {
        const val TABLE_NAME = "selectDificultad"
    }
}
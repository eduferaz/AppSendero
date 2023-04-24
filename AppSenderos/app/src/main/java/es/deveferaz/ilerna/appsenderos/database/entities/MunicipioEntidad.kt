package es.deveferaz.ilerna.appsenderos.database.entities

import androidx.room.Entity
import androidx.room.Index

@Entity(
        tableName = MunicipioEntidad.TABLE_NAME,
    indices = [
        Index(value = ["nombre"], unique = true)]
)
data class MunicipioEntidad(
    var nombre: String
) : BaseEntity(){

    companion object {
        const val TABLE_NAME = "municipios"
    }

    override fun toString(): String {
        return "Municipio(id= $id, nombre = '$nombre')"
    }
}


package es.deveferaz.ilerna.appsenderos.database.entities

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = SenderoEntidad.TABLE_NAME,
    indices = [
        Index(value = ["nombre"], unique = true),
        Index(value = ["id_municipio"])
    ],
    foreignKeys = [
        ForeignKey(
            entity = MunicipioEntidad::class,
            parentColumns = ["id"],
            childColumns = ["id_municipio"],
            onUpdate = ForeignKey.CASCADE, onDelete = ForeignKey.CASCADE
        )
    ]
)

data class SenderoEntidad(
    val nombre: String,
    val descrpcion: String,
    val ubicacion: String,
    val latitude: Double,
    val longitude: Double,
    val imagen: String,
    val distanciaKm: Double,
    @ColumnInfo(name = "id_municipio")
    val municipioId: Long
) : BaseEntity(), Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readString()!!,
        parcel.readDouble(),
        parcel.readLong()
    ){
        id = parcel.readLong()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nombre)
        parcel.writeString(descrpcion)
        parcel.writeString(ubicacion)
        parcel.writeDouble(latitude)
        parcel.writeDouble(longitude)
        parcel.writeString(imagen)
        parcel.writeDouble(distanciaKm)
        parcel.writeLong(municipioId)
        parcel.writeLong(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SenderoEntidad> {
        override fun createFromParcel(parcel: Parcel): SenderoEntidad {
            return SenderoEntidad(parcel)
        }

        override fun newArray(size: Int): Array<SenderoEntidad?> {
            return arrayOfNulls(size)
        }

        const val TABLE_NAME = "senderos"
    }
}

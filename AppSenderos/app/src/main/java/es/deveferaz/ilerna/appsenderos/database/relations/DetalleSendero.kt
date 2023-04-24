package es.deveferaz.ilerna.appsenderos.database.relations

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Embedded
import es.deveferaz.ilerna.appsenderos.database.entities.SenderoEntidad

data class DetalleSendero(
    @Embedded val senderoEntidad: SenderoEntidad,
    val municipio: String,
    val favorito: Boolean,
    val completado: Boolean,
    val bajadificultad: Boolean,
    val mediadificultad: Boolean,
    val altadificultad: Boolean

    ): Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readParcelable(SenderoEntidad::class.java.classLoader)!!,
        parcel.readString()!!,
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte()
    )

    override fun toString(): String {
        return super.toString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(senderoEntidad, flags)
        parcel.writeString(municipio)
        parcel.writeByte(if (favorito) 1 else 0)
        parcel.writeByte(if (completado) 1 else 0)
        parcel.writeByte(if (bajadificultad) 1 else 0)
        parcel.writeByte(if (mediadificultad) 1 else 0)
        parcel.writeByte(if (altadificultad) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    //Creamos nuevas instancias
    companion object CREATOR : Parcelable.Creator<DetalleSendero> {
        override fun createFromParcel(parcel: Parcel): DetalleSendero {
            return DetalleSendero(parcel)
        }

        override fun newArray(size: Int): Array<DetalleSendero?> {
            return arrayOfNulls(size)
        }
    }



}

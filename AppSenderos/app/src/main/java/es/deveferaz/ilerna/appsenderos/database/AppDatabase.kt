package es.deveferaz.ilerna.appsenderos.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import es.deveferaz.ilerna.appsenderos.app.App
import es.deveferaz.ilerna.appsenderos.database.converters.DateConverter
import es.deveferaz.ilerna.appsenderos.database.daos.*
import es.deveferaz.ilerna.appsenderos.database.entities.*
import kotlinx.coroutines.*

@Database(
    entities = [Usuario::class, SenderoEntidad::class, MunicipioEntidad::class, Favorito::class, Completado::class, SelectDificultad::class, Dificultad::class],
    version = 1
)

@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
    abstract fun senderoDao(): SenderoDao
    abstract fun completadoDao(): CompletadoDao
    abstract fun favoritoDao(): FavoritoDao
    abstract fun selectDificultadDao(): SelectDificultadDao
    abstract fun dificultadDao(): DificultadDao
    abstract fun municipioDao(): MunicipioDao

    companion object { //static
        private lateinit var db: AppDatabase

        fun initDB(context: Context): AppDatabase {
            if (!this::db.isInitialized) { //Sigleton
                db = Room.databaseBuilder(context, AppDatabase::class.java, "database-name")
                    .addCallback(callback)
                    .build()
            }
            return db
        }

        @OptIn(DelicateCoroutinesApi::class)
        private val callback: Callback = object : Callback() { //Creamos un objeto de la clase
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                GlobalScope.launch {
                    //Insercción de datos
                    withContext(Dispatchers.IO) {
                        App.getDatabase().usuarioDao().save(Usuario("eduardo", "123456"))
                        App.getDatabase().selectDificultadDao().insertAll(
                            SelectDificultad("Dificultad Baja"),
                            SelectDificultad("Dificultad Media"),
                            SelectDificultad("Dificultad Alta")
                        )
                        App.getDatabase().municipioDao().insertAll(
                            MunicipioEntidad(("Cazalla de la Sierra")),
                            MunicipioEntidad("El Pedroso"),
                            MunicipioEntidad("Constantina"),
                            MunicipioEntidad("Las Navas de la Concepción"),
                            MunicipioEntidad("San Nicolás del Puerto"),
                            MunicipioEntidad("Alanís")
                        )
                        App.getDatabase().senderoDao().save(
                            SenderoEntidad(
                                "Sendero de las Laderas",
                                "Las Laderas es una propuesta que nos lleva desde Cazalla de La Sierra a la Rivera del Huéznar " +
                                        "por una antigua vereda, y nos trae de regreso por otro camino histórico. Un circuito que " +
                                        "ofrece al senderista la oportunidad de aproximarse a los paisajes y valores más representativos " +
                                        "del Parque Natural Sierra Norte de Sevilla, desde los más o menos modificados, como ruedos agrícolas " +
                                        "o dehesas con ganadería extensiva, a los que conservan en mayor medida sus rasgos naturales, como el " +
                                        "bosque de galería.\n" + "El sendero se inicia en lo que se conoce como Vereda del valle o Camino " +
                                        "de las Laderas de Cazalla y prosigue en descenso hasta la Rivera del Huesna, pudiéndose contemplar " +
                                        "en su recorrido un rico mosaico de huertas, olivares y bosque mediterráneo. El regreso es por el " +
                                        "Camino Viejo de la Estación, y muestra antiguos viñedos, vestigios de un cultivo muy importante " +
                                        "antaño en la comarca del que permanecen viejos lagares.",
                                "Sendero de las Laderas",
                                37.92757081435537,
                                -5.755008457440394,
                                "https://lh3.googleusercontent.com/p/AF1QipNL25IWDXosbqcczRZ0JG6ElRql-yq4fUwPebSn=s680-w680-h510",
                                8.11,
                                1
                            )
                        )
                    }
                }
            }
        }
    }
}
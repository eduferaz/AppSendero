package es.deveferaz.ilerna.appsenderos.app

import android.app.Application
import android.content.Context
import android.util.Log
import android.view.View
import androidx.room.Room
import com.google.android.material.snackbar.Snackbar
import es.deveferaz.ilerna.appsenderos.database.AppDatabase
import es.deveferaz.ilerna.appsenderos.database.entities.Usuario

class App : Application() {


    init {
        instancia = this

    }

    companion object {
        private lateinit var instancia: App
        fun saveUser(usuario: Usuario) {
            val prefs =
                instancia.getSharedPreferences(Constantes.PREFERENCES, Context.MODE_PRIVATE)!!
            prefs.edit().apply {
                putLong(Constantes.USUARIO_ID, usuario.id)
                putString(Constantes.USUARIO_NOMBRE, usuario.nombre)
            }.apply()

        }

        fun getUsuario(): Usuario? {
            val prefs =
                instancia.getSharedPreferences(Constantes.PREFERENCES, Context.MODE_PRIVATE)!!
            val id = prefs.getLong(Constantes.USUARIO_ID, 0)
            val nombre = prefs.getString(Constantes.USUARIO_NOMBRE, null)
            nombre?.let {
                return Usuario(it).apply {
                    this.id = id
                }
            }
            return null

        }

        fun clear() {
            val prefs =
                instancia.getSharedPreferences(Constantes.PREFERENCES, Context.MODE_PRIVATE)!!
            val edit = prefs.edit()!!
            edit.clear().apply()
        }

        private lateinit var db: AppDatabase

        fun getDatabase(): AppDatabase {
            return db
        }


        fun showSnackbar(view: View, text: String) {
            Snackbar.make(view, text, Snackbar.LENGTH_SHORT)
                .show()
        }
    }

    override fun onCreate() {
        super.onCreate()
        db = AppDatabase.initDB(this)
        if (db.isOpen) {
            Log.d("Database", "DATABASE OPENED")
        }
    }
}
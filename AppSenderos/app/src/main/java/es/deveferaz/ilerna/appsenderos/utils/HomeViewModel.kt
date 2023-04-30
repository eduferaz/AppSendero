package es.deveferaz.ilerna.appsenderos.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import es.deveferaz.ilerna.appsenderos.app.App
import es.deveferaz.ilerna.appsenderos.database.entities.Completado
import es.deveferaz.ilerna.appsenderos.database.entities.Favorito
import es.deveferaz.ilerna.appsenderos.database.relations.DetalleSendero
import es.deveferaz.ilerna.appsenderos.ui.home.TipoSendero
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel : ViewModel() {
    private val senderoDao = App.getDatabase().senderoDao()
    private val favoritoDao = App.getDatabase().favoritoDao()
    private val completadoDao = App.getDatabase().completadoDao()

    fun senderos(tipo: TipoSendero): LiveData<List<DetalleSendero>> {
        return when (tipo.tipo) {
            1 -> senderoDao.findAll(App.getUsuario()!!.id).asLiveData()
            2 -> senderoDao.findAllFavs(App.getUsuario()!!.id).asLiveData()
            else -> senderoDao.findAllCompletado(App.getUsuario()!!.id).asLiveData()
        }
    }

    fun addFav(senderoId: Long) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                favoritoDao.save(Favorito(senderoId, App.getUsuario()!!.id))
            }
        }
    }

    fun delFav(senderoId: Long) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                favoritoDao.delete(Favorito(senderoId, App.getUsuario()!!.id))
            }
        }
    }

    fun addCompletado(senderoId: Long) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                completadoDao.save(Completado(senderoId, App.getUsuario()!!.id))
            }
        }
    }

    fun delCompletado(senderoId: Long) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                completadoDao.delete(Completado(senderoId, App.getUsuario()!!.id))
            }
        }
    }

}
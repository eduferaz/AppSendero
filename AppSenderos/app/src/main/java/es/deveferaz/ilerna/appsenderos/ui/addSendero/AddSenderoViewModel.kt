package es.deveferaz.ilerna.appsenderos.ui.addSendero

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import androidx.lifecycle.*
import es.deveferaz.ilerna.appsenderos.app.App
import es.deveferaz.ilerna.appsenderos.database.entities.Dificultad
import es.deveferaz.ilerna.appsenderos.database.entities.MunicipioEntidad
import es.deveferaz.ilerna.appsenderos.database.entities.SenderoEntidad
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddSenderoViewModel : ViewModel() {

    private val dificultadDao = App.getDatabase().dificultadDao()
    private val senderoDao = App.getDatabase().senderoDao()
    private val municipioDao = App.getDatabase().municipioDao()
    val municipios: LiveData<List<MunicipioEntidad>> = municipioDao.findAll().asLiveData()

    fun save(sendero: SenderoEntidad): LiveData<Long>{
        val data = MutableLiveData<Long>()
        viewModelScope.launch {
            try {
                val id = withContext(Dispatchers.IO){
                    senderoDao.save(sendero)
                }
                data.value = id
            } catch (ex: SQLiteConstraintException){
                data.value = -1
            }
        }
        return data
    }

    fun save(municipio: MunicipioEntidad): LiveData<Boolean>{
        val data = MutableLiveData<Boolean>()
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    municipioDao.save(municipio)
                    data.postValue(true)
                } catch (ex: SQLiteConstraintException){
                    data.postValue(false)
                    Log.d("TEST", "TEST1")
                }
            }
        }
        return data
    }

    fun addDificultad(selectDificultad: Long, sendero: Long){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dificultadDao.save(Dificultad(selectDificultad, sendero))
            }
        }
    }
    fun delDificultad(selectDificultad: Long, id: Long){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                dificultadDao.delete(Dificultad(selectDificultad, id))
            }
        }
    }

    fun update(sendero: SenderoEntidad): LiveData<Boolean>{
        val data = MutableLiveData<Boolean>()
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    senderoDao.update(sendero)
                    data.postValue(true)
                }catch (ex: SQLiteConstraintException){
                    data.postValue(false)
                }
            }
        }
        return data
    }

    fun eliminar(sendero: SenderoEntidad){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                senderoDao.delete(sendero)
            }
        }
    }

}
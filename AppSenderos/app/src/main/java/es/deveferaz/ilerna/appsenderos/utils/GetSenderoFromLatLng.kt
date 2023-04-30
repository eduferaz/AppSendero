package es.deveferaz.ilerna.appsenderos.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.Locale

class GetSenderoFromLatLng(
    context: Context,
    private val latitude: Double,
    private val longitude: Double
) {

        // Geocoder nos permite obtener la dirección a partir de coordenadas
        private val geocoder: Geocoder = Geocoder(context, Locale.getDefault())

        private lateinit var mAddressListener: AddressListener

    // Función suspendida que devuelve la dirección como una cadena
    suspend fun getAddress(): String {

        // Lanza la función en un contexto de corrutina con Dispatcher.IO, que es adecuado para tareas de E/S
        return withContext(Dispatchers.IO) {
            @Suppress("DEPRECATION")
            try {
                // Obtiene la lista de direcciones
                val addressList: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)
                if (addressList != null && addressList.isNotEmpty()) {
                    val address: Address = addressList[0]
                    val sb = StringBuilder()
                    for (i in 0..address.maxAddressLineIndex) {
                        sb.append(address.getAddressLine(i)).append(" ")
                    }
                    sb.deleteCharAt(sb.length - 1)
                    sb.toString()
                } else {
                    ""
                }
            } catch (e: IOException) {
                Log.e("HikeLog", "Unable to connect to Geocoder")
                ""
            }
        }
    }

    fun setAddressListener(addressListener: AddressListener) {
        mAddressListener = addressListener
    }

    fun executeAddressSearch() {
        // Lanza la corrutina en el hilo principal (Dispatchers.Main) para interactuar con la interfaz de usuario
        CoroutineScope(Dispatchers.Main).launch {
            // Llama a la función suspendida getAddress() y guarda el resultado en la variable 'address'
            val address = getAddress()
            // Si la dirección está vacía, llama a onError() en AddressListener
            if (address.isBlank()) {
                mAddressListener.onError()
            } else {
                // Si se encuentra una dirección, llama a onAddressFound() en AddressListener y pasa la dirección
                mAddressListener.onAddressFound(address)
            }
        }
    }

    // Define la interfaz AddressListener con dos funciones suspendidas
    interface AddressListener {
        suspend fun onAddressFound(address: String?)
        suspend fun onError()
    }
}
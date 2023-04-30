package es.deveferaz.ilerna.appsenderos.ui.addSendero

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import es.deveferaz.ilerna.appsenderos.R
import es.deveferaz.ilerna.appsenderos.app.Constantes
import es.deveferaz.ilerna.appsenderos.app.Constantes.Companion.ALTADIFICULTAD
import es.deveferaz.ilerna.appsenderos.app.Constantes.Companion.BAJADIFICULTAD
import es.deveferaz.ilerna.appsenderos.app.Constantes.Companion.MEDIADIFICULTAD
import es.deveferaz.ilerna.appsenderos.database.entities.SenderoEntidad
import es.deveferaz.ilerna.appsenderos.database.relations.DetalleSendero
import es.deveferaz.ilerna.appsenderos.databinding.ActivityAddSenderoBinding
import es.deveferaz.ilerna.appsenderos.ui.home.MunicipioFragment
import es.deveferaz.ilerna.appsenderos.utils.GetAddressFromLatLng
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AddSenderoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddSenderoBinding
    private val viewModel: AddSenderoViewModel by viewModels()

    private var municipioId = -1L

    private var mLatitude: Double = 0.0 // Guardamos los datos de latitud en esta variable.
    private var mLongitude: Double = 0.0 // Guardamos los datos de longitud en esta variable.
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    // Esta variable se utiliza obtener la localización actual del usuario.

    private val autocompleteLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val sendero : Place = Autocomplete.getPlaceFromIntent(result.data!!)
            val tieUbicacion = binding.tieUbicacion
            tieUbicacion.setText(sendero.address)
            mLatitude = sendero.latLng!!.latitude
            mLongitude = sendero.latLng!!.longitude
        }

    }

    private lateinit var detalleSendero: DetalleSendero
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddSenderoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.extras?.let {
            binding.eliminar.visibility = View.VISIBLE
            detalleSendero= it.getParcelable(Constantes.SENDERO)!!
            binding.tieNombre.setText(detalleSendero.senderoEntidad.nombre)
            binding.tieDescripcion.setText(detalleSendero.senderoEntidad.descrpcion)
            binding.tieImagen.setText(detalleSendero.senderoEntidad.imagen)
            binding.tieUbicacion.setText(detalleSendero.senderoEntidad.ubicacion)
            mLatitude = detalleSendero.senderoEntidad.latitude
            mLongitude = detalleSendero.senderoEntidad.longitude
            binding.tieDistancia.setText(detalleSendero.senderoEntidad.distanciaKm.toString())

            if (detalleSendero.altadificultad){
                binding.chipAlta.isChecked = true
            }
            if (detalleSendero.mediadificultad){
                binding.chipMedia.isChecked = true
            }
            if (detalleSendero.bajadificultad){
                binding.chipBaja.isChecked = true
            }
        }

        viewModel.municipios.observe(this){
            val spinner = binding.spinnerMunicipio
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item, it.map { item -> item.nombre }
            )
            spinner.adapter = adapter
            if (this::detalleSendero.isInitialized){
                val pos = it.map { item -> item.nombre }.indexOf(detalleSendero.municipio)
                spinner.setSelection(pos)
            }

            spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ){
                    municipioId = it[position].id
                }
                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Función implementada por el object del spinner
                }
            }
        }
        //Inicializar FusedLocation
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Inicializar Places SDK en el método onCreate de la Activity
        if (!Places.isInitialized()){
            Places.initialize(this@AddSenderoActivity,
                resources.getString(R.string.google_maps_api_key))
        }
    }

    private fun isLocationEnabled(): Boolean{
        val locationManager: LocationManager = getSystemService(
            Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(
            LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER)
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData(locationCallback: LocationCallback) {
        val locationRequest = com.google.android.gms.location.LocationRequest.create().apply {
            priority = PRIORITY_HIGH_ACCURACY
            interval = 0
            fastestInterval = 1000
            numUpdates = 1
        }

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()
        )
    }

    private suspend fun getLocationCallback() = suspendCoroutine<LocationCallback> { continuation ->
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val mLastLocation: Location? = locationResult.lastLocation
                mLatitude = mLastLocation!!.latitude
                Log.i("Current Latitude", "$mLatitude")
                mLongitude = mLastLocation!!.longitude
                Log.i("Current Longitude", "$mLongitude")

                val addressTask = GetAddressFromLatLng(this@AddSenderoActivity, mLatitude, mLongitude)
                addressTask.setAddressListener(
                    object : GetAddressFromLatLng.AddressListener {
                        val tieUbicacion = binding.tieUbicacion
                        override suspend fun onAddressFound(address: String?) {
                            Log.e("Adress ::", "" + address)
                            tieUbicacion.setText(address) //Añadimos la dirección al editText
                        }

                        override suspend fun onError() {
                            TODO("Not yet implemented")
                        }
                    }
                )
                // Lanzar una coroutine usando lifecycleScope
                lifecycleScope.launch {
                    addressTask.executeAddressSearch()
                }
            }
        }
        continuation.resume(locationCallback)
    }

    override fun onStart() {
        super.onStart()

        binding.tieUbicacion.setOnClickListener {
            try {
                // These are the list of fields which we required is passed
                val fields = listOf(
                    Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG,
                    Place.Field.ADDRESS
                )
                // Start the autocomplete intent with a unique request code.
                val intent = Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.FULLSCREEN, fields
                ).build(this)

                autocompleteLauncher.launch(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        binding.tvSelectCurrentLocation.setOnClickListener {
            if (!isLocationEnabled()) {
                Toast.makeText(
                    this,
                    "Your location provider is turned off. Please turn it on.",
                    Toast.LENGTH_SHORT
                ).show()

                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
            else {
                Dexter.withActivity(this)
                    .withPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                    .withListener(object : MultiplePermissionsListener {
                        override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                            if (report!!.areAllPermissionsGranted()) {
                                lifecycleScope.launch {
                                    val locationCallback = getLocationCallback()
                                    requestNewLocationData(locationCallback)
                                }
                            }
                        }

                        override fun onPermissionRationaleShouldBeShown(
                            permissions: MutableList<PermissionRequest>?,
                            token: PermissionToken?
                        ) {
                            ActivityCompat.requestPermissions(
                                this@AddSenderoActivity,
                                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,
                                    Manifest.permission.ACCESS_FINE_LOCATION),
                                PERMISSION_REQUEST_CODE
                            )
                        }
                    }).onSameThread()
                    .check()
            }
        }

        binding.newMunicipio.setOnClickListener{
            val senderoFragment = MunicipioFragment()
            senderoFragment.show(supportFragmentManager, "municipio")
        }

        binding.eliminar.setOnClickListener {
            viewModel.eliminar(detalleSendero.senderoEntidad)
            finish()
        }

        binding.guardar.setOnClickListener {
            val nombre = binding.tieNombre.text.toString()
            val descripcion = binding.tieDescripcion.text.toString()
            val ubicacion = binding.tieUbicacion.text.toString()
            val imagen = binding.tieImagen.text.toString()
            val distancia = binding.tieDistancia.text.toString()

            if (nombre.isBlank() || descripcion.isBlank() || distancia.isBlank()){
                Toast.makeText(this, getString(R.string.rellenar_campos), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (municipioId == -1L){
                Toast.makeText(this, getString(R.string.seleccionar_municipio), Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            val sendero = SenderoEntidad(
                nombre,
                descripcion,
                ubicacion,
                mLatitude,
                mLongitude,
                imagen,
                distancia.toDouble(),
                municipioId
            )
            var errors = false

            if (!this::detalleSendero.isInitialized)
                viewModel.save(sendero).observe(this) {
                    if (it == -1L) {
                        Toast.makeText(this,
                        getString(R.string.sendero_existe),
                            Toast.LENGTH_SHORT
                        ).show()
                        errors = true
                    }
                    if (binding.chipAlta.isChecked){
                        addDificultad(ALTADIFICULTAD, it)
                    }
                    if (binding.chipMedia.isChecked){
                        addDificultad(MEDIADIFICULTAD, it)
                    }
                    if (binding.chipBaja.isChecked){
                        addDificultad(BAJADIFICULTAD, it)
                    }
                }
            else{
                sendero.id = this.detalleSendero.senderoEntidad.id
                viewModel.update(sendero).observe(this) {
                    if (!it) {
                        Toast.makeText(
                            this,
                            getString(R.string.sendero_existe),
                            Toast.LENGTH_SHORT
                        ).show()
                        errors = true
                        return@observe
                    }
                    if (detalleSendero.altadificultad && !binding.chipAlta.isChecked) {
                        delDificultad(ALTADIFICULTAD, sendero.id)
                    }
                    if (!detalleSendero.altadificultad && binding.chipAlta.isChecked) {
                        addDificultad(ALTADIFICULTAD, sendero.id)
                    }
                    if (detalleSendero.mediadificultad && !binding.chipMedia.isChecked){
                        delDificultad(MEDIADIFICULTAD, sendero.id)
                    }
                    if (!detalleSendero.mediadificultad && binding.chipMedia.isChecked){
                        addDificultad(MEDIADIFICULTAD, sendero.id)
                    }
                    if (detalleSendero.bajadificultad && !binding.chipBaja.isChecked) {
                        delDificultad(BAJADIFICULTAD, sendero.id)
                    }
                    if (!detalleSendero.bajadificultad && binding.chipBaja.isChecked) {
                        addDificultad(BAJADIFICULTAD, sendero.id)
                    }
                }
            }
            if (!errors)
                finish()
        }

    }

    private fun addDificultad(selectDificultad: Long, id: Long) {
        viewModel.addDificultad(selectDificultad, id)
    }
    private fun delDificultad(selectDificultad: Long, id: Long) {
        viewModel.delDificultad(selectDificultad, id)
    }

    companion object{
        private val PERMISSION_REQUEST_CODE = 1001
    }

}
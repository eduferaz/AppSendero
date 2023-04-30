package es.deveferaz.ilerna.appsenderos.utils

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import es.deveferaz.ilerna.appsenderos.R
import es.deveferaz.ilerna.appsenderos.app.Constantes
import es.deveferaz.ilerna.appsenderos.database.entities.SenderoEntidad
import es.deveferaz.ilerna.appsenderos.databinding.ActivityMapsBinding

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityMapsBinding
    private var mSenderoDetail: SenderoEntidad? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (intent.hasExtra(Constantes.SENDERO)){
            mSenderoDetail = intent.getParcelableExtra(Constantes.SENDERO)
                    as? SenderoEntidad
        }
        if (mSenderoDetail!= null){
            val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(this)
        }
    }
    override fun onMapReady(googleMap: GoogleMap) {
        val position = LatLng(
            mSenderoDetail!!.latitude,
            mSenderoDetail!!.longitude
        )
        googleMap.addMarker(MarkerOptions().position(position).title(mSenderoDetail!!.ubicacion))
        val newLatLngZoom = CameraUpdateFactory.newLatLngZoom(position, 15f)
        googleMap.animateCamera(newLatLngZoom)
    }
}
package es.deveferaz.ilerna.appsenderos.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import es.deveferaz.ilerna.appsenderos.R
import es.deveferaz.ilerna.appsenderos.app.Constantes
import es.deveferaz.ilerna.appsenderos.database.entities.SenderoEntidad
import es.deveferaz.ilerna.appsenderos.database.relations.DetalleSendero
import es.deveferaz.ilerna.appsenderos.databinding.ActivitySenderoDetailBinding
import es.deveferaz.ilerna.appsenderos.utils.HomeViewModel
import es.deveferaz.ilerna.appsenderos.utils.MapsActivity

class SenderoDetailActivity : AppCompatActivity() {

    lateinit var binding: ActivitySenderoDetailBinding
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySenderoDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data: DetalleSendero? = intent.getParcelableExtra(Constantes.SENDERO)
        var mDetailSendero : SenderoEntidad? = null

        if (data != null) {
            mDetailSendero = data.senderoEntidad
            binding.nombre.text = data.senderoEntidad.nombre
            binding.descripcion.text = data.senderoEntidad.descrpcion
            binding.municipio.text = data.municipio
            binding.distancia.text = getString(R.string.show_distancia, data.senderoEntidad.distanciaKm)
            binding.ubicacion.text = data.senderoEntidad.ubicacion
            binding.btnViewOnMap.setOnClickListener {
                val mapIntent = Intent(this, MapsActivity::class.java)
                mapIntent.putExtra(Constantes.SENDERO, mDetailSendero)
                startActivity(mapIntent)
            }
            binding.like.setOnClickListener {
                if (data.favorito) {
                    homeViewModel.delFav(data.senderoEntidad.id)
                    binding.like.setIconTintResource(R.color.md_theme_light_outline)
                } else {
                    binding.like.setIconTintResource(R.color.md_theme_light_error)
                    homeViewModel.addFav(data.senderoEntidad.id)
                }
            }
            binding.completado.setOnClickListener {
                if (data.completado) {
                    homeViewModel.delCompletado(data.senderoEntidad.id)
                    binding.completado.setIconTintResource(R.color.md_theme_light_outline)
                } else {
                    homeViewModel.addCompletado(data.senderoEntidad.id)
                    binding.completado.setIconTintResource(R.color.md_theme_light_error)
                }
            }
            val circularProgressDrawable = CircularProgressDrawable(this)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()
            Glide
                .with(this)
                .load(data.senderoEntidad.imagen)
                .centerCrop()
                .placeholder(circularProgressDrawable)
                .into(binding.imagen)

            if (data.favorito) {
                binding.like.setIconTintResource(R.color.md_theme_light_error)

            } else {
                binding.like.setIconTintResource(R.color.md_theme_light_outline)
            }
            if (data.completado) {
                binding.completado.setIconTintResource(R.color.md_theme_light_error)
            } else {
                binding.completado.setIconTintResource(R.color.md_theme_light_outline)
            }

            if (!data.altadificultad) {
                binding.chipAlta.visibility = View.GONE

            }
            if (!data.mediadificultad) {
                binding.chipMedia.visibility = View.GONE

            }
            if (!data.bajadificultad) {
                binding.chipBaja.visibility = View.GONE

            }

        } else {
            // Maneja el caso en que data es nulo, por ejemplo, muestra un mensaje de error o termina la actividad
            Toast.makeText(this, "Error al cargar detalles del sendero", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}

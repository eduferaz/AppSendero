package es.deveferaz.ilerna.appsenderos.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import es.deveferaz.ilerna.appsenderos.ui.home.MapsFragment
import es.deveferaz.ilerna.appsenderos.R
import es.deveferaz.ilerna.appsenderos.database.relations.DetalleSendero
import es.deveferaz.ilerna.appsenderos.databinding.VistaSenderoBinding
import es.deveferaz.ilerna.appsenderos.ui.home.HomeViewModel

class SenderoDialog (val data: DetalleSendero) : DialogFragment() {

    lateinit var binding: VistaSenderoBinding
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {

            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.vista_sendero, null)
            binding = VistaSenderoBinding.bind(view)
            binding.nombre.text = data.senderoEntidad.nombre
            binding.descripcion.text = data.senderoEntidad.descrpcion
            binding.municipio.text = data.municipio
            binding.distancia.text = getString(R.string.show_distancia, data.senderoEntidad.distanciaKm)
            binding.ubicacion.text = getString(R.string.externo)
            binding.ubicacion.setTextColor(Color.MAGENTA)
            binding.ubicacion.setOnClickListener {
                val intent = Intent(this.context, MapsFragment::class.java)
                startActivity(intent)
            }
            val circularProgressDrawable = CircularProgressDrawable(requireContext())
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()
            Glide
                .with(requireContext())
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
            builder.setView(view)
            builder.create()

        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
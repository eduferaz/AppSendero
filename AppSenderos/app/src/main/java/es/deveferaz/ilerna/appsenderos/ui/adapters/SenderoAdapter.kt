package es.deveferaz.ilerna.appsenderos.ui.adapters

import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import es.deveferaz.ilerna.appsenderos.R
import es.deveferaz.ilerna.appsenderos.database.relations.DetalleSendero
import es.deveferaz.ilerna.appsenderos.databinding.SenderoBinding

class SenderoAdapter(
    val list: List<DetalleSendero>,
    private val listener: SenderoListener,
    val context: Context
):
        RecyclerView.Adapter<SenderoAdapter.ViewHolder>() {

    class ViewHolder private constructor(
        private val binding: SenderoBinding,
        private val listener: SenderoListener,
        private val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {

        fun rellenarDatos(data: DetalleSendero) {
            binding.root.setOnClickListener {
                listener.details(data)
            }
            binding.nombre.text = data.senderoEntidad.nombre
            val resaltado = SpannableStringBuilder(binding.nombre.text)

            resaltado.setSpan(
                StyleSpan(Typeface.BOLD),
                0,
                binding.nombre.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            resaltado.setSpan(
                UnderlineSpan(),
                0,
                binding.nombre.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            binding.nombre.text = resaltado
            binding.distancia.text = context.getString(R.string.show_distancia, data.senderoEntidad.distanciaKm)
            binding.municipio.text = data.municipio
            binding.ubicacion.text = data.senderoEntidad.ubicacion
            binding.ubicacion.setTextColor(ContextCompat.getColor(context, R.color.md_theme_dark_errorContainer))

            binding.ubicacion.setOnClickListener {
                listener.open(data)
            }
            binding.edit.setOnClickListener {
                listener.edit(data)
            }
            if (data.favorito) {
                binding.like.setIconTintResource(R.color.md_theme_dark_errorContainer)
            }else {
                binding.like.setIconTintResource(R.color.md_theme_dark_onSurfaceVariant)
            }
            if (data.completado) {
                binding.completado.setIconTintResource(R.color.md_theme_dark_background)
            }else {
                binding.completado.setIconTintResource(R.color.md_theme_dark_onSurfaceVariant)
            }
            val circularProgressDrawable = CircularProgressDrawable(context)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()
            Glide
                .with(context)
                .load(data.senderoEntidad.imagen)
                .centerCrop()
                .placeholder(circularProgressDrawable)
                .into(binding.imagen)

            binding.like.setOnClickListener {
                if (data.favorito) {
                    listener.delFavorito(data.senderoEntidad.id)
                    binding.like.setIconTintResource(R.color.md_theme_light_outline)
                } else {
                    binding.like.setIconTintResource(R.color.md_theme_dark_error)
                    listener.addFavorito(data.senderoEntidad.id)
                }
            }
            binding.completado.setOnClickListener {
                if (data.completado) {
                    listener.delCompletado(data.senderoEntidad.id)
                    binding.completado.setIconTintResource(R.color.md_theme_light_outline)
                }else {
                    listener.addCompletado(data.senderoEntidad.id)
                    binding.completado.setIconTintResource(R.color.md_theme_light_error)
                }
            }

        }
        companion object {
            fun newInstance(
                parent: ViewGroup, listener: SenderoListener, context: Context
            ): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = SenderoBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding, listener, context)
            }
        }

    }

    //Devuelve la cantidad máxima de elementos que existen en la lista
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder.newInstance(parent, listener, context)

    //Crea instancias de la clase VIewHolder

    //Envia la información a la instancia creada para ser dibujada en la pantalla
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) =
        viewHolder.rellenarDatos(list[position])

    override fun getItemCount() = list.size
}

interface SenderoListener {
    fun open(detalleSendero: DetalleSendero)
    fun addFavorito(id: Long)
    fun delFavorito(id: Long)
    fun addCompletado(id: Long)
    fun delCompletado(id: Long)
    fun details(detalleSendero: DetalleSendero)
    fun edit(detalleSendero: DetalleSendero)
}
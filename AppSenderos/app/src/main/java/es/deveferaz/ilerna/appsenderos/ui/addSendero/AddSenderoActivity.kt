package es.deveferaz.ilerna.appsenderos.ui.addSendero

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import es.deveferaz.ilerna.appsenderos.R
import es.deveferaz.ilerna.appsenderos.app.Constantes
import es.deveferaz.ilerna.appsenderos.app.Constantes.Companion.ALTADIFICULTAD
import es.deveferaz.ilerna.appsenderos.app.Constantes.Companion.BAJADIFICULTAD
import es.deveferaz.ilerna.appsenderos.app.Constantes.Companion.MEDIADIFICULTAD
import es.deveferaz.ilerna.appsenderos.database.entities.SenderoEntidad
import es.deveferaz.ilerna.appsenderos.database.relations.DetalleSendero
import es.deveferaz.ilerna.appsenderos.databinding.ActivityAddSenderoBinding
import es.deveferaz.ilerna.appsenderos.ui.dialogs.MunicipioDialog

class AddSenderoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddSenderoBinding
    private val viewModel: AddSenderoViewModel by viewModels()

    private var municipioId = -1L

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
                    // write code to perform some action
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        binding.newMunicipio.setOnClickListener{
            val senderoFragment = MunicipioDialog()
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

}
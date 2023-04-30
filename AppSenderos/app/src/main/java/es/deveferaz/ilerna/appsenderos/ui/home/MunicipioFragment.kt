package es.deveferaz.ilerna.appsenderos.ui.home

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.findViewTreeLifecycleOwner
import es.deveferaz.ilerna.appsenderos.R
import es.deveferaz.ilerna.appsenderos.database.entities.MunicipioEntidad
import es.deveferaz.ilerna.appsenderos.databinding.FragmentMunicipioBinding
import es.deveferaz.ilerna.appsenderos.ui.addSendero.AddSenderoViewModel

class MunicipioFragment : DialogFragment() {
    lateinit var binding: FragmentMunicipioBinding
    private val addSenderoViewModel: AddSenderoViewModel by viewModels()

    override fun onCreateDialog(savedInstaceState: Bundle?): Dialog {
        return activity?.let {
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.fragment_municipio, null)
            binding = FragmentMunicipioBinding.bind(view)

            val builder =
                AlertDialog.Builder(it).setPositiveButton(
                    getString(R.string.guardar), null
                )

            builder.setView(view)
            val dialog = builder.create()
            dialog.show()

            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                if (binding.tieNombre.text.toString().isBlank()){
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.establecer_nombre),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    return@setOnClickListener
                }
                addSenderoViewModel.save(MunicipioEntidad(binding.tieNombre.text.toString()))
                    .observe(view.findViewTreeLifecycleOwner()!!){ response ->
                        if (!response) {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.sendero_existe),
                                Toast.LENGTH_SHORT
                            ).show()
                        }else
                            dialog.dismiss()
                    }
            }
            dialog
        } ?: throw java.lang.IllegalStateException("Activity cannot be null")
    }
}
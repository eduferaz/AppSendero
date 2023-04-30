package es.deveferaz.ilerna.appsenderos.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import es.deveferaz.ilerna.appsenderos.databinding.FragmentSenderosBinding
import es.deveferaz.ilerna.appsenderos.utils.HomeViewModel


class SenderoFragment : Fragment() {
    /**
     * Este fragmento muestra una lista de senderos en un
     * RecyclerView y se encarga de actualizar la UI cuando
     * los datos cambian. Utiliza el patrón de diseño MVVM
     * (Model-View-ViewModel) a través de la clase HomeViewModel
     * y Data Binding para separar la lógica de la UI de la lógica
     * de negocio y facilitar la gestión del estado de la aplicación.
     */

    private var _binding: FragmentSenderosBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSenderosBinding.inflate(inflater, container, false)
        val root: View = binding.root
        viewModel.senderos(TipoSendero.GENERAL).observe(viewLifecycleOwner) {
            CommonFragmentImpl(
                SenderoListenerImpl(
                    requireContext(),
                    viewModel,
                    parentFragmentManager
                ), requireContext(), binding
            ).createRecyclerView(it)
        }
        return root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
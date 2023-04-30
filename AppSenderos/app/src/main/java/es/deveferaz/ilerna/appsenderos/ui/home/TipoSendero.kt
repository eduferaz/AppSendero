package es.deveferaz.ilerna.appsenderos.ui.home

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import es.deveferaz.ilerna.appsenderos.database.relations.DetalleSendero
import es.deveferaz.ilerna.appsenderos.databinding.FragmentSenderosBinding
import es.deveferaz.ilerna.appsenderos.ui.adapters.SenderoListener
import es.deveferaz.ilerna.appsenderos.ui.adapters.SenderoAdapter

enum class TipoSendero(val tipo: Int) {
    GENERAL(1), FAVORITOS(2), COMPLETADOS(3)
}

class CommonFragmentImpl(
    val senderoListener: SenderoListener,
    val context: Context,
    val binding: FragmentSenderosBinding
){
    private lateinit var mAdapterSenderos: SenderoAdapter
    fun createRecyclerView(senderos: List<DetalleSendero>) {
        mAdapterSenderos =
            SenderoAdapter(
                senderos as MutableList<DetalleSendero>,
                senderoListener,
                context
            )
        val recyclerView = binding.rvHome
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = mAdapterSenderos
        }
    }
}
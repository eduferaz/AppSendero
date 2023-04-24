package es.deveferaz.ilerna.appsenderos.ui.home

import android.content.Context
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import es.deveferaz.ilerna.appsenderos.database.relations.DetalleSendero
import es.deveferaz.ilerna.appsenderos.databinding.FragmentSenderosBinding
import es.deveferaz.ilerna.appsenderos.ui.adapters.SenderoListener
import es.deveferaz.ilerna.appsenderos.ui.adapters.SenderosRecyclerViewAdapter

enum class TipoSendero(val tipo: Int) {
    GENERAL(1), FAVORITOS(2), COMPLETADOS(3)
}

class CommonFragmentImpl(
    val senderoListener: SenderoListener,
    val context: Context,
    val binding: FragmentSenderosBinding
){
    private lateinit var mAdapterSenderos: SenderosRecyclerViewAdapter
    fun createRecyclerView(senderos: List<DetalleSendero>) {
        mAdapterSenderos =
            SenderosRecyclerViewAdapter(
                senderos as MutableList<DetalleSendero>,
                senderoListener,
                context
            )
        val recyclerView = binding.rvHome
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = mAdapterSenderos
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
        }
    }
}
package es.deveferaz.ilerna.appsenderos.ui.home

import android.content.Context
import android.content.Intent
import androidx.fragment.app.FragmentManager
import es.deveferaz.ilerna.appsenderos.app.Constantes
import es.deveferaz.ilerna.appsenderos.database.relations.DetalleSendero
import es.deveferaz.ilerna.appsenderos.ui.adapters.SenderoListener
import es.deveferaz.ilerna.appsenderos.ui.addSendero.AddSenderoActivity
import es.deveferaz.ilerna.appsenderos.ui.dialogs.SenderoDialog

class SenderoListenerImpl (
    val context: Context,
    val viewModel: HomeViewModel,
    private val fragmentManager: FragmentManager
) : SenderoListener {

    override fun edit(detalleSendero: DetalleSendero) {
        val intent = Intent(context, AddSenderoActivity::class.java)
        intent.putExtra(Constantes.SENDERO, detalleSendero)
        context.startActivity(intent)
    }

    override fun open(url: String) {
        val browserIntent = Intent(context, MapsFragment::class.java)
        context.startActivity(browserIntent)
    }

    override fun addFavorito(id: Long) {
        viewModel.addFav(id)
    }

    override fun delFavorito(id: Long) {
        viewModel.delFav(id)
    }

    override fun addCompletado(id: Long) {
        viewModel.addCompletado(id)
    }

    override fun delCompletado(id: Long) {
        viewModel.delCompletado(id)
    }

    override fun details(detalleSendero: DetalleSendero) {
        val senderoFragment = SenderoDialog(detalleSendero)
        senderoFragment.show(fragmentManager, Constantes.SENDERO)
    }

}
package es.deveferaz.ilerna.appsenderos.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import es.deveferaz.ilerna.appsenderos.R
import es.deveferaz.ilerna.appsenderos.app.App
import es.deveferaz.ilerna.appsenderos.databinding.ActivityHomeBinding
import es.deveferaz.ilerna.appsenderos.databinding.NavHeaderHomeBinding
import es.deveferaz.ilerna.appsenderos.ui.addSendero.AddSenderoActivity
import es.deveferaz.ilerna.appsenderos.ui.sign.LoginActivity

class HomeActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarHome.toolbar)
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_content_home)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_favs, R.id.nav_completados
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_home)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onStart() {
        super.onStart()
        binding.appBarHome.fab.setOnClickListener {
            startActivity(Intent(this, AddSenderoActivity::class.java))
        }
        val navViewBinding = NavHeaderHomeBinding.bind(binding.navView.getHeaderView(0))
        navViewBinding.usuario.text = App.getUsuario()!!.nombre

        navViewBinding.logout.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            App.clear()
            finish()
        }
    }
}
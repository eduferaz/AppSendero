package es.deveferaz.ilerna.appsenderos.ui.sign

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import es.deveferaz.ilerna.appsenderos.R
import es.deveferaz.ilerna.appsenderos.app.App
import es.deveferaz.ilerna.appsenderos.database.entities.Usuario
import es.deveferaz.ilerna.appsenderos.databinding.ActivityLoginBinding
import es.deveferaz.ilerna.appsenderos.ui.HomeActivity

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    private val viewModel: UsuarioViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()

        val usuario = App.getUsuario()
        usuario?.let {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }

        binding.login.setOnClickListener {
            login()
        }
        binding.registro.setOnClickListener {
            registro()
        }
    }

    private fun login() {
        val nombre = binding.tieUsername.text.toString()
        val password = binding.tiePassword.text.toString()
        if (nombre.isBlank() || password.isBlank()) {
            App.showSnackbar(binding.root, getString(R.string.rellenar_campos))
            return
        }
        viewModel.login(Usuario(nombre, password)).observe(this) {
            if (!it.result) {
                App.showSnackbar(binding.root, it.msg)
            } else {
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
        }

    }

    private fun registro() {
        startActivity(Intent(this, RegisterActivity::class.java))
        finish()
    }
}
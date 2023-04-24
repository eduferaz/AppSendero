package es.deveferaz.ilerna.appsenderos.ui.sign

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import es.deveferaz.ilerna.appsenderos.R
import es.deveferaz.ilerna.appsenderos.app.App
import es.deveferaz.ilerna.appsenderos.database.entities.Usuario
import es.deveferaz.ilerna.appsenderos.databinding.ActivityLoginBinding
import es.deveferaz.ilerna.appsenderos.databinding.ActivityRegisterBinding
import es.deveferaz.ilerna.appsenderos.ui.HomeActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: UsuarioViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        binding.cancelar.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        binding.registro.setOnClickListener {
            registro()
        }
    }

    private fun registro() {
        val nombre = binding.tieUsername.text.toString()
        val password = binding.tiePassword.text.toString()
        val password2 = binding.tieConfirmPassword.text.toString()
        if (nombre.isBlank() || password.isBlank() || password2.isBlank()) {
            App.showSnackbar(binding.root, getString(R.string.rellenar_campos))
            return
        }

        if (password != password2) {
            App.showSnackbar(binding.root, getString(R.string.contrasenyas_no_coinciden))
            return
        }
        viewModel.save(Usuario(nombre, password)).observe(this) {
            if (!it) {
                App.showSnackbar(binding.root, getString(R.string.error_crear_usuario))
            } else {
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
        }
    }
}
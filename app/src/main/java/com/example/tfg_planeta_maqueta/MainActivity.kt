package com.example.tfg_planeta_maqueta

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DataBaseHelper
    private lateinit var emailEditText: EditText
    private lateinit var contrasenaEditText: EditText
    private lateinit var codigoAdminEditText: EditText
    private lateinit var botonUsuario: Button
    private lateinit var botonAdmin: Button
    private lateinit var textViewRegistro: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicialización de vistas
        emailEditText = findViewById(R.id.email)
        contrasenaEditText = findViewById(R.id.Contrasena)
        codigoAdminEditText = findViewById(R.id.Contrasenaadmin)
        botonUsuario = findViewById(R.id.boton_EntradaUsuario)
        botonAdmin = findViewById(R.id.boton_EntradaAdmin)
        textViewRegistro = findViewById(R.id.linkregistro)

        // Inicialización de la base de datos
        dbHelper = DataBaseHelper(this)


    // Configurar listeners
    configurarListeners()
}

    private fun configurarListeners() {
        // Login de usuario normal
        botonUsuario.setOnClickListener {
            try {
                val email = emailEditText.text.toString().trim()
                val contrasena = contrasenaEditText.text.toString().trim()

                if (email.isEmpty() || contrasena.isEmpty()) {
                    Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val esValido = dbHelper.validarUsuario(email, contrasena)

                if (esValido) {
                    Toast.makeText(this, "Bienvenido", Toast.LENGTH_SHORT).show()
                    iniciarSesionUsuario()
                    // startActivity(Intent(this, PantallaUsuario::class.java))
                } else {
                    Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this, "Error al validar credenciales", Toast.LENGTH_SHORT).show()
                Log.e("MainActivity", "Error en login", e)
            }
        }

        // Login de administrador
        botonAdmin.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val contrasena = contrasenaEditText.text.toString().trim()
            val Cod_Administrador = codigoAdminEditText.text.toString().trim()

            if (validarCamposAdmin(email, contrasena, Cod_Administrador)) {
                try {
                    val codigo = Cod_Administrador.toInt()
                    if (dbHelper.validarAdministrador(email, contrasena, Cod_Administrador)) {
                        iniciarSesionAdmin()
                    } else {
                        mostrarError("Credenciales o código incorrectos")
                    }
                } catch (e: NumberFormatException) {
                    mostrarError("El código de administrador debe ser numérico")
                }
            }
        }

        // Registro de nuevo usuario
        textViewRegistro.setOnClickListener {
            startActivity(Intent(this, PantallaRegistro::class.java))
        }
    }

    private fun validarCamposUsuario(email: String, contrasena: String): Boolean {
        return when {
            email.isEmpty() -> {
                mostrarError("Ingrese su email")
                false
            }
            contrasena.isEmpty() -> {
                mostrarError("Ingrese su contraseña")
                false
            }
            else -> true
        }
    }

    private fun validarCamposAdmin(email: String, contrasena: String, codigo: String): Boolean {
        return when {
            email.isEmpty() -> {
                mostrarError("Ingrese su email")
                false
            }
            contrasena.isEmpty() -> {
                mostrarError("Ingrese su contraseña")
                false
            }
            codigo.isEmpty() -> {
                mostrarError("Ingrese el código de administrador")
                false
            }
            else -> true
        }
    }



    private fun iniciarSesionUsuario() {
        Toast.makeText(this, "Bienvenido Usuario", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, PantallaUsuario::class.java).apply {
            putExtra("EMAIL", emailEditText.text.toString())
        }
        startActivity(intent)
        finish()
    }

    private fun iniciarSesionAdmin() {
        Toast.makeText(this, "Bienvenido Administrador", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, PantallaAdministrador::class.java).apply {
            putExtra("EMAIL", emailEditText.text.toString())
        }
        startActivity(intent)
        finish()
    }


    private fun mostrarError(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }

}
package com.example.tfg_planeta_maqueta

import android.content.Intent
import android.os.Bundle
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

        // Insertar datos de prueba (solo para desarrollo)
        insertarDatosPrueba()

        // Configurar listeners
        configurarListeners()
    }

    private fun insertarDatosPrueba() {
        // Usuario de prueba
        dbHelper.insertarUsuario(
            dni = "usuario@test.com", // Usamos el email como DNI para este caso
            nombre = "Usuario",
            apellidos = "Prueba",
            edad = 25,
            direccion = "Calle Prueba 123",
            fechaNacimiento = "1998-01-01",
            contrasena = "123456"
        )

        // Administrador de prueba
        dbHelper.insertarAdministrador(
            dni = "admin@test.com",
            nombre = "Admin",
            apellidos = "Prueba",
            codAdministrador = 9999,
            edad = 30,
            direccion = "Calle Admin 456",
            fechaNacimiento = "1993-01-01",
            contrasena = "admin123"
        )
    }

    private fun configurarListeners() {
        // Login de usuario normal
        botonUsuario.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val contrasena = contrasenaEditText.text.toString().trim()

            if (validarCamposUsuario(email, contrasena)) {
                if (dbHelper.validarUsuario(email, contrasena)) {
                    iniciarSesionUsuario()
                } else {
                    mostrarError("Credenciales incorrectas")
                }
            }
        }

        // Login de administrador
        botonAdmin.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val contrasena = contrasenaEditText.text.toString().trim()
            val codigoAdmin = codigoAdminEditText.text.toString().trim()

            if (validarCamposAdmin(email, contrasena, codigoAdmin)) {
                try {
                    val codigo = codigoAdmin.toInt()
                    if (dbHelper.validarAdministrador(email, contrasena, codigo)) {
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
package com.example.tfg_planeta_maqueta

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.tfg_planeta_maqueta.DataBaseHelper

class PantallaRegistro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantalla_registro)

        // Referencias a los campos del layout
        val dni = findViewById<EditText>(R.id.editTextDNI)
        val nombre = findViewById<EditText>(R.id.editTextNombre)
        val apellidos = findViewById<EditText>(R.id.editTextApellidos)
        val usuario = findViewById<EditText>(R.id.editTextUsuario)
        val email = findViewById<EditText>(R.id.editTextEmail)
        val fechaNacimiento = findViewById<EditText>(R.id.editTextFechaNacimiento)
        val direccion = findViewById<EditText>(R.id.editTextDireccion)
        val contrasena = findViewById<EditText>(R.id.editTextContrasena)
        val codigoAdmin = findViewById<EditText>(R.id.editTextCodigoAdmin)

        val sexoMasculino = findViewById<RadioButton>(R.id.radio_masculino)
        val sexoFemenino = findViewById<RadioButton>(R.id.radio_femenino)
        val sexoOtro = findViewById<RadioButton>(R.id.radio_otro)

        val btnRegistrar = findViewById<Button>(R.id.btnRegistrar)

        btnRegistrar.setOnClickListener {
            val dniTexto = dni.text.toString().trim()
            val nombreTexto = nombre.text.toString().trim()
            val apellidosTexto = apellidos.text.toString().trim()
            val usuarioTexto = usuario.text.toString().trim()
            val emailTexto = email.text.toString().trim()
            val fechaTexto = fechaNacimiento.text.toString().trim()
            val direccionTexto = direccion.text.toString().trim()
            val contrasenaTexto = contrasena.text.toString().trim()
            val adminCodigoTexto = codigoAdmin.text.toString().trim()
            val sexoTexto = when {
                sexoMasculino.isChecked -> "Masculino"
                sexoFemenino.isChecked -> "Femenino"
                sexoOtro.isChecked -> "Otro"
                else -> ""
            }

            // Validación básica
            if (dniTexto.isEmpty() || nombreTexto.isEmpty() || apellidosTexto.isEmpty()
                || usuarioTexto.isEmpty() || emailTexto.isEmpty() || fechaTexto.isEmpty()
                || contrasenaTexto.isEmpty() || sexoTexto.isEmpty()
            ) {
                Toast.makeText(
                    this,
                    "Por favor complete todos los campos obligatorios",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // Aquí iría la lógica de inserción en la base de datos
            val db = DataBaseHelper(this)

            val esAdmin = adminCodigoTexto.isNotEmpty() // si hay código, es admin

            val resultado: Long = if (esAdmin) {
                db.insertarAdministrador(
                    dniTexto,
                    nombreTexto,
                    apellidosTexto,
                    usuarioTexto,
                    emailTexto,
                    fechaTexto,
                    direccionTexto,
                    contrasenaTexto,
                    sexoTexto,
                    adminCodigoTexto
                )
            } else {
                db.insertarUsuario(
                    dniTexto,
                    nombreTexto,
                    apellidosTexto,
                    usuarioTexto,
                    emailTexto,
                    fechaTexto,
                    direccionTexto,
                    contrasenaTexto,
                    sexoTexto
                )
            }

            if (resultado != -1L) {  // -1 indica error en SQLite
                Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Error al registrar en la base de datos", Toast.LENGTH_SHORT).show()
            }
        }

    }

}
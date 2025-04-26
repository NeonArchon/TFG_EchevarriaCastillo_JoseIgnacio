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

        dbHelper = DataBaseHelper(this)
        dbHelper.writableDatabase

        emailEditText = findViewById(R.id.email)
        contrasenaEditText = findViewById(R.id.Contrasena)
        codigoAdminEditText = findViewById(R.id.Contrasenaadmin)
        botonUsuario = findViewById(R.id.boton_EntradaUsuario)
        botonAdmin = findViewById(R.id.boton_EntradaAdmin)
        textViewRegistro = findViewById(R.id.linkregistro)

        botonUsuario.setOnClickListener {
            val email = emailEditText.text.toString()
            val contrasena = contrasenaEditText.text.toString()

            if (dbHelper.verificarUsuario(email, contrasena)) {
                Toast.makeText(this, "Bienvenido", Toast.LENGTH_SHORT).show()
                // startActivity(Intent(this, PantallaUsuario::class.java))
            } else {
                Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
            }
        }

        botonAdmin.setOnClickListener {
            val email = emailEditText.text.toString()
            val contrasena = contrasenaEditText.text.toString()
            val codigoAdmin = codigoAdminEditText.text.toString()

            if (dbHelper.verificarAdmin(email, contrasena, codigoAdmin)) {
                Toast.makeText(this, "Bienvenido", Toast.LENGTH_SHORT).show()
                // startActivity(Intent(this, PantallaAdmin::class.java))
            } else {
                Toast.makeText(this, "Credenciales o código incorrectos", Toast.LENGTH_SHORT).show()
            }
        }

        textViewRegistro.setOnClickListener {
            val intent = Intent(this, PantallaRegistro::class.java)
            startActivity(intent)
        }
    }

}
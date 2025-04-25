package com.example.tfg_planeta_maqueta

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DataBaseHelper
    private lateinit var emailEditText: EditText
    private lateinit var contrasenaEditText: EditText
    private lateinit var codigoAdminEditText: EditText
    private lateinit var botonUsuario: Button
    private lateinit var botonAdmin: Button



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        // Log.i("DataBaseHelper", "Base de datos inicializada correctamente")

        // Inicializar la base de datos
        dbHelper = DataBaseHelper(this)
        dbHelper.writableDatabase // <- Esto dispara la creación si aún no existe

        // Enlazar elementos de layout
        emailEditText = findViewById(R.id.email)
        contrasenaEditText = findViewById(R.id.Contrasena)
        codigoAdminEditText = findViewById(R.id.Contrasenaadmin)
        botonUsuario = findViewById(R.id.boton_EntradaUsuario)
        botonAdmin = findViewById(R.id.boton_EntradaAdmin)

        //Accion de boton para logear como Usuario
        botonUsuario.setOnClickListener {
            val email = emailEditText.text.toString()
            val contrasena = contrasenaEditText.text.toString()

            if (dbHelper.verificarUsuario(email, contrasena)) {
                Toast.makeText(this, "Bienvenido", Toast.LENGTH_SHORT).show()
                // Ir a pantalla de usuario
                // startActivity(Intent(this, PantallaUsuario::class.java))
            } else {
                Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
            }
        }

        //Accion de boton para logear como Administrador
        botonAdmin.setOnClickListener {
            val email = emailEditText.text.toString()
            val contrasena = contrasenaEditText.text.toString()
            val codigoAdmin = codigoAdminEditText.text.toString()

            if (dbHelper.verificarAdmin(email, contrasena, codigoAdmin)) {
                Toast.makeText(this, "Bienvenido", Toast.LENGTH_SHORT).show()
                // Ir a pantalla de admin
                // startActivity(Intent(this, PantallaAdmin::class.java))
            } else {
                Toast.makeText(this, "Credenciales o código incorrectos", Toast.LENGTH_SHORT).show()
            }

            /*
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets


        }
        */

        }


    }



}
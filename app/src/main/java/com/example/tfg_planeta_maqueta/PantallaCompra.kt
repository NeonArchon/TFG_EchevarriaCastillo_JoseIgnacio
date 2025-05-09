package com.example.tfg_planeta_maqueta

import android.os.Bundle
import android.widget.*
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity

class PantallaCompra : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantalla_compra)

        val metodoPagoDropdown = findViewById<AutoCompleteTextView>(R.id.metodoPagoDropdown)
        val formContainer = findViewById<FrameLayout>(R.id.formContainer)
        val btnConfirmar = findViewById<Button>(R.id.btnConfirmarPago)

        // Opciones del menú desplegable
        val metodosPago = listOf("Bizzum", "Transferencia", "Tarjeta bancaria")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, metodosPago)
        metodoPagoDropdown.setAdapter(adapter)

        // Manejador de selección del método de pago
        metodoPagoDropdown.setOnItemClickListener { _, _, position, _ ->
            val seleccionado = metodosPago[position]
            val layoutId = when (seleccionado) {
                "Bizzum" -> R.layout.formulario_bizzum
                "Transferencia" -> R.layout.formulario_tranferencia
                "Tarjeta bancaria" -> R.layout.formulario_tarjeta
                else -> null
            }

            // Mostrar el formulario correspondiente
            formContainer.removeAllViews()
            layoutId?.let {
                val view = layoutInflater.inflate(it, formContainer, false)
                formContainer.addView(view)
            }
        }

        // Evento del botón de confirmación (puedes expandirlo según tu lógica)
        btnConfirmar.setOnClickListener {
            Toast.makeText(this, "Procesando pago...", Toast.LENGTH_SHORT).show()
        }
    }

    }
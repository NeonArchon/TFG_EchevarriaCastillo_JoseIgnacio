package com.example.tfg_planeta_maqueta

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    private lateinit var dataBaseHelper: DataBaseHelper

    @Before
    fun seTup(){

        val context = ApplicationProvider.getApplicationContext<Context>()
        dataBaseHelper = DataBaseHelper(context, useMemory = true)
        dataBaseHelper.writableDatabase

    }

    @Test
    fun addition_isCorrect() {

        // Insertar persona como usuario
        dataBaseHelper.insertarPersona("11111111X", "Ana", "Torres", "2000-01-01", 24, "Calle 1", "Femenino")
        dataBaseHelper.insertarUsuario("11111111X")

        // Insertar persona como administrador
        dataBaseHelper.insertarPersona("22222222Y", "Luis", "Fernández", "1980-02-02", 44, "Calle 2", "Masculino")
        dataBaseHelper.insertarAdministrador("22222222Y")

        // Verificar si se insertaron correctamente
        val db = dataBaseHelper.readableDatabase


        val cursorUsuario = db.rawQuery("SELECT * FROM Usuario WHERE DNI = ?", arrayOf("11111111X"))
        assertTrue(cursorUsuario.moveToFirst()) // usuario encontrado
        cursorUsuario.close()

        val cursorAdmin = db.rawQuery("SELECT * FROM Administrador WHERE DNI = ?", arrayOf("22222222Y"))
        assertTrue(cursorAdmin.moveToFirst()) // admin encontrado
        cursorAdmin.close()

    }

}
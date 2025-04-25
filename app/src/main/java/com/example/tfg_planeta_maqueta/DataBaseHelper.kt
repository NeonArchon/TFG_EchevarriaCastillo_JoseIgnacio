package com.example.tfg_planeta_maqueta

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DataBaseHelper (context: Context, useMemory: Boolean = false) : SQLiteOpenHelper (context, if (useMemory) null else "Planeta_Maqueta.db", null,1){

    override fun onCreate(db: SQLiteDatabase?) {
        db?.let { database ->
            try {
                // Ejecutar las sentencias SQL dentro de una transacción
                database.beginTransaction()

                // Tabla de Personas (corregí un error de sintaxis: faltaba coma después de "direccion")
                database.execSQL("""
                    CREATE TABLE Persona (
                        dni TEXT PRIMARY KEY,
                        nombre TEXT NOT NULL,
                        apellido TEXT NOT NULL,
                        fechanacimiento TEXT,
                        edad INTEGER,
                        direccion TEXT,
                        sexo TEXT
                    )
                """.trimIndent())

                // Tabla de usuarios
                database.execSQL("""
                    CREATE TABLE Usuario (
                        Id_Usuario INTEGER PRIMARY KEY AUTOINCREMENT,
                        DNI TEXT,
                        FOREIGN KEY (DNI) REFERENCES Persona(DNI)
                        )
                """.trimIndent())

                // Tabla de administradores (corregí para que Id_Admin sea AUTOINCREMENT)
                database.execSQL("""
                    CREATE TABLE Administrador (
                        Id_Admin INTEGER PRIMARY KEY,
                        DNI TEXT,
                        FOREIGN KEY (DNI) REFERENCES Persona(DNI)
                    )
                """.trimIndent())

                // Marcar la transacción como exitosa
                database.setTransactionSuccessful()
                Log.i("DataBaseHelper", "Tablas creadas exitosamente")
            } catch (e: Exception) {
                Log.i("DatabaseHelper", "Error al crear tablas: ${e.message}")
            } finally {
                // Finalizar la transacción
                database.endTransaction()
            }
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.let { database ->
            try {
                database.beginTransaction()
                database.execSQL("DROP TABLE IF EXISTS Administrador")
                database.execSQL("DROP TABLE IF EXISTS Usuario")
                database.execSQL("DROP TABLE IF EXISTS Persona")
                database.setTransactionSuccessful()
                onCreate(database)
            } catch (e: Exception) {
                Log.e("DatabaseHelper", "Error en onUpgrade: ${e.message}")
            } finally {
                database.endTransaction()
            }
        }
    }

    fun insertarPersona(dni: String, nombre: String, apellido: String, fechaNacimiento: String?, edad: Int?, direccion: String?, sexo: String?) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("dni", dni)
            put("nombre", nombre)
            put("apellido", apellido)
            put("fechanacimiento", fechaNacimiento)
            put("edad", edad)
            put("direccion", direccion)
            put("sexo", sexo)
        }
        db.insert("Persona", null, values)
    }

    fun insertarUsuario(dni: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("DNI", dni)
        }
        db.insert("Usuario", null, values)
    }

    fun insertarAdministrador(dni: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("DNI", dni)
        }
        db.insert("Administrador", null, values)
    }

    fun validarUsuario(email: String, contrasena: String): Boolean {
        val db = this.readableDatabase
        val query = """
        SELECT * FROM Persona 
        WHERE email = ? AND contrasena = ? 
        AND dni IN (SELECT dni FROM Usuario)
    """.trimIndent()

        val cursor = db.rawQuery(query, arrayOf(email, contrasena))
        val result = cursor.count > 0
        cursor.close()
        return result
    }

    fun validarAdministrador(email: String, contrasena: String, idAdmin: Int): Boolean {
        val db = this.readableDatabase
        val query = """
        SELECT * FROM Persona 
        WHERE email = ? AND contrasena = ? 
        AND dni IN (
            SELECT dni FROM Administrador WHERE Id_Admin = ?
        )
        
    """.trimIndent()

        val cursor = db.rawQuery(query, arrayOf(email, contrasena, idAdmin.toString()))
        val result = cursor.count > 0
        cursor.close()
        return result
    }

    // Funcion para verificar usuario
    fun verificarUsuario(email: String, contrasena: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Usuarios WHERE email = ? AND contrasena = ?", arrayOf(email, contrasena))
        val existe = cursor.count > 0
        cursor.close()
        return existe
    }

    // Funcion para verificar Administrador
    fun verificarAdmin(email: String, contrasena: String, codigo: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Administradores WHERE email = ? AND contrasena = ? AND codigo = ?", arrayOf(email, contrasena, codigo))
        val existe = cursor.count > 0
        cursor.close()
        return existe
    }


}
package com.example.tfg_planeta_maqueta

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DataBaseHelper (context: Context) : SQLiteOpenHelper (context, "Planeta_Maqueta.db", null,1){

    override fun onCreate(db: SQLiteDatabase?) {
        db?.let { database ->
            try {
                // Ejecutar las sentencias SQL dentro de una transacción
                database.beginTransaction()

                // Tabla de Personas (corregí un error de sintaxis: faltaba coma después de "direccion")
                database.execSQL("""
                    CREATE TABLE Persona (
                        dni TEXT PRIMARY KEY,
                        nombr TEXT NOT NULL,
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
                        Id_Admin INTEGER PRIMARY KEY AUTOINCREMENT,
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

}
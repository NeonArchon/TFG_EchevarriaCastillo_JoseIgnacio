package com.example.tfg_planeta_maqueta

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DataBaseHelper (context: Context, useMemory: Boolean = false) : SQLiteOpenHelper (context, if (useMemory) null else "Planeta_Maqueta.db", null,9){

    override fun onCreate(db: SQLiteDatabase?) {
        db?.let { database ->
            try {
                // Ejecutar las sentencias SQL dentro de una transacción


                // Tabla de usuarios
                database.execSQL("""
                    CREATE TABLE Usuario (
                    DNI TEXT,
                    Nombre TEXT NOT NULL,
                    Apellidos TEXT NOT NULL,
                    Email TEXT UNIQUE NOT NULL,
                    Usuario TEXT UNIQUE NOT NULL,
                    ID_Usuario integer PRIMARY KEY AUTOINCREMENT,
                    Edad INTEGER,
                    Direccion TEXT,
                    Fecha_Nacimiento TEXT,
                    Contrasena TEXT NOT NULL,
                    Sexo TEXT
                        )
                """.trimIndent())

                // Tabla de administradores
                database.execSQL("""
                    CREATE TABLE Administrador (
                    DNI TEXT PRIMARY KEY,
                    Nombre TEXT NOT NULL,
                    Apellidos TEXT NOT NULL,
                    Email TEXT UNIQUE NOT NULL,
                    Usuario TEXT UNIQUE NOT NULL,
                    Cod_Administrador INTEGER,
                    Edad INTEGER,
                    Direccion TEXT,
                    Fecha_Nacimiento TEXT,
                    Contrasena TEXT NOT NULL,
                    Sexo TEXT
                    )
                """.trimIndent())

                // Tabla de Productos
                database.execSQL("""
                    CREATE TABLE Productos (
                        Id_Producto INTEGER PRIMARY KEY,
                        Nombre TEXT NOT NULL,
                        Tipo TEXT,
                        Stock INTEGER DEFAULT 0,
                        Descripcion TEXT,
                        Precio REAL NOT NULL,
                        Ofertas TEXT,
                        Fotos TEXT
                    )
                """.trimIndent())

                // Tabla de Compra
                database.execSQL("""
                    CREATE TABLE Compra (
                        Id_Producto INTEGER,
                        Id_Usuario INTEGER,
                        Fecha_Compra TEXT DEFAULT CURRENT_TIMESTAMP,
                        Cantidad INTEGER DEFAULT 1,
                        PRIMARY KEY (Id_Producto, Id_Usuario, Fecha_Compra),
                        FOREIGN KEY (Id_Producto) REFERENCES Productos(Id_Producto),
                        FOREIGN KEY (Id_Usuario) REFERENCES Usuario(ID_Usuario)
                    )
                """.trimIndent())

                // Tabla de Vende
                database.execSQL("""
                    CREATE TABLE Vende (
                        Id_Vendedor INTEGER,
                        Id_Comprador INTEGER,
                        Fecha_Venta TEXT DEFAULT CURRENT_TIMESTAMP,
                        PRIMARY KEY (Id_Vendedor, Id_Comprador, Fecha_Venta),
                        FOREIGN KEY (Id_Vendedor) REFERENCES Usuario(ID_Usuario),
                        FOREIGN KEY (Id_Comprador) REFERENCES Usuario(ID_Usuario)
                    )
                """.trimIndent())

                insertarDatosPrueba(database)

            }catch(e: Exception){
                Log.i("DataBaseHelper", "FALLO INSERCCION");
            } catch (e: Exception) {
                Log.i("DatabaseHelper", "Error al crear tablas: ${e.message}")
            }

        }

    }

    //Función onUpgrade

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.let { database ->
            try {

                database.execSQL("DROP TABLE IF EXISTS Administrador")
                database.execSQL("DROP TABLE IF EXISTS Usuario")
                database.execSQL("DROP TABLE IF EXISTS Persona")
                database.execSQL("DROP TABLE IF EXISTS Compra")
                database.execSQL("DROP TABLE IF EXISTS Vende")

                onCreate(database)

            } catch (e: Exception) {
                Log.e("DatabaseHelper", "Error en onUpgrade: ${e.message}")
            }
        }
    }

    // Insertar un nuevo usuario
    fun insertarUsuario(
        dni: String,
        nombre: String,
        apellidos: String,
        usuario: String,
        email: String,
        fechaNacimiento: String?,
        direccion: String?,
        contrasena: String,
        sexo: String
    ): Boolean  {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("DNI", dni)
            put("Nombre", nombre)
            put("Apellidos", apellidos)
            put("Usuario", usuario)
            put("Email", email)
            put("Direccion", direccion)
            put("Fecha_Nacimiento", fechaNacimiento)
            put("Contrasena", contrasena)
            put("Sexo", sexo)
        }
        val result = db.insert("Usuario", null, values)
        db.close() // Cierra la conexión
        return result != -1L
    }



    // Insertar un nuevo administrador
    fun insertarAdministrador(
        dni: String,
        nombre: String,
        apellidos: String,
        usuario: String,
        email: String,
        fechaNacimiento: String?,
        direccion: String?,
        contrasena: String,
        sexo: String,
        codAdministrador: String
    ): Boolean  {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("DNI", dni)
            put("Nombre", nombre)
            put("Apellidos", apellidos)
            put("Usuario", usuario)
            put("Email", email)
            put("Direccion", direccion)
            put("Fecha_Nacimiento", fechaNacimiento)
            put("Contrasena", contrasena)
            put("Sexo", sexo)
            put("Cod_Administrador", codAdministrador.toIntOrNull() ?: 0)
        }
        val result = db.insert("Administrador", null, values)
        db.close() // Cierra la conexión
        return result != -1L
    }


    // Insertar un nuevo producto
    fun insertarProducto(
        nombre: String,
        tipo: String?,
        stock: Int = 0,
        descripcion: String?,
        precio: Double,
        ofertas: Boolean?,
        fotos: String?
    ): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("Nombre", nombre)
            put("Tipo", tipo)
            put("Stock", stock)
            put("Descripcion", descripcion)
            put("Precio", precio)
            put("Ofertas", ofertas)
            put("Fotos", fotos)
        }
        val result = db.insert("Productos", null, values)
        db.close() // Cierra la conexión
        return result
    }

    // Validar usuario
    fun validarUsuario(email: String, contraseña: String): Boolean {
        val db = this.readableDatabase
        var cursor: Cursor? = null
        return try {
            val query = "SELECT * FROM Usuario WHERE Email = ? AND Contrasena = ?"
            cursor = db.rawQuery(query, arrayOf(email, contraseña))
            cursor.count > 0
        } finally {
            cursor?.close()
            db.close()
        }
    }


    //validar administrador
    fun validarAdministrador(email: String, contraseña: String, codigoAdmin: String): Boolean {
        val db = this.readableDatabase
        var cursor: Cursor? = null
        return try {
            val query = "SELECT * FROM Administrador WHERE Email = ? AND Contrasena = ? AND Cod_Administrador = ?"
            cursor = db.rawQuery(query, arrayOf(email, contraseña, codigoAdmin))
            cursor.count > 0
        } finally {
            cursor?.close()
            db.close()
        }
    }



    // Función para obtener el ID de usuario basado en DNI
    fun obtenerIdUsuario(dni: String): Int {
        val db = this.readableDatabase
        val query = "SELECT ID_Usuario FROM Usuario WHERE DNI = ?"
        val cursor = db.rawQuery(query, arrayOf(dni))
        return if (cursor.moveToFirst()) {
            cursor.getInt(0)
        } else {
            -1
        }.also { cursor.close() }
    }


    // Insertar usuario y administrador de prueba
    private fun insertarDatosPrueba(db: SQLiteDatabase) {
        try {
            db.beginTransaction()

            val valuesUsuario = ContentValues().apply {
                put("DNI", "12345678A")
                put("Nombre", "Usuario")
                put("Apellidos", "Prueba")
                put("Email", "usuario@test.com")
                put("Edad", 25)
                put("Direccion", "Calle Prueba 123")
                put("Fecha_Nacimiento", "1998-01-01")
                put("Contrasena", "password123")
            }
            db.insert("Usuario", null, valuesUsuario)

            val valuesAdmin = ContentValues().apply {
                put("DNI", "87654321Z")
                put("Nombre", "Admin")
                put("Apellidos", "Prueba")
                put("Email", "admin@test.com")
                put("Cod_Administrador", 9999)
                put("Edad", 30)
                put("Direccion", "Calle Admin 456")
                put("Fecha_Nacimiento", "1993-01-01")
                put("Contrasena", "admin123")
            }
            db.insert("Administrador", null, valuesAdmin)

            db.setTransactionSuccessful()
        } catch (e: Exception) {
            Log.e("DatabaseHelper", "Error insertando datos de prueba: ${e.message}")
        } finally {
            db.endTransaction()
        }
    }

    // Obtener usuario por Email
    fun obtenerUsuarioPorEmail(email: String): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM Usuario WHERE Email = ?", arrayOf(email))
    }

    // Verificar si un Email ya existe
    fun existeEmail(email: String, esAdmin: Boolean = false): Boolean {
        val db = this.readableDatabase
        val tabla = if (esAdmin) "Administrador" else "Usuario"
        val cursor = db.rawQuery("SELECT 1 FROM $tabla WHERE Email = ?", arrayOf(email))
        val existe = cursor.count > 0
        cursor.close()
        return existe
    }

    // Verificar si un usuario existe
    fun verificarUsuario(context: Context, dni: String, contrasena: String): Boolean {
        val dbHelper = DataBaseHelper(context)  // Pasamos el contexto recibido
        return dbHelper.validarUsuario(dni, contrasena)
    }

    // Verificar si un administrador existe
    fun verificarAdministrador(context: Context, dni: String, contrasena: String, Cod_Administrador: String): Boolean {
        val dbHelper = DataBaseHelper(context)  // Pasamos el contexto recibido
        return dbHelper.validarAdministrador(dni, contrasena, Cod_Administrador)
    }

}
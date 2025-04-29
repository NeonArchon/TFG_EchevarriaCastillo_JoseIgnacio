package com.example.tfg_planeta_maqueta

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DataBaseHelper (context: Context, useMemory: Boolean = false) : SQLiteOpenHelper (context, if (useMemory) null else "Planeta_Maqueta.db", null,2){

    override fun onCreate(db: SQLiteDatabase?) {
        db?.let { database ->
            try {
                // Ejecutar las sentencias SQL dentro de una transacción
                database.beginTransaction()

                // Tabla de usuarios
                database.execSQL("""
                    CREATE TABLE Usuario (
                        DNI TEXT,
                        Nombre TEXT NOT NULL,
                        Apellidos TEXT NOT NULL,
                        ID_Usuario integer PRIMARY KEY AUTOINCREMENT,
                        Edad INTEGER,
                        Direccion TEXT,
                        Fecha_Nacimiento TEXT,
                        Contrasena TEXT NOT NULL
                        )
                """.trimIndent())

                // Tabla de administradores
                database.execSQL("""
                     CREATE TABLE Administrador (
                        DNI TEXT PRIMARY KEY,
                        Nombre TEXT NOT NULL,
                        Apellidos TEXT NOT NULL,
                        Cod_Administrador INTEGER ,
                        Edad INTEGER,
                        Direccion TEXT,
                        Fecha_Nacimiento TEXT,
                        Contrasena TEXT NOT NULL
                    )
                """.trimIndent())

                // Tabla de Productos (new table)
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

                // Tabla de Compra (new table)
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

                // Tabla de Vende (new table)
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

    //Función onUpgrade

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.let { database ->
            try {
                database.beginTransaction()
                database.execSQL("DROP TABLE IF EXISTS Administrador")
                database.execSQL("DROP TABLE IF EXISTS Usuario")
                database.execSQL("DROP TABLE IF EXISTS Persona")
                database.execSQL("DROP TABLE IF EXISTS Compra")
                database.execSQL("DROP TABLE IF EXISTS Vende")
                database.setTransactionSuccessful()
                onCreate(database)
            } catch (e: Exception) {
                Log.e("DatabaseHelper", "Error en onUpgrade: ${e.message}")
            } finally {
                database.endTransaction()
            }
        }
    }

    // Insertar un nuevo usuario
    fun insertarUsuario(
        dni: String,
        nombre: String,
        apellidos: String,
        edad: Int?,
        direccion: String?,
        fechaNacimiento: String?,
        contrasena: String
    ): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("DNI", dni)
            put("Nombre", nombre)
            put("Apellidos", apellidos)
            put("Edad", edad)
            put("Direccion", direccion)
            put("Fecha_Nacimiento", fechaNacimiento)
            put("Contrasena", contrasena)
        }
        return db.insert("Usuario", null, values)
    }

    // Insertar un nuevo administrador
    fun insertarAdministrador(
        dni: String,
        nombre: String,
        apellidos: String,
        codAdministrador: Int,
        edad: Int?,
        direccion: String?,
        fechaNacimiento: String?,
        contrasena: String
    ): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("DNI", dni)
            put("Nombre", nombre)
            put("Apellidos", apellidos)
            put("Cod_Administrador", codAdministrador)
            put("Edad", edad)
            put("Direccion", direccion)
            put("Fecha_Nacimiento", fechaNacimiento)
            put("Contrasena", contrasena)
        }
        return db.insert("Administrador", null, values)
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
        return db.insert("Productos", null, values)
    }

    // Validar usuario (ahora con DNI y contraseña)
    fun validarUsuario(dni: String, contrasena: String): Boolean {
        val db = this.readableDatabase
        val query = """
        SELECT * FROM Usuario 
        WHERE DNI = ? AND Contrasena = ?
    """.trimIndent()

        val cursor = db.rawQuery(query, arrayOf(dni, contrasena))
        val result = cursor.count > 0
        cursor.close()
        return result
    }

    // Validar administrador (ahora con DNI, contraseña y código)
    fun validarAdministrador(dni: String, contrasena: String, codAdmin: Int): Boolean {
        val db = this.readableDatabase
        val query = """
        SELECT * FROM Administrador 
        WHERE DNI = ? AND Contrasena = ? AND Cod_Administrador = ?
    """.trimIndent()

        val cursor = db.rawQuery(query, arrayOf(dni, contrasena, codAdmin.toString()))
        val result = cursor.count > 0
        cursor.close()
        return result
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


    // Insertar usuario de prueba
    fun insertarUsuarioPrueba(context: Context) {
        val dbHelper = DataBaseHelper(context) // Usar el contexto recibido
        dbHelper.insertarUsuario(
            dni = "11111111A",
            nombre = "Usuario",
            apellidos = "Prueba",
            edad = 25,
            direccion = "Dirección prueba",
            fechaNacimiento = "1998-01-01",
            contrasena = "usuario123"
        )
    }

    // Insertar administrador de prueba
    fun insertarAdminPrueba(context: Context) {
        val dbHelper = DataBaseHelper(context) // Usar el contexto recibido
        dbHelper.insertarAdministrador(
            dni = "99999999Z",
            nombre = "Admin",
            apellidos = "Prueba",
            codAdministrador = 9999,
            edad = 40,
            direccion = "Dirección admin",
            fechaNacimiento = "1983-01-01",
            contrasena = "admin123"
        )
    }

    // Verificar si un usuario existe
    fun verificarUsuario(context: Context, dni: String, contrasena: String): Boolean {
        val dbHelper = DataBaseHelper(context)  // Pasamos el contexto recibido
        return dbHelper.validarUsuario(dni, contrasena)
    }

    // Verificar si un administrador existe
    fun verificarAdministrador(context: Context, dni: String, contrasena: String, codAdmin: Int): Boolean {
        val dbHelper = DataBaseHelper(context)  // Pasamos el contexto recibido
        return dbHelper.validarAdministrador(dni, contrasena, codAdmin)
    }

}
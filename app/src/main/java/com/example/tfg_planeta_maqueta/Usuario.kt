package com.example.tfg_planeta_maqueta

class Usuario (
    dni: String,
    nombre: String,
    apellido: String,
    fechaNacimiento: String,
    edad: Int,
    direccion: String,
    sexo: Sexo,
    val idUsuario: Int,
    val NombreUsuario: String,
) : Persona(dni, nombre, apellido, fechaNacimiento, edad, direccion, sexo) {

    override fun toString(): String {
        return "Usuario(idUsuario=$idUsuario, apodo='$NombreUsuario')"
    }
}
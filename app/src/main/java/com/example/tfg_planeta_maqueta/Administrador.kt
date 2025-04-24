package com.example.tfg_planeta_maqueta

class Administrador (
    dni: String,
    nombre: String,
    apellido: String,
    fechaNacimiento: String,
    edad: Int,
    direccion: String,
    sexo: Sexo,
    val idUsuario: Int

    ) : Persona(dni, nombre, apellido, fechaNacimiento, edad, direccion, sexo){

    override fun toString(): String {
        return "Administrador(idUsuario=$idUsuario)"
    }

}

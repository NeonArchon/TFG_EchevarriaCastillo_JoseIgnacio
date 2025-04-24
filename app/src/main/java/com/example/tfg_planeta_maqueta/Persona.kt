package com.example.tfg_planeta_maqueta

open class Persona(
    val dni: String,
    val nombre: String,
    val apellido: String,
    val fechaNacimiento: String,
    val edad: Int,
    val direccion: String,
    val sexo: Sexo

)
{
 // funciones

    override fun toString(): String {
        return "Persona(dni='$dni', nombre='$nombre', apellido='$apellido', fechaNacimiento='$fechaNacimiento', edad=$edad, direccion='$direccion', sexo=$sexo)"
    }
}
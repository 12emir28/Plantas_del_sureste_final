package com.example.plantas.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlantaDB(
    @SerialName("id")               val id              : Long   = 0,
    @SerialName("nombre_cientifico") val nombreCientifico: String = "",
    @SerialName("nombre_comun")      val nombreComun     : String = "",
    @SerialName("familia")           val familia         : String = "",
    @SerialName("origen")            val origen          : String = "",
    @SerialName("imagen_url")        val imagenUrl       : String = "",
    @SerialName("descripcion_uso")   val descripcionUso  : String = ""
)

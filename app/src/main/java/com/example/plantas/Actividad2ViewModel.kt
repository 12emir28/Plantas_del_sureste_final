package com.example.plantas

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class Actividad2ViewModel : ViewModel() {
    // Almacenamos el ID final
    private val _videoId = MutableLiveData<String>("akr7WXdpeAM")
    val videoId: LiveData<String> = _videoId

    private val _videoTitle = MutableLiveData<String>("Video de la planta")
    val videoTitle: LiveData<String> = _videoTitle

    /**
     * Función experta: Puedes pasarle el link completo y ella extrae el ID
     * Soporta: youtube.com/watch?v=ID, youtu.be/ID y youtube.com/shorts/ID
     */
    fun setVideoFromUrl(url: String) {
        val extractedId = when {
            url.contains("v=") -> url.substringAfter("v=").substringBefore("&")
            url.contains("youtu.be/") -> url.substringAfter("youtu.be/")
            url.contains("shorts/") -> url.substringAfter("shorts/").substringBefore("?")
            else -> url // Si ya es un ID, lo deja igual
        }
        _videoId.value = extractedId
    }
}
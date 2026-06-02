package com.example.plantas.data

import android.util.Log
import io.github.jan.supabase.postgrest.postgrest

class PlantasRepository {
    private val db = SupabaseClient.client.postgrest

    suspend fun getPlants(): List<PlantaDB> {
        return try {
            Log.d("PlantasRepo", "Conectando a Supabase...")
            val result = db["plantas"].select().decodeList<PlantaDB>()
            Log.d("PlantasRepo", "Plantas cargadas: ${result.size}")
            result
        } catch (e: Exception) {
            Log.e("PlantasRepo", "ERROR: ${e::class.simpleName}: ${e.message}")
            throw e
        }
    }
}

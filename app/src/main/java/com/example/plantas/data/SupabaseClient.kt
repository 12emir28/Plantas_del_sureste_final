package com.example.plantas.data

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseClient {
    private const val SUPABASE_URL = "https://gqiqmzrjnitxubevnzjl.supabase.co"
    private const val SUPABASE_KEY = "sb_publishable_-Uf69FU9oPgcKAji7wvQdg_4_A_LoZs"

    val client = createSupabaseClient(
        supabaseUrl = SUPABASE_URL,
        supabaseKey = SUPABASE_KEY
    ) {
        install(Postgrest)
    }
}

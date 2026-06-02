package com.example.plantas

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.plantas.R
import com.example.plantas.databinding.ActivityActividad2Binding

class Actividad2 : AppCompatActivity() {

    private lateinit var binding: ActivityActividad2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActividad2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SoyUnFragmentoFragment())
                .commit()
        }

        binding.activity2BottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_actividad1 -> {
                    finish()
                    true
                }
                else -> false
            }
        }
    }
}
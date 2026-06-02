package com.example.plantas.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.plantas.CatalogState
import com.example.plantas.PlantasViewModel
import com.example.plantas.ui.navigation.Screen

data class SymptomDetail(
    val name        : String,
    val description : String,
    val plantNames  : List<String>,
    val tips        : List<String>
)

val symptomDetailList = listOf(
    SymptomDetail("Dolor de estómago",
        "El dolor de estómago puede ser causado por indigestión, gases o inflamación.",
        listOf("Menta", "Achiote"),
        listOf("Evitar comidas pesadas.", "Mantenerse hidratado.", "Aplicar compresas calientes.", "Caminar suavemente.")),
    SymptomDetail("Gripe y Tos",
        "La gripe y la tos son afecciones respiratorias comunes.",
        listOf("Maguey Morado", "Menta"),
        listOf("Descansar lo suficiente.", "Mantenerse hidratado.", "Ventilar los espacios.", "Evitar contacto con vulnerables.")),
    SymptomDetail("Insomnio",
        "El insomnio puede ser consecuencia del estrés, ansiedad o malos hábitos de sueño.",
        listOf("Menta", "Achiote"),
        listOf("Horarios fijos de sueño.", "Evitar pantallas 1 hora antes.", "Ambiente oscuro y tranquilo.", "Reducir cafeína.")),
    SymptomDetail("Inflamación",
        "La inflamación es la respuesta natural del cuerpo ante lesiones o irritaciones.",
        listOf("Maguey Morado", "Sábila"),
        listOf("Aplicar frío las primeras 24 horas.", "Elevar la extremidad.", "Evitar actividad física intensa.", "Consultar médico si persiste.")),
    SymptomDetail("Estrés y Ansiedad",
        "El estrés crónico puede afectar gravemente la salud.",
        listOf("Menta"),
        listOf("Respiración profunda.", "Reducir azúcar.", "Actividad física moderada.", "Actividades de bienestar.")),
    SymptomDetail("Heridas Leves",
        "Las heridas superficiales pueden tratarse con plantas cicatrizantes.",
        listOf("Sábila", "Maguey Morado"),
        listOf("Limpiar con agua y jabón.", "No cubrir heridas pequeñas.", "Evitar manos sucias.", "Vigilar signos de infección."))
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SymptomDetailScreen(
    navController : NavController,
    viewModel     : PlantasViewModel = viewModel()
) {
    val symptom      by viewModel.selectedSymptom.collectAsState()
    val catalogState by viewModel.catalogState.collectAsState()

    // Busca en la BD plantas cuyos nombre_comun contengan alguno de los nombres del síntoma
    val plantasEnBD = remember(catalogState, symptom) {
        val allPlants = (catalogState as? CatalogState.Success)?.plants ?: emptyList()
        allPlants.filter { planta ->
            symptom.plantNames.any { nombre ->
                planta.nombreComun.contains(nombre, ignoreCase = true) ||
                nombre.contains(planta.nombreComun, ignoreCase = true)
            }
        }
    }

    // Plantas hardcodeadas como fallback
    val plantasHardcoded = com.example.plantas.plantDetailList.filter { it.name in symptom.plantNames }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            TopAppBar(
                title = { Text(symptom.name, style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, "Regresar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
            )
        }

        item {
            Card(modifier = Modifier.padding(16.dp).fillMaxWidth(),
                shape  = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Descripción", style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.height(8.dp))
                    Text(symptom.description, style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer, lineHeight = 22.sp)
                }
            }
        }

        item {
            Text("Plantas Recomendadas", style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
        }

        // Mostrar plantas de la BD si hay, si no, las hardcodeadas
        if (plantasEnBD.isNotEmpty()) {
            items(plantasEnBD) { planta ->
                ElevatedCard(
                    onClick = {
                        viewModel.selectPlantFromDB(planta)
                        navController.navigate(Screen.Detail.route)
                    },
                    modifier  = Modifier.padding(horizontal = 16.dp, vertical = 4.dp).fillMaxWidth(),
                    shape     = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp)) {
                    Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                        AsyncImage(model = planta.imagenUrl, contentDescription = planta.nombreComun,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.width(90.dp).fillMaxHeight())
                        Column(modifier = Modifier.weight(1f).padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(planta.nombreComun, style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold)
                            Text(planta.nombreCientifico, style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary, fontStyle = FontStyle.Italic)
                            Text(planta.descripcionUso, style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 2)
                        }
                        Icon(Icons.Filled.ChevronRight, null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.align(Alignment.CenterVertically).padding(end = 8.dp))
                    }
                }
            }
        } else {
            // Fallback con hardcodeadas
            items(plantasHardcoded) { plant ->
                ElevatedCard(
                    onClick = {
                        viewModel.selectPlant(plant)
                        navController.navigate(Screen.Detail.route)
                    },
                    modifier  = Modifier.padding(horizontal = 16.dp, vertical = 4.dp).fillMaxWidth(),
                    shape     = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp)) {
                    Row(modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        AsyncImage(model = plant.imageUrl, contentDescription = plant.name,
                            contentScale = ContentScale.Crop, modifier = Modifier.size(72.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(plant.name, style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold)
                            Text(plant.properties.take(60) + "…",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Icon(Icons.Filled.ChevronRight, null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }

        item {
            Spacer(Modifier.height(16.dp))
            Text("Consejos Naturales", style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
            Card(modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth(),
                shape  = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer)) {
                Column(modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    symptom.tips.forEach { tip ->
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.Top) {
                            Icon(Icons.Filled.CheckCircle, null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp).padding(top = 2.dp))
                            Text(tip, style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 20.sp)
                        }
                    }
                }
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}

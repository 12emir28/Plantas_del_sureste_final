package com.example.plantas.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.plantas.PlantasViewModel
import com.example.plantas.ui.navigation.Screen
import com.example.plantas.ui.screens.symptomDetailList

data class Symptom(
    val icon   : ImageVector,
    val name   : String,
    val plants : String
)

val symptomList = listOf(
    Symptom(Icons.Filled.Sick,           "Dolor de estómago",  "Manzanilla, Menta, Jengibre"),
    Symptom(Icons.Filled.Thermostat,     "Gripe y Tos",        "Gordolobo, Eucalipto, Miel"),
    Symptom(Icons.Filled.Bedtime,        "Insomnio",           "Zapote blanco, Flor de Azahar"),
    Symptom(Icons.Filled.PersonalInjury, "Inflamación",        "Cúrcuma, Árnica, Sábila"),
    Symptom(Icons.Filled.Psychology,     "Estrés y Ansiedad",  "Valeriana, Pasiflora, Toronjil"),
    Symptom(Icons.Filled.Healing,        "Heridas Leves",      "Caléndula, Tepezcohuite"),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SymptomsScreen(
    navController : NavController,
    viewModel     : PlantasViewModel = viewModel()
) {
    Column(modifier = Modifier.fillMaxSize()) {

        TopAppBar(
            title = { Text("Síntomas", style = MaterialTheme.typography.titleLarge) },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow
            )
        )

        SearchBarField(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
            Text(
                text       = "¿Qué te duele hoy?",
                style      = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text  = "Selecciona una categoría para ver remedios naturales",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(Modifier.height(8.dp))

        LazyVerticalGrid(
            columns               = GridCells.Fixed(2),
            contentPadding        = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement   = Arrangement.spacedBy(10.dp),
            modifier              = Modifier.weight(1f)
        ) {
            items(symptomList) { symptom ->
                SymptomCard(
                    symptom = symptom,
                    onClick = {
                        val detail = symptomDetailList.find { it.name == symptom.name }
                        if (detail != null) {
                            viewModel.selectSymptom(detail)
                            navController.navigate(Screen.SymptomDetail.route)
                        }
                    }
                )
            }
            item { PromoBanner() }
            item { Spacer(Modifier.height(8.dp)) }
        }
    }
}

@Composable
fun SymptomCard(symptom: Symptom, onClick: () -> Unit) {
    ElevatedCard(
        onClick   = onClick,
        modifier  = Modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Surface(
                shape    = RoundedCornerShape(12.dp),
                color    = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector        = symptom.icon,
                        contentDescription = null,
                        tint               = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier           = Modifier.size(26.dp)
                    )
                }
            }
            Text(
                text       = symptom.name,
                style      = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color      = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text       = symptom.plants,
                style      = MaterialTheme.typography.bodySmall,
                color      = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium,
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
fun PromoBanner() {
    ElevatedCard(
        modifier  = Modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(28.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp),
        colors    = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            // ── Imagen arriba ──
            AsyncImage(
                model              = "https://lh3.googleusercontent.com/aida-public/AB6AXuDddgLcaUIRsNjS0mhXodb-P_jmg31POP6q-jfR5oR8JBjPWT23dQ4Q0dP59Q4tCW9Yevj2H2J3U55rCw7jYg_AifHLehDou_CW8tcJYd4MfbCLO71Wz32f0NN3khrQX1g19chWhXiIYB2q4vK2ZZ4Ei6bFr-i4M6jFK5A0nMLQQbcyQVQTr4G3LP92Jr9jW-3MAye4LLShdttxALBv_ILoE4HByoHB7JqMVMtOK9wGqpd60Fwlx3rJkFTmg48ODRhM2WXqFKPUSWc",
                contentDescription = "Eucalipto",
                contentScale       = ContentScale.Crop,
                modifier           = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
            )

            // ── Texto abajo ──
            Column(
                modifier            = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text       = "Planta del mes: Eucalipto",
                    style      = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color      = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text       = "Ideal para problemas respiratorios de temporada en el sureste mexicano.",
                    style      = MaterialTheme.typography.bodySmall,
                    color      = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.75f),
                    lineHeight = 18.sp
                )
                Spacer(Modifier.height(4.dp))
                FilledTonalButton(
                    onClick        = {},
                    shape          = RoundedCornerShape(50),
                    modifier       = Modifier.height(36.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    colors         = ButtonDefaults.filledTonalButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor   = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                ) {
                    Text("Ver Detalles", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
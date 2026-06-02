package com.example.plantas.ui.screens

import android.content.Intent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.plantas.Actividad2
import com.example.plantas.CatalogState
import com.example.plantas.PlantasViewModel
import com.example.plantas.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: PlantasViewModel = viewModel()
) {
    val scrollState  = rememberScrollState()
    val context      = LocalContext.current
    val catalogState by viewModel.catalogState.collectAsState()

    // Planta del día: una aleatoria de la BD (cambia al recomponer)
    val plantaDelDia = remember(catalogState) {
        (catalogState as? CatalogState.Success)?.plants?.randomOrNull()
    }
    val totalPlantas = (catalogState as? CatalogState.Success)?.plants?.size ?: 0

    Column(modifier = Modifier.verticalScroll(scrollState)) {

        // ── Top App Bar ──
        TopAppBar(
            title = {
                Row(verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Surface(shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.primaryContainer) {
                        Icon(Icons.Filled.Eco, null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(8.dp))
                    }
                    Text("Plantas del Sureste", style = MaterialTheme.typography.titleLarge)
                }
            },
            actions = {
                IconButton(onClick = { context.startActivity(Intent(context, Actividad2::class.java)) }) {
                    Icon(Icons.Filled.VideoLibrary, "Ver Videos")
                }
                IconButton(onClick = {}) { Icon(Icons.Filled.AccountCircle, "Perfil") }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
        )

        SearchBarField(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))

        // ── Categorías ──
        SectionHeader(title = "Categorías Populares", actionLabel = "Ver todas")
        CategoryRow(navController = navController, viewModel = viewModel)

        // ── Planta del Día ──
        SectionHeader(title = "Planta del Día")
        if (plantaDelDia != null) {
            PlantOfTheDayCard(
                name        = plantaDelDia.nombreComun,
                sciName     = plantaDelDia.nombreCientifico,
                imageUrl    = plantaDelDia.imagenUrl,
                description = plantaDelDia.descripcionUso,
                onClick     = {
                    viewModel.selectPlantFromDB(plantaDelDia)
                    navController.navigate(Screen.Detail.route)
                }
            )
        } else {
            Box(Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        // ── Total de plantas ──
        if (totalPlantas > 0) {
            Card(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp).fillMaxWidth(),
                shape    = RoundedCornerShape(16.dp),
                colors   = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Row(modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Icon(Icons.Filled.Eco, null,
                        tint = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.size(32.dp))
                    Column {
                        Text("$totalPlantas plantas en el catálogo",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer)
                        Text("Todas las plantas del sureste mexicano",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f))
                    }
                }
            }
        }

        // ── Remedios Rápidos ──
        SectionHeader(title = "Remedios Rápidos")
        QuickRemediesGrid()

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun SearchBarField(modifier: Modifier = Modifier) {
    Surface(modifier = modifier.fillMaxWidth(), shape = RoundedCornerShape(50),
        color = MaterialTheme.colorScheme.surfaceContainerHighest) {
        Row(modifier = Modifier.padding(horizontal = 16.dp).height(56.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(Icons.Filled.Search, null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("¿Qué planta o síntoma buscas?",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
            Icon(Icons.Filled.Tune, null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun SectionHeader(title: String, actionLabel: String? = null) {
    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically) {
        Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        if (actionLabel != null) {
            TextButton(onClick = {}) {
                Text(actionLabel, color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

data class Category(val icon: ImageVector, val label: String, val filtro: String)

@Composable
fun CategoryRow(navController: NavController, viewModel: PlantasViewModel) {
    val categories = listOf(
        Category(Icons.Filled.Restaurant, "Digestión",   "Hierbas"),
        Category(Icons.Filled.Air,        "Respiración", "Hierbas"),
        Category(Icons.Filled.Bedtime,    "Sueño",       "Hierbas"),
        Category(Icons.Filled.Healing,    "Piel",        "Hierbas"),
        Category(Icons.Filled.Psychology, "Estrés",      "Hierbas"),
    )
    Row(modifier = Modifier.horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        categories.forEach { cat ->
            Column(horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(72.dp)) {
                Surface(shape = CircleShape, color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.size(64.dp), shadowElevation = 2.dp,
                    onClick = {
                        viewModel.setFilter(cat.filtro)
                        navController.navigate(Screen.Catalog.route)
                    }) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(cat.icon, cat.label,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(28.dp))
                    }
                }
                Spacer(Modifier.height(6.dp))
                Text(cat.label, style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
fun PlantOfTheDayCard(name: String, sciName: String, imageUrl: String,
                      description: String, onClick: () -> Unit) {
    ElevatedCard(onClick = onClick,
        modifier  = Modifier.padding(horizontal = 16.dp).fillMaxWidth(),
        shape     = RoundedCornerShape(28.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)) {
        Box {
            AsyncImage(model = imageUrl, contentDescription = name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth().height(200.dp))
            Surface(modifier = Modifier.align(Alignment.TopEnd).padding(12.dp),
                shape = RoundedCornerShape(50), color = MaterialTheme.colorScheme.primary) {
                Text("DESTACADA", modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.8.sp)
            }
        }
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Text(sciName, color = MaterialTheme.colorScheme.primary,
                        fontSize = 13.sp, fontStyle = FontStyle.Italic)
                }
                IconButton(onClick = {}) {
                    Icon(Icons.Outlined.FavoriteBorder, null, tint = MaterialTheme.colorScheme.primary)
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(description.take(120) + if (description.length > 120) "…" else "",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(14.dp))
            Button(onClick = onClick, modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(50)) {
                Text("Ver detalles", fontWeight = FontWeight.Bold)
                Spacer(Modifier.width(8.dp))
                Icon(Icons.Default.ArrowForward, null, modifier = Modifier.size(18.dp))
            }
        }
    }
}

data class QuickRemedy(val icon: ImageVector, val title: String, val count: String)

@Composable
fun QuickRemediesGrid() {
    val items = listOf(
        QuickRemedy(Icons.Filled.Coffee,       "Infusiones", "12 recetas"),
        QuickRemedy(Icons.Filled.Science,      "Tinturas",   "8 métodos"),
        QuickRemedy(Icons.Filled.Spa,          "Compresas",  "5 técnicas"),
        QuickRemedy(Icons.Filled.LocalFlorist, "Pomadas",    "6 recetas"),
    )
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            items.take(2).forEach { QuickTile(it, Modifier.weight(1f)) }
        }
        Spacer(Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            items.drop(2).forEach { QuickTile(it, Modifier.weight(1f)) }
        }
    }
}

@Composable
fun QuickTile(item: QuickRemedy, modifier: Modifier = Modifier) {
    Surface(modifier = modifier, shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceContainer,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Icon(item.icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(28.dp))
            Text(item.title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
            Text(item.count, style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

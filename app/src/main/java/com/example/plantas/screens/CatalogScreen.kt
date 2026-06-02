package com.example.plantas.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import com.example.plantas.data.PlantaDB
import com.example.plantas.ui.navigation.Screen

// Mantener Plant para compatibilidad con FavoritesScreen
data class Plant(val id: String, val name: String, val sciName: String,
                 val tag: String, val description: String, val imageUrl: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    navController : NavController,
    viewModel     : PlantasViewModel = viewModel()
) {
    val filters = listOf("Todas", "Hierbas", "Árboles", "Arbustos", "Endémicas")

    val catalogState by viewModel.catalogState.collectAsState()
    val searchQuery  by viewModel.searchQuery.collectAsState()
    val activeFilter by viewModel.activeFilter.collectAsState()

    val filteredPlants = remember(catalogState, searchQuery, activeFilter) {
        val base = (catalogState as? CatalogState.Success)?.plants ?: return@remember emptyList()
        var result = base
        if (searchQuery.isNotBlank()) {
            result = result.filter {
                it.nombreComun.contains(searchQuery, ignoreCase = true) ||
                it.nombreCientifico.contains(searchQuery, ignoreCase = true)
            }
        }
        result
    }

    Column(modifier = Modifier.fillMaxSize()) {

        TopAppBar(
            title = { Text("Catálogo", style = MaterialTheme.typography.titleLarge) },
            actions = {
                if (catalogState is CatalogState.Success) {
                    Text("${filteredPlants.size} plantas",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(end = 16.dp))
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
        )

        // Search bar con estado real
        CatalogSearchBar(query = searchQuery,
            onQueryChange = { viewModel.onSearchQueryChange(it) },
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))

        LazyRow(contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 8.dp)) {
            items(filters) { filter ->
                FilterChip(selected = activeFilter == filter,
                    onClick = { viewModel.setFilter(filter) },
                    label = { Text(filter) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor   = MaterialTheme.colorScheme.secondaryContainer,
                        selectedLabelColor       = MaterialTheme.colorScheme.onSecondaryContainer,
                        selectedLeadingIconColor = MaterialTheme.colorScheme.onSecondaryContainer))
            }
        }

        when (val state = catalogState) {
            is CatalogState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        CircularProgressIndicator()
                        Text("Cargando plantas…", style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
            is CatalogState.Error -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Filled.CloudOff, null,
                            tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(48.dp))
                        Text("No se pudo cargar el catálogo", style = MaterialTheme.typography.titleMedium)
                        Text(state.message, style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Button(onClick = { viewModel.loadPlants() }) { Text("Reintentar") }
                    }
                }
            }
            is CatalogState.Success -> {
                if (filteredPlants.isEmpty()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Filled.SearchOff, null, modifier = Modifier.size(48.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(Modifier.height(8.dp))
                            Text("Sin resultados", style = MaterialTheme.typography.titleMedium)
                            Text("Prueba otro filtro o búsqueda",
                                color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                } else {
                    LazyColumn(
                        contentPadding      = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(filteredPlants, key = { it.id }) { planta ->
                            PlantDBCard(planta = planta, onClick = {
                                viewModel.selectPlantFromDB(planta)
                                navController.navigate(Screen.Detail.route)
                            })
                        }
                        item { Spacer(Modifier.height(80.dp)) }
                    }
                }
            }
        }
    }
}

@Composable
fun CatalogSearchBar(query: String, onQueryChange: (String) -> Unit, modifier: Modifier = Modifier) {
    Surface(modifier = modifier.fillMaxWidth(), shape = RoundedCornerShape(50),
        color = MaterialTheme.colorScheme.surfaceContainerHighest) {
        Row(modifier = Modifier.padding(horizontal = 16.dp).height(56.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(Icons.Filled.Search, null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
            TextField(value = query, onValueChange = onQueryChange,
                placeholder = { Text("¿Qué planta buscas?",
                    color = MaterialTheme.colorScheme.onSurfaceVariant) },
                modifier   = Modifier.weight(1f), singleLine = true,
                colors     = TextFieldDefaults.colors(
                    focusedContainerColor   = androidx.compose.ui.graphics.Color.Transparent,
                    unfocusedContainerColor = androidx.compose.ui.graphics.Color.Transparent,
                    focusedIndicatorColor   = androidx.compose.ui.graphics.Color.Transparent,
                    unfocusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent))
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(Icons.Filled.Clear, null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

@Composable
fun PlantListCard(plant: Plant, onClick: () -> Unit) {
    ElevatedCard(onClick = onClick, modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp)) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            AsyncImage(model = plant.imageUrl, contentDescription = plant.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.width(112.dp).fillMaxHeight())
            Column(modifier = Modifier.weight(1f).padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(plant.tag.uppercase(), color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold, letterSpacing = 0.6.sp)
                Text(plant.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(plant.description, style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 2)
                Spacer(Modifier.height(4.dp))
                FilledTonalButton(onClick = onClick, modifier = Modifier.height(32.dp),
                    shape = RoundedCornerShape(50),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 0.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor   = MaterialTheme.colorScheme.onSecondaryContainer)) {
                    Text("Ver más", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun PlantDBCard(planta: PlantaDB, onClick: () -> Unit) {
    ElevatedCard(onClick = onClick, modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp)) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            AsyncImage(model = planta.imagenUrl, contentDescription = planta.nombreComun,
                contentScale = ContentScale.Crop,
                modifier = Modifier.width(112.dp).fillMaxHeight())
            Column(modifier = Modifier.weight(1f).padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(planta.familia.uppercase(), color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold, letterSpacing = 0.6.sp)
                Text(planta.nombreComun, style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold)
                Text(planta.nombreCientifico, style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary, fontStyle = FontStyle.Italic)
                Text(planta.descripcionUso, style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 2)
                Spacer(Modifier.height(4.dp))
                FilledTonalButton(onClick = onClick, modifier = Modifier.height(32.dp),
                    shape = RoundedCornerShape(50),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 0.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor   = MaterialTheme.colorScheme.onSecondaryContainer)) {
                    Text("Ver más", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

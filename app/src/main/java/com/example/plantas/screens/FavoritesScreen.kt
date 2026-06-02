package com.example.plantas.ui.screens
import com.example.plantas.ui.navigation.Screen
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import com.example.plantas.PlantasViewModel
import androidx.navigation.NavController

// ── Modelo de datos ──
data class FavoritePlant(
    val name        : String,
    val benefit     : String,
    val description : String,
    val imageUrl    : String,
    val category    : String
)

val favoritePlants = listOf(
    FavoritePlant(
        name        = "Aloe Vera (Sábila)",
        benefit     = "Cicatrizante y antiinflamatorio",
        description = "Ideal para tratar quemaduras leves y problemas digestivos de forma natural.",
        imageUrl    = "https://lh3.googleusercontent.com/aida-public/AB6AXuDAAEfOU1DG5oEx8xZSxdbNQur5Jefzrz1IMvvOd8Slo4cXuZChWIWjTNgphRtcsIcbRvj4_yeRpGzkyCqobtOYvZ8BxWa0enP2-NZZpv5EV0uy89kZebVDj1hzgbiEGCeHBKceRvTCD6s_-AjIdapO3-Bu-wE1-1VttCW32OcKg2CFmlCLhPszTMYliRP1dW5Cf6sL8LlRuAV9rFQva9eVIxpyJuc-SRPLbUi8Cf2dwVyCad3hf3GwsvETgCRba2pYaQkRLLTKvHs",
        category    = "Hierbas"
    ),
    FavoritePlant(
        name        = "Achiote",
        benefit     = "Digestivo y colorante natural",
        description = "Utilizado en la medicina tradicional maya para afecciones de la piel.",
        imageUrl    = "https://lh3.googleusercontent.com/aida-public/AB6AXuDxETJPSszw6vK6xnqmGREHaIn6-sHPyDqZ2NKlJHSPhwkznSkwNbfybiBvcRT9Y3L25lThn_n6EPffjwMr9xyZbByT8SvNOenm33-w9ilrNz1-WbLe7VzCkHWlvZlCyad5_J6XTC4BjtkijLYCxHqFpqfvoLBVrFTKNsJpkz6QFK2mg0WagoaHKm4XWouZlIfiNdCXFs87mrB7ZzPgM8yJ8eZajrqtjrIMSgrJTavwAd8Ro4MVdIoP_SgY1d4u15DIIRIGv76PPp8",
        category    = "Arbustos"
    ),
    FavoritePlant(
        name        = "Menta",
        benefit     = "Relajante y digestivo",
        description = "Excelente para infusiones que ayudan a la digestión y refrescan el aliento.",
        imageUrl    = "https://lh3.googleusercontent.com/aida-public/AB6AXuBpxj_34t9do9UCOxuHh3M6qgrWXf5XWPS1hgO4moC559mgGFHE7StufCsbg_ElY7SRSkt_FvTmuFVRyivH8PEU7l5PdVF1b1H2FC2hGea0QrB_i8vA4pVWZh_klamMZ4V8mSqXrBhLt2T-lx45yCSWOXq2nOuaggM61BIE730HV1Ni8X20RQZCx2vdquHwhB3xDJRX92s2ICVqWgzE9GCIVJaHggfxyVWRe4Y2BrJwWI7MwxXIlAQYZb8H3fypqAuwXzB8QuoW5ic",
        category    = "Hierbas"
    ),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen( navController : NavController,
                     viewModel     : PlantasViewModel = viewModel()) {
    val tabs     = listOf("Todas", "Hierbas", "Árboles", "Arbustos")
    var selected by remember { mutableStateOf("Todas") }
    val favorites by viewModel.favorites.collectAsState()

    val filtered = remember(selected, favorites) {
        if (selected == "Todas") favorites
        else favorites.filter { it.category == selected }
    }

    Column(modifier = Modifier.fillMaxSize()) {

        // ── Top App Bar ──
        TopAppBar(
            title = { Text("Mis Favoritas", style = MaterialTheme.typography.titleLarge) },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow
            )
        )

        // ── Tabs ──
        ScrollableTabRow(
            selectedTabIndex = tabs.indexOf(selected),
            containerColor   = MaterialTheme.colorScheme.surfaceContainerLow,
            contentColor     = MaterialTheme.colorScheme.primary,
            edgePadding      = 16.dp
        ) {
            tabs.forEachIndexed { index, tab ->
                Tab(
                    selected = selected == tab,
                    onClick  = { selected = tab },
                    text     = {
                        Text(
                            text       = tab,
                            fontWeight = FontWeight.SemiBold,
                            fontSize   = 14.sp
                        )
                    }
                )
            }
        }

        // ── Contenido ──
        if (filtered.isEmpty()) {
            // Estado vacío
            EmptyFavorites()
        } else {
            LazyColumn(
                contentPadding      = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(items = filtered, key = { it.name }) { plant ->
                    FavoritePlantCard(
                        plant    = plant,
                        onRemove = { viewModel.removeFavorite(plant.name) },
                        onVerMas = {
                            // Busca la planta en la lista completa y la selecciona
                            val detail = com.example.plantas.plantDetailList.find { it.name == plant.name }
                            if (detail != null) {
                                viewModel.selectPlant(detail)
                                navController.navigate(Screen.Detail.route)
                            }
                        }
                    )
                }
            }
        }
    }
}

// ── Componente: Favorite Plant Card ──
@Composable
fun FavoritePlantCard(
    plant    : FavoritePlant,
    onRemove : () -> Unit,
    onVerMas : () -> Unit
) {
    ElevatedCard(
        modifier  = Modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp)
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {

            // Imagen
            AsyncImage(
                model              = plant.imageUrl,
                contentDescription = plant.name,
                contentScale       = ContentScale.Crop,
                modifier           = Modifier
                    .width(112.dp)
                    .fillMaxHeight()
            )

            // Contenido
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text       = plant.name,
                    style      = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text       = plant.benefit,
                    color      = MaterialTheme.colorScheme.primary,
                    style      = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text     = plant.description,
                    style    = MaterialTheme.typography.bodySmall,
                    color    = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2
                )
                Spacer(Modifier.height(4.dp))

                // Botones
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    // Ver más
                    Button(
                        onClick        = onVerMas,
                        modifier       = Modifier.weight(1f).height(36.dp),
                        shape          = RoundedCornerShape(50),
                        contentPadding = PaddingValues(horizontal = 12.dp)
                    ) {
                        Text("Ver más", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }

                    // Eliminar de favoritos
                    FilledTonalIconButton(
                        onClick = onRemove,
                        modifier = Modifier.size(36.dp),
                        shape    = RoundedCornerShape(10.dp),
                        colors   = IconButtonDefaults.filledTonalIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor   = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Icon(
                            imageVector        = Icons.Filled.HeartBroken,
                            contentDescription = "Eliminar de favoritas",
                            modifier           = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

// ── Componente: Estado vacío ──
@Composable
fun EmptyFavorites() {
    Column(
        modifier              = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment   = Alignment.CenterHorizontally,
        verticalArrangement   = Arrangement.Center
    ) {
        Surface(
            shape  = RoundedCornerShape(50),
            color  = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.size(96.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector        = Icons.Filled.LocalFlorist,
                    contentDescription = null,
                    tint               = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier           = Modifier.size(48.dp)
                )
            }
        }
        Spacer(Modifier.height(16.dp))
        Text(
            text       = "Tu jardín está vacío",
            style      = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text  = "Explora nuestro catálogo y guarda tus plantas medicinales favoritas para verlas aquí.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        Spacer(Modifier.height(24.dp))
        Button(
            onClick = {},
            shape   = RoundedCornerShape(50)
        ) {
            Text("Explorar catálogo", fontWeight = FontWeight.Bold)
        }
    }
}
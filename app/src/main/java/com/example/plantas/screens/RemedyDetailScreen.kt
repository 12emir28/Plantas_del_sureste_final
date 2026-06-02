package com.example.plantas.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import androidx.compose.runtime.collectAsState

data class RemedyDetail(
    val title: String, val subtitle: String, val imageUrl: String,
    val benefit: String, val ingredients: List<String>, val steps: List<String>
)

val remedyDetailList = listOf(
    RemedyDetail("Infusión para la tos","Gordolobo y Eucalipto",
        "https://lh3.googleusercontent.com/aida-public/AB6AXuA6X-H-x2BokDh3S45rAkTdok9mk7B3WvP1tg4URkqP17SSInG8iQEU2SuV9AAi5Z2odiw4a1Vnp4wMf-fREluNNuekyl2yjrIGMeIZWhls41AWNDjEDjV11ctAoy38-DZrQ7ajMRizwZ9TY1Kcr0f55bRfv2mee1UUjzFYJnFxaNj3op0nqPIh1ko0IHgvw7JPGb0bpAyDoiYf5l34m-lTyFlmjJgupVnZgRqeL76KtbSs_v4wyOCxcLhYZhI_SB9FEIoXL1ny9KI",
        "Alivia la irritación de garganta y descongestiona las vías respiratorias.",
        listOf("Hojas de gordolobo","Eucalipto","Miel","Agua (1 taza)"),
        listOf("Hervir el agua.","Agregar gordolobo y eucalipto.","Reposar 10 min tapado.","Colar y endulzar con miel.")),
    RemedyDetail("Pomada de caléndula","Caléndula y Cera de abeja",
        "https://lh3.googleusercontent.com/aida-public/AB6AXuADU8Ge3xdjaN-j09FgNdfKAMQVm4AaugF1QJ1z4vHl5u3DOXzkFsLJC4SDSSxXZdwTBv5yDFU6NpnDYeU28qM89xyTrfoZyRn0SsITYIEckKevxDTccvoLCt5gTAcrBHXJoEXQ3OQgW2U0-vp_POkoHQaOLRgCdC4GHPr2gaM5KiKVEzxv_ldJK68oDMqp_rGPym8xzJniwxO7tI641byYmgLYd-7VT-3sKOg9KXsdm3ZCVHBrj269ebwVGoLGfPO7FNu3tPOiW1Q",
        "Ideal para cicatrización de heridas leves y quemaduras.",
        listOf("Flores de caléndula","Cera de abeja","Aceite de oliva","Vitamina E"),
        listOf("Macerar flores en aceite 4 semanas.","Filtrar y calentar a baño maría.","Añadir cera derretida y mezclar.","Verter en frascos.")),
    RemedyDetail("Té digestivo","Manzanilla y Menta",
        "https://lh3.googleusercontent.com/aida-public/AB6AXuAT1N4upMsgUZsaDd8QkiczOEp3m7XURyTG4-LDyGF2ITMJxZO8cemFpLTlps1J8uFBEKgwSbaPGy3nMCrrd4QeinS_UxZIKnVT0D_dwsNyai-FiJpsCjr5IUeLNQRhQcQfJ-HfaufcjQ4vU42CnWVoWjMz1KzfmyeaSKsAwP9UR0FOPbUQBcydF2tRUYIIOVmW4Zz0jSIkhFURRljQ-4ekWeInrqiUQ1XN4hmd9W9MyIWqNMWNtzqtXgcMjWhGzyK6v0RVoW6dD6g",
        "Mezcla clásica para reducir la inflamación abdominal.",
        listOf("Flores de manzanilla","Hojas de menta","Agua (1 taza)","Miel opcional"),
        listOf("Hervir una taza de agua.","Añadir manzanilla y menta.","Tapar 7 minutos.","Colar y servir.")),
    RemedyDetail("Jarabe de Buganvilla","Buganvilla y Miel",
        "https://lh3.googleusercontent.com/aida-public/AB6AXuCz7XTD5KTQ0d30sRBy65QbkRw_y1czOqY8XIGZq5eIHUvx9WE0u1NduzLY_8IofcbPtqK_LsOGrz9-WeuckfDEZBV3R1tdy5frVfiVFbkF4Xy5C6iVMRA-6jTj5LAoDN2zYrzoL9c1tl-jj0kXxZMkZcGnR0ay6ARz1yVitHYNvj5X5m8mR4gVEQzuS6z4TIdwMGMBB5ttwA_0k1YQW5_W41k-rNeU7uW92Pf0dFagcbvbtJRuX6o26ijqBFuoAy0OHRN1NJl6fkg",
        "Remedio tradicional del sureste para calmar la tos seca.",
        listOf("Flores de buganvilla","Miel de abeja","Limón","Agua (2 tazas)"),
        listOf("Hervir flores en agua 10 min.","Colar y enfriar.","Añadir miel y limón.","Tomar una cucharada cada 4 horas."))
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemedyDetailScreen(
    navController : NavController,
    viewModel     : PlantasViewModel = viewModel()
) {
    val remedy       by viewModel.selectedRemedy.collectAsState()
    val catalogState by viewModel.catalogState.collectAsState()

    // Busca en la BD las plantas que aparecen como ingredientes
    val plantasEnBD = remember(catalogState, remedy) {
        val allPlants = (catalogState as? CatalogState.Success)?.plants ?: emptyList()
        allPlants.filter { planta ->
            remedy.ingredients.any { ing ->
                planta.nombreComun.contains(ing, ignoreCase = true) ||
                ing.contains(planta.nombreComun, ignoreCase = true)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {

            TopAppBar(
                title = { Text("Detalle del Remedio", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, "Regresar")
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Filled.Share, "Compartir", tint = MaterialTheme.colorScheme.primary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
            )

            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                    .fillMaxWidth().height(240.dp).clip(RoundedCornerShape(20.dp))) {
                AsyncImage(model = remedy.imageUrl, contentDescription = remedy.title,
                    contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                Box(modifier = Modifier.fillMaxSize().background(
                    Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.4f)),
                        startY = 300f)))
            }

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(remedy.title, style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold)
                Text(remedy.subtitle, style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 4.dp))
            }

            Spacer(Modifier.height(16.dp))

            Card(modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth(),
                shape  = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Icon(Icons.Filled.HealthAndSafety, null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(24.dp))
                    Column {
                        Text("Beneficios", style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                        Spacer(Modifier.height(4.dp))
                        Text(remedy.benefit, style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer, lineHeight = 22.sp)
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            Row(modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Filled.ShoppingBasket, null, tint = MaterialTheme.colorScheme.primary)
                Text("Ingredientes", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(12.dp))
            Column(modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)) {
                remedy.ingredients.forEach { ingredient ->
                    ElevatedCard(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                        Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Box(modifier = Modifier.size(10.dp)
                                .background(MaterialTheme.colorScheme.primary, CircleShape))
                            Text(ingredient, style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
            }

            // Plantas encontradas en la BD
            if (plantasEnBD.isNotEmpty()) {
                Spacer(Modifier.height(24.dp))
                Row(modifier = Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Filled.Eco, null, tint = MaterialTheme.colorScheme.primary)
                    Text("Plantas en nuestra base de datos",
                        style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.height(12.dp))
                Column(modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    plantasEnBD.forEach { planta ->
                        ElevatedCard(
                            onClick = {
                                viewModel.selectPlantFromDB(planta)
                                navController.navigate(Screen.Detail.route)
                            },
                            modifier  = Modifier.fillMaxWidth(),
                            shape     = RoundedCornerShape(16.dp)) {
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
                }
            }

            Spacer(Modifier.height(24.dp))

            Row(modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Filled.RestaurantMenu, null, tint = MaterialTheme.colorScheme.primary)
                Text("Preparación", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(12.dp))
            Column(modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)) {
                remedy.steps.forEachIndexed { index, step -> PrepStep(index + 1, step) }
            }

            Spacer(Modifier.height(100.dp))
        }

        Box(modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth()
                .background(Brush.verticalGradient(
                    listOf(Color.Transparent, MaterialTheme.colorScheme.surface)))
                .padding(horizontal = 16.dp, vertical = 12.dp).navigationBarsPadding()) {
            Button(onClick = {}, modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(50)) {
                Icon(Icons.Filled.Favorite, null)
                Spacer(Modifier.width(8.dp))
                Text("Guardar en Favoritos", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

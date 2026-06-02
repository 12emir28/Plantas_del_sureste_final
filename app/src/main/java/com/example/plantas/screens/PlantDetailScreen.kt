package com.example.plantas.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import com.example.plantas.PlantasViewModel

// ── Modelo de datos del detalle ──
data class PlantDetail(
    val name             : String,
    val sciName          : String,
    val imageUrl         : String,
    val properties       : String,
    val steps            : List<String>,
    val contraindications: String
)

val samplePlantDetail = PlantDetail(
    name     = "Maguey Morado",
    sciName  = "Tradescantia spathacea",
    imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuBo7rOF3_krpUz8pbko9ZlT-CDeX0NnsqNDDo-_CtNX8oY1LU_osYS93e_lSfoTRdzoW4h_sUkXdAISN1tbZEiljcS3FOYqiCV9kttm0UcbzlhptwEzp0Tuq-L5tR0pdZelOn7U6awjDSCC9VFscXGYFDIti8IMlRhbE-3sT2d9UKiDXiF_ax-20ZlGHhIeDSvx2zLwX8lYtuUAjxeBk8q8jdVguy9eZed6L4rEajk4FsplkHgA4yC0KQCSLeleQ9swXlTV47QBAcI",
    properties = "Es ampliamente reconocido por sus potentes capacidades antiinflamatorias. " +
            "Se utiliza tradicionalmente para acelerar la cicatrización de heridas " +
            "superficiales y aliviar dolores reumáticos. Sus hojas contienen compuestos " +
            "bioactivos que ayudan a reducir la hinchazón de tejidos.",
    steps = listOf(
        "Lavar cuidadosamente 2 o 3 hojas frescas de la planta.",
        "Hervir un litro de agua y añadir las hojas una vez alcance el punto de ebullición.",
        "Dejar reposar la infusión tapada durante 10 minutos antes de colar y servir."
    ),
    contraindications = "El contacto directo con la savia de las hojas puede causar " +
            "irritación severa en piel sensible o dermatitis por contacto. " +
            "Se recomienda usar guantes durante su manipulación."
)

@Composable
fun PlantDetailScreen(
navController : NavController,
viewModel     : PlantasViewModel = viewModel()
) {
    val plant   by viewModel.selectedPlant.collectAsState()
    val isSaved by viewModel.favorites.collectAsState()
    val saved    = isSaved.any { it.name == plant.name }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            // ── Hero Image ──
            Box(modifier = Modifier.fillMaxWidth().height(300.dp)) {
                AsyncImage(
                    model              = plant.imageUrl,
                    contentDescription = plant.name,
                    contentScale       = ContentScale.Crop,
                    modifier           = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier.fillMaxSize().background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.55f)),
                            startY = 400f
                        )
                    )
                )
                Row(
                    modifier = Modifier.fillMaxWidth().padding(8.dp).statusBarsPadding(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Surface(shape = CircleShape, color = MaterialTheme.colorScheme.surface.copy(alpha = 0.88f), modifier = Modifier.size(48.dp)) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Regresar", tint = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                    Surface(shape = CircleShape, color = MaterialTheme.colorScheme.surface.copy(alpha = 0.88f), modifier = Modifier.size(48.dp)) {
                        IconButton(onClick = { viewModel.toggleFavorite(plant) }) {
                            Icon(
                                imageVector        = if (saved) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                contentDescription = "Favorita",
                                tint               = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

            // ── Sheet ──
            Surface(
                modifier = Modifier.offset(y = (-24).dp).fillMaxWidth(),
                shape    = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                color    = MaterialTheme.colorScheme.surface
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(plant.name, style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold)
                    Text(plant.sciName, color = MaterialTheme.colorScheme.primary, fontSize = 16.sp, fontStyle = FontStyle.Italic, modifier = Modifier.padding(top = 4.dp, bottom = 20.dp))

                    DetailSectionHeader(icon = Icons.Filled.HealthAndSafety, title = "Propiedades")
                    Spacer(Modifier.height(10.dp))
                    Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest), border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)) {
                        Text(plant.properties, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(16.dp), lineHeight = 24.sp)
                    }

                    Spacer(Modifier.height(20.dp))
                    DetailSectionHeader(icon = Icons.Filled.LocalCafe, title = "Preparación")
                    Spacer(Modifier.height(10.dp))
                    plant.steps.forEachIndexed { index, step ->
                        PrepStep(number = index + 1, text = step)
                        if (index < plant.steps.lastIndex) Spacer(Modifier.height(12.dp))
                    }

                    Spacer(Modifier.height(20.dp))
                    DetailSectionHeader(icon = Icons.Filled.Warning, title = "Contraindicaciones", iconColor = MaterialTheme.colorScheme.error)
                    Spacer(Modifier.height(10.dp))
                    Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer), border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.3f))) {
                        Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Icon(Icons.Filled.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(20.dp).padding(top = 2.dp))
                            Text(plant.contraindications, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onErrorContainer, lineHeight = 22.sp)
                        }
                    }
                    Spacer(Modifier.height(90.dp))
                }
            }
        }

        // ── Botón sticky ──
        Surface(modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth(), color = Color.Transparent) {
            Box(
                modifier = Modifier.background(Brush.verticalGradient(colors = listOf(Color.Transparent, MaterialTheme.colorScheme.surface)))
                    .padding(horizontal = 16.dp, vertical = 12.dp).navigationBarsPadding()
            ) {
                Button(
                    onClick  = { viewModel.toggleFavorite(plant) },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape    = RoundedCornerShape(50),
                    colors   = ButtonDefaults.buttonColors(
                        containerColor = if (saved) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.primary,
                        contentColor   = if (saved) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Icon(imageVector = if (saved) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text(if (saved) "Guardada en Favoritas ✓" else "Guardar en Favoritas", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// ── Componente: Section Header ──
@Composable
fun DetailSectionHeader(
    icon      : ImageVector,
    title     : String,
    iconColor : Color = MaterialTheme.colorScheme.primary
) {
    Row(
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector        = icon,
            contentDescription = null,
            tint               = iconColor,
            modifier           = Modifier.size(22.dp)
        )
        Text(
            text       = title,
            style      = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
    }
}

// ── Componente: Paso de preparación ──
@Composable
fun PrepStep(number: Int, text: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment     = Alignment.Top
    ) {
        Surface(
            shape    = CircleShape,
            color    = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(32.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text       = "$number",
                    color      = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize   = 14.sp
                )
            }
        }
        Text(
            text      = text,
            style     = MaterialTheme.typography.bodyLarge,
            modifier  = Modifier.padding(top = 4.dp),
            lineHeight = 22.sp
        )
    }
}
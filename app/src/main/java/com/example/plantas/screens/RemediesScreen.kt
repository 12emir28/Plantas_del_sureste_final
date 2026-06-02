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
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.plantas.PlantasViewModel
import com.example.plantas.ui.navigation.Screen

data class Remedy(
    val tag         : String,
    val title       : String,
    val description : String,
    val imageUrl    : String,
    val category    : String
)

val remedyList = listOf(
    Remedy(
        tag         = "Gordolobo · Eucalipto",
        title       = "Infusión para la tos",
        description = "Alivia la irritación de garganta y descongestiona los bronquios de forma natural.",
        imageUrl    = "https://lh3.googleusercontent.com/aida-public/AB6AXuA6X-H-x2BokDh3S45rAkTdok9mk7B3WvP1tg4URkqP17SSInG8iQEU2SuV9AAi5Z2odiw4a1Vnp4wMf-fREluNNuekyl2yjrIGMeIZWhls41AWNDjEDjV11ctAoy38-DZrQ7ajMRizwZ9TY1Kcr0f55bRfv2mee1UUjzFYJnFxaNj3op0nqPIh1ko0IHgvw7JPGb0bpAyDoiYf5l34m-lTyFlmjJgupVnZgRqeL76KtbSs_v4wyOCxcLhYZhI_SB9FEIoXL1ny9KI",
        category    = "Infusiones"
    ),
    Remedy(
        tag         = "Caléndula · Cera de abeja",
        title       = "Pomada de caléndula",
        description = "Ideal para cicatrización de heridas leves, quemaduras e irritaciones cutáneas.",
        imageUrl    = "https://lh3.googleusercontent.com/aida-public/AB6AXuADU8Ge3xdjaN-j09FgNdfKAMQVm4AaugF1QJ1z4vHl5u3DOXzkFsLJC4SDSSxXZdwTBv5yDFU6NpnDYeU28qM89xyTrfoZyRn0SsITYIEckKevxDTccvoLCt5gTAcrBHXJoEXQ3OQgW2U0-vp_POkoHQaOLRgCdC4GHPr2gaM5KiKVEzxv_ldJK68oDMqp_rGPym8xzJniwxO7tI641byYmgLYd-7VT-3sKOg9KXsdm3ZCVHBrj269ebwVGoLGfPO7FNu3tPOiW1Q",
        category    = "Pomadas"
    ),
    Remedy(
        tag         = "Manzanilla · Menta",
        title       = "Té digestivo",
        description = "Mezcla clásica para reducir la inflamación abdominal y mejorar la digestión.",
        imageUrl    = "https://lh3.googleusercontent.com/aida-public/AB6AXuAT1N4upMsgUZsaDd8QkiczOEp3m7XURyTG4-LDyGF2ITMJxZO8cemFpLTlps1J8uFBEKgwSbaPGy3nMCrrd4QeinS_UxZIKnVT0D_dwsNyai-FiJpsCjr5IUeLNQRhQcQfJ-HfaufcjQ4vU42CnWVoWjMz1KzfmyeaSKsAwP9UR0FOPbUQBcydF2tRUYIIOVmW4Zz0jSIkhFURRljQ-4ekWeInrqiUQ1XN4hmd9W9MyIWqNMWNtzqtXgcMjWhGzyK6v0RVoW6dD6g",
        category    = "Infusiones"
    ),
    Remedy(
        tag         = "Buganvilla · Miel",
        title       = "Jarabe de Buganvilla",
        description = "Remedio tradicional del sureste para calmar la tos seca y recurrente.",
        imageUrl    = "https://lh3.googleusercontent.com/aida-public/AB6AXuCz7XTD5KTQ0d30sRBy65QbkRw_y1czOqY8XIGZq5eIHUvx9WE0u1NduzLY_8IofcbPtqK_LsOGrz9-WeuckfDEZBV3R1tdy5frVfiVFbkF4Xy5C6iVMRA-6jTj5LAoDN2zYrzoL9c1tl-jj0kXxZMkZcGnR0ay6ARz1yVitHYNvj5X5m8mR4gVEQzuS6z4TIdwMGMBB5ttwA_0k1YQW5_W41k-rNeU7uW92Pf0dFagcbvbtJRuX6o26ijqBFuoAy0OHRN1NJl6fkg",
        category    = "Jarabes"
    ),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemediesScreen(
    navController : NavController,
    viewModel     : PlantasViewModel = viewModel()
) {
    val filters  = listOf("Todos", "Infusiones", "Pomadas", "Jarabes")
    var selected by remember { mutableStateOf("Todos") }

    val filtered = remember(selected) {
        if (selected == "Todos") remedyList
        else remedyList.filter { it.category == selected }
    }

    Column(modifier = Modifier.fillMaxSize()) {

        TopAppBar(
            title = { Text("Remedios Naturales", style = MaterialTheme.typography.titleLarge) },
            navigationIcon = {
                IconButton(onClick = {}) {
                    Icon(Icons.Filled.Menu, contentDescription = "Menú")
                }
            },
            actions = {
                IconButton(onClick = {}) {
                    Icon(Icons.Filled.Search, contentDescription = "Buscar")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow
            )
        )

        LazyRow(
            contentPadding        = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier              = Modifier.padding(vertical = 8.dp)
        ) {
            items(filters) { filter ->
                FilterChip(
                    selected = selected == filter,
                    onClick  = { selected = filter },
                    label    = { Text(filter) },
                    colors   = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                        selectedLabelColor     = MaterialTheme.colorScheme.onSecondaryContainer,
                    )
                )
            }
        }

        Text(
            text       = "Recetas Tradicionales",
            style      = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier   = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
        )

        LazyColumn(
            contentPadding      = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(filtered) { remedy ->
                RemedyCard(
                    remedy  = remedy,
                    onClick = {
                        val detail = remedyDetailList.find { it.title == remedy.title }
                        if (detail != null) {
                            viewModel.selectRemedy(detail)
                            navController.navigate(Screen.RemedyDetail.route)
                        }
                    }
                )
            }
            item { Spacer(Modifier.height(8.dp)) }
        }
    }
}

@Composable
fun RemedyCard(remedy: Remedy, onClick: () -> Unit) {
    ElevatedCard(
        onClick   = onClick,
        modifier  = Modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp)
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            AsyncImage(
                model              = remedy.imageUrl,
                contentDescription = remedy.title,
                contentScale       = ContentScale.Crop,
                modifier           = Modifier.width(112.dp).fillMaxHeight()
            )
            Column(
                modifier = Modifier.weight(1f).padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text          = remedy.tag.uppercase(),
                    color         = MaterialTheme.colorScheme.primary,
                    style         = MaterialTheme.typography.labelSmall,
                    fontWeight    = FontWeight.Bold,
                    letterSpacing = 0.6.sp
                )
                Text(
                    text       = remedy.title,
                    style      = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text     = remedy.description,
                    style    = MaterialTheme.typography.bodySmall,
                    color    = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2
                )
                Spacer(Modifier.height(4.dp))
                FilledTonalButton(
                    onClick        = onClick,
                    modifier       = Modifier.height(32.dp),
                    shape          = RoundedCornerShape(50),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 0.dp),
                    colors         = ButtonDefaults.filledTonalButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor   = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                ) {
                    Text("Ver receta", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
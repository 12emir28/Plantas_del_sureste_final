package com.example.plantas.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun PlantOfTheDayCard(onClick: () -> Unit) {
    ElevatedCard(
        onClick   = onClick,
        modifier  = Modifier.padding(horizontal = 16.dp).fillMaxWidth(),
        shape     = RoundedCornerShape(28.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Box {
            AsyncImage(
                model              = "https://lh3.googleusercontent.com/aida-public/AB6AXuCd01iSpKhPjg-Guak7KbS8COQk-JRE4MmrlcPe0fDm5hpAiUdEDvVGN751qSwIZoq197WGacXJX_I43rdxm--dpmYHrgPaFycbaVkabbU6F_i7gk-IRhOt0jAiZG_IsuCNMpqbbRu0qE-1d-Oj-wR4MqLd_-y450udvRgvGK21CKq2Wp-WnyxRUxEz08flZKXLNZowajjgcSA2OboZj6gCgc866dm-Frn6wOA376qX5QM6CxVDJ504YQuSMncw8cln68clQwkvh2s",
                contentDescription = "Albahaca",
                contentScale       = ContentScale.Crop,
                modifier           = Modifier.fillMaxWidth().height(200.dp)
            )
            Surface(
                modifier = Modifier.align(Alignment.TopEnd).padding(12.dp),
                shape    = RoundedCornerShape(50),
                color    = MaterialTheme.colorScheme.primary
            ) {
                Text(
                    "DESTACADA",
                    modifier      = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    color         = MaterialTheme.colorScheme.onPrimary,
                    fontSize      = 11.sp,
                    fontWeight    = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                )
            }
        }
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Albahaca",
                        style      = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Ocimum basilicum",
                        color     = MaterialTheme.colorScheme.primary,
                        fontSize  = 13.sp,
                        fontStyle = FontStyle.Italic
                    )
                }
                IconButton(onClick = {}) {
                    Icon(
                        Icons.Outlined.FavoriteBorder,
                        tint               = MaterialTheme.colorScheme.primary,
                        contentDescription = null
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(
                "Conocida por sus propiedades digestivas y antiespasmódicas. " +
                        "Ideal para aliviar el malestar estomacal y reducir el estrés.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(14.dp))
            Button(
                onClick  = onClick,
                modifier = Modifier.fillMaxWidth(),
                shape    = RoundedCornerShape(50)
            ) {
                Text("Ver detalles", fontWeight = FontWeight.Bold)
                Spacer(Modifier.width(8.dp))
                Icon(
                    Icons.Filled.ArrowForward,
                    contentDescription = null,
                    modifier           = Modifier.size(18.dp)
                )
            }
        }
    }
}
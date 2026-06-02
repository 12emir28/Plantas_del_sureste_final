package com.example.plantas.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary                = PrimaryGreen,
    onPrimary              = OnPrimary,
    primaryContainer       = PrimaryContainer,
    onPrimaryContainer     = OnPrimaryContainer,
    secondary              = SecondaryGreen,
    onSecondary            = OnSecondary,
    secondaryContainer     = SecondaryContainer,
    onSecondaryContainer   = OnSecondaryContainer,
    tertiary               = TertiaryBlue,
    tertiaryContainer      = TertiaryContainer,
    error                  = ErrorRed,
    errorContainer         = ErrorContainer,
    onErrorContainer       = OnErrorContainer,
    surface                = SurfaceLight,
    onSurface              = OnSurface,
    onSurfaceVariant       = OnSurfaceVariant,
    outline                = Outline,
    outlineVariant         = OutlineVariant,
    surfaceContainerLowest = SurfaceContainerLowest,
    surfaceContainerLow    = SurfaceContainerLow,
    surfaceContainer       = SurfaceContainer,
    surfaceContainerHigh   = SurfaceContainerHigh,
    surfaceContainerHighest= SurfaceContainerHighest,
)

@Composable
fun PlantasTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography  = Typography,
        content     = content
    )
}
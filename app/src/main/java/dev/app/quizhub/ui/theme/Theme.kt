package dev.app.quizhub.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Cores base do app - Esquema Pastel
private val primaryColor = Color(0xFF1490C7)      // Azul pastel
private val secondaryColor = Color(0xFFF7CAC9)    // Rosa pastel
private val tertiaryColor = Color(0xFF98B4D4)     // Azul pastel mais claro
private val errorColor = Color(0xFFEF9A9A)
private val successColor = Color(0xFFCCFF90)// Vermelho pastel

// Esquema de cores claro personalizado
private val LightColors = lightColorScheme(
    primary = primaryColor,
    onPrimary = Color.White,
    primaryContainer = primaryColor.copy(alpha = 0.2f),
    onPrimaryContainer = primaryColor,
    
    secondary = secondaryColor,
    onSecondary = Color(0xFF442626),  // Marrom escuro suave para contraste
    secondaryContainer = secondaryColor.copy(alpha = 0.2f),
    onSecondaryContainer = secondaryColor,
    
    tertiary = tertiaryColor,
    onTertiary = Color.White,
    tertiaryContainer = tertiaryColor.copy(alpha = 0.2f),
    onTertiaryContainer = tertiaryColor,
    
    error = errorColor,
    onError = Color.White,
    errorContainer = errorColor.copy(alpha = 0.2f),
    onErrorContainer = errorColor,
    
    background = Color(0xFFF8F9FA),  // Cinza muito claro
    onBackground = Color(0xFF1C1B1F),
    surface = Color(0xFFF8F9FA),
    onSurface = Color(0xFF1C1B1F)
)

// Esquema de cores escuro personalizado
private val DarkColors = darkColorScheme(
    primary = primaryColor.copy(alpha = 0.8f),
    onPrimary = Color.White,
    primaryContainer = primaryColor.copy(alpha = 0.3f),
    onPrimaryContainer = primaryColor.copy(alpha = 0.9f),
    
    secondary = secondaryColor.copy(alpha = 0.8f),
    onSecondary = Color.White,
    secondaryContainer = secondaryColor.copy(alpha = 0.3f),
    onSecondaryContainer = secondaryColor.copy(alpha = 0.9f),
    
    tertiary = tertiaryColor.copy(alpha = 0.8f),
    onTertiary = Color.White,
    tertiaryContainer = tertiaryColor.copy(alpha = 0.3f),
    onTertiaryContainer = tertiaryColor.copy(alpha = 0.9f),
    
    error = errorColor.copy(alpha = 0.8f),
    onError = Color.White,
    errorContainer = errorColor.copy(alpha = 0.3f),
    onErrorContainer = errorColor.copy(alpha = 0.9f),
    
    background = Color(0xFF1C1B1F),
    onBackground = Color.White,
    surface = Color(0xFF1C1B1F),
    onSurface = Color.White
)

@Composable
fun QuizhubTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Definindo dynamicColor como false por padrão
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColors
        else -> LightColors
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
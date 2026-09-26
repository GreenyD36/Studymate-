package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = BlueDarkPrimary,
    onPrimary = SlateBackgroundDark,
    primaryContainer = BlueDarkContainer,
    onPrimaryContainer = OnBlueDarkContainer,
    secondary = TealDarkSecondary,
    onSecondary = SlateBackgroundDark,
    secondaryContainer = TealDarkContainer,
    onSecondaryContainer = OnTealDarkContainer,
    tertiary = PurpleDarkTertiary,
    onTertiary = SlateBackgroundDark,
    tertiaryContainer = PurpleDarkContainer,
    onTertiaryContainer = OnPurpleDarkContainer,
    background = SlateBackgroundDark,
    onBackground = SlateTextDark,
    surface = SlateSurfaceDark,
    onSurface = SlateTextDark,
    surfaceVariant = SlateSurfaceVariantDark,
    onSurfaceVariant = SlateTextMutedDark,
    error = RoseError
)

private val LightColorScheme = lightColorScheme(
    primary = BluePrimary,
    onPrimary = SlateSurfaceLight,
    primaryContainer = BlueContainer,
    onPrimaryContainer = OnBlueContainer,
    secondary = TealSecondary,
    onSecondary = SlateSurfaceLight,
    secondaryContainer = TealContainer,
    onSecondaryContainer = OnTealContainer,
    tertiary = PurpleTertiary,
    onTertiary = SlateSurfaceLight,
    tertiaryContainer = PurpleContainer,
    onTertiaryContainer = OnPurpleContainer,
    background = SlateBackgroundLight,
    onBackground = SlateTextLight,
    surface = SlateSurfaceLight,
    onSurface = SlateTextLight,
    surfaceVariant = SlateSurfaceVariantLight,
    onSurfaceVariant = SlateTextMutedLight,
    error = RoseError
)

@Composable
fun StudyMateTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep branded learning colors consistent
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

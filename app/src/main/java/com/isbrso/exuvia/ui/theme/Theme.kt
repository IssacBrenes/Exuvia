package com.isbrso.exuvia.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

/*
 * Esquema de colores principal de Exuvia.
 *
 * Usamos darkColorScheme porque la identidad visual
 * está basada en un fondo oscuro.
 */
private val ExuviaColorScheme = darkColorScheme(

    /*
     * Color principal usado por botones,
     * indicadores y elementos destacados.
     */
    primary = ExuviaPrimary,

    /*
     * Color del contenido colocado sobre primary.
     */
    onPrimary = ExuviaOnPrimary,

    /*
     * Fondo general de las pantallas.
     */
    background = ExuviaBackground,

    /*
     * Texto principal colocado sobre el fondo.
     */
    onBackground = ExuviaTextPrimary,

    /*
     * Tarjetas y superficies.
     */
    surface = ExuviaSurface,

    /*
     * Texto colocado sobre las superficies.
     */
    onSurface = ExuviaTextPrimary,

    /*
     * Contenedores secundarios.
     */
    surfaceVariant = ExuviaSurfaceVariant,

    /*
     * Texto secundario colocado sobre variantes de superficie.
     */
    onSurfaceVariant = ExuviaTextSecondary,

    /*
     * Bordes y divisores.
     */
    outline = ExuviaOutline
)

/*
 * Tema global de Exuvia.
 *
 * dynamicColor permanece desactivado para impedir
 * que Android sustituya nuestra identidad visual
 * por los colores extraídos del fondo de pantalla del usuario.
 */
@Composable
fun ExuviaTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = ExuviaColorScheme,
        typography = Typography,
        content = content
    )
}
package com.isbrso.exuvia.feature.arthropod.modules

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.network.NetworkHeaders
import coil3.network.httpHeaders
import coil3.request.ImageRequest
import com.isbrso.exuvia.domain.model.ArthropodImage

/*
 * =============================================================================
 * GalleryModule
 * =============================================================================
 *
 * Responsabilidad:
 * Mostrar la colección visual asociada con una especie.
 *
 * Estado actual:
 * - Selecciona la imagen principal.
 * - Si no existe una imagen principal, utiliza la primera disponible.
 * - Descarga la fotografía mediante Coil.
 * - Muestra estados de carga y error.
 *
 * Evolución prevista:
 * - HorizontalPager.
 * - Varias fotografías.
 * - Indicadores de posición.
 * - Registro específico del ejemplar mostrado.
 * - Cambio hacia fotografía del sexo contrario.
 * - Zoom.
 *
 * Aunque actualmente solo existe una fotografía,
 * este componente ya se llama GalleryModule porque su
 * responsabilidad futura será administrar toda la galería.
 * =============================================================================
 */
@Composable
fun GalleryModule(
    images: List<ArthropodImage>,
    scientificName: String,
    modifier: Modifier = Modifier
) {
    /*
     * Busca primero una fotografía marcada como principal.
     *
     * Si ninguna tiene isPrimary = true,
     * se utiliza la primera fotografía disponible.
     */
    val primaryImage =
        images.firstOrNull { image ->
            image.isPrimary
        } ?: images.firstOrNull()

    /*
     * Surface define el espacio visual de la galería.
     *
     * Por ahora contiene una sola fotografía.
     * Más adelante este mismo contenedor alojará HorizontalPager.
     */
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(
                min = 380.dp,
                max = 520.dp
            ),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        if (primaryImage != null) {
            RemoteGalleryImage(
                imageUrl = primaryImage.url,
                imageDescription =
                    primaryImage.description
                        ?: "Fotografía de $scientificName"
            )
        } else {
            /*
             * La ausencia de fotografías no debe romper la interfaz.
             */
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Fotografía no disponible",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/*
 * Componente privado encargado únicamente
 * de descargar y representar una imagen remota.
 *
 * No es accesible desde otros módulos porque forma
 * parte de la implementación interna de GalleryModule.
 */
@Composable
private fun RemoteGalleryImage(
    imageUrl: String,
    imageDescription: String
) {
    val context = LocalContext.current

    /*
     * Estados asociados con la fotografía actual.
     *
     * Cuando cambia imageUrl, remember reinicia ambos valores.
     */
    var isLoading by remember(imageUrl) {
        mutableStateOf(true)
    }

    var hasError by remember(imageUrl) {
        mutableStateOf(false)
    }

    /*
     * Wikimedia requiere que las solicitudes automáticas
     * incluyan un User-Agent identificable.
     */
    val headers = remember {
        NetworkHeaders.Builder()
            .set(
                "User-Agent",
                "Exuvia/1.0 (Android biodiversity application)"
            )
            .build()
    }

    /*
     * ImageRequest representa la petición completa.
     *
     * Incluye:
     * - URL remota.
     * - Encabezados HTTP.
     * - Configuración utilizada por Coil.
     */
    val imageRequest = remember(imageUrl) {
        ImageRequest.Builder(context)
            .data(imageUrl)
            .httpHeaders(headers)
            .build()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = imageRequest,
            contentDescription = imageDescription,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),

            onLoading = {
                isLoading = true
                hasError = false
            },

            onSuccess = {
                isLoading = false
                hasError = false
            },

            onError = { state ->
                isLoading = false
                hasError = true

                Log.e(
                    "GalleryModule",
                    "No fue posible cargar la imagen: $imageUrl",
                    state.result.throwable
                )
            }
        )

        if (isLoading) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary
            )
        }

        if (hasError) {
            Text(
                text = "No fue posible cargar la fotografía",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
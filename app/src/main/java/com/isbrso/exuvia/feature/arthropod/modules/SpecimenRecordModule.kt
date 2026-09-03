package com.isbrso.exuvia.feature.arthropod.modules

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.isbrso.exuvia.domain.model.ArthropodImage
import com.isbrso.exuvia.feature.arthropod.modules.shared.ScientificTextCard

/*
 * =============================================================================
 * SpecimenRecordModule
 * =============================================================================
 *
 * Registro relacionado específicamente con la fotografía seleccionada.
 *
 * Estado actual:
 * - autor;
 * - fuente;
 * - licencia;
 * - descripción.
 *
 * Futuro:
 * - sexo;
 * - localidad;
 * - país;
 * - coordenadas cuando sean apropiadas;
 * - altitud;
 * - fecha;
 * - colector/fotógrafo;
 * - código de registro;
 * - vínculo visual con ejemplar del sexo contrario.
 *
 * Cuando GalleryModule permita cambiar de fotografía,
 * este módulo cambiará automáticamente junto con ella.
 * =============================================================================
 */
@Composable
fun SpecimenRecordModule(
    image: ArthropodImage,
    modifier: Modifier = Modifier
) {
    val content = buildString {

        image.description?.let {
            append(it)
            append("\n\n")
        }

        append("Autor: ")
        append(image.author ?: "No especificado")

        append("\nFuente: ")
        append(image.source)

        append("\nLicencia: ")
        append(image.license)
    }

    ScientificTextCard(
        title = "Registro del ejemplar",
        content = content,
        modifier = modifier
    )
}
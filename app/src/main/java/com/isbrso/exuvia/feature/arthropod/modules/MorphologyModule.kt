
/*
 * =============================================================================
 * MorphologyModule
 * =============================================================================
 *
 * Estado actual:
 * Muestra la descripción morfológica disponible.
 *
 * Diseño definido para su evolución:
 *
 * RESUMEN VISIBLE
 * - rango de tamaño;
 * - peso cuando exista;
 * - forma corporal;
 * - coloración;
 * - textura;
 * - estructuras distintivas.
 *
 * CONTENIDO EXPANDIBLE
 * - descripción morfológica completa;
 * - caracteres diagnósticos;
 * - morfometría;
 * - anatomía interna cuando sea relevante;
 * - microestructura cuando sea relevante.
 *
 * VariabilityModule NO formará parte del desplegable.
 * Será un módulo independiente situado después de Morfología.
 * =============================================================================
 */
package com.isbrso.exuvia.feature.arthropod.modules

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.isbrso.exuvia.domain.model.Morphology

/*
 * =============================================================================
 * MorphologyModule
 * =============================================================================
 *
 * El módulo se divide conceptualmente en dos niveles.
 *
 * NIVEL PRINCIPAL:
 *
 * - rango de tamaño;
 * - promedio cuando exista;
 * - descripción general.
 *
 * NIVEL EXPANDIBLE:
 *
 * - caracteres diagnósticos;
 * - morfometría;
 * - anatomía interna;
 * - microestructura.
 *
 * VariabilityModule permanece como una tarjeta independiente
 * inmediatamente después de este módulo.
 * =============================================================================
 */
@Composable
fun MorphologyModule(
    morphology: Morphology,
    modifier: Modifier = Modifier
) {
    /*
     * Estado exclusivamente visual.
     *
     * No pertenece al ViewModel porque expandir o contraer
     * la tarjeta no modifica datos de negocio.
     */
    var expanded by remember {
        mutableStateOf(false)
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text(
                text = "Morfología",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )


            /*
             * =============================================================
             * TAMAÑO
             * =============================================================
             */

            AdultSizeContent(
                minMm = morphology.adultLengthMinMm,
                maxMm = morphology.adultLengthMaxMm,
                averageMm = morphology.averageAdultLengthMm
            )


            /*
             * =============================================================
             * DESCRIPCIÓN GENERAL
             * =============================================================
             */

            morphology.description?.let { description ->

                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }


            /*
             * Solo mostramos el control expandible si existe
             * algún nivel avanzado disponible.
             */
            val hasAdvancedContent =
                morphology.diagnosticCharacters != null ||
                        morphology.morphometrics != null ||
                        morphology.internalAnatomy != null ||
                        morphology.microstructure != null

            if (hasAdvancedContent) {

                TextButton(
                    onClick = {
                        expanded = !expanded
                    }
                ) {
                    Text(
                        text =
                            if (expanded) {
                                "Ocultar descripción morfológica completa"
                            } else {
                                "Ver descripción morfológica completa"
                            }
                    )
                }

                if (expanded) {

                    HorizontalDivider()

                    morphology.diagnosticCharacters?.let { content ->
                        MorphologySubsection(
                            title = "Caracteres diagnósticos",
                            content = content
                        )
                    }

                    morphology.morphometrics?.let { content ->
                        MorphologySubsection(
                            title = "Morfometría",
                            content = content
                        )
                    }

                    morphology.internalAnatomy?.let { content ->
                        MorphologySubsection(
                            title = "Anatomía interna",
                            content = content
                        )
                    }

                    morphology.microstructure?.let { content ->
                        MorphologySubsection(
                            title = "Microestructura",
                            content = content
                        )
                    }
                }
            }
        }
    }
}


/*
 * =============================================================================
 * AdultSizeContent
 * =============================================================================
 *
 * Decide cómo representar los valores de tamaño disponibles.
 */
@Composable
private fun AdultSizeContent(
    minMm: Double?,
    maxMm: Double?,
    averageMm: Double?
) {
    /*
     * Si no tenemos ningún dato de tamaño,
     * no mostramos una sección vacía.
     */
    if (
        minMm == null &&
        maxMm == null &&
        averageMm == null
    ) {
        return
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {

        Text(
            text = "Tamaño adulto",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        /*
         * Rango conocido.
         */
        if (minMm != null && maxMm != null) {

            MorphologyValueRow(
                label = "Rango",
                value = "${formatMillimeters(minMm)}–${formatMillimeters(maxMm)} mm"
            )

        } else if (minMm != null) {

            MorphologyValueRow(
                label = "Longitud mínima",
                value = "${formatMillimeters(minMm)} mm"
            )

        } else if (maxMm != null) {

            MorphologyValueRow(
                label = "Longitud máxima",
                value = "${formatMillimeters(maxMm)} mm"
            )
        }


        /*
         * El promedio solo aparece cuando fue documentado
         * explícitamente por una fuente.
         */
        averageMm?.let { average ->

            MorphologyValueRow(
                label = "Promedio",
                value = "${formatMillimeters(average)} mm"
            )
        }
    }
}


@Composable
private fun MorphologyValueRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = value,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}


@Composable
private fun MorphologySubsection(
    title: String,
    content: String
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = content,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}


/*
 * Evita mostrar:
 *
 * 50.0 mm
 *
 * cuando podemos mostrar:
 *
 * 50 mm
 *
 * pero conserva decimales cuando realmente existen.
 */
private fun formatMillimeters(
    value: Double
): String {

    return if (value % 1.0 == 0.0) {
        value.toInt().toString()
    } else {
        value.toString()
    }
}
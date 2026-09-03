package com.isbrso.exuvia.feature.arthropod.modules

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.isbrso.exuvia.domain.model.Arthropod

/*
 * =============================================================================
 * SpecimenModule
 * =============================================================================
 *
 * Responsabilidad:
 * Mostrar la identificación resumida del artrópodo y, en el futuro,
 * los datos específicos del ejemplar representado por la fotografía activa.
 *
 * Estado actual:
 * - Nombre científico.
 * - Nombre común.
 * - Clase, orden y familia.
 *
 * Evolución prevista:
 * - Sexo del ejemplar.
 * - Localidad.
 * - País.
 * - Fecha del registro.
 * - Código del espécimen cuando exista.
 * =============================================================================
 */
@Composable
fun SpecimenModule(
    arthropod: Arthropod
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Text(
            text = arthropod.scientificName,
            style = MaterialTheme.typography.headlineMedium,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        arthropod.commonName?.let { commonName ->
            Text(
                text = commonName,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Text(
            text = "${arthropod.taxonomicClass} · " +
                    "${arthropod.order} · ${arthropod.family}",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
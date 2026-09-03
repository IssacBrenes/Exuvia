package com.isbrso.exuvia.feature.arthropod.modules

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.isbrso.exuvia.domain.model.Arthropod

/*
 * =============================================================================
 * TaxonomyModule
 * =============================================================================
 *
 * Responsabilidad:
 * Mostrar la clasificación taxonómica completa del artrópodo.
 *
 * Estado actual:
 * - Reino
 * - Filo
 * - Subfilo
 * - Clase
 * - Orden
 * - Familia
 * - Género
 * - Especie
 *
 * Evolución prevista:
 * - Posibles enlaces hacia grupos taxonómicos.
 * - Integración con filtros futuros.
 * - Representación visual más rica de la jerarquía taxonómica.
 * =============================================================================
 */
@Composable
fun TaxonomyModule(
    arthropod: Arthropod,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Taxonomía",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            TaxonomyRow(
                label = "Reino",
                value = arthropod.kingdom
            )

            TaxonomyRow(
                label = "Filo",
                value = arthropod.phylum
            )

            arthropod.subphylum?.let { subphylum ->
                TaxonomyRow(
                    label = "Subfilo",
                    value = subphylum
                )
            }

            TaxonomyRow(
                label = "Clase",
                value = arthropod.taxonomicClass
            )

            TaxonomyRow(
                label = "Orden",
                value = arthropod.order
            )

            TaxonomyRow(
                label = "Familia",
                value = arthropod.family
            )

            TaxonomyRow(
                label = "Género",
                value = arthropod.genus
            )

            TaxonomyRow(
                label = "Especie",
                value = arthropod.species
            )
        }
    }
}

/*
 * Fila interna reutilizada por cada nivel taxonómico.
 *
 * Se mantiene privada porque solamente tiene sentido
 * dentro de TaxonomyModule.
 */
@Composable
private fun TaxonomyRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = value,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
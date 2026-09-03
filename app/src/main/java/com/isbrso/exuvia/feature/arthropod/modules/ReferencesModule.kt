package com.isbrso.exuvia.feature.arthropod.modules

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.isbrso.exuvia.domain.model.ScientificReference

/*
 * =============================================================================
 * ReferencesModule
 * =============================================================================
 *
 * Muestra las fuentes utilizadas para respaldar
 * científicamente el contenido de la ficha.
 *
 * Futuro:
 * - iconos según tipo de fuente;
 * - DOI clickeable;
 * - URL;
 * - artículos;
 * - museos;
 * - bases de biodiversidad;
 * - organismos científicos.
 * =============================================================================
 */
@Composable
fun ReferencesModule(
    references: List<ScientificReference>,
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
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "Referencias",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            references.forEachIndexed { index, reference ->

                ReferenceContent(
                    number = index + 1,
                    reference = reference
                )

                if (index < references.lastIndex) {
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    }
}


@Composable
private fun ReferenceContent(
    number: Int,
    reference: ScientificReference
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "$number. ${reference.title}",
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )

        reference.authors?.let { authors ->
            Text(
                text = authors,
                style = MaterialTheme.typography.bodyMedium,
                color =
                    MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        reference.organization?.let { organization ->
            Text(
                text = organization,
                style = MaterialTheme.typography.bodyMedium,
                color =
                    MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        reference.year?.let { year ->
            Text(
                text = year.toString(),
                style = MaterialTheme.typography.bodyMedium,
                color =
                    MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        reference.doi?.let { doi ->
            Text(
                text = "DOI: $doi",
                style = MaterialTheme.typography.bodySmall,
                color =
                    MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
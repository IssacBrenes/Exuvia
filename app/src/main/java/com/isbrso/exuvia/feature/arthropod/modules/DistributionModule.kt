package com.isbrso.exuvia.feature.arthropod.modules

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.isbrso.exuvia.feature.arthropod.modules.shared.ScientificTextCard

/*
 * =============================================================================
 * DistributionModule
 * =============================================================================
 *
 * Versión actual:
 * - Texto.
 *
 * Diseño definitivo:
 *
 * Visor geográfico interactivo:
 *
 * Mundo
 *   ↓
 * Continente / región
 *   ↓
 * País
 *   ↓
 * División administrativa
 *   ↓
 * Localidad
 *
 * Futuras capas:
 *
 * - registros de ocurrencia;
 * - GBIF;
 * - áreas protegidas;
 * - distribución histórica;
 * - elevación;
 * - ecosistemas.
 *
 * Este archivo permanecerá como punto de entrada del módulo aunque
 * internamente el mapa crezca en varios componentes.
 * =============================================================================
 */
@Composable
fun DistributionModule(
    distribution: String,
    modifier: Modifier = Modifier
) {
    ScientificTextCard(
        title = "Distribución",
        content = distribution,
        modifier = modifier
    )
}
package com.isbrso.exuvia.feature.arthropod.modules

/*
 * =============================================================================
 * VariabilityModule
 * =============================================================================
 *
 * ESTADO:
 * Pendiente de incorporación al modelo de dominio.
 *
 * Posición definitiva:
 *
 * MorphologyModule
 *       ↓
 * VariabilityModule
 *       ↓
 * BehaviorModule
 *
 * IMPORTANTE:
 *
 * VariabilityModule NO pertenece al contenido expandible
 * de MorphologyModule.
 *
 * Es una tarjeta independiente.
 *
 * Contendrá variaciones intraespecíficas como:
 *
 * - morfotipos;
 * - variaciones de color;
 * - diferencias entre poblaciones;
 * - castas;
 * - polimorfismos;
 * - otras variaciones documentadas.
 *
 * Solo se mostrará cuando exista información científicamente
 * respaldada.
 * =============================================================================
 */

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.isbrso.exuvia.feature.arthropod.modules.shared.ScientificTextCard

/*
 * Variabilidad intraespecífica.
 *
 * Este módulo es independiente de Morfología,
 * tal como definimos en el diseño.
 */
@Composable
fun VariabilityModule(
    variability: String,
    modifier: Modifier = Modifier
) {
    ScientificTextCard(
        title = "Variabilidad",
        content = variability,
        modifier = modifier
    )
}
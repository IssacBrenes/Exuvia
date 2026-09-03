
/*
 * =============================================================================
 * SexualDimorphismModule
 * =============================================================================
 *
 * ESTADO:
 * Pendiente de incorporación al modelo de dominio.
 *
 * Posición definitiva:
 *
 * ReproductionModule
 *       ↓
 * SexualDimorphismModule
 *       ↓
 * LifeCycleModule
 *
 * Presentará diferencias documentadas entre sexos.
 *
 * Futuro:
 *
 * ♂ Macho
 * características...
 *
 * ♀ Hembra
 * características...
 *
 * También podrá incluir un control visual que cambie
 * GalleryModule hacia una fotografía representativa
 * del sexo contrario.
 * =============================================================================
 */

package com.isbrso.exuvia.feature.arthropod.modules

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.isbrso.exuvia.feature.arthropod.modules.shared.ScientificTextCard

/*
 * Describe las diferencias documentadas
 * entre machos y hembras de la especie.
 *
 * Más adelante este módulo podrá interactuar
 * con GalleryModule para cambiar entre fotografías
 * representativas de ambos sexos.
 */
@Composable
fun SexualDimorphismModule(
    sexualDimorphism: String,
    modifier: Modifier = Modifier
) {
    ScientificTextCard(
        title = "Dimorfismo sexual",
        content = sexualDimorphism,
        modifier = modifier
    )
}
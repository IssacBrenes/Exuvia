package com.isbrso.exuvia.feature.arthropod.modules

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.isbrso.exuvia.feature.arthropod.modules.shared.ScientificTextCard

@Composable
fun ConservationModule(
    conservationStatus: String,
    modifier: Modifier = Modifier
) {
    ScientificTextCard(
        title = "Conservación",
        content = conservationStatus,
        modifier = modifier
    )
}
package com.isbrso.exuvia.feature.arthropod.modules

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.isbrso.exuvia.feature.arthropod.modules.shared.ScientificTextCard

@Composable
fun EcologyModule(
    ecology: String,
    modifier: Modifier = Modifier
) {
    ScientificTextCard(
        title = "Ecología",
        content = ecology,
        modifier = modifier
    )
}
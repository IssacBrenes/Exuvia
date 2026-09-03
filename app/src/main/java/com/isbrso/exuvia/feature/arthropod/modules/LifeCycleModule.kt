package com.isbrso.exuvia.feature.arthropod.modules

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.isbrso.exuvia.feature.arthropod.modules.shared.ScientificTextCard

@Composable
fun LifeCycleModule(
    lifeCycle: String,
    modifier: Modifier = Modifier
) {
    ScientificTextCard(
        title = "Ciclo de vida",
        content = lifeCycle,
        modifier = modifier
    )
}
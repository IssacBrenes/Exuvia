package com.isbrso.exuvia.feature.arthropod

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.isbrso.exuvia.domain.model.Arthropod
import com.isbrso.exuvia.feature.arthropod.modules.BehaviorModule
import com.isbrso.exuvia.feature.arthropod.modules.ConservationModule
import com.isbrso.exuvia.feature.arthropod.modules.DistributionModule
import com.isbrso.exuvia.feature.arthropod.modules.EcologyModule
import com.isbrso.exuvia.feature.arthropod.modules.GalleryModule
import com.isbrso.exuvia.feature.arthropod.modules.HabitatModule
import com.isbrso.exuvia.feature.arthropod.modules.HighlightModule
import com.isbrso.exuvia.feature.arthropod.modules.LifeCycleModule
import com.isbrso.exuvia.feature.arthropod.modules.MorphologyModule
import com.isbrso.exuvia.feature.arthropod.modules.ReferencesModule
import com.isbrso.exuvia.feature.arthropod.modules.ReproductionModule
import com.isbrso.exuvia.feature.arthropod.modules.SexualDimorphismModule
import com.isbrso.exuvia.feature.arthropod.modules.SpecimenModule
import com.isbrso.exuvia.feature.arthropod.modules.SpecimenRecordModule
import com.isbrso.exuvia.feature.arthropod.modules.TaxonomyModule
import com.isbrso.exuvia.feature.arthropod.modules.VariabilityModule
import kotlinx.coroutines.launch


/*
 * =============================================================================
 * ArthropodRoute
 * =============================================================================
 *
 * Conecta el ViewModel con la interfaz.
 *
 * Esta función conoce el ViewModel.
 * Los módulos visuales NO conocen el ViewModel.
 */
@Composable
fun ArthropodRoute(
    viewModel: ArthropodViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ArthropodScreen(
        uiState = uiState,
        onRetry = viewModel::loadArthropod
    )
}


/*
 * =============================================================================
 * ArthropodScreen
 * =============================================================================
 *
 * Decide qué interfaz mostrar dependiendo del UiState.
 */
@Composable
fun ArthropodScreen(
    uiState: ArthropodUiState,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        when (uiState) {

            ArthropodUiState.Loading -> {
                LoadingContent()
            }

            is ArthropodUiState.Success -> {
                ArthropodContent(
                    arthropod = uiState.arthropod,
                    onLoadAnother = onRetry
                )
            }

            is ArthropodUiState.Error -> {
                ErrorContent(
                    message = uiState.message,
                    onRetry = onRetry
                )
            }
        }
    }
}


/*
 * =============================================================================
 * LoadingContent
 * =============================================================================
 */
@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary
        )
    }
}


/*
 * =============================================================================
 * ArthropodContent
 * =============================================================================
 *
 * Esta función se convierte en el "director" de la ficha.
 *
 * Ya no sabe cómo dibujar:
 *
 * - fotografías;
 * - taxonomía;
 * - comportamiento;
 * - referencias;
 * - etc.
 *
 * Su responsabilidad es decidir el ORDEN.
 */
@Composable
private fun ArthropodContent(
    arthropod: Arthropod,
    onLoadAnother: () -> Unit
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    /*
     * Por ahora necesitamos conocer la imagen principal aquí
     * únicamente para SpecimenRecordModule.
     *
     * Cuando GalleryModule tenga HorizontalPager, la fotografía
     * seleccionada será un estado compartido y esta lógica evolucionará.
     */
    val primaryImage =
        arthropod.images.firstOrNull { image ->
            image.isPrimary
        } ?: arthropod.images.firstOrNull()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = listState,
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {

        /*
         * =============================================================
         * PORTADA
         * =============================================================
         */

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 20.dp,
                        top = 16.dp,
                        end = 20.dp,
                        bottom = 4.dp
                    ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                GalleryModule(
                    images = arthropod.images,
                    scientificName = arthropod.scientificName
                )

                SpecimenModule(
                    arthropod = arthropod
                )

                /*
                 * Este botón NO abre otra pantalla.
                 *
                 * Solo desplaza suavemente la misma ficha
                 * hasta el contenido científico.
                 */
                TextButton(
                    onClick = {
                        coroutineScope.launch {
                            listState.animateScrollToItem(
                                index = 1
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "⌄  Ver ficha científica",
                        color =
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }


        /*
         * =============================================================
         * INICIO DE LA FICHA CIENTÍFICA
         * =============================================================
         */

        item {
            DetailHeader(
                scientificName = arthropod.scientificName
            )
        }


        /*
         * 1. ASPECTO DESTACADO
         *
         * Aspecto destacado + explicación científica
         * pertenecen a una única tarjeta.
         */
        item {
            HighlightModule(
                highlightedAspect =
                    arthropod.highlightedAspect
            )
        }


        /*
         * 2. TAXONOMÍA
         */
        item {
            TaxonomyModule(
                arthropod = arthropod
            )
        }


        /*
         * 3. MORFOLOGÍA
         */
        arthropod.morphology?.let { morphology ->
            item {
                MorphologyModule(
                    morphology = morphology
                )
            }
        }

        arthropod.variability?.let { variability ->
            item {
                VariabilityModule(
                    variability = variability
                )
            }
        }


        /*
         * 4. COMPORTAMIENTO
         */
        arthropod.behavior?.let { behavior ->
            item {
                BehaviorModule(
                    behavior = behavior
                )
            }
        }


        /*
         * 5. REPRODUCCIÓN
         */
        arthropod.reproduction?.let { reproduction ->
            item {
                ReproductionModule(
                    reproduction = reproduction
                )
            }
        }


        arthropod.sexualDimorphism?.let { sexualDimorphism ->
            item {
                SexualDimorphismModule(
                    sexualDimorphism = sexualDimorphism
                )
            }
        }


        /*
         * 6. CICLO DE VIDA
         */
        arthropod.lifeCycle?.let { lifeCycle ->
            item {
                LifeCycleModule(
                    lifeCycle = lifeCycle
                )
            }
        }


        /*
         * 7. HÁBITAT
         */
        arthropod.habitat?.let { habitat ->
            item {
                HabitatModule(
                    habitat = habitat,
                    habitatExplanation = arthropod.habitatExplanation
                )
            }
        }

        /*
         * 8. DISTRIBUCIÓN
         *
         * Actualmente es texto.
         *
         * Este módulo será reemplazado internamente por
         * nuestro visor geográfico interactivo.
         */
        arthropod.distribution?.let { distribution ->
            item {
                DistributionModule(
                    distribution = distribution
                )
            }
        }


        /*
         * 9. ECOLOGÍA
         */
        arthropod.ecology?.let { ecology ->
            item {
                EcologyModule(
                    ecology = ecology
                )
            }
        }


        /*
         * 10. CONSERVACIÓN
         */
        arthropod.conservationStatus?.let { conservation ->
            item {
                ConservationModule(
                    conservationStatus = conservation
                )
            }
        }


        /*
         * 11. REFERENCIAS
         */
        if (arthropod.references.isNotEmpty()) {
            item {
                ReferencesModule(
                    references = arthropod.references
                )
            }
        }


        /*
         * 12. REGISTRO DEL EJEMPLAR
         *
         * Por ahora contiene únicamente la información
         * disponible en ArthropodImage.
         */
        primaryImage?.let { image ->
            item {
                SpecimenRecordModule(
                    image = image
                )
            }
        }


        /*
         * =============================================================
         * FINAL
         * =============================================================
         */

        item {
            Button(
                onClick = onLoadAnother,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor =
                        MaterialTheme.colorScheme.primary,
                    contentColor =
                        MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(
                    text = "Descubrir otro artrópodo"
                )
            }
        }

        item {
            Spacer(
                modifier = Modifier.height(32.dp)
            )
        }
    }
}


/*
 * Marca el inicio de la ficha.
 *
 * Más adelante también podremos convertirlo en
 * un componente compartido si encontramos reutilización real.
 */
@Composable
private fun DetailHeader(
    scientificName: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 20.dp,
                vertical = 8.dp
            ),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        HorizontalDivider(
            color = MaterialTheme.colorScheme.outline
        )

        Text(
            text = "Ficha científica",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = scientificName,
            style = MaterialTheme.typography.titleMedium,
            fontStyle = FontStyle.Italic,
            color = MaterialTheme.colorScheme.primary
        )
    }
}


/*
 * =============================================================================
 * ErrorContent
 * =============================================================================
 */
@Composable
private fun ErrorContent(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(
                containerColor =
                    MaterialTheme.colorScheme.primary,
                contentColor =
                    MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(
                text = "Reintentar"
            )
        }
    }
}
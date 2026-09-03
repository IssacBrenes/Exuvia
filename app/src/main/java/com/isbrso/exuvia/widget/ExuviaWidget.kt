package com.isbrso.exuvia.widget

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.util.Log
import coil3.request.ErrorResult

import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.GlanceComposable

import androidx.glance.action.clickable

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.provideContent

import androidx.glance.background

import androidx.glance.layout.Column
import androidx.glance.layout.ContentScale
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding

import androidx.glance.text.FontStyle
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle

import androidx.glance.unit.ColorProvider

import coil3.SingletonImageLoader
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import coil3.request.allowHardware
import coil3.toBitmap

import coil3.network.NetworkHeaders
import coil3.network.httpHeaders

import com.isbrso.exuvia.MainActivity

import androidx.glance.LocalSize
import androidx.glance.appwidget.cornerRadius
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Row
import androidx.glance.layout.width

import androidx.glance.appwidget.SizeMode

/*
 * =============================================================================
 * ExuviaWidget
 * =============================================================================
 *
 * Primera versión funcional del widget de Exuvia.
 *
 * Objetivos actuales:
 *
 * 1. Mostrar un artrópodo.
 * 2. Mostrar su fotografía.
 * 3. Mostrar nombre común y científico.
 * 4. Mostrar el aspecto destacado.
 * 5. Abrir Exuvia al tocar el widget.
 *
 * Más adelante:
 *
 * - Los datos vendrán desde ArthropodRepository.
 * - Existirán varias especies.
 * - El widget rotará automáticamente.
 * - El intervalo podrá configurarse.
 * =============================================================================
 */
class ExuviaWidget : GlanceAppWidget() {


    /*
 * Queremos conocer las dimensiones exactas que el launcher
 * asigna al widget cada vez que el usuario lo redimensiona.
 *
 * Esto permite recalcular:
 *
 * - altura de fotografía;
 * - espacio de contenido;
 * - encuadre.
 */
    override val sizeMode: SizeMode =
        SizeMode.Exact

    /*
 * Intervalo TEMPORAL de desarrollo.
 *
 * WorkManager solicitará una actualización periódica
 * y cada intervalo temporal corresponderá a otra especie.
 */
    private val rotationIntervalMillis =
        30* 1000L

    /*
     * =========================================================================
     * Datos TEMPORALES del widget.
     * =========================================================================
     *
     * Esta lista será reemplazada posteriormente por nuestro Repository.
     *
     * Por ahora nos permite obtener un resultado visible inmediatamente
     * sin volver a modificar la arquitectura de datos.
     */
    private val widgetArthropods: List<WidgetArthropod> =
        listOf(

            /*
             * =============================================================
             * 1. ESCARABAJO HÉRCULES
             * =============================================================
             */
            WidgetArthropod(
                scientificName = "Dynastes hercules",
                commonName = "Escarabajo Hércules",

                taxonomySummary =
                    "Insecta · Coleoptera · Scarabaeidae",

                highlightedAspect =
                    "Cuernos especializados para el combate",

                widgetImageUrl =
                    "https://commons.wikimedia.org/wiki/Special:Redirect/file/Dynastes_hercules.lichyi_%28male%292.JPG"
            ),


            /*
             * =============================================================
             * 2. MANTIS ORQUÍDEA
             * =============================================================
             */
            WidgetArthropod(
                scientificName = "Hymenopus coronatus",
                commonName = "Mantis orquídea",

                taxonomySummary =
                    "Insecta · Mantodea · Hymenopodidae",

                highlightedAspect =
                    "Su apariencia floral forma parte de una extraordinaria estrategia de mimetismo",

                widgetImageUrl =
                    "https://commons.wikimedia.org/wiki/Special:Redirect/file/Hymenopodidae_-_Hymenopus_coronatus.JPG"
            ),


            /*
             * =============================================================
             * 3. ESCARABAJO BLANCO
             * =============================================================
             *
             * Por ahora se mantiene a nivel de género.
             */
            WidgetArthropod(
                scientificName = "Cyphochilus sp.",
                commonName = "Escarabajo blanco",

                taxonomySummary =
                    "Insecta · Coleoptera · Scarabaeidae",

                highlightedAspect =
                    "Sus diminutas escamas producen una blancura estructural extraordinariamente intensa",

                widgetImageUrl =
                    "https://commons.wikimedia.org/wiki/Special:Redirect/file/Cyphochilus_beetle.jpg"
            ),


            /*
             * =============================================================
             * 4. INSECTO HOJA GIGANTE
             * =============================================================
             */
            WidgetArthropod(
                scientificName = "Pulchriphyllium giganteum",
                commonName = "Insecto hoja gigante",

                taxonomySummary =
                    "Insecta · Phasmatodea · Phylliidae",

                highlightedAspect =
                    "Su cuerpo reproduce la forma, coloración y detalles visuales de una hoja",

                widgetImageUrl =
                    "https://commons.wikimedia.org/wiki/Special:Redirect/file/Pulchriphyllium_giganteum%2C_adult_femal_from_dorsal.jpg"
            )
        )


    /*
     * =============================================================================
     * provideGlance
     * =============================================================================
     *
     * Es el punto de entrada visual del widget.
     *
     * Android solicita contenido al widget y aquí:
     *
     * 1. seleccionamos el artrópodo;
     * 2. descargamos la fotografía;
     * 3. construimos la interfaz.
     */
    override suspend fun provideGlance(
        context: Context,
        id: GlanceId
    ) {

        /*
         * Seleccionamos el artrópodo correspondiente
         * al período temporal actual.
         */
        val arthropod =
            selectCurrentArthropod()


        /*
         * Descargamos la fotografía antes de construir
         * el contenido visual del widget.
         */
        val bitmap =
            loadImageBitmap(
                context = context,
                imageUrl = arthropod.widgetImageUrl
            )


        /*
         * provideContent inicia la composición de Glance.
         */
        provideContent {

            ExuviaWidgetContent(
                context = context,
                arthropod = arthropod,
                bitmap = bitmap
            )
        }
    }


    /*
     * =============================================================================
     * selectCurrentArthropod
     * =============================================================================
     *
     * Divide el tiempo en bloques de seis horas.
     *
     * Cada bloque obtiene un índice.
     *
     * Con una sola especie:
     *
     * siempre obtenemos índice 0.
     *
     * Cuando existan varias especies:
     *
     * bloque 0 → especie 0
     * bloque 1 → especie 1
     * bloque 2 → especie 2
     *
     * etc.
     */
    private fun selectCurrentArthropod(): WidgetArthropod {

        val slot =
            System.currentTimeMillis() /
                    rotationIntervalMillis

        val index =
            (slot % widgetArthropods.size)
                .toInt()

        return widgetArthropods[index]
    }


    /*
     * =============================================================================
     * loadImageBitmap
     * =============================================================================
     *
     * Glance no utiliza AsyncImage como Jetpack Compose.
     *
     * Por eso descargamos la fotografía con Coil y obtenemos
     * un Bitmap que Glance puede mostrar mediante ImageProvider.
     */
    private suspend fun loadImageBitmap(
        context: Context,
        imageUrl: String
    ): Bitmap? {

        val imageLoader =
            SingletonImageLoader.get(context)

        val headers =
            NetworkHeaders.Builder()
                .set(
                    "User-Agent",
                    "Exuvia/1.0 (Android biodiversity application)"
                )
                .build()

        val request =
            ImageRequest.Builder(context)
                .data(imageUrl)
                .httpHeaders(headers)
                .allowHardware(false)
                .size(
                    width = 900,
                    height = 600
                )
                .build()

        return try {

            val result =
                imageLoader.execute(request)

            when (result) {

                is SuccessResult -> {

                    Log.d(
                        "ExuviaWidget",
                        "Imagen cargada correctamente: $imageUrl"
                    )

                    result.image.toBitmap()
                }

                is ErrorResult -> {

                    Log.e(
                        "ExuviaWidget",
                        "Error real de Coil cargando: $imageUrl",
                        result.throwable
                    )

                    null
                }

                else -> {

                    Log.e(
                        "ExuviaWidget",
                        "Resultado desconocido de Coil: ${result::class.java.name}"
                    )

                    null
                }
            }

        } catch (exception: Exception) {

            Log.e(
                "ExuviaWidget",
                "Error cargando imagen del widget: $imageUrl",
                exception
            )

            null
        }
    }
}


/*
 * =============================================================================
 * ExuviaWidgetContent
 * =============================================================================
 *
 * Diseño visual del widget.
 *
 * Filosofía:
 *
 * Aproximadamente 75 % → fotografía
 * Aproximadamente 25 % → información
 *
 * La fotografía es el elemento principal del widget.
 * El texto solamente identifica al artrópodo y presenta
 * brevemente el aspecto destacado.
 *
 * Toda la superficie puede tocarse para abrir Exuvia.
 * =============================================================================
 */
@GlanceComposable
@Composable
private fun ExuviaWidgetContent(
    context: Context,
    arthropod: WidgetArthropod,
    bitmap: Bitmap?
) {

    val openExuviaIntent =
        Intent(
            context,
            MainActivity::class.java
        )

    /*
     * =========================================================================
     * CONTENEDOR PRINCIPAL
     * =========================================================================
     *
     * El widget completo mantiene el fondo actual de Exuvia.
     *
     * La fotografía usa todo el espacio sobrante.
     * El texto solo consume la altura que realmente necesita.
     */
    Column(
        modifier =
            GlanceModifier
                .fillMaxSize()
                .background(
                    ColorProvider(
                        Color(0xFF242D20)
                    )
                )
                .padding(14.dp)
                .clickable(
                    actionStartActivity(
                        openExuviaIntent
                    )
                )
    ) {

        /*
         * =========================================================================
         * FOTOGRAFÍA
         * =========================================================================
         *
         * defaultWeight() hace que la imagen se quede con todo
         * el espacio vertical disponible después de medir el texto.
         *
         * Por eso:
         *
         * texto corto  → imagen más grande
         * texto largo  → imagen cede un poco de espacio
         */
        if (bitmap != null) {

            Image(
                provider =
                    ImageProvider(bitmap),

                contentDescription =
                    "Fotografía de ${arthropod.scientificName}",

                modifier =
                    GlanceModifier
                        .fillMaxWidth()
                        .defaultWeight()
                        .cornerRadius(18.dp),

                /*
                 * Crop permite que la imagen siga llenando
                 * completamente el marco aunque cambie
                 * el tamaño del widget.
                 */
                contentScale =
                    ContentScale.Crop
            )

        } else {

            /*
             * =========================================================================
             * FALLBACK
             * =========================================================================
             *
             * Si la fotografía falla, este bloque ocupa exactamente
             * el mismo espacio flexible que ocuparía la imagen.
             */
            Column(
                modifier =
                    GlanceModifier
                        .fillMaxWidth()
                        .defaultWeight()
                        .cornerRadius(18.dp)
                        .background(
                            ColorProvider(
                                Color(0xFF313F2C)
                            )
                        )
                        .padding(14.dp)
            ) {

                Text(
                    text = "Exuvia",

                    style =
                        TextStyle(
                            color =
                                ColorProvider(
                                    Color(0xFFD6C6A5)
                                ),

                            fontSize = 18.sp,

                            fontWeight =
                                FontWeight.Bold
                        )
                )
            }
        }


        /*
         * Separación visual entre foto e información.
         */
        Spacer(
            modifier =
                GlanceModifier
                    .height(10.dp)
        )


        /*
         * =========================================================================
         * INFORMACIÓN
         * =========================================================================
         *
         * IMPORTANTE:
         *
         * Esta Column NO tiene altura fija.
         *
         * Solo crecerá lo necesario según el contenido.
         */
        Column(
            modifier =
                GlanceModifier
                    .fillMaxWidth()
        ) {

            /*
             * ---------------------------------------------------------------------
             * NOMBRE COMÚN
             * ---------------------------------------------------------------------
             */
            Text(
                text =
                    arthropod.commonName,

                style =
                    TextStyle(
                        color =
                            ColorProvider(
                                Color(0xFFE0D0AD)
                            ),

                        fontSize = 18.sp,

                        fontWeight =
                            FontWeight.Bold
                    ),

                maxLines = 1
            )


            Spacer(
                modifier =
                    GlanceModifier
                        .height(2.dp)
            )


            /*
             * ---------------------------------------------------------------------
             * NOMBRE CIENTÍFICO
             * ---------------------------------------------------------------------
             */
            Text(
                text =
                    arthropod.scientificName,

                style =
                    TextStyle(
                        color =
                            ColorProvider(
                                Color(0xFFBCA987)
                            ),

                        fontSize = 13.sp,

                        fontStyle =
                            FontStyle.Italic
                    ),

                maxLines = 1
            )


            Spacer(
                modifier =
                    GlanceModifier
                        .height(4.dp)
            )


            /*
             * ---------------------------------------------------------------------
             * TAXONOMÍA RESUMIDA
             * ---------------------------------------------------------------------
             *
             * Ejemplo:
             *
             * Insecta · Coleoptera · Scarabaeidae
             */
            Text(
                text =
                    arthropod.taxonomySummary,

                style =
                    TextStyle(
                        color =
                            ColorProvider(
                                Color(0xFFC9994D)
                            ),

                        fontSize = 12.sp,

                        fontWeight =
                            FontWeight.Medium
                    ),

                maxLines = 1
            )


            Spacer(
                modifier =
                    GlanceModifier
                        .height(7.dp)
            )


            /*
             * ---------------------------------------------------------------------
             * DIVISOR
             * ---------------------------------------------------------------------
             */
            Box(
                modifier =
                    GlanceModifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(
                            ColorProvider(
                                Color(0xFF56634E)
                            )
                        )
            ) {}


            Spacer(
                modifier =
                    GlanceModifier
                        .height(7.dp)
            )


            /*
             * =========================================================================
             * ASPECTO DESTACADO
             * =========================================================================
             *
             * Puede utilizar hasta 3 líneas.
             *
             * Pero si el texto solo necesita una línea,
             * NO se reserva espacio para las otras dos.
             */
            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                /*
                 * Marcador visual inspirado en el diseño
                 * que aprobamos.
                 *
                 * Más adelante podemos reemplazar este símbolo
                 * por un recurso de hoja real.
                 */
                Text(
                    text = "◊",

                    style =
                        TextStyle(
                            color =
                                ColorProvider(
                                    Color(0xFFC9994D)
                                ),

                            fontSize = 20.sp
                        )
                )


                Spacer(
                    modifier =
                        GlanceModifier
                            .width(8.dp)
                )


                Text(
                    text =
                        arthropod.highlightedAspect,

                    style =
                        TextStyle(
                            color =
                                ColorProvider(
                                    Color(0xFFD2BE95)
                                ),

                            fontSize = 13.sp
                        ),

                    /*
                     * No obliga al texto a tener tres líneas.
                     * Solo establece el máximo permitido.
                     */
                    maxLines = 3
                )
            }
        }
    }
}
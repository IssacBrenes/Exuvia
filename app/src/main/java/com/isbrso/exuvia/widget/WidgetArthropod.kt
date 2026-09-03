package com.isbrso.exuvia.widget

/*
 * Modelo reducido utilizado exclusivamente por el widget.
 *
 * No representa la ficha científica completa.
 * Contiene únicamente la información necesaria
 * para presentar un artrópodo en la pantalla de inicio.
 */
data class WidgetArthropod(

    val scientificName: String,

    val commonName: String,

    /*
     * Clasificación resumida mostrada debajo
     * del nombre científico.
     *
     * Ejemplo:
     * Insecta · Coleoptera · Scarabaeidae
     */
    val taxonomySummary: String,

    /*
     * Característica especial presentada
     * en la parte inferior del widget.
     */
    val highlightedAspect: String,

    /*
     * Fotografía seleccionada específicamente
     * para funcionar visualmente en el widget.
     */
    val widgetImageUrl: String
)
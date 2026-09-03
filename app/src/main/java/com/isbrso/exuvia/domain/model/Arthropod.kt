package com.isbrso.exuvia.domain.model

data class Arthropod(
    val id: String,
    val scientificName: String,
    val commonName: String?,

    val kingdom: String,
    val phylum: String,
    val subphylum: String?,
    val taxonomicClass: String,
    val order: String,
    val family: String,
    val genus: String,
    val species: String,

    val highlightedAspect: HighlightedAspect,

    val morphology: Morphology?,


    /*
     * Variación morfológica dentro de la especie.
     *
     * Ejemplos:
     * - morfotipos;
     * - variaciones de color;
     * - castas;
     * - diferencias poblacionales.
     */
    val variability: String?,

    val behavior: String?,
    val reproduction: String?,

    /*
     * Diferencias documentadas entre machos y hembras.
     */
    val sexualDimorphism: String?,

    val lifeCycle: String?,

    val habitat: String?,

    /*
     * Explica por qué ese hábitat resulta apropiado
     * para la especie desde el punto de vista biológico.
     */
    val habitatExplanation: String?,

    val distribution: String?,
    val ecology: String?,
    val conservationStatus: String?,

    val images: List<ArthropodImage>,
    val references: List<ScientificReference>
)

/*
 * =============================================================================
 * Morphology
 * =============================================================================
 *
 * Representa la información morfológica de una especie.
 *
 * Se agrupa en un objeto propio para evitar llenar Arthropod
 * con una gran cantidad de propiedades especializadas.
 *
 * La interfaz podrá mostrar primero un resumen y después
 * desplegar los niveles de información más técnicos.
 *
 * Variability NO pertenece a este objeto.
 * Continúa siendo un módulo independiente.
 * =============================================================================
 */
data class Morphology(

    /*
     * Descripción general de la morfología externa.
     */
    val description: String?,

    /*
     * =========================================================================
     * TAMAÑO DEL ADULTO
     * =========================================================================
     *
     * Unidad estándar interna de Exuvia:
     * milímetros.
     */
    val adultLengthMinMm: Double?,
    val adultLengthMaxMm: Double?,
    val averageAdultLengthMm: Double?,

    /*
     * =========================================================================
     * INFORMACIÓN MORFOLÓGICA AVANZADA
     * =========================================================================
     *
     * Estos campos quedan preparados desde ahora.
     *
     * Todavía no existen como columnas independientes en Room,
     * por lo que inicialmente llegarán como null.
     */

    /*
     * Caracteres que ayudan a distinguir la especie
     * de taxones semejantes.
     */
    val diagnosticCharacters: String?,

    /*
     * Mediciones, conteos y proporciones morfométricas
     * más especializadas.
     */
    val morphometrics: String?,

    /*
     * Anatomía interna cuando tenga relevancia científica
     * para describir o identificar la especie.
     */
    val internalAnatomy: String?,

    /*
     * Características microscópicas o ultraestructurales
     * cuando exista información relevante.
     */
    val microstructure: String?
)

data class HighlightedAspect(
    val title: String,
    val summary: String,
    val scientificExplanation: String
)

enum class SpecimenSex {
    MALE,
    FEMALE,
    UNKNOWN,
    NOT_APPLICABLE
}

data class ArthropodImage(
    /*
     * Identificador estable definido por Exuvia.
     *
     * Ejemplo:
     * dynastes-hercules-female-cahuita
     */
    val id: String,

    val url: String,
    val author: String?,
    val source: String,
    val sourceUrl: String,
    val license: String,
    val description: String?,

    /*
     * Información específica del ejemplar representado.
     */
    val specimenSex: SpecimenSex?,
    val locality: String?,
    val country: String?,
    val date: String?,
    val catalogCode: String?,

    /*
     * Relación opcional con otra fotografía
     * representativa del sexo contrario.
     */
    val oppositeSexImageId: String?,

    val isPrimary: Boolean
)

data class ScientificReference(
    val id: Long,
    val title: String,
    val authors: String?,
    val organization: String?,
    val year: Int?,
    val url: String,
    val doi: String?,
    val sourceType: String
)
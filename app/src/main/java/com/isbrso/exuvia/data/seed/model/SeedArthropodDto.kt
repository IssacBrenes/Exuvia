package com.isbrso.exuvia.data.seed.model

import kotlinx.serialization.Serializable


/*
 * =============================================================================
 * SeedCollectionDto
 * =============================================================================
 *
 * Representa el documento JSON completo.
 *
 * schemaVersion:
 * indica la versión de la ESTRUCTURA del documento.
 *
 * contentVersion:
 * indica la versión del CONTENIDO científico.
 *
 * Ejemplo:
 *
 * schemaVersion cambia cuando:
 * - agregamos nuevos campos;
 * - modificamos la estructura;
 * - reorganizamos objetos.
 *
 * contentVersion cambia cuando:
 * - agregamos especies;
 * - corregimos información;
 * - agregamos imágenes;
 * - actualizamos referencias.
 * =============================================================================
 */
@Serializable
data class SeedCollectionDto(
    val schemaVersion: Int,
    val contentVersion: Int,
    val arthropods: List<SeedArthropodDto>
)


/*
 * =============================================================================
 * SeedArthropodDto
 * =============================================================================
 *
 * Representa un artrópodo tal como aparece dentro del JSON.
 *
 * Esta clase:
 *
 * NO es Room.
 * NO es el modelo de dominio.
 *
 * Solo representa datos que entran desde el archivo seed.
 * =============================================================================
 */
@Serializable
data class SeedArthropodDto(

    val id: String,

    val scientificName: String,

    val commonName: String?,

    /*
     * La taxonomía se mantiene agrupada dentro del JSON
     * para que el documento sea más fácil de leer y mantener.
     */
    val taxonomy: SeedTaxonomyDto,

    /*
     * Aspecto destacado utilizado por la ficha
     * y posteriormente por el widget.
     */
    val highlightedAspect: SeedHighlightedAspectDto,


    /*
     * =========================================================================
     * MORFOLOGÍA
     * =========================================================================
     */

    val morphology: String?,

    /*
     * Rango de tamaño adulto.
     *
     * Unidad interna oficial de Exuvia:
     * milímetros.
     */
    val adultLengthMinMm: Double?,
    val adultLengthMaxMm: Double?,

    /*
     * Solo debe incluirse cuando una fuente científica
     * proporciona un promedio válido.
     *
     * NO se calcula automáticamente como:
     *
     * (mínimo + máximo) / 2
     */
    val averageAdultLengthMm: Double?,

    /*
     * Variabilidad intraespecífica.
     *
     * Ejemplos:
     * - variaciones de color;
     * - morfotipos;
     * - diferencias poblacionales;
     * - castas.
     */
    val variability: String?,


    /*
     * =========================================================================
     * BIOLOGÍA
     * =========================================================================
     */

    val behavior: String?,

    val reproduction: String?,

    /*
     * Diferencias documentadas entre machos y hembras.
     */
    val sexualDimorphism: String?,

    val lifeCycle: String?,


    /*
     * =========================================================================
     * HÁBITAT Y DISTRIBUCIÓN
     * =========================================================================
     */

    val habitat: String?,

    /*
     * Explica por qué ese hábitat es relevante
     * para la biología de la especie.
     */
    val habitatExplanation: String?,

    val distribution: String?,


    /*
     * =========================================================================
     * ECOLOGÍA Y CONSERVACIÓN
     * =========================================================================
     */

    val ecology: String?,

    val conservationStatus: String?,


    /*
     * =========================================================================
     * IMÁGENES Y REFERENCIAS
     * =========================================================================
     */

    val images: List<SeedImageDto>,

    val references: List<SeedReferenceDto>
)


@Serializable
data class SeedTaxonomyDto(
    val kingdom: String,
    val phylum: String,
    val subphylum: String?,
    val taxonomicClass: String,
    val order: String,
    val family: String,
    val genus: String,
    val species: String
)


@Serializable
data class SeedHighlightedAspectDto(
    val title: String,
    val summary: String,
    val scientificExplanation: String
)


/*
 * =============================================================================
 * SeedImageDto
 * =============================================================================
 *
 * Representa una fotografía dentro del JSON.
 *
 * La fotografía deja de ser únicamente un recurso visual.
 *
 * Puede representar un registro concreto de un ejemplar.
 * =============================================================================
 */
@Serializable
data class SeedImageDto(

    /*
     * Identificador permanente de la fotografía dentro de Exuvia.
     *
     * Ejemplo:
     *
     * dynastes-hercules-female-cahuita
     */
    val id: String,

    /*
     * URL directa utilizada para cargar la fotografía.
     */
    val url: String,

    /*
     * =========================================================================
     * CRÉDITOS Y LICENCIA
     * =========================================================================
     */

    val author: String?,

    val sourceName: String,

    val sourceUrl: String,

    val license: String,

    val description: String?,


    /*
     * =========================================================================
     * REGISTRO DEL EJEMPLAR
     * =========================================================================
     */

    /*
     * Valores previstos:
     *
     * MALE
     * FEMALE
     * UNKNOWN
     * NOT_APPLICABLE
     */
    val specimenSex: String?,

    /*
     * Ejemplo:
     *
     * Cahuita
     */
    val locality: String?,

    /*
     * Ejemplo:
     *
     * Costa Rica
     */
    val country: String?,

    /*
     * Fecha cuando la fuente la proporcione.
     *
     * Por ahora permanece como String.
     */
    val date: String?,

    /*
     * Código de catálogo, voucher u otro identificador
     * científico cuando exista.
     */
    val catalogCode: String?,

    /*
     * Identificador estable de una fotografía
     * representativa del sexo contrario.
     */
    val oppositeSexImageId: String?,

    /*
     * Identifica la fotografía utilizada inicialmente.
     */
    val isPrimary: Boolean
)


@Serializable
data class SeedReferenceDto(
    val title: String,
    val authors: String?,
    val organization: String?,
    val publicationYear: Int?,
    val url: String,
    val doi: String?,
    val sourceType: String
)
package com.isbrso.exuvia.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/*
 * =============================================================================
 * ArthropodEntity
 * =============================================================================
 *
 * Representa la tabla principal de artrópodos dentro de Room.
 *
 * IMPORTANTE:
 *
 * Esta clase pertenece exclusivamente a la capa de persistencia.
 * No es el modelo que utiliza directamente la interfaz.
 *
 * Flujo:
 *
 * Room
 * ArthropodEntity
 *      ↓
 * ArthropodMapper.toDomain()
 *      ↓
 * Arthropod
 * =============================================================================
 */
@Entity(tableName = "arthropods")
data class ArthropodEntity(

    /*
     * Identificador estable de la especie dentro de Exuvia.
     *
     * Ejemplo:
     * dynastes-hercules
     */
    @PrimaryKey
    val id: String,

    /*
     * =========================================================================
     * IDENTIFICACIÓN
     * =========================================================================
     */

    val scientificName: String,
    val commonName: String?,

    /*
     * =========================================================================
     * TAXONOMÍA
     * =========================================================================
     */

    val kingdom: String,
    val phylum: String,
    val subphylum: String?,
    val taxonomicClass: String,

    /*
     * Se llama orderName y no simplemente order porque "order"
     * puede producir ambigüedad como término SQL.
     */
    val orderName: String,

    val family: String,
    val genus: String,
    val species: String,

    /*
     * =========================================================================
     * ASPECTO DESTACADO
     * =========================================================================
     */

    val highlightTitle: String,
    val highlightSummary: String,
    val highlightScientificExplanation: String,

    /*
     * =========================================================================
     * MORFOLOGÍA
     * =========================================================================
     */

    /*
     * Descripción morfológica general.
     */
    val morphology: String?,

    /*
     * Rango de longitud corporal del adulto.
     *
     * La unidad interna estándar utilizada por Exuvia
     * será el milímetro.
     *
     * Guardar números y no texto permitirá posteriormente:
     *
     * - filtros;
     * - comparaciones;
     * - cálculos;
     * - diferentes representaciones en la UI.
     */
    val adultLengthMinMm: Double?,
    val adultLengthMaxMm: Double?,

    /*
     * Promedio documentado cuando una fuente científica
     * proporciona un valor suficientemente respaldado.
     *
     * No debe calcularse automáticamente a partir del mínimo
     * y máximo: un rango no implica que el promedio sea su punto medio.
     */
    val averageAdultLengthMm: Double?,

    /*
     * Variación intraespecífica documentada.
     *
     * Ejemplos:
     * - morfotipos;
     * - coloraciones;
     * - castas;
     * - diferencias poblacionales.
     *
     * Se mostrará en VariabilityModule como tarjeta independiente.
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

    /*
     * Describe el ambiente donde vive.
     */
    val habitat: String?,

    /*
     * Explica por qué las características de ese ambiente
     * son apropiadas o importantes para la biología de la especie.
     */
    val habitatExplanation: String?,

    /*
     * Por ahora conserva una descripción textual.
     *
     * Posteriormente DistributionModule utilizará datos
     * geográficos estructurados para nuestro mapa interactivo.
     */
    val distribution: String?,

    /*
     * =========================================================================
     * ECOLOGÍA Y CONSERVACIÓN
     * =========================================================================
     */

    val ecology: String?,
    val conservationStatus: String?
)
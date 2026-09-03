package com.isbrso.exuvia.data.mapper

import com.isbrso.exuvia.data.local.entity.ArthropodImageEntity
import com.isbrso.exuvia.data.local.entity.ArthropodReferenceEntity
import com.isbrso.exuvia.data.local.relation.ArthropodWithDetails
import com.isbrso.exuvia.domain.model.Arthropod
import com.isbrso.exuvia.domain.model.ArthropodImage
import com.isbrso.exuvia.domain.model.HighlightedAspect
import com.isbrso.exuvia.domain.model.ScientificReference
import com.isbrso.exuvia.domain.model.SpecimenSex
import com.isbrso.exuvia.domain.model.Morphology

/*
 * =============================================================================
 * ArthropodMapper
 * =============================================================================
 *
 * Responsabilidad:
 *
 * Transformar los modelos utilizados por Room en los modelos
 * limpios utilizados por el dominio de Exuvia.
 *
 * Flujo:
 *
 * ArthropodWithDetails
 * ├── ArthropodEntity
 * ├── List<ArthropodImageEntity>
 * └── List<ArthropodReferenceEntity>
 *
 *                  ↓ toDomain()
 *
 * Arthropod
 * ├── HighlightedAspect
 * ├── List<ArthropodImage>
 * └── List<ScientificReference>
 *
 * La interfaz nunca necesita conocer las entidades de Room.
 * =============================================================================
 */


/*
 * Función de extensión.
 *
 * Permite escribir:
 *
 * arthropodWithDetails.toDomain()
 *
 * como si toDomain() perteneciera directamente
 * a ArthropodWithDetails.
 */
fun ArthropodWithDetails.toDomain(): Arthropod {

    return Arthropod(

        /*
         * =========================================================================
         * IDENTIFICACIÓN
         * =========================================================================
         */

        id = arthropod.id,

        scientificName = arthropod.scientificName,

        commonName = arthropod.commonName,


        /*
         * =========================================================================
         * TAXONOMÍA
         * =========================================================================
         */

        kingdom = arthropod.kingdom,

        phylum = arthropod.phylum,

        subphylum = arthropod.subphylum,

        taxonomicClass = arthropod.taxonomicClass,

        /*
         * En Room la propiedad se llama orderName.
         *
         * En el dominio simplemente se llama order.
         */
        order = arthropod.orderName,

        family = arthropod.family,

        genus = arthropod.genus,

        species = arthropod.species,


        /*
         * =========================================================================
         * ASPECTO DESTACADO
         * =========================================================================
         *
         * Room almacena estos tres valores como columnas separadas.
         *
         * El dominio los agrupa en un objeto HighlightedAspect.
         */

        highlightedAspect = HighlightedAspect(

            title = arthropod.highlightTitle,

            summary = arthropod.highlightSummary,

            scientificExplanation =
                arthropod.highlightScientificExplanation
        ),


        /*
 * =========================================================================
 * MORFOLOGÍA
 * =========================================================================
 *
 * Room mantiene estos datos como columnas independientes.
 *
 * El mapper los agrupa en un único concepto de dominio.
 */
        morphology = createMorphology(
            description = arthropod.morphology,
            adultLengthMinMm = arthropod.adultLengthMinMm,
            adultLengthMaxMm = arthropod.adultLengthMaxMm,
            averageAdultLengthMm = arthropod.averageAdultLengthMm
        ),

        /*
         * Variabilidad permanece fuera de Morphology porque
         * será una tarjeta independiente en la ficha.
         */
        variability =
            arthropod.variability,


        /*
         * =========================================================================
         * BIOLOGÍA
         * =========================================================================
         */

        behavior =
            arthropod.behavior,

        reproduction =
            arthropod.reproduction,

        sexualDimorphism =
            arthropod.sexualDimorphism,

        lifeCycle =
            arthropod.lifeCycle,


        /*
         * =========================================================================
         * HÁBITAT Y DISTRIBUCIÓN
         * =========================================================================
         */

        habitat =
            arthropod.habitat,

        habitatExplanation =
            arthropod.habitatExplanation,

        distribution =
            arthropod.distribution,


        /*
         * =========================================================================
         * ECOLOGÍA Y CONSERVACIÓN
         * =========================================================================
         */

        ecology =
            arthropod.ecology,

        conservationStatus =
            arthropod.conservationStatus,


        /*
         * =========================================================================
         * IMÁGENES
         * =========================================================================
         *
         * Room entrega:
         *
         * List<ArthropodImageEntity>
         *
         * map transforma cada elemento en:
         *
         * ArthropodImage
         *
         * obteniendo finalmente:
         *
         * List<ArthropodImage>
         */

        images = images.map { imageEntity ->

            imageEntity.toDomain()
        },


        /*
         * =========================================================================
         * REFERENCIAS
         * =========================================================================
         */

        references = references.map { referenceEntity ->

            referenceEntity.toDomain()
        }
    )
}


/*
 * =============================================================================
 * ArthropodImageEntity → ArthropodImage
 * =============================================================================
 *
 * Esta transformación ahora es especialmente importante porque:
 *
 * Room utiliza:
 *
 * id: Long
 * stableId: String
 *
 * mientras que el dominio utiliza:
 *
 * id: String
 *
 * El dominio NO necesita conocer el identificador interno
 * autogenerado por SQLite.
 */
private fun ArthropodImageEntity.toDomain(): ArthropodImage {

    return ArthropodImage(

        /*
         * IMPORTANTE:
         *
         * No utilizamos:
         *
         * id = id
         *
         * porque ese id es el Long interno de Room.
         *
         * El identificador que Exuvia utiliza fuera
         * de la base de datos es stableId.
         */
        id = stableId,

        url = url,

        author = author,

        source = sourceName,

        sourceUrl = sourceUrl,

        license = license,

        description = description,


        /*
         * Room guarda el sexo como String.
         *
         * El dominio utiliza SpecimenSex.
         *
         * Ejemplo:
         *
         * "FEMALE"
         *      ↓
         * SpecimenSex.FEMALE
         */
        specimenSex =
            specimenSex.toSpecimenSex(),

        locality = locality,

        country = country,

        date = date,

        catalogCode = catalogCode,

        oppositeSexImageId =
            oppositeSexImageId,

        isPrimary = isPrimary
    )
}


/*
 * =============================================================================
 * Conversión String? → SpecimenSex?
 * =============================================================================
 *
 * La base de datos almacena:
 *
 * "MALE"
 * "FEMALE"
 * "UNKNOWN"
 * "NOT_APPLICABLE"
 *
 * El dominio trabaja con el enum SpecimenSex.
 */
private fun String?.toSpecimenSex(): SpecimenSex? {

    /*
     * Si Room contiene null,
     * simplemente devolvemos null.
     */
    if (this == null) {
        return null
    }

    /*
     * valueOf convierte:
     *
     * "FEMALE"
     *
     * en:
     *
     * SpecimenSex.FEMALE
     *
     * runCatching evita que la aplicación falle si por alguna razón
     * la base contiene un valor desconocido.
     */
    return runCatching {

        SpecimenSex.valueOf(
            this.uppercase()
        )

    }.getOrNull()
}


/*
 * =============================================================================
 * ArthropodReferenceEntity → ScientificReference
 * =============================================================================
 *
 * Esta parte no cambió porque todavía no modificamos
 * el modelo de referencias.
 */
private fun ArthropodReferenceEntity.toDomain(): ScientificReference {

    return ScientificReference(

        id = id,

        title = title,

        authors = authors,

        organization = organization,

        year = publicationYear,

        url = url,

        doi = doi,

        sourceType = sourceType
    )
}

/*
 * =============================================================================
 * createMorphology
 * =============================================================================
 *
 * Construye el objeto Morphology a partir de las columnas
 * que actualmente existen en Room.
 *
 * También evita crear un objeto Morphology completamente vacío.
 */
private fun createMorphology(
    description: String?,
    adultLengthMinMm: Double?,
    adultLengthMaxMm: Double?,
    averageAdultLengthMm: Double?
): Morphology? {

    /*
     * Si absolutamente ningún dato morfológico existe,
     * el módulo completo puede considerarse ausente.
     */
    val hasMorphologyData =
        description != null ||
                adultLengthMinMm != null ||
                adultLengthMaxMm != null ||
                averageAdultLengthMm != null

    if (!hasMorphologyData) {
        return null
    }

    return Morphology(
        description = description,

        adultLengthMinMm = adultLengthMinMm,
        adultLengthMaxMm = adultLengthMaxMm,
        averageAdultLengthMm = averageAdultLengthMm,

        /*
         * Estos niveles ya están definidos en el dominio,
         * aunque aún no tienen persistencia propia.
         */
        diagnosticCharacters = null,
        morphometrics = null,
        internalAnatomy = null,
        microstructure = null
    )
}
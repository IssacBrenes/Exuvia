package com.isbrso.exuvia.data.seed.mapper

import com.isbrso.exuvia.data.local.entity.ArthropodEntity
import com.isbrso.exuvia.data.local.entity.ArthropodImageEntity
import com.isbrso.exuvia.data.local.entity.ArthropodReferenceEntity
import com.isbrso.exuvia.data.seed.model.SeedArthropodDto


/*
 * =============================================================================
 * SeedArthropodEntities
 * =============================================================================
 *
 * Objeto auxiliar utilizado durante la importación.
 *
 * Un artrópodo completo termina distribuido en tres tablas:
 *
 * arthropods
 * arthropod_images
 * arthropod_references
 *
 * Esta clase agrupa temporalmente esas entidades
 * antes de enviarlas a Room.
 * =============================================================================
 */
data class SeedArthropodEntities(
    val arthropod: ArthropodEntity,
    val images: List<ArthropodImageEntity>,
    val references: List<ArthropodReferenceEntity>
)


/*
 * =============================================================================
 * SeedArthropodDto → entidades Room
 * =============================================================================
 *
 * El JSON utiliza una estructura cómoda para humanos:
 *
 * Arthropod
 * ├── taxonomy
 * ├── highlightedAspect
 * ├── images
 * └── references
 *
 * Room necesita una estructura relacional:
 *
 * arthropods
 * arthropod_images
 * arthropod_references
 *
 * Este mapper realiza esa transformación.
 * =============================================================================
 */
fun SeedArthropodDto.toEntities(): SeedArthropodEntities {

    /*
     * =========================================================================
     * ENTIDAD PRINCIPAL
     * =========================================================================
     */

    val arthropodEntity = ArthropodEntity(

        id = id,

        scientificName = scientificName,

        commonName = commonName,


        /*
         * TAXONOMÍA
         */

        kingdom = taxonomy.kingdom,

        phylum = taxonomy.phylum,

        subphylum = taxonomy.subphylum,

        taxonomicClass =
            taxonomy.taxonomicClass,

        orderName =
            taxonomy.order,

        family =
            taxonomy.family,

        genus =
            taxonomy.genus,

        species =
            taxonomy.species,


        /*
         * ASPECTO DESTACADO
         */

        highlightTitle =
            highlightedAspect.title,

        highlightSummary =
            highlightedAspect.summary,

        highlightScientificExplanation =
            highlightedAspect.scientificExplanation,


        /*
         * MORFOLOGÍA
         */

        morphology =
            morphology,

        adultLengthMinMm =
            adultLengthMinMm,

        adultLengthMaxMm =
            adultLengthMaxMm,

        averageAdultLengthMm =
            averageAdultLengthMm,

        variability =
            variability,


        /*
         * BIOLOGÍA
         */

        behavior =
            behavior,

        reproduction =
            reproduction,

        sexualDimorphism =
            sexualDimorphism,

        lifeCycle =
            lifeCycle,


        /*
         * HÁBITAT Y DISTRIBUCIÓN
         */

        habitat =
            habitat,

        habitatExplanation =
            habitatExplanation,

        distribution =
            distribution,


        /*
         * ECOLOGÍA Y CONSERVACIÓN
         */

        ecology =
            ecology,

        conservationStatus =
            conservationStatus
    )


    /*
     * =========================================================================
     * IMÁGENES
     * =========================================================================
     *
     * Cada SeedImageDto se transforma en una fila
     * de arthropod_images.
     */
    val imageEntities = images.map { image ->

        ArthropodImageEntity(

            /*
             * Room continúa generando su propio id numérico.
             */
            id = 0,

            /*
             * Este identificador sí pertenece a Exuvia.
             */
            stableId =
                image.id,

            /*
             * Llave foránea hacia la especie.
             */
            arthropodId =
                id,

            url =
                image.url,

            author =
                image.author,

            sourceName =
                image.sourceName,

            sourceUrl =
                image.sourceUrl,

            license =
                image.license,

            description =
                image.description,


            /*
             * INFORMACIÓN DEL EJEMPLAR
             */

            specimenSex =
                image.specimenSex,

            locality =
                image.locality,

            country =
                image.country,

            date =
                image.date,

            catalogCode =
                image.catalogCode,

            oppositeSexImageId =
                image.oppositeSexImageId,

            isPrimary =
                image.isPrimary
        )
    }


    /*
     * =========================================================================
     * REFERENCIAS
     * =========================================================================
     */

    val referenceEntities =
        references.map { reference ->

            ArthropodReferenceEntity(

                /*
                 * Room genera automáticamente el id.
                 */
                id = 0,

                arthropodId =
                    id,

                title =
                    reference.title,

                authors =
                    reference.authors,

                organization =
                    reference.organization,

                publicationYear =
                    reference.publicationYear,

                url =
                    reference.url,

                doi =
                    reference.doi,

                sourceType =
                    reference.sourceType
            )
        }


    /*
     * Entregamos las tres colecciones necesarias
     * para que SeedDataLoader las inserte.
     */
    return SeedArthropodEntities(

        arthropod =
            arthropodEntity,

        images =
            imageEntities,

        references =
            referenceEntities
    )
}
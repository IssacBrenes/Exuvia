package com.isbrso.exuvia.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/*
 * =============================================================================
 * ArthropodImageEntity
 * =============================================================================
 *
 * Una especie puede contener múltiples fotografías.
 *
 * Cada fotografía representa además un posible
 * REGISTRO DE EJEMPLAR.
 *
 * Por eso una imagen puede almacenar información como:
 *
 * - sexo;
 * - localidad;
 * - país;
 * - fecha;
 * - código de catálogo;
 * - procedencia.
 * =============================================================================
 */
@Entity(
    tableName = "arthropod_images",

    foreignKeys = [

        /*
         * Relación:
         *
         * ArthropodEntity
         *      1
         *      │
         *      │
         *      N
         * ArthropodImageEntity
         *
         * Si una especie se elimina, también desaparecen
         * todas las fotografías asociadas.
         */
        ForeignKey(
            entity = ArthropodEntity::class,
            parentColumns = ["id"],
            childColumns = ["arthropodId"],
            onDelete = ForeignKey.CASCADE
        )
    ],

    indices = [

        /*
         * Acelera la búsqueda de todas las imágenes
         * pertenecientes a una especie.
         */
        Index(
            value = ["arthropodId"]
        ),

        /*
         * stableId debe identificar de manera única
         * cada registro visual dentro de Exuvia.
         */
        Index(
            value = ["stableId"],
            unique = true
        )
    ]
)
data class ArthropodImageEntity(

    /*
     * =========================================================================
     * IDENTIFICADOR INTERNO DE ROOM
     * =========================================================================
     *
     * Lo conservamos como Long autogenerado.
     *
     * Su única responsabilidad es funcionar como llave
     * física eficiente dentro de SQLite/Room.
     */
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    /*
     * =========================================================================
     * IDENTIFICADOR ESTABLE DE EXUVIA
     * =========================================================================
     *
     * Este es el identificador que podrá venir desde el JSON,
     * una API o cualquier fuente futura de contenido.
     *
     * Ejemplo:
     *
     * dynastes-hercules-female-cahuita
     *
     * A diferencia del id numérico de Room,
     * stableId NO cambia al volver a importar la fotografía.
     */
    val stableId: String,

    /*
     * Especie propietaria de la fotografía.
     */
    val arthropodId: String,

    /*
     * =========================================================================
     * RECURSO VISUAL
     * =========================================================================
     */

    val url: String,

    /*
     * =========================================================================
     * ATRIBUCIÓN Y LICENCIA
     * =========================================================================
     */

    val author: String?,
    val sourceName: String,
    val sourceUrl: String,
    val license: String,

    /*
     * Descripción suministrada por la fuente
     * o preparada editorialmente por Exuvia.
     */
    val description: String?,

    /*
     * =========================================================================
     * REGISTRO DEL EJEMPLAR
     * =========================================================================
     */

    /*
     * Guardamos el sexo como texto en la capa de persistencia.
     *
     * Valores previstos:
     *
     * MALE
     * FEMALE
     * UNKNOWN
     * NOT_APPLICABLE
     *
     * El mapper será responsable de convertir este String
     * al enum SpecimenSex utilizado por el dominio.
     *
     * Esto mantiene Room separado del modelo de dominio.
     */
    val specimenSex: String?,

    /*
     * Localidad concreta publicada para el registro.
     *
     * Ejemplo:
     * Cahuita
     */
    val locality: String?,

    /*
     * País asociado con el registro.
     *
     * Ejemplo:
     * Costa Rica
     */
    val country: String?,

    /*
     * Fecha relacionada con la fotografía o registro,
     * únicamente cuando la fuente la proporciona.
     *
     * Inicialmente utilizaremos una representación String.
     * La normalización del formato se definirá cuando
     * trabajemos el estándar editorial de registros.
     */
    val date: String?,

    /*
     * Código de catálogo, voucher u otro identificador
     * científico cuando exista.
     */
    val catalogCode: String?,

    /*
     * =========================================================================
     * RELACIÓN ENTRE FOTOGRAFÍAS
     * =========================================================================
     *
     * Identificador estable de una fotografía representativa
     * del sexo contrario.
     *
     * Ejemplo:
     *
     * Esta fotografía:
     * dynastes-hercules-female-cahuita
     *
     * puede apuntar a:
     * dynastes-hercules-male-example
     *
     * GalleryModule podrá utilizarlo posteriormente
     * para realizar el cambio mediante un icono.
     */
    val oppositeSexImageId: String?,

    /*
     * Determina cuál fotografía utiliza inicialmente:
     *
     * - la portada;
     * - la pantalla de descubrimiento;
     * - eventualmente el widget.
     */
    val isPrimary: Boolean
)
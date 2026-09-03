package com.isbrso.exuvia.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.isbrso.exuvia.data.local.dao.ArthropodDao
import com.isbrso.exuvia.data.local.entity.ArthropodEntity
import com.isbrso.exuvia.data.local.entity.ArthropodImageEntity
import com.isbrso.exuvia.data.local.entity.ArthropodReferenceEntity

/*
 * =============================================================================
 * ExuviaDatabase
 * =============================================================================
 *
 * Punto de entrada de la base de datos local.
 *
 * Room necesita conocer:
 *
 * - qué entidades forman parte de la base;
 * - la versión actual del esquema;
 * - qué DAO expone;
 * - cómo migrar desde versiones anteriores.
 *
 * Versión 1:
 * estructura inicial del MVP.
 *
 * Versión 2:
 * amplía información científica y registros de fotografías.
 * =============================================================================
 */
@Database(
    entities = [
        ArthropodEntity::class,
        ArthropodImageEntity::class,
        ArthropodReferenceEntity::class
    ],

    /*
     * Cambiamos:
     *
     * 1 → 2
     *
     * porque modificamos físicamente el esquema de Room.
     */
    version = 2,

    /*
     * Room seguirá exportando los schemas.
     *
     * Esto será importante posteriormente para probar migraciones.
     */
    exportSchema = true
)
abstract class ExuviaDatabase : RoomDatabase() {

    /*
     * DAO principal de Exuvia.
     */
    abstract fun arthropodDao(): ArthropodDao


    companion object {

        /*
         * =====================================================================
         * MIGRATION 1 → 2
         * =====================================================================
         *
         * Esta migración transforma una base creada con el MVP
         * en la nueva estructura sin borrar los datos existentes.
         */
        val MIGRATION_1_2 = object : Migration(1, 2) {

            override fun migrate(
                db: SupportSQLiteDatabase
            ) {

                /*
                 * =============================================================
                 * TABLA: arthropods
                 * =============================================================
                 *
                 * Todos estos campos son opcionales.
                 *
                 * Por eso SQLite puede añadirlos directamente permitiendo NULL.
                 */


                /*
                 * Longitud mínima adulta en milímetros.
                 *
                 * Double de Kotlin corresponde a REAL en SQLite.
                 */
                db.execSQL(
                    """
                    ALTER TABLE arthropods
                    ADD COLUMN adultLengthMinMm REAL
                    """.trimIndent()
                )


                /*
                 * Longitud máxima adulta.
                 */
                db.execSQL(
                    """
                    ALTER TABLE arthropods
                    ADD COLUMN adultLengthMaxMm REAL
                    """.trimIndent()
                )


                /*
                 * Longitud promedio documentada.
                 */
                db.execSQL(
                    """
                    ALTER TABLE arthropods
                    ADD COLUMN averageAdultLengthMm REAL
                    """.trimIndent()
                )


                /*
                 * Variabilidad intraespecífica.
                 */
                db.execSQL(
                    """
                    ALTER TABLE arthropods
                    ADD COLUMN variability TEXT
                    """.trimIndent()
                )


                /*
                 * Dimorfismo sexual.
                 */
                db.execSQL(
                    """
                    ALTER TABLE arthropods
                    ADD COLUMN sexualDimorphism TEXT
                    """.trimIndent()
                )


                /*
                 * Explicación biológica del hábitat.
                 */
                db.execSQL(
                    """
                    ALTER TABLE arthropods
                    ADD COLUMN habitatExplanation TEXT
                    """.trimIndent()
                )


                /*
                 * =============================================================
                 * TABLA: arthropod_images
                 * =============================================================
                 */


                /*
                 * stableId es diferente.
                 *
                 * Nuestro nuevo Entity exige:
                 *
                 * val stableId: String
                 *
                 * es decir:
                 *
                 * NOT NULL.
                 *
                 * Pero las fotografías que ya existen fueron creadas
                 * antes de que stableId existiera.
                 *
                 * Primero añadimos la columna con un valor provisional.
                 */
                db.execSQL(
                    """
                    ALTER TABLE arthropod_images
                    ADD COLUMN stableId TEXT NOT NULL DEFAULT ''
                    """.trimIndent()
                )


                /*
                 * Ahora asignamos un identificador estable y diferente
                 * a cada fotografía existente.
                 *
                 * Ejemplo:
                 *
                 * dynastes-hercules-image-1
                 *
                 * Utilizamos:
                 *
                 * arthropodId + "-image-" + id interno de Room
                 *
                 * De esta forma dos imágenes nunca reciben
                 * accidentalmente el mismo stableId.
                 */
                db.execSQL(
                    """
                    UPDATE arthropod_images
                    SET stableId =
                        arthropodId || '-image-' || id
                    WHERE stableId = ''
                    """.trimIndent()
                )


                /*
                 * Sexo del ejemplar fotografiado.
                 */
                db.execSQL(
                    """
                    ALTER TABLE arthropod_images
                    ADD COLUMN specimenSex TEXT
                    """.trimIndent()
                )


                /*
                 * Localidad.
                 */
                db.execSQL(
                    """
                    ALTER TABLE arthropod_images
                    ADD COLUMN locality TEXT
                    """.trimIndent()
                )


                /*
                 * País.
                 */
                db.execSQL(
                    """
                    ALTER TABLE arthropod_images
                    ADD COLUMN country TEXT
                    """.trimIndent()
                )


                /*
                 * Fecha asociada con el registro.
                 */
                db.execSQL(
                    """
                    ALTER TABLE arthropod_images
                    ADD COLUMN date TEXT
                    """.trimIndent()
                )


                /*
                 * Código de catálogo o voucher.
                 */
                db.execSQL(
                    """
                    ALTER TABLE arthropod_images
                    ADD COLUMN catalogCode TEXT
                    """.trimIndent()
                )


                /*
                 * Relación con una fotografía representativa
                 * del sexo contrario.
                 */
                db.execSQL(
                    """
                    ALTER TABLE arthropod_images
                    ADD COLUMN oppositeSexImageId TEXT
                    """.trimIndent()
                )


                /*
                 * ArthropodImageEntity declara:
                 *
                 * Index(
                 *     value = ["stableId"],
                 *     unique = true
                 * )
                 *
                 * Por eso debemos crear también ese índice
                 * durante la migración.
                 */
                db.execSQL(
                    """
                    CREATE UNIQUE INDEX IF NOT EXISTS
                    index_arthropod_images_stableId
                    ON arthropod_images(stableId)
                    """.trimIndent()
                )
            }
        }
    }
}
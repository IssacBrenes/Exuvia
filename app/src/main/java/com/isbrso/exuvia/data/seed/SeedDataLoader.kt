package com.isbrso.exuvia.data.seed

import android.content.Context
import androidx.room.withTransaction
import com.isbrso.exuvia.data.local.ExuviaDatabase
import com.isbrso.exuvia.data.seed.mapper.toEntities
import com.isbrso.exuvia.data.seed.model.SeedCollectionDto
import kotlinx.serialization.json.Json
import com.isbrso.exuvia.data.local.preferences.SeedVersionStore

/*
 * Lee la colección inicial incluida en assets y la inserta en Room.
 *
 * La importación se ejecuta únicamente cuando la base está vacía.
 *
 * Esta clase no contiene información científica directamente:
 * solo interpreta el archivo JSON y coordina su persistencia.
 */
class SeedDataLoader(
    private val context: Context,
    private val database: ExuviaDatabase,
    private val seedVersionStore: SeedVersionStore
) {

    /*
     * Configuración del lector JSON.
     *
     * ignoreUnknownKeys permite que el archivo incorpore campos nuevos
     * sin romper inmediatamente versiones anteriores de la aplicación.
     *
     * explicitNulls = false permite omitir del JSON propiedades opcionales
     * cuando el DTO tenga un valor predeterminado. En los DTO actuales,
     * los campos nullable siguen pudiendo declararse explícitamente como null.
     */
    private val json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    /*
     * Comprueba si Room ya contiene datos.
     *
     * Si existen artrópodos, termina sin volver a importar el archivo.
     * Si la base está vacía, lee e inserta la colección inicial.
     */
    /*
 * Comprueba si la colección incluida en assets es más reciente
 * que la importada anteriormente.
 *
 * Si es la misma versión, no hace trabajo innecesario.
 *
 * Si es una versión nueva, actualiza los registros dentro
 * de una única transacción.
 */
    suspend fun ensureSeeded() {
        val arthropodDao = database.arthropodDao()

        /*
         * Primero leemos siempre el archivo para conocer
         * qué contentVersion contiene.
         */
        val jsonContent = context.assets
            .open(SEED_FILE_PATH)
            .bufferedReader()
            .use { reader ->
                reader.readText()
            }

        /*
         * Convertimos el documento en DTO antes de decidir
         * si necesita importarse.
         */
        val seedCollection =
            json.decodeFromString<SeedCollectionDto>(
                jsonContent
            )

        /*
         * Versión que esta instalación importó anteriormente.
         *
         * Si todavía no existe, SeedVersionStore devuelve 0.
         */
        val importedContentVersion =
            seedVersionStore.getImportedContentVersion()

        /*
         * También comprobamos que realmente existan datos en Room.
         *
         * Esto cubre el caso donde DataStore conserve una versión,
         * pero la base haya sido eliminada o recreada.
         */
        val databaseHasArthropods =
            arthropodDao.countArthropods() > 0

        /*
         * No es necesario importar cuando:
         *
         * 1. Room contiene datos.
         * 2. La versión instalada es igual o superior
         *    a la que trae el JSON.
         */
        if (
            databaseHasArthropods &&
            importedContentVersion >=
            seedCollection.contentVersion
        ) {
            return
        }

        /*
         * Todas las modificaciones se realizan como una unidad.
         *
         * Si alguna especie, fotografía o referencia falla,
         * Room revierte completamente esta actualización.
         */
        database.withTransaction {
            seedCollection.arthropods.forEach { arthropodDto ->

                val entities =
                    arthropodDto.toEntities()

                /*
                 * Limpiamos primero las colecciones dependientes.
                 *
                 * Así la nueva edición reemplaza las imágenes
                 * y referencias en lugar de duplicarlas.
                 */
                arthropodDao.deleteImagesForArthropod(
                    arthropodId = arthropodDto.id
                )

                arthropodDao.deleteReferencesForArthropod(
                    arthropodId = arthropodDto.id
                )

                /*
                 * Inserta o reemplaza la información principal.
                 */
                arthropodDao.insertArthropod(
                    arthropod = entities.arthropod
                )

                /*
                 * Inserta la edición actual de las fotografías.
                 */
                if (entities.images.isNotEmpty()) {
                    arthropodDao.insertImages(
                        images = entities.images
                    )
                }

                /*
                 * Inserta la edición actual de las referencias.
                 */
                if (entities.references.isNotEmpty()) {
                    arthropodDao.insertReferences(
                        references = entities.references
                    )
                }
            }
        }

        /*
         * La versión se guarda únicamente después de que Room
         * haya confirmado toda la transacción.
         *
         * Si la importación falla, esta línea no se ejecuta
         * y Exuvia volverá a intentarlo la próxima vez.
         */
        seedVersionStore.saveImportedContentVersion(
            version = seedCollection.contentVersion
        )
    }

    private companion object {
        /*
         * Ruta relativa dentro de app/src/main/assets.
         */
        const val SEED_FILE_PATH = "seed/arthropods.json"
    }
}
package com.isbrso.exuvia.data.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import java.io.IOException
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/*
 * Se declara en el nivel superior del archivo para que exista
 * una sola instancia de DataStore asociada con este nombre.
 *
 * Android guardará internamente un archivo de preferencias llamado
 * seed_metadata.preferences_pb.
 */
private val Context.seedMetadataDataStore:
        DataStore<Preferences> by preferencesDataStore(
    name = "seed_metadata"
)

/*
 * SeedVersionStore administra los metadatos relacionados
 * con la colección inicial de Exuvia.
 *
 * Su responsabilidad actual es recordar cuál contentVersion
 * fue importada correctamente.
 *
 * No almacena artrópodos ni información científica.
 * Esa información continúa perteneciendo a Room.
 */
class SeedVersionStore(
    private val context: Context
) {

    /*
     * Las claves de DataStore se concentran aquí.
     *
     * Esto evita escribir manualmente el mismo texto
     * en diferentes funciones.
     */
    private object PreferenceKeys {

        /*
         * Número de la última versión de contenido importada.
         *
         * Ejemplo:
         *
         * 0 → nunca se importó una colección
         * 1 → se importó contentVersion 1
         * 2 → se importó contentVersion 2
         */
        val importedContentVersion =
            intPreferencesKey(
                name = "imported_content_version"
            )
    }

    /*
     * Obtiene la versión ya importada.
     *
     * DataStore expone sus valores mediante Flow,
     * pero aquí solo necesitamos el valor actual.
     *
     * first() obtiene la primera emisión y termina la lectura.
     */
    suspend fun getImportedContentVersion(): Int {
        return context.seedMetadataDataStore.data

            /*
             * Si el archivo de preferencias no puede leerse
             * debido a un problema de entrada/salida,
             * utilizamos preferencias vacías.
             *
             * Otros errores se vuelven a lanzar porque podrían
             * indicar un problema de programación más serio.
             */
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }

            /*
             * Convierte Preferences en el número que necesitamos.
             *
             * Si la clave todavía no existe, devuelve 0.
             */
            .map { preferences ->
                preferences[
                    PreferenceKeys.importedContentVersion
                ] ?: 0
            }

            .first()
    }

    /*
     * Registra que una versión fue importada exitosamente.
     *
     * Solo debe llamarse después de que la transacción de Room
     * haya terminado correctamente.
     */
    suspend fun saveImportedContentVersion(
        version: Int
    ) {
        context.seedMetadataDataStore.edit { preferences ->
            preferences[
                PreferenceKeys.importedContentVersion
            ] = version
        }
    }
}
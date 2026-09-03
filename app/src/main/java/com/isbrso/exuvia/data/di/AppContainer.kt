package com.isbrso.exuvia.data.di

import android.content.Context
import androidx.room.Room
import com.isbrso.exuvia.data.local.ExuviaDatabase
import com.isbrso.exuvia.data.repository.ArthropodRepository
import com.isbrso.exuvia.data.repository.DefaultArthropodRepository
import com.isbrso.exuvia.data.seed.SeedDataLoader
import com.isbrso.exuvia.data.local.preferences.SeedVersionStore

interface AppContainer {
    val arthropodRepository: ArthropodRepository
}

class DefaultAppContainer(
    context: Context
) : AppContainer {

    private val database: ExuviaDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            ExuviaDatabase::class.java,
            "exuvia_database"
        )
            /*
             * Le indica a Room cómo transformar una base v1
             * cuando la aplicación espera una base v2.
             */
            .addMigrations(
                ExuviaDatabase.MIGRATION_1_2
            )
            .build()

    /*
     * Metadatos técnicos de la colección.
     */
    private val seedVersionStore =
        SeedVersionStore(
            context = context.applicationContext
        )

    /*
     * Importa o actualiza la colección inicial.
     */
    private val seedDataLoader =
        SeedDataLoader(
            context = context.applicationContext,
            database = database,
            seedVersionStore = seedVersionStore
        )

    override val arthropodRepository:
            ArthropodRepository =
        DefaultArthropodRepository(
            arthropodDao =
                database.arthropodDao(),
            seedDataLoader =
                seedDataLoader
        )
}
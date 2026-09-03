package com.isbrso.exuvia.data.repository

import com.isbrso.exuvia.data.local.dao.ArthropodDao
import com.isbrso.exuvia.data.mapper.toDomain
import com.isbrso.exuvia.data.seed.SeedDataLoader
import com.isbrso.exuvia.domain.model.Arthropod

/*
 * Implementación real del repositorio.
 *
 * Coordina:
 *
 * - la preparación inicial de la colección;
 * - la consulta de Room;
 * - la conversión hacia el modelo de dominio.
 */
class DefaultArthropodRepository(
    private val arthropodDao: ArthropodDao,
    private val seedDataLoader: SeedDataLoader
) : ArthropodRepository {

    override suspend fun getRandomArthropod(): Arthropod? {

        /*
         * Garantiza que la colección incluida en assets esté cargada.
         *
         * Si Room ya contiene datos, ensureSeeded() termina rápidamente.
         * Si está vacío, importa el JSON antes de ejecutar la consulta.
         */
        seedDataLoader.ensureSeeded()

        /*
         * Room devuelve ArthropodWithDetails?.
         *
         * La llamada segura ?. ejecuta toDomain() únicamente
         * cuando el DAO encontró un registro.
         */
        return arthropodDao
            .getRandomArthropodWithDetails()
            ?.toDomain()
    }
}
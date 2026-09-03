package com.isbrso.exuvia.data.repository

import com.isbrso.exuvia.domain.model.Arthropod

interface ArthropodRepository {

    suspend fun getRandomArthropod(): Arthropod?
}
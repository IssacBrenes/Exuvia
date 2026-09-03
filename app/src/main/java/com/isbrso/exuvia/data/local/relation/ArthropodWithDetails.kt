//consulta las tres tablas juntas.
package com.isbrso.exuvia.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.isbrso.exuvia.data.local.entity.ArthropodEntity
import com.isbrso.exuvia.data.local.entity.ArthropodImageEntity
import com.isbrso.exuvia.data.local.entity.ArthropodReferenceEntity

//es una clase que indica a room como unir conceptualmente un artropodo
data class ArthropodWithDetails(

    //indica que ArthropodEntity es el objeto principal del resultado
    @Embedded
    val arthropod: ArthropodEntity,

    //define como se relacionan las tablas
    @Relation(
        parentColumn = "id",
        entityColumn = "arthropodId"
    )
    val images: List<ArthropodImageEntity>,

    @Relation(
        parentColumn = "id",
        entityColumn = "arthropodId"
    )
    //Room consulta los registros asociados y construye las listas
    val references: List<ArthropodReferenceEntity>
)
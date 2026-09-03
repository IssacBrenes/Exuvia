//Cada fila será una fuente utilizada para respaldar la información científica de una especie.
//Relación de uno a mucho. un artropodo puede tener muchas referencias
package com.isbrso.exuvia.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "arthropod_references",
    foreignKeys = [
        ForeignKey(
            entity = ArthropodEntity::class,
            parentColumns = ["id"],
            childColumns = ["arthropodId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["arthropodId"])
    ]
)
data class ArthropodReferenceEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val arthropodId: String,

    val title: String,
    val authors: String?,
    val organization: String?,
    val publicationYear: Int?,

    val url: String,
    val doi: String?, //el doi es un identificador persistente de publicaciones cientificas
    val sourceType: String
)
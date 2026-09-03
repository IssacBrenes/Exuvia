//el dao es el que define las operaciones permitidas sobre las tablas de Room
/*dao: data access object. Su responsabilidad es definir como el resto de la capa local puede:
* insertar información, consultar registros, actualizar datos, eliminar registros */

package com.isbrso.exuvia.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.isbrso.exuvia.data.local.entity.ArthropodEntity
import com.isbrso.exuvia.data.local.entity.ArthropodImageEntity
import com.isbrso.exuvia.data.local.entity.ArthropodReferenceEntity
import kotlinx.coroutines.flow.Flow
import androidx.room.Transaction
import com.isbrso.exuvia.data.local.relation.ArthropodWithDetails


@Dao //la anotacion le indica a Room que esta interfaz contiene operaciones de acceso a la base de datos
interface ArthropodDao {

    //Inserta una fila. Si ya existe un artropodo con la misma llave primeria Room remplaza el registro anterior.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArthropod(arthropod: ArthropodEntity)
    //suspend indica que la funcion puede realizar una operación que tarda, sin bloquear el hilo principal.

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImages(images: List<ArthropodImageEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReferences(
        references: List<ArthropodReferenceEntity>
    )

    //Busca el artropodo a utilizar. lo busca por id
    @Query(
        """
        SELECT * 
        FROM arthropods
        WHERE id = :arthropodId
        LIMIT 1
        """
    )
    fun observeArthropodById(
        arthropodId: String
    ): Flow<ArthropodEntity?>//flow representa un flujo de valores que puede cambiar con el tiempo

    //Busca el artropodo a mostrar pero lo hace de manera random
    @Query(
        """
        SELECT *
        FROM arthropods
        ORDER BY RANDOM()
        LIMIT 1
        """
    )
    suspend fun getRandomArthropod(): ArthropodEntity?

    //busca la imagen del artropodo seleccionado. primero coloca la imagen principal y despues las demas segun su id
    @Query(
        """
        SELECT *
        FROM arthropod_images
        WHERE arthropodId = :arthropodId
        ORDER BY isPrimary DESC, id ASC
        """
    )
    suspend fun getImagesForArthropod(
        arthropodId: String
    ): List<ArthropodImageEntity>

    //busca la referencia del artropodo seleecionado. acomodadas por año
    @Query(
        """
        SELECT *
        FROM arthropod_references
        WHERE arthropodId = :arthropodId
        ORDER BY publicationYear DESC
        """
    )
    suspend fun getReferencesForArthropod(
        arthropodId: String
    ): List<ArthropodReferenceEntity>

    /*Room necesita realizar varias consultas:Buscar el artrópodo.
     Buscar sus imágenes. Buscar sus referencias.
     @Transaction hace que esas operaciones se interpreten como una unidad coherente.*/
    @Transaction
    @Query(
        """
    SELECT *
    FROM arthropods
    WHERE id = :arthropodId
    LIMIT 1
    """
    )
    suspend fun getArthropodWithDetails(
        arthropodId: String
    ): ArthropodWithDetails?

    @Transaction
    @Query(
        """
    SELECT *
    FROM arthropods
    ORDER BY RANDOM()
    LIMIT 1
    """
    )
    suspend fun getRandomArthropodWithDetails(): ArthropodWithDetails?

    /*
 * Devuelve la cantidad total de artrópodos almacenados.
 *
 * El cargador de datos iniciales utilizará esta consulta para evitar
 * importar el JSON nuevamente cada vez que se abra la aplicación.
 */
    @Query("SELECT COUNT(*) FROM arthropods")
    suspend fun countArthropods(): Int


    /*
 * Elimina las imágenes asociadas con un artrópodo
 * antes de insertar la versión actualizada del JSON.
 *
 * No elimina la entidad principal.
 */
    @Query(
        """
    DELETE FROM arthropod_images
    WHERE arthropodId = :arthropodId
    """
    )
    suspend fun deleteImagesForArthropod(
        arthropodId: String
    )

    /*
     * Elimina las referencias anteriores de una especie.
     *
     * Después de esta operación se insertará la lista completa
     * incluida en la nueva versión de la colección.
     */
    @Query(
        """
    DELETE FROM arthropod_references
    WHERE arthropodId = :arthropodId
    """
    )
    suspend fun deleteReferencesForArthropod(
        arthropodId: String
    )

}
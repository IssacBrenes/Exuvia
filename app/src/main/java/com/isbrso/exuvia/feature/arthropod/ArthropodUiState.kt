//todo lo de este archivo pertenece a:
package com.isbrso.exuvia.feature.arthropod

//importa la clase Arthropod
import com.isbrso.exuvia.domain.model.Arthropod

//sealed: decide quien puede implementar la interfaz
//Hace imposible representar estados invalidos
sealed interface ArthropodUiState {

    //object: solo existirá una unica instancia
    // los dos puntos señalan que hereda o implementa
    data object Loading : ArthropodUiState

    /* se usa class porque aquí sí existe información
    * Succes significa que todo salió bien, pero qué salió bien? */
    data class Success(
        val arthropod: Arthropod //un estado succes siempre debe de traer un Arthropod
    ) : ArthropodUiState

    //mismo razonamiento, algo salió mal pero hay que saber qué salió mal, por eso tiene un message
    data class Error(
        val message: String
    ) : ArthropodUiState
}

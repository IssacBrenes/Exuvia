package com.isbrso.exuvia.feature.arthropod

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.isbrso.exuvia.data.repository.ArthropodRepository

/*
 * Esta fábrica le explica a Android cómo crear
 * DailyArthropodViewModel y entregarle su repositorio.
 *
 * la fabrica recibe el repositorio que utilizara para construir el viewmodel
 */
class ArthropodViewModelFactory(
    private val arthropodRepository: ArthropodRepository
) : ViewModelProvider.Factory {//indica que esta clase cumple el contrato que android espera para una fabica de viewmodels

    /*
     * Android llama a create() cuando necesita una instancia
     * de un ViewModel.
     */
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        /*
         * isAssignableFrom comprueba si el tipo solicitado
         * corresponde a DailyArthropodViewModel.
         */
        if (
            modelClass.isAssignableFrom(
                ArthropodViewModel::class.java
            )
        ) {
            /*
             * Creamos el ViewModel e inyectamos el repositorio.
             *
             * El cast "as T" es necesario porque create() utiliza
             * un tipo genérico T.
             */
            @Suppress("UNCHECKED_CAST")
            return ArthropodViewModel(
                arthropodRepository = arthropodRepository
            ) as T
        }

        /*
         * Si Android solicita un ViewModel que esta fábrica
         * no sabe construir, se lanza una excepción explícita.
         */
        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}"
        )
    }
}
package com.isbrso.exuvia.feature.arthropod

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isbrso.exuvia.data.repository.ArthropodRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.util.Log

/*
 * El ViewModel administra el estado y la lógica de la pantalla principal.
 *
 * No conoce Room, SQLite, DAO ni entidades.
 * Solo depende del contrato ArthropodRepository.
 */
class ArthropodViewModel(
    /*para crear la variable es hay que proporcionarle un repositorio
    * Al utilizar private val, Kotlin hace tres cosas:
    * Recibe el argumento en el constructor.
    * Crea una propiedad llamada arthropodRepository. Impide que otras clases accedan directamente a ella. */
    private val arthropodRepository: ArthropodRepository
) : ViewModel() { //hereda de viewModel

    /*
     * MutableStateFlow mantiene el estado actual y permite modificarlo.
     *
     * Es privado para impedir que la interfaz cambie el estado directamente.
     * Solo el ViewModel controla las transiciones entre:
     *
     * Loading → Success
     *
     * o
     *
     * Loading → Error
     */
    private val _uiState =
        /*Es un contenedor observable que siempre conserva un valor actual
        * el guion bajo indica que esta propiedad es interna, es modificable, no debe de exponerse directamente*/
        MutableStateFlow<ArthropodUiState>(
            ArthropodUiState.Loading
        )

    /*
     * StateFlow expone el estado como solo lectura.
     *
     * La pantalla podrá observar uiState, pero no podrá asignarle
     * un valor nuevo.
     *
     * asStateFlow() oculta las operaciones de modificación
     * de MutableStateFlow. crea una vista publica de solo lectura del estado interno
     */
    val uiState: StateFlow<ArthropodUiState> =
        _uiState.asStateFlow()

    /*
     * init se ejecuta automáticamente cuando se crea el ViewModel.
     *
     * Por eso la primera consulta comienza sin que la pantalla
     * tenga que solicitarla manualmente.
     */
    init {
        loadArthropod()
    }

    /*
     * Solicita al repositorio un artrópodo destacado.
     *
     * Esta función también podrá llamarse posteriormente desde un botón
     * para cargar otro registro.
     */
    fun loadArthropod() {
        _uiState.value = ArthropodUiState.Loading

        viewModelScope.launch {
            runCatching {
                arthropodRepository.getRandomArthropod()
            }.onSuccess { arthropod ->

                _uiState.value =
                    if (arthropod != null) {
                        ArthropodUiState.Success(
                            arthropod = arthropod
                        )
                    } else {
                        ArthropodUiState.Error(
                            message = "Todavía no hay artrópodos disponibles."
                        )
                    }

            }.onFailure { exception ->

                Log.e(
                    "ArthropodViewModel",
                    "Error al cargar el artrópodo",
                    exception
                )

                _uiState.value =
                    ArthropodUiState.Error(
                        message = exception.message
                            ?: "No fue posible cargar el artrópodo."
                    )
            }
        }
    }
}
package com.isbrso.exuvia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.isbrso.exuvia.feature.arthropod.ArthropodRoute
import com.isbrso.exuvia.feature.arthropod.ArthropodViewModel
import com.isbrso.exuvia.feature.arthropod.ArthropodViewModelFactory
import com.isbrso.exuvia.ui.theme.ExuviaTheme
import com.isbrso.exuvia.widget.ExuviaWidgetScheduler
import androidx.glance.appwidget.updateAll
import androidx.lifecycle.lifecycleScope
import com.isbrso.exuvia.widget.ExuviaWidget
import kotlinx.coroutines.launch

/*
 * MainActivity es el punto de entrada visual de la aplicación.
 *
 * Android crea esta Activity cuando el usuario abre Exuvia
 * desde el lanzador.
 */
class MainActivity : ComponentActivity() {

    /*
     * onCreate se ejecuta cuando Android crea la Activity.
     *
     * savedInstanceState puede contener información previa
     * si Android está reconstruyendo la pantalla.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*
         * Recuperamos la instancia personalizada de Application.
         *
         * application es de tipo Application, por eso hacemos
         * un cast hacia ExuviaApplication.
         *
         * Desde ella accedemos al AppContainer y al repositorio.
         */
        val app = application as ExuviaApplication

        ExuviaWidgetScheduler.schedule(
            applicationContext
        )

        /*
 * TEMPORAL — PRUEBA DEL WIDGET
 *
 * Cada vez que abrimos Exuvia solicitamos
 * una actualización inmediata del widget.
 *
 * Esto se eliminará después de comprobar
 * las cuatro especies.
 */
        lifecycleScope.launch {

            ExuviaWidget()
                .updateAll(applicationContext)
        }

        /*
         * setContent reemplaza el sistema tradicional de layouts XML
         * y define la interfaz utilizando Jetpack Compose.
         */
        setContent {

            /*
             * ExuviaTheme aplica colores, tipografía y estilos
             * generales definidos en ui/theme.
             */
            ExuviaTheme {

                /*
                 * viewModel() solicita a Android una instancia
                 * de ArthropodViewModel.
                 *
                 * La Factory explica cómo construirlo,
                 * ya que necesita ArthropodRepository
                 * como parámetro de su constructor.
                 */
                val arthropodViewModel: ArthropodViewModel =
                    viewModel(
                        factory = ArthropodViewModelFactory(
                            arthropodRepository =
                                app.container.arthropodRepository
                        )
                    )

                /*
                 * ArthropodRoute observa el StateFlow del ViewModel
                 * y entrega el estado a ArthropodScreen.
                 */
                ArthropodRoute(
                    viewModel = arthropodViewModel
                )
            }
        }
    }
}
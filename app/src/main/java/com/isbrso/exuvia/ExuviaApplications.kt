package com.isbrso.exuvia

import android.app.Application
import com.isbrso.exuvia.data.di.AppContainer
import com.isbrso.exuvia.data.di.DefaultAppContainer

/*
 * Application representa el proceso completo de Exuvia.
 *
 * Android crea esta clase antes de crear MainActivity.
 * Su instancia permanece activa mientras el proceso de la aplicación exista.
 *
 * Por eso es un lugar adecuado para inicializar dependencias globales,
 * como la base de datos y los repositorios.
 */
class ExuviaApplication : Application() {

    /*
     * lateinit significa que esta propiedad será inicializada después
     * de construir el objeto, pero antes de utilizarla.
     *
     * No se inicializa directamente aquí porque necesitamos esperar
     * a que Android llame a onCreate().
     *
     * private set permite que otras clases lean container,
     * pero impide que lo reemplacen.
     */
    lateinit var container: AppContainer
        private set

    /*
     * onCreate() se ejecuta cuando Android crea la instancia
     * de ExuviaApplication.
     */
    override fun onCreate() {
        /*
         * super.onCreate() ejecuta primero el comportamiento definido
         * por la clase padre Application.
         */
        super.onCreate()

        /*
         * Aquí se crea el contenedor de dependencias.
         *
         * this representa la instancia actual de ExuviaApplication
         * y también funciona como Context.
         */
        container = DefaultAppContainer(
            context = this
        )
    }
}
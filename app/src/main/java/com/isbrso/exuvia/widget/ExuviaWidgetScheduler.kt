package com.isbrso.exuvia.widget

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

/*
 * =============================================================================
 * ExuviaWidgetScheduler
 * =============================================================================
 *
 * Configura la actualización periódica del widget.
 *
 * WorkManager impone un intervalo mínimo de aproximadamente
 * 15 minutos para trabajo periódico.
 *
 * Durante desarrollo utilizaremos ese mínimo.
 *
 * Más adelante cambiaremos este valor al intervalo real
 * elegido para Exuvia.
 * =============================================================================
 */
object ExuviaWidgetScheduler {

    private const val WORK_NAME =
        "exuvia_widget_periodic_update"

    fun schedule(context: Context) {

        /*
         * Solicitud periódica.
         *
         * IMPORTANTE:
         * 15 minutos es para desarrollo.
         */
        val request =
            PeriodicWorkRequestBuilder<ExuviaWidgetWorker>(
                15,
                TimeUnit.MINUTES
            )
                .build()


        /*
         * KEEP evita crear múltiples Workers idénticos
         * cada vez que Exuvia inicia.
         */
        WorkManager
            .getInstance(context)
            .enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                request
            )
    }
}
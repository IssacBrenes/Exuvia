package com.isbrso.exuvia.widget

import android.content.Context
import androidx.glance.appwidget.updateAll
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

/*
 * =============================================================================
 * ExuviaWidgetWorker
 * =============================================================================
 *
 * Trabajo en segundo plano encargado de solicitar
 * una actualización de todos los widgets de Exuvia.
 *
 * No selecciona la especie.
 * Esa responsabilidad sigue perteneciendo a ExuviaWidget.
 * =============================================================================
 */
class ExuviaWidgetWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(
    appContext,
    workerParams
) {

    override suspend fun doWork(): Result {

        return try {

            /*
             * Fuerza la recomposición de todas las
             * instancias existentes de ExuviaWidget.
             */
            ExuviaWidget()
                .updateAll(applicationContext)

            Result.success()

        } catch (exception: Exception) {

            Result.retry()
        }
    }
}
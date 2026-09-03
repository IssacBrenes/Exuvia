package com.isbrso.exuvia.widget

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver


/*
 * =============================================================================
 * ExuviaWidgetReceiver
 * =============================================================================
 *
 * Android no habla directamente con ExuviaWidget.
 *
 * El sistema Android envía eventos al Receiver:
 *
 * - widget añadido;
 * - widget actualizado;
 * - widget eliminado;
 * - widget redimensionado.
 *
 * Glance transforma esos eventos en actualizaciones
 * de nuestro GlanceAppWidget.
 * =============================================================================
 */
class ExuviaWidgetReceiver :
    GlanceAppWidgetReceiver() {

    override val glanceAppWidget:
            GlanceAppWidget =
        ExuviaWidget()
}
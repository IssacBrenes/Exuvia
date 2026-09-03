/*
 * Este archivo registra los plugins disponibles para los módulos
 * que forman parte del proyecto.
 *
 * apply false significa que el plugin se declara aquí, pero se aplicará
 * únicamente en los módulos que realmente lo necesiten.
 */
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ksp) apply false

    /*
     * Plugin oficial de Room.
     *
     * Se utilizará en el módulo app para configurar la carpeta
     * donde se exportarán los esquemas de la base de datos.
     */
    alias(libs.plugins.room) apply false

    /*
    * Registra el plugin de serialización de Kotlin.
    *
    * Este plugin generará el código necesario para convertir
    * objetos Kotlin desde y hacia formatos como JSON.
    */
    alias(libs.plugins.kotlin.serialization) apply false
}
/*
 * Gradle es el sistema de construcción que sigue la configuración escrita
 * en build.gradle.kts.
 *
 * Este archivo funciona como una receta que le indica a Gradle:
 *
 * - qué plugins utilizar;
 * - cómo configurar la aplicación Android;
 * - qué dependencias descargar;
 * - cómo compilar y empaquetar Exuvia.
 *
 * Cuando Android Studio construye la aplicación, Gradle realiza
 * aproximadamente este proceso:
 *
 * 1. Lee los archivos build.gradle.kts.
 * 2. Lee el catálogo gradle/libs.versions.toml.
 * 3. Descarga las dependencias necesarias.
 * 4. Compila el código Kotlin.
 * 5. Procesa las anotaciones mediante KSP.
 * 6. Compila Jetpack Compose y los recursos Android.
 * 7. Genera y empaqueta la aplicación.
 */

/*
 * Los plugins agregan capacidades al módulo.
 *
 * android.application:
 * convierte este módulo en una aplicación Android.
 *
 * kotlin.compose:
 * configura el compilador necesario para Jetpack Compose.
 *
 * ksp:
 * permite que herramientas como Room analicen anotaciones
 * y generen código durante la compilación.
 */
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)

    /*
     * Habilita la configuración Gradle específica de Room.
     *
     * Gracias a este plugin podemos declarar schemaDirectory
     * sin configurar manualmente argumentos de KSP.
     */
    alias(libs.plugins.room)

    /*
    * Permite utilizar @Serializable y generar automáticamente
    * los serializadores de nuestras clases DTO.
    */
    alias(libs.plugins.kotlin.serialization)
}

/*
 * El bloque android configura cómo se construirá la aplicación.
 */
android {
    /*
     * Namespace utilizado por el código generado de Android,
     * como la clase R que permite acceder a recursos.
     */
    namespace = "com.isbrso.exuvia"

    /*
     * Versión de las APIs de Android contra las que se compila Exuvia.
     */
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    /*
     * Configuración base de la aplicación.
     */
    defaultConfig {
        /*
         * Identificador único de Exuvia como aplicación instalada
         * y como futura aplicación publicada.
         */
        applicationId = "com.isbrso.exuvia"

        /*
         * Versión mínima de Android admitida.
         * API 26 corresponde a Android 8.0.
         */
        minSdk = 26

        /*
         * Nivel de Android cuyo comportamiento moderno declara
         * soportar la aplicación.
         */
        targetSdk = 36

        /*
         * Número interno de versión.
         * Debe incrementarse en cada actualización publicada.
         */
        versionCode = 1

        /*
         * Nombre de versión visible para el usuario.
         */
        versionName = "1.0"

        /*
         * Componente que ejecutará las pruebas instrumentadas
         * en un dispositivo físico o emulador.
         */
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    /*
     * Permite configurar distintas variantes de construcción.
     *
     * release representa la versión destinada a distribución.
     */
    buildTypes {
        release {
            /*
             * La optimización permanece desactivada por ahora.
             * Se revisará antes de publicar la aplicación.
             */
            optimization {
                enable = false
            }
        }
    }

    /*
     * Configura la compatibilidad con el ecosistema Java.
     *
     * Aunque Exuvia está escrita en Kotlin, Android y muchas bibliotecas
     * interoperan con código y bytecode Java.
     */
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    /*
     * Activa Jetpack Compose para este módulo.
     */
    buildFeatures {
        compose = true
    }
}

/*
 * Configuración proporcionada por el plugin oficial de Room.
 *
 * Room exportará aquí un archivo JSON por cada versión
 * de la estructura de la base de datos.
 *
 * projectDir representa la carpeta del módulo app.
 *
 * Por tanto, la ruta será:
 *
 * app/schemas/
 */
room {
    schemaDirectory("$projectDir/schemas")
}

/*
 * Las dependencias son bibliotecas externas que Exuvia necesita
 * para compilar, ejecutarse o realizar pruebas.
 *
 * Debe existir un único bloque dependencies.
 */
dependencies {

    /*
     * Compose BOM mantiene compatibles las versiones
     * de las diferentes bibliotecas de Jetpack Compose.
     */
    implementation(platform(libs.androidx.compose.bom))

    /*
     * Integra las Activity de Android con Jetpack Compose
     * y permite utilizar setContent().
     */
    implementation(libs.androidx.activity.compose)

    /*
     * Componentes visuales basados en Material Design 3.
     */
    implementation(libs.androidx.compose.material3)

    /*
     * Núcleo de la interfaz de Jetpack Compose.
     */
    implementation(libs.androidx.compose.ui)

    /*
     * Herramientas para gráficos, colores y dibujo.
     */
    implementation(libs.androidx.compose.ui.graphics)

    /*
     * Permite crear vistas previas de componentes Compose
     * dentro de Android Studio.
     */
    implementation(libs.androidx.compose.ui.tooling.preview)

    /*
     * Extensiones Kotlin para APIs comunes de Android.
     */
    implementation(libs.androidx.core.ktx)

    /*
     * Integración básica entre Kotlin, corrutinas
     * y el ciclo de vida Android.
     */
    implementation(libs.androidx.lifecycle.runtime.ktx)

    /*
     * Proporciona ViewModel y viewModelScope.
     *
     * viewModelScope permite ejecutar corrutinas que se cancelan
     * automáticamente cuando el ViewModel deja de existir.
     */
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    /*
     * Permite crear y recuperar ViewModels desde Compose
     * mediante la función viewModel().
     */
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    /*
     * Permite observar StateFlow desde Compose respetando
     * el ciclo de vida mediante collectAsStateWithLifecycle().
     */
    implementation(libs.androidx.lifecycle.runtime.compose)

    /*
     * Funcionalidad principal de Room durante la ejecución.
     */
    implementation(libs.androidx.room.runtime)

    /*
     * Integración de Room con Kotlin, corrutinas y Flow.
     */
    implementation(libs.androidx.room.ktx)

    /*
     * Procesador de anotaciones de Room.
     *
     * KSP analiza @Entity, @Dao y @Database y genera
     * las implementaciones necesarias durante la compilación.
     */
    ksp(libs.androidx.room.compiler)

    /*
     * JUnit para pruebas unitarias ejecutadas localmente.
     */
    testImplementation(libs.junit)

    /*
     * Compose BOM para las pruebas instrumentadas.
     */
    androidTestImplementation(platform(libs.androidx.compose.bom))

    /*
     * Herramientas para probar interfaces Compose
     * en dispositivo o emulador.
     */
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)

    /*
     * Espresso para pruebas de interfaz Android.
     */
    androidTestImplementation(libs.androidx.espresso.core)

    /*
     * Integración de JUnit con pruebas instrumentadas Android.
     */
    androidTestImplementation(libs.androidx.junit)

    /*
     * Dependencias disponibles únicamente en compilaciones debug.
     */
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)

    /*
 * Permite leer y escribir documentos JSON mediante clases Kotlin tipadas.
 */
    implementation(libs.kotlinx.serialization.json)

    /*
 * Integra Coil con Jetpack Compose y proporciona AsyncImage.
 */
    implementation(libs.coil.compose)

    /*
     * Añade soporte de red mediante OkHttp para cargar imágenes
     * desde direcciones HTTP y HTTPS.
     */
    implementation(libs.coil.network.okhttp)

    /*
 * Preferences DataStore almacenará metadatos pequeños de Exuvia.
 *
 * En esta etapa se utilizará para recordar qué versión
 * de la colección inicial ya fue importada.
 */
    implementation(libs.androidx.datastore.preferences)

    /*
    * Jetpack Glance permite construir el widget de Exuvia
    * utilizando una API declarativa similar a Compose.
    */
    implementation(libs.androidx.glance.appwidget)

    implementation(libs.androidx.work.runtime.ktx)
}

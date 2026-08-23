# InterCommerce App (Android Nativo)

MVP de una aplicación móvil de E-commerce robusta, optimizada para rendimiento nativo en Android utilizando Kotlin.

## Arquitectura y Decisiones Técnicas

El proyecto sigue estrictamente los principios de **Clean Architecture** (Presentation, Domain, Data) junto con el patrón **MVVM** en la capa de UI.

*   **Persistencia Local Obligatoria (Room):** Se optó por **Room (SQLite)** en lugar de SharedPreferences o DataStore. Room ofrece una capa de abstracción robusta sobre SQLite, permitiendo consultas estructuradas, migraciones de esquemas y lo más importante: **seguridad de que el carrito sobrevivirá a cierres o purgas de memoria**, gracias a que se persiste directamente en disco.
*   **Mitigación de Pérdida de Datos (Offline-First):** La carga del catálogo emplea *Paging 3*. Nuestro ProductPagingSource funciona como un repositorio Offline-First: primero intenta consultar a la red (API de DummyJSON), si tiene éxito, guarda los resultados en la caché de Room. Si el dispositivo pierde la conexión (ej. IOException), la aplicación se degrada de manera elegante mostrando la página solicitada directamente desde Room.

## Respuestas Técnicas (Profundidad Técnica)

### 1. Arquitectura y Resiliencia
**Pregunta:** ¿Cómo la inversión de dependencia y el uso de interfaces en la capa de Domain facilitarían migrar de SQLite (Room) a ObjectBox sin alterar la UI en Compose?
**Respuesta:** Gracias a la Inversión de Dependencias (DIP), la capa de Dominio define contratos (interfaces como CartRepository) y la capa de Presentación (ViewModels/Compose) solo depende de estos contratos. La implementación técnica reside en la capa de Data (CartRepositoryImpl usando Room). Para migrar a ObjectBox, simplemente crearíamos un nuevo ObjectBoxCartRepositoryImpl, lo proveeríamos a través del módulo de inyección de dependencias (Hilt) reemplazando la implementación anterior, y ninguna de las clases de Dominio ni de UI tendrían que ser modificadas. 

### 2. Estrategias Offline
**Pregunta:** ¿Cómo gestionaría la sincronización de datos de la API para evitar conflictos de concurrencia o sobrescritura de datos locales si el usuario añade elementos al carrito mientras no tiene conexión?
**Respuesta:** Cuando la app está offline, las acciones (añadir, quitar) mutan únicamente la base de datos local (Room) marcando cada registro con un estado (ej. sync_status = PENDING) o guardando la acción en una cola local. Al detectar que la conexión vuelve (usando ConnectivityManager o WorkManager), se dispara un Job de sincronización en *background* que envía los cambios a la API. Para resolver conflictos de concurrencia se pueden usar estrategias como *Last Write Wins* (basado en un *timestamp* de la modificación local contra el del servidor) o realizar un *Merge* de deltas, dando prioridad a las interacciones explícitas del usuario.

### 3. Seguridad y Profiling
**Pregunta:** ¿Qué estrategias de cifrado implementaría si los datos contuvieran información sensible, y qué herramientas de Profiler utilizaría?
**Respuesta:** 
*   **Cifrado:** Utilizaría **SQLCipher** integrado con Room para cifrar la base de datos completa. La clave de cifrado se generaría de forma segura y se almacenaría en el **Android Keystore** o en *EncryptedSharedPreferences*. Para la red, asegurar el uso de HTTPS y *Certificate Pinning*.
*   **Profiling:** Utilizaría el **App Inspection (Database Inspector)** de Android Studio para visualizar la base de datos en tiempo real y ejecutar consultas SQL de prueba. Para medir el rendimiento, usaría el **CPU Profiler** (para detectar llamadas a DB que puedan estar bloqueando el hilo principal o causando *Jank*) y el **Memory Profiler** para evitar fugas de memoria al cargar el catálogo de productos.

## Ejecución del Proyecto

1. Abrir el proyecto en **Android Studio**.
2. Esperar a que Gradle descargue las dependencias y sincronice (Compose, Room, Hilt, Retrofit).
3. Seleccionar el emulador o dispositivo físico y hacer clic en **Run (Shift + F10)**.

### Ejecución de Pruebas Unitarias (Troubleshooting)

Para ejecutar la suite de pruebas unitarias desde la terminal, utiliza el siguiente comando:
`ash
./gradlew test
`

**⚠️ Nota sobre el error JAVA_HOME is not set:**
Si al ejecutar el comando anterior en tu terminal de Windows (PowerShell) obtienes un error indicando que no se encuentra Java, se debe a que tu consola no reconoce el JDK interno que usa Android Studio. Puedes solucionarlo fácilmente de dos maneras:

* **Opción 1 (Por consola):** Dile a tu terminal dónde está el Java de Android Studio ejecutando esta variable de entorno justo antes de correr las pruebas:
  `powershell
  $env:JAVA_HOME = "C:\Program Files\Android\Android Studio\jbr"
  ./gradlew test
  `
* **Opción 2 (Interfaz de Android Studio):** Sin usar la consola, abre la pestaña *Project* a tu izquierda, navega hasta pp/src/test/java/..., haz clic derecho sobre la carpeta raíz de las pruebas y selecciona el botón verde **"Run 'Tests in...'"**.

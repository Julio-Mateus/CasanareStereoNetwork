# 📻 Casanare Stereo 🎶

**Casanare Stereo** es una aplicación móvil de radio digital con dos roles principales:

🎙️ **EMISORA**  
Publica noticias, podcasts, programación, y todo lo relacionado con una emisora de radio.

👤 **USUARIO**  
Consume la información publicada por las emisoras, puede guardar sus favoritas ⭐, dar like ❤️, dejar comentarios 💬 y explorar todo el contenido disponible.

---

## 🛠️ Tecnologías y dependencias

| 📦 Categoría        | 🚀 Tecnologías                                  |
|:-------------------|:-----------------------------------------------|
| 🖥️ Lenguaje         | [Kotlin 1.9.25](https://kotlinlang.org)         |
| 🎨 UI               | Jetpack Compose, Compose Material, Material3    |
| 📱 Android          | Android Gradle Plugin 8.3.2, Gradle 8.4         |
| ☁️ Firebase         | Auth, Firestore, Analytics, Crashlytics         |
| 📡 Google Services  | Play Services Auth, Location                    |
| 🎥 Video & Audio    | ExoPlayer, YouTube Player                        |
| 📆 Fechas           | kotlinx-datetime                                 |
| 🧪 Test             | JUnit, Espresso                                  |

🔗 Ver más en [`gradle/libs.versions.toml`](./gradle/libs.versions.toml)

---

## 📥 Instalación y ejecución

### ✅ Requisitos

- Android Studio **Hedgehog** o superior
- **JDK 17**
- **Gradle 8.4** (ya configurado vía wrapper)
- Cuenta y proyecto en **Firebase**

### 📝 Pasos para correr la app

1. **Clona el repositorio**
   ```bash
   git clone https://github.com/Julio-Mateus/CasanareStereo.git
   cd CasanareStereo
2. Abre el proyecto en Android Studio

    File → Open

    Selecciona la carpeta del proyecto

3. Configura google-services.json

    Descárgalo desde tu consola de Firebase

    Colócalo en app/src/main/

4. Sincroniza y descarga dependencias

    File → Sync Project with Gradle Files

5. Corre la aplicación

    Elige un dispositivo o emulador

    Haz clic en ▶️ Run

    🗂️ Estructura del Proyecto
    .
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/                      # Código fuente Kotlin
│   │   │   ├── res/                       # Recursos XML
│   │   │   └── AndroidManifest.xml
│   ├── build.gradle.kts
│   └── google-services.json               # (Agregarlo tú)
├── gradle/
│   └── wrapper/
│       └── gradle-wrapper.properties
├── gradle/libs.versions.toml              # Versiones centralizadas de dependencias
├── settings.gradle.kts
└── build.gradle.kts

📌 Roles en la app
Rol	Descripción
🎙️ EMISORA -	Publica contenido: noticias, podcasts, programación, eventos y más.
👤 USUARIO	- Consume contenido, guarda favoritos ⭐, da like ❤️, comenta 💬, y explora.

📚 Comandos útiles

Comando | Descripción

./gradlew assembleDebug | Compilar la app en modo Debug
./gradlew test | Ejecutar los tests unitarios
./gradlew clean | Limpiar el proyecto

🔐 Notas importantes

    📌 Este proyecto usa version catalogs con libs.versions.toml para centralizar todas las versiones.

    📌 Asegúrate de tener configurado tu proyecto en Firebase y descargar correctamente el google-services.json.

    📌 Respetar las versiones especificadas evita conflictos de dependencias.

    ❤️ Autor

Casanare Stereo
GitHub Julio Mateus
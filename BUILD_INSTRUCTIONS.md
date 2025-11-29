# Instrucciones de Compilación - DMX Mobile

## 🔨 Métodos de Compilación

### Método 1: Android Studio (Recomendado)

#### Paso 1: Instalar Android Studio
1. Descarga desde: https://developer.android.com/studio
2. Instala Android Studio
3. Durante instalación, asegúrate de incluir:
   - Android SDK
   - Android SDK Platform
   - Android Virtual Device (opcional)

#### Paso 2: Abrir Proyecto
```bash
# Navegar al proyecto
cd /home/polula/Documentos/DMX_MOBILE

# Abrir Android Studio
# File → Open → Seleccionar carpeta DMX_MOBILE
```

#### Paso 3: Sincronizar Gradle
- Android Studio detectará el proyecto automáticamente
- Aparecerá mensaje "Gradle sync in progress..."
- Esperar a que termine (puede tardar 2-5 minutos la primera vez)
- Si hay errores, hacer clic en "Try Again"

#### Paso 4: Configurar SDK
1. Tools → SDK Manager
2. Verificar que esté instalado:
   - Android SDK Platform 34
   - Android SDK Build-Tools 34.0.0
   - Android SDK Platform-Tools
   - Android SDK Tools

#### Paso 5: Compilar
**Opción A - Debug (Desarrollo):**
```
Build → Make Project (Ctrl+F9)
```
Genera APK en: `app/build/outputs/apk/debug/app-debug.apk`

**Opción B - Release (Producción):**
```
Build → Build Bundle(s) / APK(s) → Build APK(s)
```
Genera APK en: `app/build/outputs/apk/release/`

#### Paso 6: Instalar en Dispositivo
**Con dispositivo conectado:**
```
Run → Run 'app' (Shift+F10)
```

**O instalar APK manualmente:**
1. Copiar APK al dispositivo
2. Habilitar "Instalar apps desconocidas"
3. Abrir APK y tocar "Instalar"

---

### Método 2: Línea de Comandos

#### Requisitos Previos
```bash
# Verificar Java JDK instalado
java -version
# Debe mostrar Java 11 o superior

# Si no está instalado:
sudo apt install openjdk-17-jdk  # Linux
# O descargar de: https://adoptium.net/
```

#### Paso 1: Navegar al Proyecto
```bash
cd /home/polula/Documentos/DMX_MOBILE
```

#### Paso 2: Dar Permisos a Gradle Wrapper
```bash
chmod +x gradlew
```

#### Paso 3: Compilar

**Debug Build:**
```bash
./gradlew assembleDebug
```
Output: `app/build/outputs/apk/debug/app-debug.apk`

**Release Build:**
```bash
./gradlew assembleRelease
```
Output: `app/build/outputs/apk/release/app-release-unsigned.apk`

**Limpiar antes de compilar:**
```bash
./gradlew clean assembleDebug
```

#### Paso 4: Instalar en Dispositivo
```bash
# Verificar que dispositivo esté conectado
adb devices

# Instalar APK
./gradlew installDebug

# O manualmente
adb install app/build/outputs/apk/debug/app-debug.apk
```

---

### Método 3: Build Automatizado (CI/CD)

#### GitHub Actions (Ejemplo)
Crea `.github/workflows/build.yml`:

```yaml
name: Android CI

on:
  push:
    branches: [ main ]
  pull_request:
    branches: [ main ]

jobs:
  build:
    runs-on: ubuntu-latest
    
    steps:
    - uses: actions/checkout@v3
    
    - name: Set up JDK 17
      uses: actions/setup-java@v3
      with:
        java-version: '17'
        distribution: 'temurin'
        
    - name: Grant execute permission for gradlew
      run: chmod +x gradlew
      
    - name: Build with Gradle
      run: ./gradlew assembleDebug
      
    - name: Upload APK
      uses: actions/upload-artifact@v3
      with:
        name: app-debug
        path: app/build/outputs/apk/debug/app-debug.apk
```

---

## 🔐 Firmar APK para Release

### Paso 1: Crear Keystore
```bash
keytool -genkey -v -keystore dmx-mobile-release.keystore \
  -alias dmx-mobile -keyalg RSA -keysize 2048 -validity 10000

# Responder preguntas:
# - Contraseña del keystore
# - Nombre, organización, etc.
```

### Paso 2: Configurar Gradle
Crea `keystore.properties` (no incluir en Git):
```properties
storePassword=TU_PASSWORD
keyPassword=TU_KEY_PASSWORD
keyAlias=dmx-mobile
storeFile=/ruta/a/dmx-mobile-release.keystore
```

### Paso 3: Modificar app/build.gradle
Agregar antes de `android {`:
```gradle
def keystorePropertiesFile = rootProject.file("keystore.properties")
def keystoreProperties = new Properties()
if (keystorePropertiesFile.exists()) {
    keystoreProperties.load(new FileInputStream(keystorePropertiesFile))
}
```

Dentro de `android {`, después de `buildTypes {`:
```gradle
signingConfigs {
    release {
        if (keystorePropertiesFile.exists()) {
            keyAlias keystoreProperties['keyAlias']
            keyPassword keystoreProperties['keyPassword']
            storeFile file(keystoreProperties['storeFile'])
            storePassword keystoreProperties['storePassword']
        }
    }
}

buildTypes {
    release {
        signingConfig signingConfigs.release
        minifyEnabled true
        proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
    }
}
```

### Paso 4: Compilar Release Firmado
```bash
./gradlew assembleRelease
```
Output: `app/build/outputs/apk/release/app-release.apk` (firmado)

---

## 📦 Generar AAB (Android App Bundle)

Para publicar en Google Play Store:

```bash
./gradlew bundleRelease
```

Output: `app/build/outputs/bundle/release/app-release.aab`

---

## 🧹 Comandos Útiles de Gradle

### Limpiar proyecto
```bash
./gradlew clean
```

### Ver todas las tareas disponibles
```bash
./gradlew tasks
```

### Compilar con stacktrace detallado
```bash
./gradlew assembleDebug --stacktrace
```

### Compilar sin caché
```bash
./gradlew assembleDebug --no-build-cache
```

### Ver dependencias
```bash
./gradlew app:dependencies
```

### Verificar código (lint)
```bash
./gradlew lint
```

### Ejecutar tests
```bash
./gradlew test
```

---

## 🐛 Solución de Problemas de Compilación

### Error: "SDK location not found"
**Solución:**
Crea `local.properties`:
```properties
sdk.dir=/ruta/a/Android/Sdk
```

Linux: `sdk.dir=/home/usuario/Android/Sdk`
Mac: `sdk.dir=/Users/usuario/Library/Android/sdk`
Windows: `sdk.dir=C\:\\Users\\usuario\\AppData\\Local\\Android\\Sdk`

### Error: "Could not resolve com.github.mik3y:usb-serial-for-android"
**Solución:**
Verifica que `settings.gradle` tenga:
```gradle
repositories {
    google()
    mavenCentral()
    maven { url 'https://jitpack.io' }
}
```

### Error: "Gradle sync failed"
**Solución:**
```bash
# Limpiar caché de Gradle
rm -rf ~/.gradle/caches/

# Volver a sincronizar
./gradlew clean build
```

### Error: "AAPT: error: resource android:attr/lStar not found"
**Solución:**
Actualizar `compileSdk` a 34 en `app/build.gradle`

### Error de memoria "OutOfMemoryError"
**Solución:**
Edita `gradle.properties`:
```properties
org.gradle.jvmargs=-Xmx4096m -XX:MaxPermSize=512m
```

### Error: "Unable to find a matching variant"
**Solución:**
```bash
./gradlew clean
./gradlew assembleDebug --refresh-dependencies
```

---

## 📊 Tamaños de Compilación Esperados

### Debug Build
- **APK size**: ~8-12 MB
- **Tiempo compilación**: 1-3 minutos (primera vez)
- **Tiempo compilación**: 20-60 segundos (incremental)

### Release Build
- **APK size**: ~5-8 MB (con ProGuard)
- **AAB size**: ~4-6 MB
- **Tiempo compilación**: 2-5 minutos

---

## 🚀 Optimizaciones de Compilación

### Habilitar Build Cache
En `gradle.properties`:
```properties
org.gradle.caching=true
```

### Compilación Paralela
```properties
org.gradle.parallel=true
org.gradle.workers.max=4
```

### Daemon de Gradle
```properties
org.gradle.daemon=true
```

### Configurar Kotlin Incremental
```properties
kotlin.incremental=true
kotlin.incremental.java=true
```

---

## 📱 Instalación en Diferentes Dispositivos

### Instalación ADB (Cualquier dispositivo)
```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
# -r: Reinstalar manteniendo datos
```

### Múltiples Dispositivos
```bash
# Listar dispositivos
adb devices

# Instalar en dispositivo específico
adb -s DEVICE_ID install app-debug.apk
```

### Instalación WiFi (sin cable)
```bash
# Conectar dispositivo por USB primero
adb tcpip 5555
adb connect 192.168.1.XXX:5555

# Ahora puedes desconectar USB
adb install app-debug.apk
```

---

## 🔍 Verificar APK

### Información del APK
```bash
aapt dump badging app/build/outputs/apk/debug/app-debug.apk
```

### Listar contenido
```bash
unzip -l app-debug.apk
```

### Verificar firma
```bash
jarsigner -verify -verbose -certs app-release.apk
```

---

## 📋 Checklist Pre-Release

Antes de publicar versión release:

- [ ] Compilar sin warnings
- [ ] Pasar todos los tests
- [ ] Lint sin errores críticos
- [ ] ProGuard configurado correctamente
- [ ] Keystore seguro y respaldado
- [ ] Versión actualizada en `build.gradle`
- [ ] Changelog documentado
- [ ] APK probado en múltiples dispositivos
- [ ] Permisos verificados
- [ ] Screenshots actualizados
- [ ] Descripción Play Store lista

---

## 🎯 Scripts Útiles

### Script de Build Completo
Crea `build.sh`:
```bash
#!/bin/bash
echo "Limpiando proyecto..."
./gradlew clean

echo "Compilando Debug..."
./gradlew assembleDebug

echo "Ejecutando tests..."
./gradlew test

echo "Ejecutando lint..."
./gradlew lint

echo "✅ Build completo!"
ls -lh app/build/outputs/apk/debug/app-debug.apk
```

Uso:
```bash
chmod +x build.sh
./build.sh
```

### Script de Instalación Rápida
Crea `quick-install.sh`:
```bash
#!/bin/bash
./gradlew installDebug && adb shell am start -n com.dmx.mobile/.MainActivity
```

---

## 📚 Recursos Adicionales

### Documentación Oficial
- [Android Build](https://developer.android.com/studio/build)
- [Gradle Plugin](https://developer.android.com/studio/releases/gradle-plugin)
- [App Signing](https://developer.android.com/studio/publish/app-signing)

### Herramientas
- [APK Analyzer](https://developer.android.com/studio/build/apk-analyzer)
- [Bundle Tool](https://github.com/google/bundletool)

---

**¡Compilación exitosa!** 🎉

Para cualquier problema, consulta la sección de troubleshooting o el DEVELOPER_GUIDE.md

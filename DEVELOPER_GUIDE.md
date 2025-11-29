# DMX Mobile - Guía de Desarrollo

## Arquitectura de la Aplicación

### Patrón MVVM
La aplicación utiliza el patrón Model-View-ViewModel (MVVM):

- **Model** (`models/`): Clases de datos (Fixture, Scene, ColorPreset)
- **View** (`ui/`, `res/layout/`): Fragments y Activities
- **ViewModel** (`viewmodel/`): Lógica de negocio y estado

### Componentes Principales

#### 1. DMXController
**Ubicación**: `dmx/DMXController.kt`

Responsable de:
- Mantener el buffer DMX (513 bytes)
- Gestionar valores de canales (1-512)
- Proporcionar funciones de alto nivel (setRGB, setRGBW)

```kotlin
val dmxController = DMXController()
dmxController.setChannel(1, 255)  // Canal 1 al máximo
dmxController.setRGB(1, 255, 0, 0)  // Rojo en canales 1-3
```

#### 2. USBDMXManager
**Ubicación**: `usb/USBDMXManager.kt`

Responsable de:
- Detección de dispositivos USB
- Gestión de permisos USB
- Comunicación serial FTDI
- Envío de paquetes DMX con timing correcto
- Transmisión continua

```kotlin
val usbManager = USBDMXManager(context)
usbManager.scanDevices()
usbManager.connect(device)
usbManager.startContinuousSend { dmxData }
```

#### 3. DMXViewModel
**Ubicación**: `viewmodel/DMXViewModel.kt`

Responsable de:
- Coordinación entre DMXController y USBDMXManager
- Gestión de estado de conexión
- Gestión de fixtures y escenas
- Exposición de LiveData para la UI

### Flujo de Datos

```
Usuario → Fragment → ViewModel → DMXController → USBDMXManager → Adaptador USB → Luces DMX
                         ↓
                    LiveData
                         ↓
                    Fragment (actualiza UI)
```

## Protocolo DMX512 Implementado

### Timing DMX
```
BREAK (100µs) → MAB (12µs) → START CODE (0x00) → 512 Canales → Repetir
```

### Implementación en Código

```kotlin
// En USBDMXManager.kt
private fun sendDMXPacket(data: ByteArray) {
    port.setBreak(true)           // BREAK
    Thread.sleep(0, 100_000)      // 100 microsegundos
    
    port.setBreak(false)          // MAB
    Thread.sleep(0, 12_000)       // 12 microsegundos
    
    port.write(data, 1000)        // START CODE + 512 canales
}
```

### Estructura del Buffer DMX

```
Índice 0:   START CODE (0x00)
Índice 1:   Canal DMX 1
Índice 2:   Canal DMX 2
...
Índice 512: Canal DMX 512
```

## Agregar Nuevas Características

### 1. Agregar un Nuevo Tipo de Fixture

Edita `models/DMXModels.kt`:

```kotlin
enum class FixtureType(val channelCount: Int, val channelNames: List<String>) {
    // ... fixtures existentes
    
    LED_STROBE(5, listOf("Intensidad", "Strobe", "Velocidad", "Auto", "Sound")),
}
```

### 2. Crear un Nuevo Preset de Color

Edita `models/DMXModels.kt`:

```kotlin
companion object {
    val PRESETS = listOf(
        // ... presets existentes
        ColorPreset("Lavanda", 230, 230, 250),
        ColorPreset("Turquesa", 64, 224, 208),
    )
}
```

### 3. Agregar Control de Fixture Específico

Crea un nuevo Fragment:

```kotlin
class MovingHeadFragment : Fragment() {
    private val viewModel: DMXViewModel by activityViewModels()
    
    fun setPanTilt(pan: Int, tilt: Int) {
        val channel = 1  // Canal base
        viewModel.setChannelValue(channel, pan / 256)      // Pan MSB
        viewModel.setChannelValue(channel + 1, pan % 256)  // Pan LSB
        viewModel.setChannelValue(channel + 2, tilt / 256) // Tilt MSB
        viewModel.setChannelValue(channel + 3, tilt % 256) // Tilt LSB
    }
}
```

### 4. Implementar Fade/Transiciones

```kotlin
class DMXViewModel {
    fun fadeToScene(scene: Scene, duration: Long) {
        scope.launch {
            val steps = 50
            val delay = duration / steps
            
            scene.channelValues.forEach { (channel, targetValue) ->
                val currentValue = dmxController.getChannel(channel)
                val step = (targetValue - currentValue).toFloat() / steps
                
                repeat(steps) { i ->
                    val newValue = currentValue + (step * i).toInt()
                    dmxController.setChannel(channel, newValue)
                    delay(delay)
                }
            }
        }
    }
}
```

## Optimizaciones

### 1. Reducir Latencia
- Aumentar FPS de transmisión (actualmente 25 fps)
- Usar hilos de alta prioridad

```kotlin
// En USBDMXManager.kt
fun startContinuousSend(..., updateRate: Long = 20) // 50 fps
```

### 2. Reducir Consumo de Batería
- Detener transmisión cuando no hay cambios
- Implementar modo "sleep" cuando todos los canales están en 0

```kotlin
private var lastDmxData: ByteArray? = null

fun sendDMXPacket(data: ByteArray) {
    if (data.contentEquals(lastDmxData)) {
        return  // No enviar si no hay cambios
    }
    lastDmxData = data.copyOf()
    // ... enviar datos
}
```

### 3. Gestión de Memoria
- Usar object pool para ByteArray
- Limpiar LiveData en ViewModel

## Testing

### Probar sin Hardware USB

Crea un `MockUSBDMXManager`:

```kotlin
class MockUSBDMXManager : USBDMXManager {
    override fun sendDMXPacket(data: ByteArray) {
        Log.d("MockDMX", "Would send: ${data.joinToString()}")
    }
}
```

### Simular Fixtures

```kotlin
class DMXSimulator {
    fun simulateParLED(channelValues: Map<Int, Int>) {
        // Visualizar valores en consola
        println("PAR LED: R=${channelValues[1]} G=${channelValues[2]} B=${channelValues[3]}")
    }
}
```

## Debugging

### Logs Importantes

```kotlin
// En USBDMXManager.kt
Log.d(TAG, "DMX Packet sent: ${data.size} bytes")
Log.d(TAG, "Connection state: $isConnected")

// En DMXController.kt
Log.d(TAG, "Channel $channel set to $value")
```

### Verificar Timing DMX

Usa un osciloscopio o analizador lógico en los pines del adaptador USB:
- TX (Transmit): Debe mostrar señal DMX
- BREAK debe ser ~100µs
- Velocidad debe ser 250,000 baudios

## Troubleshooting Común

### Problema: "No se encontró driver para el dispositivo"

**Solución**: Agrega el VID/PID a `device_filter.xml`:

```xml
<usb-device vendor-id="XXXX" product-id="YYYY" />
```

Encuentra VID/PID con:
```bash
adb shell lsusb
```

### Problema: "Las luces parpadean"

**Causas posibles**:
1. Cable USB-DMX de mala calidad
2. Interferencia en la línea DMX
3. Falta terminador DMX (120Ω)
4. FPS de transmisión demasiado bajo

**Soluciones**:
- Usar cable DMX de calidad (calibre 22 AWG o mejor)
- Agregar terminador al final de la cadena DMX
- Aumentar FPS a 40-50
- Reducir longitud de cable DMX

### Problema: Retraso en la respuesta

**Soluciones**:
- Aumentar FPS de transmisión
- Reducir operaciones en el hilo principal
- Usar DataBinding en lugar de findViewById

```kotlin
// Mal
button.setOnClickListener {
    findViewById<TextView>(R.id.value).text = "..."
}

// Mejor
binding.button.setOnClickListener {
    binding.value.text = "..."
}
```

## Roadmap Futuro

- [ ] Soporte para Art-Net (DMX sobre WiFi/Ethernet)
- [ ] Secuenciador de escenas con timeline
- [ ] Editor de shows con música
- [ ] Soporte para múltiples universos DMX
- [ ] Biblioteca de perfiles de fixtures
- [ ] Control por voz (Google Assistant)
- [ ] Widget de inicio rápido
- [ ] Modo paisaje optimizado
- [ ] Exportar/importar configuraciones
- [ ] Control remoto desde otro dispositivo

## Recursos Adicionales

### Documentación DMX512
- [DMX512 Standard (ANSI E1.11)](https://tsp.esta.org/tsp/documents/docs/ANSI_E1-11_2008R2018.pdf)
- [DMX512 Wikipedia](https://en.wikipedia.org/wiki/DMX512)

### Hardware Recomendado
- **Adaptadores USB-DMX**:
  - Enttec DMX USB PRO
  - DMXking ultraDMX Micro
  - UDMX (basado en FTDI)
  
- **Cables**:
  - XLR de 5 pines (estándar profesional)
  - XLR de 3 pines (común en equipos económicos)
  - Resistencia terminadora 120Ω

### Bibliotecas Útiles
- [usb-serial-for-android](https://github.com/mik3y/usb-serial-for-android)
- [QLC+ (referencia de software DMX)](https://www.qlcplus.org/)

---

**Happy Lighting!** 🎭💡✨

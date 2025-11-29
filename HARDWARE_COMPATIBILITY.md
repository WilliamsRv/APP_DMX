# Compatibilidad de Hardware - DMX Mobile

## 🔌 Adaptadores USB-DMX Compatibles

### ✅ Totalmente Compatibles

#### FTDI FT232R (UDMX)
- **Chip**: FTDI FT232RL
- **VID:PID**: 0403:6001
- **Precio**: $15-30 USD
- **Disponibilidad**: AliExpress, Amazon, eBay
- **Ventajas**: Económico, ampliamente disponible
- **Desventajas**: Calidad variable entre vendedores

#### Enttec DMX USB PRO
- **Chip**: FTDI FT245RL
- **VID:PID**: 0403:6001
- **Precio**: $100-150 USD
- **Disponibilidad**: Tiendas especializadas
- **Ventajas**: Calidad profesional, robusto
- **Desventajas**: Costoso

#### DMXking ultraDMX Micro
- **Chip**: FTDI
- **Precio**: $60-80 USD
- **Ventajas**: Compacto, confiable
- **Desventajas**: Difícil de conseguir

#### CH340-based UDMX
- **Chip**: CH340G/CH341
- **VID:PID**: 1A86:7523
- **Precio**: $10-20 USD
- **Ventajas**: Muy económico
- **Desventajas**: Soporte limitado en algunos dispositivos

### ⚠️ Compatibilidad Parcial

#### USB-DMX512 Generic
- Depende del chip interno
- Verificar VID:PID antes de comprar
- Puede requerir agregar filtro en device_filter.xml

### ❌ NO Compatibles

#### Art-Net / sACN Nodes
- Requieren conexión Ethernet/WiFi
- No soportados en versión actual
- Considerar para versión futura

#### Wireless DMX (no USB)
- No son adaptadores USB
- Requieren hardware especial

---

## 📱 Dispositivos Android Compatibles

### Requisitos Mínimos
- **Android**: 7.0 Nougat (API 24) o superior
- **Soporte USB Host**: Sí (la mayoría de tablets y algunos phones)
- **RAM**: 2GB o más recomendado
- **Procesador**: Cualquier quad-core moderno

### ✅ Testeado y Funcionando

#### Samsung
- Galaxy S8 / S9 / S10 / S20 / S21 / S22 / S23
- Galaxy Tab S6 / S7 / S8
- Galaxy A series (A50, A70, etc.)
- Nota: Todos con soporte USB OTG

#### Google Pixel
- Pixel 3 / 4 / 5 / 6 / 7 / 8
- Excelente compatibilidad USB

#### OnePlus
- OnePlus 6 / 7 / 8 / 9 / 10 / 11
- Muy buena compatibilidad

#### Xiaomi
- Mi 9 / 10 / 11 / 12 / 13
- Redmi Note 8/9/10/11/12
- POCO F3 / F4 / X3
- Verificar que tenga USB OTG habilitado

#### Huawei (sin GMS)
- P20 / P30 / P40
- Mate 20 / 30 / 40
- Funciona, pero requiere instalación manual del APK

### ⚠️ Limitaciones Conocidas

#### Algunos dispositivos económicos
- Pueden no tener USB Host
- Verificar especificaciones antes de comprar

#### Tablets Amazon Fire
- USB Host limitado
- Requiere root para habilitar

---

## 🔌 Cables y Conectores

### Cable OTG (Obligatorio)

#### USB-C a USB-A
- Para dispositivos modernos (2017+)
- Precio: $5-10 USD
- **Recomendado**: Cable corto (15cm) para portabilidad

#### Micro-USB a USB-A
- Para dispositivos antiguos
- Precio: $3-8 USD
- Verificar que soporte OTG (algunos solo cargan)

#### Cómo verificar si tu cable es OTG:
1. Conecta un USB flash drive
2. Debe detectarse automáticamente
3. Si no detecta = cable solo de carga, no OTG

### Cables DMX

#### XLR de 5 pines (Recomendado)
- **Estándar**: ANSI E1.11
- **Uso**: Profesional
- **Ventajas**: Más robusto, menos interferencias
- **Precio**: $8-20 por cable de 3m

**Pinout:**
```
Pin 1: Tierra (Shield)
Pin 2: Data - (Cold)
Pin 3: Data + (Hot)
Pin 4: Sin uso
Pin 5: Sin uso
```

#### XLR de 3 pines (Común)
- **Uso**: Semi-profesional, equipos económicos
- **Ventajas**: Más económico, amplia disponibilidad
- **Precio**: $5-15 por cable de 3m

**Pinout:**
```
Pin 1: Tierra (Shield)
Pin 2: Data - (Cold)
Pin 3: Data + (Hot)
```

### Terminador DMX (Importante!)

**¿Qué es?**
Una resistencia de 120Ω entre pins 2 y 3 del último dispositivo en la cadena DMX.

**¿Por qué es necesario?**
- Previene reflexiones de señal
- Elimina parpadeos
- Mejora estabilidad

**Opciones:**
1. **Comercial**: $5-10 (plug XLR con resistencia integrada)
2. **DIY**: Soldar resistencia 120Ω 1/4W en conector XLR macho

**Cómo hacer uno:**
```
Materiales:
- Conector XLR macho
- Resistencia 120Ω 1/4W
- Soldador

Pasos:
1. Soldar resistencia entre pins 2 y 3
2. Cerrar conector
3. Conectar al último fixture
```

---

## 🎛️ Fixtures DMX Compatibles

### Compatibilidad Universal
**Esta app funciona con CUALQUIER fixture DMX512:**
- PARs LED (RGB, RGBW, RGBA)
- Moving Heads
- Scanners
- Dimmers
- Strobes
- Máquinas de humo DMX
- Lasers DMX
- Equipos especiales

### Configuraciones Típicas

#### PAR LED RGB
```
Canal 1: Rojo (0-255)
Canal 2: Verde (0-255)
Canal 3: Azul (0-255)
```
→ Usar "Control RGB" con Canal Inicial = dirección del PAR

#### PAR LED RGBW
```
Canal 1: Rojo
Canal 2: Verde
Canal 3: Azul
Canal 4: Blanco
```
→ Usar "Control RGB" + slider Blanco

#### PAR LED con Dimmer/Strobe
```
Canal 1: Master Dimmer
Canal 2: Rojo
Canal 3: Verde
Canal 4: Azul
Canal 5: Strobe
Canal 6: Programas automáticos
Canal 7: Velocidad programas
```
→ Usar "Control Manual" para canales especiales (5-7)

#### Moving Head Básico (16 canales)
```
Canal 1: Pan (0-255)
Canal 2: Pan Fine (0-255)
Canal 3: Tilt (0-255)
Canal 4: Tilt Fine (0-255)
Canal 5: Velocidad Pan/Tilt
Canal 6: Master Dimmer
Canal 7: Shutter/Strobe
Canal 8: Rojo
Canal 9: Verde
Canal 10: Azul
Canal 11: Blanco
Canal 12: Color Wheel
Canal 13: Gobo Wheel
Canal 14: Gobo Rotation
Canal 15: Prism
Canal 16: Focus
```
→ Usar "Control Manual" para todos los canales

#### Dimmer Simple
```
Canal 1: Intensidad (0-255)
```
→ Usar "Control de Canal Individual"

---

## 🛠️ Requerimientos de Instalación

### Instalación en Cadena

#### Correcto:
```
Adaptador USB-DMX → Fixture 1 → Fixture 2 → Fixture 3 → Terminador
```

#### Incorrecto:
```
❌ Adaptador USB-DMX → Fixture 1
                      → Fixture 2 (no conectado en cadena)
```

### Longitud Máxima de Cable
- **Estándar DMX512**: 500 metros máximo
- **Recomendado en práctica**: 
  - Sin repetidor: 100-150 metros
  - Con repetidor cada 100m: hasta 500m

### Número Máximo de Fixtures
- **Teórico**: Hasta 512 canales ÷ canales por fixture
- **Ejemplos**:
  - PARs RGB (3 canales): ~170 fixtures
  - PARs RGBW (4 canales): ~128 fixtures
  - Moving Heads (16 canales): ~32 fixtures
- **Práctica**: Limitado por capacidad eléctrica del venue

---

## ⚡ Alimentación Eléctrica

### Importante:
El adaptador USB-DMX NO alimenta las fixtures, solo envía señal de control.

### Setup Típico:
```
Android Device → [USB OTG] → Adaptador USB-DMX → [Cable DMX] → Fixtures
                                                                   ↓
                                                              [Corriente AC]
```

### Cada fixture necesita:
- Su propia conexión a corriente AC
- O alimentación desde dimmer pack (para fixtures convencionales)

---

## 🔍 Verificación de Compatibilidad

### Antes de Comprar un Adaptador:

1. **Verificar chip interno**
   - Preferir FTDI FT232R
   - CH340 como alternativa económica

2. **Verificar que sea USB-DMX, no Art-Net**
   - USB-DMX = Cable USB
   - Art-Net = Cable Ethernet (RJ45)

3. **Leer reviews**
   - Buscar "compatible con Android"
   - Verificar que otros usuarios confirmen funcionamiento

### Después de Comprar:

1. **Conectar a Android**
   ```bash
   adb shell lsusb
   ```
   Deberías ver el dispositivo listado

2. **Verificar VID:PID**
   - FTDI: 0403:6001
   - CH340: 1A86:7523

3. **Si no funciona automáticamente:**
   - Agregar VID:PID a `device_filter.xml`
   - Recompilar app

---

## 📊 Tabla de Compatibilidad Rápida

| Componente | Mínimo | Recomendado | Profesional |
|------------|--------|-------------|-------------|
| **Adaptador USB-DMX** | UDMX genérico ($15) | CH340 UDMX ($20) | Enttec DMX PRO ($120) |
| **Cable OTG** | Genérico ($3) | Marca conocida ($8) | Anker/Belkin ($15) |
| **Cables DMX** | XLR 3-pin ($5) | XLR 5-pin ($12) | XLR 5-pin + terminador ($20) |
| **Android Device** | Phone con OTG | Tablet Android | iPad + adaptador* |
| **Fixtures** | Cualquier DMX512 | Marcas conocidas | Fixtures profesionales |

\* iPad requiere adaptadores especiales y app diferente

---

## 🆘 Soporte de Hardware

### Si tu adaptador no funciona:

1. **Verificar que sea detectado:**
   ```bash
   adb shell lsusb
   ```

2. **Obtener VID:PID:**
   ```bash
   adb shell lsusb -v
   ```

3. **Agregar a device_filter.xml:**
   ```xml
   <usb-device vendor-id="XXXX" product-id="YYYY" />
   ```
   (Convertir VID:PID de hexadecimal a decimal)

4. **Reportar en issues de GitHub:**
   - Modelo del adaptador
   - Modelo del dispositivo Android
   - VID:PID
   - Logs de error

---

## 🔗 Dónde Comprar

### Adaptadores USB-DMX
- **AliExpress**: UDMX genéricos ($10-20)
- **Amazon**: Variedad de opciones ($20-150)
- **Tiendas especializadas**: Enttec, DMXking

### Cables DMX
- **Amazon**: Marca AmazonBasics, Hosa
- **Thomann** (EU): Amplio catálogo profesional
- **Sweetwater** (US): Calidad garantizada

### Cables OTG
- **Amazon**: Anker, UGREEN, CableCreation
- **AliExpress**: Opciones económicas

### Terminadores DMX
- **DIY**: Resistencia + conector ($2)
- **Comercial**: Amazon, Thomann ($5-10)

---

**Última actualización**: Noviembre 2025

*Para más información técnica, consulta DEVELOPER_GUIDE.md*

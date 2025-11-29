# Guía Rápida de Uso - DMX Mobile

## 🚀 Inicio Rápido (5 minutos)

### Paso 1: Preparar el Hardware
1. Conecta tu adaptador USB-DMX a tu dispositivo Android con un cable OTG
2. Conecta tus luces DMX al adaptador usando cables XLR
3. Enciende las luces y configura sus direcciones DMX

### Paso 2: Conectar la Aplicación
1. Abre **DMX Mobile**
2. Toca **"Escanear Dispositivos"**
3. Selecciona tu adaptador de la lista
4. Acepta el permiso USB
5. Verás **"Conectado"** en la parte superior

### Paso 3: Probar con Control Manual
1. Ve a la pestaña **"Control Manual"**
2. En "Control RGB":
   - Canal Inicial: **1** (o la dirección de tu fixture)
   - Mueve el slider **Rojo** → La luz debe ponerse roja
   - Prueba con Verde y Azul

### Paso 4: Probar Presets
1. Ve a la pestaña **"Presets de Color"**
2. Toca cualquier color → Se aplicará inmediatamente
3. Prueba varios colores

### Paso 5: Guardar una Escena
1. Configura tus luces como quieras
2. Ve a **"Escenas"**
3. Toca **"Guardar Escena"**
4. Dale un nombre, ej: "Fiesta"
5. Toca **"Guardar"**

¡Listo! Ya estás controlando luces DMX desde tu móvil 🎉

---

## 📖 Uso Detallado

### Control RGB/RGBW

#### Para una sola fixture RGB:
```
1. Canal Inicial: 1
2. Ajusta Rojo: 255
3. Ajusta Verde: 128
4. Ajusta Azul: 64
→ Resultado: Color naranja
```

#### Para fixture RGBW (con canal blanco):
```
1. Canal Inicial: 1
2. Rojo: 200
3. Verde: 200
4. Azul: 255
5. Blanco: 100
→ Resultado: Azul claro frío
```

#### Para múltiples fixtures:
```
Fixture 1 (Canales 1-3):
- Canal Inicial: 1
- Ajusta colores → Solo Fixture 1 cambia

Fixture 2 (Canales 4-6):
- Canal Inicial: 4
- Ajusta colores → Solo Fixture 2 cambia
```

### Control de Canal Individual

Para fixtures con funciones especiales:

#### Ejemplo: PAR LED con Dimmer
```
Canal 1: Dimmer (Intensidad general)
Canal 2: Rojo
Canal 3: Verde
Canal 4: Azul
Canal 5: Strobe (Estroboscopio)
```

Control en la app:
```
1. Ve a "Control de Canal Individual"
2. Canal: 1, Valor: 255 (Dimmer al máximo)
3. Canal: 5, Valor: 128 (Strobe a velocidad media)
```

#### Ejemplo: Moving Head
```
Canal 1-2: Pan (Movimiento horizontal)
Canal 3-4: Tilt (Movimiento vertical)
Canal 5: Velocidad Pan/Tilt
Canal 6: Dimmer
Canal 7-10: Color (RGBW)
```

### Presets de Color

Los presets están optimizados para diferentes situaciones:

- **Blanco**: Iluminación general, eventos corporativos
- **Rojo**: Energía, alarma, San Valentín
- **Azul**: Profesional, corporativo, relajante
- **Verde**: Natural, eco-eventos
- **Amarillo**: Alegría, verano
- **Cyan**: Moderno, tecnológico
- **Magenta**: Creativo, artístico
- **Naranja**: Cálido, acogedor
- **Rosa**: Romántico, delicado
- **Púrpura**: Elegante, lujo
- **Ámbar**: Teatro, vintage
- **UV**: Discoteca, efectos especiales

### Escenas

#### Crear Escenas Efectivas

**Escena "Fiesta":**
```
1. Configura luces rojas, azules y verdes alternadas
2. Algunos strobes activos
3. Guarda como "Fiesta"
```

**Escena "Romántico":**
```
1. Luces rosa y ámbar suaves
2. Intensidad baja (30-50%)
3. Guarda como "Romántico"
```

**Escena "Energético":**
```
1. Todos los colores al máximo
2. Algunos blancos fríos
3. Guarda como "Energético"
```

**Escena "Corporativo":**
```
1. Blancos fríos
2. Algunos azules
3. Intensidad 100%
4. Guarda como "Corporativo"
```

#### Usar Escenas en Eventos

1. Pre-programa todas tus escenas antes del evento
2. Durante el evento, solo toca "Cargar"
3. Transiciones instantáneas entre looks

### Blackout (Apagado de Emergencia)

Usa **Blackout** para:
- Apagar todo inmediatamente
- Reset antes de cambiar de modo
- Emergencias (cortar luz)

---

## 🎭 Casos de Uso Reales

### Caso 1: DJ Móvil

**Setup:**
- 4 PARs LED RGB (8 canales c/u)
- 2 Moving Heads (16 canales c/u)

**Direcciones DMX:**
```
PAR 1: Canales 1-8
PAR 2: Canales 9-16
PAR 3: Canales 17-24
PAR 4: Canales 25-32
Moving Head 1: Canales 33-48
Moving Head 2: Canales 49-64
```

**Workflow:**
1. Crea escena "Ambiente" con PARs en colores cálidos
2. Crea escena "Baile" con todos los colores saturados
3. Crea escena "Fin" con blancos suaves
4. Durante el evento: alterna entre escenas con un toque

### Caso 2: Teatro Pequeño

**Setup:**
- 8 Reflectores Dimmer
- 4 PARs LED RGBW

**Escenas:**
- "Acto 1 - Día": Blancos cálidos
- "Acto 2 - Noche": Azules fríos + ámbar
- "Final": Todos al máximo

### Caso 3: Iglesia/Evento Religioso

**Setup:**
- 6 PARs LED RGBW en escenario
- 4 Wash LED para audiencia

**Escenas:**
- "Entrada": Blancos fríos 80%
- "Adoración": Cálidos suaves
- "Predicación": Blancos 100%
- "Salida": Fade gradual

### Caso 4: Bar/Club

**Setup:**
- Multiple PARs LED
- Strobes
- Wash de fondo

**Uso:**
- Presets para cambios rápidos entre canciones
- Blackout entre sets de DJ
- Control manual durante transiciones

---

## 💡 Tips y Trucos

### 1. Organización de Canales
Anota tus direcciones DMX:
```
Fixture          Canales      Tipo
--------------------------------
PAR Izquierdo    1-4         RGBW
PAR Centro       5-8         RGBW
PAR Derecho      9-12        RGBW
Wash Fondo       13-16       RGBW
```

### 2. Nomenclatura de Escenas
Usa nombres descriptivos:
- ✅ "Fiesta Rojo-Azul"
- ✅ "Corporativo Blanco Frío"
- ❌ "Escena 1"
- ❌ "Test"

### 3. Backup de Configuración
- Guarda todas las escenas importantes
- Documenta tus setups en un cuaderno
- Toma fotos de las configuraciones

### 4. Testing Pre-Evento
1. Llega 1 hora antes
2. Prueba todas las escenas
3. Ajusta intensidades si es necesario
4. Guarda los cambios

### 5. Optimización de Batería
- Conecta el móvil a corriente durante eventos largos
- Reduce brillo de pantalla
- Cierra apps innecesarias
- Desactiva WiFi/Bluetooth si no los usas

### 6. Seguridad
- Ten un cable OTG de repuesto
- Lleva un adaptador USB-DMX backup
- Guarda las escenas importantes
- Practica el uso antes del evento real

---

## ⚠️ Problemas Comunes y Soluciones

### "No responden todas las luces"
**Causa**: Direcciones DMX incorrectas
**Solución**: 
1. Verifica la dirección configurada en cada fixture
2. Asegúrate de que el "Canal Inicial" en la app coincida
3. Usa control individual para probar canal por canal

### "Las luces parpadean"
**Causa**: Falta terminador DMX
**Solución**: 
1. Instala resistencia de 120Ω en la última fixture
2. O compra un terminador DMX XLR

### "Se desconecta el USB"
**Causa**: Cable OTG defectuoso
**Solución**: 
1. Usa cable OTG de calidad
2. Asegura bien las conexiones
3. Evita mover el cable durante uso

### "Los colores no coinciden"
**Causa**: Fixtures con orden de canales diferente
**Solución**: 
1. Revisa el manual de tu fixture
2. Algunos usan RGB, otros GRB, etc.
3. Usa control individual para identificar qué canal es qué color

---

## 📱 Atajos Rápidos

- **Apagar todo**: Blackout
- **Cambio rápido**: Usa Presets
- **Volver a escena anterior**: Carga la escena guardada
- **Reset**: Blackout + Cargar escena base

---

## 🎓 Aprende Más

### Conceptos DMX Básicos

**¿Qué es un canal DMX?**
Un canal = Un parámetro controlable (0-255)
- 0 = Apagado / Mínimo
- 255 = Máximo / Encendido total
- 128 = 50%

**¿Qué es una dirección DMX?**
El primer canal que usa una fixture
- Fixture en dirección 1 usa canales 1, 2, 3...
- Fixture en dirección 10 usa canales 10, 11, 12...

**¿Por qué 512 canales?**
Es el estándar DMX512 (512 canales por universo)
- Universo = Una salida DMX completa
- Esta app controla 1 universo

### Próximos Pasos

1. **Practica** con una sola fixture primero
2. **Experimenta** con presets
3. **Crea** tu biblioteca de escenas
4. **Documenta** tus configuraciones
5. **Comparte** tus mejores setups

---

**¡Disfruta iluminando tus eventos!** 🎭💡✨

*Para soporte técnico, consulta el README.md y DEVELOPER_GUIDE.md*

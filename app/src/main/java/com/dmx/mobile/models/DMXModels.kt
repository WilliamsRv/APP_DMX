package com.dmx.mobile.models

/**
 * Modelo para una fixture/luminaria DMX
 */
data class Fixture(
    val id: Int,
    val name: String,
    val startChannel: Int,
    val type: FixtureType,
    val channels: Map<String, Int> = emptyMap()
)

/**
 * Tipos de fixtures soportadas
 */
enum class FixtureType(val channelCount: Int, val channelNames: List<String>) {
    DIMMER(1, listOf("Intensidad")),
    RGB(3, listOf("Rojo", "Verde", "Azul")),
    RGBW(4, listOf("Rojo", "Verde", "Azul", "Blanco")),
    RGBA(4, listOf("Rojo", "Verde", "Azul", "Ámbar")),
    PAR_LED(7, listOf("Intensidad", "Rojo", "Verde", "Azul", "Blanco", "Strobe", "Dimmer")),
    MOVING_HEAD(16, listOf("Pan", "Pan Fine", "Tilt", "Tilt Fine", "Velocidad Pan/Tilt", 
                            "Intensidad", "Rojo", "Verde", "Azul", "Blanco", 
                            "Gobo", "Strobe", "Dimmer", "Reset", "Control", "Macro"))
}

/**
 * Escena DMX
 */
data class Scene(
    val id: Int,
    val name: String,
    val channelValues: Map<Int, Int>, // Canal -> Valor
    val fadeTime: Long = 0 // Tiempo de fade en ms
)

/**
 * Preset de color
 */
data class ColorPreset(
    val name: String,
    val red: Int,
    val green: Int,
    val blue: Int,
    val white: Int = 0
) {
    companion object {
        val PRESETS = listOf(
            ColorPreset("Rojo", 255, 0, 0),
            ColorPreset("Verde", 0, 255, 0),
            ColorPreset("Azul", 0, 0, 255),
            ColorPreset("Blanco", 255, 255, 255),
            ColorPreset("Amarillo", 255, 255, 0),
            ColorPreset("Cyan", 0, 255, 255),
            ColorPreset("Magenta", 255, 0, 255),
            ColorPreset("Naranja", 255, 165, 0),
            ColorPreset("Rosa", 255, 192, 203),
            ColorPreset("Púrpura", 128, 0, 128),
            ColorPreset("Ámbar", 255, 191, 0),
            ColorPreset("UV", 75, 0, 130)
        )
    }
}

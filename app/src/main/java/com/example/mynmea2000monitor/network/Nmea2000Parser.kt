package com.example.mynmea2000monitor.network

import com.example.mynmea2000monitor.model.*
import com.google.gson.Gson
import com.google.gson.JsonObject

class Nmea2000Parser {
    private val gson = Gson()

    /**
     * Parse NMEA 0183 formatted strings (commonly used for NMEA 2000 interface)
     * Format: !AIVDM or $GPRMC, etc.
     */
    fun parseNmea0183Sentence(sentence: String): DataField? {
        return try {
            when {
                sentence.startsWith("\$GPRMC") -> parseRMCSentence(sentence)
                sentence.startsWith("\$GPGGA") -> parseGGASentence(sentence)
                sentence.startsWith("\$GPVTG") -> parseVTGSentence(sentence)
                else -> null
            }
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Parse RMC (Recommended Minimum Navigation Information)
     * $GPRMC,123519,4807.038,N,01131.000,E,022.4,084.4,230394,003.1,W*6A
     */
    private fun parseRMCSentence(sentence: String): DataField? {
        val parts = sentence.split(",")
        if (parts.size < 9) return null

        val time = parts[1]
        val latitude = parts[2]
        val latDir = parts[3]
        val longitude = parts[4]
        val lonDir = parts[5]
        val speed = parts[7]
        val course = parts[8]

        return DataField(
            name = "Position & Speed",
            value = "Lat: $latitude$latDir, Lon: $longitude$lonDir, Speed: $speed knots, Course: $course°",
            unit = "mixed"
        )
    }

    /**
     * Parse GGA (Global Positioning System Fix Data)
     */
    private fun parseGGASentence(sentence: String): DataField? {
        val parts = sentence.split(",")
        if (parts.size < 8) return null

        val time = parts[1]
        val latitude = parts[2]
        val latDir = parts[3]
        val longitude = parts[4]
        val lonDir = parts[5]
        val quality = parts[6]  // 0=invalid, 1=GPS, 2=DGPS
        val satellites = parts[7]

        return DataField(
            name = "GPS Status",
            value = "Lat: $latitude$latDir, Lon: $longitude$lonDir, Quality: $quality, Satellites: $satellites",
            unit = "mixed"
        )
    }

    /**
     * Parse VTG (Track Made Good and Ground Speed)
     */
    private fun parseVTGSentence(sentence: String): DataField? {
        val parts = sentence.split(",")
        if (parts.size < 8) return null

        val courseTrue = parts[1]
        val courseMag = parts[3]
        val speedKnots = parts[5]
        val speedKmh = parts[7]

        return DataField(
            name = "Velocity",
            value = "Course: $courseTrue° (True), $courseMag° (Mag), Speed: $speedKnots knots / $speedKmh km/h",
            unit = "mixed"
        )
    }

    /**
     * Parse JSON formatted device data (common in NMEA 2000 gateways)
     */
    fun parseJsonDeviceData(jsonString: String): List<DataField> {
        return try {
            val jsonObject = gson.fromJson(jsonString, JsonObject::class.java)
            val fields = mutableListOf<DataField>()

            jsonObject.entrySet().forEach { (key, value) ->
                val field = DataField(
                    name = key,
                    value = value.toString(),
                    unit = getUnitForField(key)
                )
                fields.add(field)
            }

            fields
        } catch (e: Exception) {
            emptyList()
        }
    }

    /**
     * Parse engine parameters from NMEA 2000 data
     */
    fun parseEngineParameters(data: Map<String, Any>): EngineParameters? {
        return try {
            EngineParameters(
                instance = (data["instance"] as? Number)?.toInt() ?: 0,
                engineSpeed = (data["engineSpeed"] as? Number)?.toDouble(),
                boostPressure = (data["boostPressure"] as? Number)?.toDouble(),
                oilPressure = (data["oilPressure"] as? Number)?.toDouble(),
                oilTemperature = (data["oilTemperature"] as? Number)?.toDouble(),
                coolantTemperature = (data["coolantTemperature"] as? Number)?.toDouble(),
                alternatorOutput = (data["alternatorOutput"] as? Number)?.toDouble(),
                fuelRate = (data["fuelRate"] as? Number)?.toDouble()
            )
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Parse fluid level data
     */
    fun parseFluidLevel(data: Map<String, Any>): FluidLevel? {
        return try {
            FluidLevel(
                instance = (data["instance"] as? Number)?.toInt() ?: 0,
                type = data["type"] as? String ?: "unknown",
                level = (data["level"] as? Number)?.toDouble(),
                capacity = (data["capacity"] as? Number)?.toDouble()
            )
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Parse electrical parameters
     */
    fun parseElectricalParameters(data: Map<String, Any>): ElectricalParameters? {
        return try {
            ElectricalParameters(
                instance = (data["instance"] as? Number)?.toInt() ?: 0,
                voltage = (data["voltage"] as? Number)?.toDouble(),
                current = (data["current"] as? Number)?.toDouble(),
                temperature = (data["temperature"] as? Number)?.toDouble()
            )
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Get appropriate unit for a field name
     */
    private fun getUnitForField(fieldName: String): String {
        return when {
            fieldName.contains("temperature", ignoreCase = true) -> "°C"
            fieldName.contains("pressure", ignoreCase = true) -> "PSI"
            fieldName.contains("voltage", ignoreCase = true) -> "V"
            fieldName.contains("current", ignoreCase = true) -> "A"
            fieldName.contains("speed", ignoreCase = true) -> "knots"
            fieldName.contains("rpm", ignoreCase = true) -> "RPM"
            fieldName.contains("level", ignoreCase = true) -> "%"
            fieldName.contains("latitude", ignoreCase = true) ||
            fieldName.contains("longitude", ignoreCase = true) -> "°"
            else -> ""
        }
    }
}


package com.example.mynmea2000monitor.model

data class Nmea2000Device(
    val id: String,
    val name: String,
    val ipAddress: String,
    val deviceType: String,
    val serialNumber: String,
    val isOnline: Boolean,
    val dataFields: List<DataField> = emptyList()
)

data class DataField(
    val name: String,
    val value: String,
    val unit: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

// Common NMEA 2000 Parameter Groups
data class EngineParameters(
    val instance: Int,
    val engineSpeed: Double? = null,  // RPM
    val boostPressure: Double? = null,  // PSI
    val oilPressure: Double? = null,  // PSI
    val oilTemperature: Double? = null,  // Celsius
    val coolantTemperature: Double? = null,  // Celsius
    val alternatorOutput: Double? = null,  // Amps
    val fuelRate: Double? = null  // L/h
)

data class FluidLevel(
    val instance: Int,
    val type: String,  // "fuel", "water", "gray water", etc.
    val level: Double? = null,  // percentage
    val capacity: Double? = null  // liters
)

data class ElectricalParameters(
    val instance: Int,
    val voltage: Double? = null,  // Volts
    val current: Double? = null,  // Amps
    val temperature: Double? = null  // Celsius
)

data class PositionData(
    val latitude: Double? = null,
    val longitude: Double? = null,
    val courseOverGround: Double? = null,  // degrees
    val speedOverGround: Double? = null  // knots
)


package com.example.mynmea2000monitor.util

object Nmea2000Constants {
    // Network ports
    const val NMEA_TCP_PORT = 10110
    const val NMEA_UDP_PORT = 10110
    const val GATEWAY_HTTP_PORT = 8080
    const val DISCOVERY_PORT = 5555

    // Discovery timeouts
    const val DISCOVERY_TIMEOUT_MS = 3000L
    const val CONNECTION_TIMEOUT_MS = 10000L

    // Discovery message
    const val DISCOVERY_MESSAGE = "NMEA2000_DISCOVERY"

    // API endpoints
    const val API_DEVICES_ENDPOINT = "/api/devices"
    const val API_DATA_ENDPOINT = "/api/data"

    // NMEA Sentence prefixes
    const val RMC_SENTENCE = "\$GPRMC"
    const val GGA_SENTENCE = "\$GPGGA"
    const val VTG_SENTENCE = "\$GPVTG"
    const val GLL_SENTENCE = "\$GPGLL"
    const val GSA_SENTENCE = "\$GPGSA"
    const val GSV_SENTENCE = "\$GPGSV"

    // Parameter indices (NMEA 0183)
    const val RMC_TIME_INDEX = 1
    const val RMC_LATITUDE_INDEX = 2
    const val RMC_LAT_DIR_INDEX = 3
    const val RMC_LONGITUDE_INDEX = 4
    const val RMC_LON_DIR_INDEX = 5
    const val RMC_SPEED_INDEX = 7
    const val RMC_COURSE_INDEX = 8

    // Device type identifiers
    const val DEVICE_TYPE_GATEWAY = "Gateway"
    const val DEVICE_TYPE_ENGINE = "Engine"
    const val DEVICE_TYPE_BATTERY = "Battery Monitor"
    const val DEVICE_TYPE_TANK = "Tank Monitor"
    const val DEVICE_TYPE_GPS = "GPS"
}

object DefaultSettings {
    const val AUTO_DISCOVER_ON_STARTUP = true
    const val AUTO_RECONNECT = true
    const val RECONNECT_INTERVAL_MS = 5000L
    const val DATA_UPDATE_INTERVAL_MS = 1000L
    const val MAX_DEVICES = 10
    const val HISTORY_SIZE = 100
}

object UnitsConverter {
    // Temperature conversions
    fun celsiusToFahrenheit(celsius: Double): Double = celsius * 9 / 5 + 32
    fun fahrenheitToCelsius(fahrenheit: Double): Double = (fahrenheit - 32) * 5 / 9

    // Pressure conversions
    fun psiToBar(psi: Double): Double = psi * 0.0689476
    fun barToPsi(bar: Double): Double = bar * 14.5038

    // Speed conversions
    fun knotsToKmh(knots: Double): Double = knots * 1.852
    fun kmhToKnots(kmh: Double): Double = kmh / 1.852
    fun knotsToMph(knots: Double): Double = knots * 1.15078

    // Fuel/volume conversions
    fun gallonsToLiters(gallons: Double): Double = gallons * 3.78541
    fun litersToGallons(liters: Double): Double = liters / 3.78541
}

object ErrorMessages {
    const val DISCOVERY_FAILED = "Device discovery failed"
    const val CONNECTION_FAILED = "Failed to connect to device"
    const val INVALID_DATA = "Invalid data received"
    const val NETWORK_ERROR = "Network communication error"
    const val TIMEOUT = "Connection timeout"
    const val PERMISSION_DENIED = "Required permissions not granted"
}


package com.example.mynmea2000monitor.util

import android.content.Context
import android.net.wifi.WifiManager
import android.util.Log

/**
 * Extension functions for common utilities
 */

// Logging extension
fun Any.logDebug(message: String) {
    Log.d(this::class.simpleName, message)
}

fun Any.logError(message: String, exception: Throwable? = null) {
    Log.e(this::class.simpleName, message, exception)
}

fun Any.logInfo(message: String) {
    Log.i(this::class.simpleName, message)
}

/**
 * WiFi utility extensions
 */
fun Context.getLocalIpAddress(): String? {
    return try {
        val wifiManager = this.getSystemService(Context.WIFI_SERVICE) as? WifiManager
        val wifiInfo = wifiManager?.connectionInfo ?: return null
        val ip = wifiInfo.ipAddress
        if (ip != 0) {
            "${ip and 0xff}.${(ip shr 8) and 0xff}.${(ip shr 16) and 0xff}.${(ip shr 24) and 0xff}"
        } else {
            null
        }
    } catch (e: Exception) {
        null
    }
}

fun Context.isWifiConnected(): Boolean {
    return try {
        val wifiManager = this.getSystemService(Context.WIFI_SERVICE) as? WifiManager
        val wifiInfo = wifiManager?.connectionInfo ?: return false
        @Suppress("DEPRECATION")
        wifiInfo.linkSpeed > 0
    } catch (e: Exception) {
        false
    }
}

/**
 * Number formatting extensions
 */
fun Double.formatTemperature(): String = String.format("%.1f", this)

fun Double.formatPressure(): String = String.format("%.2f", this)

fun Double.formatVoltage(): String = String.format("%.2f", this)

fun Double.formatCurrent(): String = String.format("%.1f", this)

fun Double.formatSpeed(): String = String.format("%.1f", this)

fun Double.formatRpm(): String = String.format("%.0f", this)

fun Double.formatPercentage(): String = String.format("%.0f", this)

fun Double.formatCoordinate(): String = String.format("%.6f", this)

/**
 * Time formatting
 */
fun Long.formatTimestamp(): String {
    val sdf = java.text.SimpleDateFormat("HH:mm:ss", java.util.Locale.getDefault())
    return sdf.format(this)
}

fun Long.formatDate(): String {
    val sdf = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault())
    return sdf.format(this)
}

/**
 * String utilities
 */
fun String.parseNmeaChecksum(): String? {
    val parts = this.split("*")
    return if (parts.size == 2) parts[1] else null
}

fun String.removeNmeaParity(): String {
    val parts = this.split("*")
    return if (parts.size > 1) parts[0] else this
}

fun String.isValidNmeaSentence(): Boolean {
    return this.startsWith("$") && (this.contains("*") || this.contains("\$"))
}

/**
 * List extensions
 */
fun <T> List<T>.getOrNull(index: Int): T? {
    return if (index in this.indices) this[index] else null
}

fun <T> MutableList<T>.removeIf(predicate: (T) -> Boolean): Boolean {
    var removed = false
    val iterator = this.iterator()
    while (iterator.hasNext()) {
        if (predicate(iterator.next())) {
            iterator.remove()
            removed = true
        }
    }
    return removed
}



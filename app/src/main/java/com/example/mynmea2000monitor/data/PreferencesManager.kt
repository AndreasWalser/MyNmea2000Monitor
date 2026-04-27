package com.example.mynmea2000monitor.data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class PreferencesManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(
        "nmea2000_prefs",
        Context.MODE_PRIVATE
    )

    companion object {
        private const val KEY_GATEWAY_IP = "gateway_ip"
        private const val KEY_GATEWAY_PORT = "gateway_port"
        private const val KEY_AUTO_DISCOVER = "auto_discover"
        private const val KEY_AUTO_RECONNECT = "auto_reconnect"
        private const val KEY_LAST_CONNECTED_DEVICE = "last_connected_device"
        private const val KEY_THEME_DARK = "theme_dark"
        private const val KEY_DISCOVERY_TIMEOUT = "discovery_timeout"
        private const val KEY_ENABLE_LOGGING = "enable_logging"
        private const val KEY_UPDATE_INTERVAL = "update_interval"
    }

    // Gateway settings
    fun getGatewayIp(): String? = prefs.getString(KEY_GATEWAY_IP, null)
    fun setGatewayIp(ip: String) = prefs.edit { putString(KEY_GATEWAY_IP, ip) }

    fun getGatewayPort(): Int = prefs.getInt(KEY_GATEWAY_PORT, 10110)
    fun setGatewayPort(port: Int) = prefs.edit { putInt(KEY_GATEWAY_PORT, port) }

    // Auto features
    fun isAutoDiscoverEnabled(): Boolean = prefs.getBoolean(KEY_AUTO_DISCOVER, true)
    fun setAutoDiscoverEnabled(enabled: Boolean) = prefs.edit { putBoolean(KEY_AUTO_DISCOVER, enabled) }

    fun isAutoReconnectEnabled(): Boolean = prefs.getBoolean(KEY_AUTO_RECONNECT, true)
    fun setAutoReconnectEnabled(enabled: Boolean) = prefs.edit { putBoolean(KEY_AUTO_RECONNECT, enabled) }

    // Last connection
    fun getLastConnectedDevice(): String? = prefs.getString(KEY_LAST_CONNECTED_DEVICE, null)
    fun setLastConnectedDevice(deviceId: String) = prefs.edit { putString(KEY_LAST_CONNECTED_DEVICE, deviceId) }

    // Theme
    fun isDarkThemeEnabled(): Boolean = prefs.getBoolean(KEY_THEME_DARK, false)
    fun setDarkThemeEnabled(enabled: Boolean) = prefs.edit { putBoolean(KEY_THEME_DARK, enabled) }

    // Timeouts
    fun getDiscoveryTimeout(): Long = prefs.getLong(KEY_DISCOVERY_TIMEOUT, 3000L)
    fun setDiscoveryTimeout(timeout: Long) = prefs.edit { putLong(KEY_DISCOVERY_TIMEOUT, timeout) }

    // Logging
    fun isLoggingEnabled(): Boolean = prefs.getBoolean(KEY_ENABLE_LOGGING, false)
    fun setLoggingEnabled(enabled: Boolean) = prefs.edit { putBoolean(KEY_ENABLE_LOGGING, enabled) }

    // Update interval
    fun getUpdateInterval(): Long = prefs.getLong(KEY_UPDATE_INTERVAL, 1000L)
    fun setUpdateInterval(interval: Long) = prefs.edit { putLong(KEY_UPDATE_INTERVAL, interval) }

    // Clear all preferences
    fun clearAll() = prefs.edit { clear() }
}


package com.example.mynmea2000monitor.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynmea2000monitor.model.DataField
import com.example.mynmea2000monitor.model.Nmea2000Device
import com.example.mynmea2000monitor.network.Nmea2000NetworkService
import com.example.mynmea2000monitor.network.Nmea2000Parser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class Nmea2000ViewModel(context: Context) : ViewModel() {
    private val networkService = Nmea2000NetworkService(context)
    private val parser = Nmea2000Parser()

    private val _devices = MutableStateFlow<List<Nmea2000Device>>(emptyList())
    val devices: StateFlow<List<Nmea2000Device>> = _devices.asStateFlow()

    private val _selectedDevice = MutableStateFlow<Nmea2000Device?>(null)
    val selectedDevice: StateFlow<Nmea2000Device?> = _selectedDevice.asStateFlow()

    private val _isDiscovering = MutableStateFlow(false)
    val isDiscovering: StateFlow<Boolean> = _isDiscovering.asStateFlow()

    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun discoverDevices() {
        viewModelScope.launch {
            _isDiscovering.value = true
            _errorMessage.value = null
            try {
                val discoveredIps = networkService.discoverDevices()
                val newDevices = discoveredIps.mapIndexed { index, ip ->
                    Nmea2000Device(
                        id = "device_$index",
                        name = "NMEA Device $index",
                        ipAddress = ip,
                        deviceType = "Gateway",
                        serialNumber = "SN-${ip.replace(".", "-")}",
                        isOnline = true
                    )
                }
                _devices.value = newDevices
            } catch (e: Exception) {
                _errorMessage.value = "Discovery failed: ${e.message}"
            } finally {
                _isDiscovering.value = false
            }
        }
    }

    fun connectToDevice(device: Nmea2000Device) {
        viewModelScope.launch {
            _isConnected.value = true
            _selectedDevice.value = device
            _errorMessage.value = null

            try {
                networkService.connectToDevice(device.ipAddress) { data ->
                    // Parse incoming data
                    val parsedData = parser.parseNmea0183Sentence(data)
                    if (parsedData != null) {
                        updateDeviceData(device.id, parsedData)
                    }
                }
            } catch (e: Exception) {
                _errorMessage.value = "Connection failed: ${e.message}"
                _isConnected.value = false
            }
        }
    }

    fun startUdpListener() {
        viewModelScope.launch {
            try {
                networkService.startUdpListener { data ->
                    val parsedData = parser.parseJsonDeviceData(data)
                    parsedData.firstOrNull()?.let { field ->
                        _selectedDevice.value?.let { device ->
                            updateDeviceData(device.id, field)
                        }
                    }
                }
            } catch (e: Exception) {
                _errorMessage.value = "UDP Listener failed: ${e.message}"
            }
        }
    }

    private fun updateDeviceData(deviceId: String, dataField: DataField) {
        val updatedDevices = _devices.value.map { device ->
            if (device.id == deviceId) {
                val existingFields = device.dataFields.toMutableList()
                // Remove old field with same name if exists
                existingFields.removeAll { it.name == dataField.name }
                existingFields.add(dataField)
                device.copy(dataFields = existingFields)
            } else {
                device
            }
        }
        _devices.value = updatedDevices

        if (_selectedDevice.value?.id == deviceId) {
            _selectedDevice.value = updatedDevices.find { it.id == deviceId }
        }
    }

    fun disconnectDevice() {
        networkService.stopListening()
        _isConnected.value = false
        _selectedDevice.value = null
    }

    override fun onCleared() {
        super.onCleared()
        networkService.shutdown()
    }
}


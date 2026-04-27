package com.example.mynmea2000monitor.network

import android.content.Context
import android.net.wifi.WifiManager
import kotlinx.coroutines.*
import okhttp3.*
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class Nmea2000NetworkService(private val context: Context) {
    private val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
    private val scope = CoroutineScope(Dispatchers.IO + Job())
    private var udpSocket: DatagramSocket? = null
    private var isListening = false

    companion object {
        private const val NMEA_PORT = 10110
        private const val BROADCAST_PORT = 5555
    }

    /**
     * Discover NMEA 2000 devices on the local network via broadcast
     */
    suspend fun discoverDevices(): List<String> {
        return withContext(Dispatchers.IO) {
            val devices = mutableListOf<String>()
            try {
                val broadcastAddress = getBroadcastAddress() ?: return@withContext devices

                // Send discovery broadcast
                val socket = DatagramSocket()
                socket.broadcast = true
                val discoveryMessage = "NMEA2000_DISCOVERY"
                val packet = DatagramPacket(
                    discoveryMessage.toByteArray(),
                    discoveryMessage.length,
                    broadcastAddress,
                    BROADCAST_PORT
                )

                socket.send(packet)

                // Wait for responses with timeout
                socket.soTimeout = 3000
                val buffer = ByteArray(256)

                repeat(10) {
                    try {
                        val responsePacket = DatagramPacket(buffer, buffer.size)
                        socket.receive(responsePacket)
                        val deviceIp = responsePacket.address.hostAddress
                        if (deviceIp != null && !devices.contains(deviceIp)) {
                            devices.add(deviceIp)
                        }
                    } catch (e: IOException) {
                        // Timeout or error, continue
                    }
                }

                socket.close()
                devices
            } catch (e: Exception) {
                e.printStackTrace()
                devices
            }
        }
    }

    /**
     * Connect to a specific NMEA 2000 device and start receiving data
     */
    fun connectToDevice(ipAddress: String, onDataReceived: (String) -> Unit) {
        scope.launch {
            try {
                val socket = java.net.Socket(ipAddress, NMEA_PORT)
                val reader = socket.getInputStream().bufferedReader()

                isListening = true
                while (isListening && socket.isConnected) {
                    val line = reader.readLine() ?: break
                    onDataReceived(line)
                }

                reader.close()
                socket.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Start listening for UDP packets from NMEA 2000 devices
     */
    fun startUdpListener(port: Int = NMEA_PORT, onDataReceived: (String) -> Unit) {
        scope.launch {
            try {
                udpSocket = DatagramSocket(port)
                isListening = true
                val buffer = ByteArray(1024)

                while (isListening) {
                    val packet = DatagramPacket(buffer, buffer.size)
                    udpSocket?.receive(packet)
                    val data = String(packet.data, 0, packet.length)
                    onDataReceived(data)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Stop listening for data
     */
    fun stopListening() {
        isListening = false
        udpSocket?.close()
    }

    /**
     * Get the broadcast address for the local network
     */
    private fun getBroadcastAddress(): InetAddress? {
        return try {
            val wifiInfo = wifiManager.connectionInfo
            val ip = wifiInfo.ipAddress
            val broadcast = (ip and 0xff) or ((ip shr 8) and 0xff shl 8) or ((ip shr 16) and 0xff shl 16) or (0xff shl 24)
            InetAddress.getByAddress(
                byteArrayOf(
                    (broadcast and 0xff).toByte(),
                    ((broadcast shr 8) and 0xff).toByte(),
                    ((broadcast shr 16) and 0xff).toByte(),
                    ((broadcast shr 24) and 0xff).toByte()
                )
            )
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Make an HTTP request to a NMEA 2000 gateway
     */
    suspend fun fetchDeviceData(ipAddress: String, endpoint: String = "/api/devices"): String? {
        return withContext(Dispatchers.IO) {
            try {
                val client = OkHttpClient()
                val url = "http://$ipAddress:8080$endpoint"
                val request = Request.Builder()
                    .url(url)
                    .build()

                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    response.body?.string()
                } else {
                    null
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    fun shutdown() {
        isListening = false
        udpSocket?.close()
        scope.cancel()
    }
}


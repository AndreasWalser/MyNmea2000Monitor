# NMEA 2000 WiFi Monitor - Setup Guide

## Prerequisites

- Android device running Android 12 or later (API 31+)
- WiFi network connectivity
- NMEA 2000 gateway or simulator on the same network

## Installation

### From Android Studio

1. Open Android Studio
2. Import the project directory
3. Sync Gradle files
4. Run on emulator or physical device
5. Grant WiFi permissions when prompted

### From Command Line

```bash
cd /Users/uex10457/AndroidStudioProjects/MyNmea2000Monitor
./gradlew installDebug
```

## Testing with a Simulator

### Option 1: Mock NMEA 2000 Server (Python)

Create a simple NMEA server for testing:

```python
import socket
import time
import threading

def mock_nmea_server():
    server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    server.bind(('0.0.0.0', 10110))
    server.listen(1)
    
    while True:
        client, addr = server.accept()
        print(f"Client connected: {addr}")
        
        try:
            while True:
                # Send NMEA sentences
                nmea_data = [
                    "$GPRMC,123519,4807.038,N,01131.000,E,022.4,084.4,230394,003.1,W*6A\n",
                    "$GPGGA,123519,4807.038,N,01131.000,E,1,08,0.9,545.4,M,46.9,M,,*47\n",
                    "$GPVTG,054.7,T,034.4,M,005.5,N,010.2,K*48\n"
                ]
                
                for data in nmea_data:
                    client.send(data.encode())
                    time.sleep(0.5)
                    
        except Exception as e:
            print(f"Error: {e}")
        finally:
            client.close()

if __name__ == "__main__":
    mock_nmea_server()
```

### Option 2: UDP Broadcast Discovery

For the app to discover devices via broadcast, create a UDP responder:

```python
import socket
import threading

def broadcast_responder():
    sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    sock.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    sock.bind(('0.0.0.0', 5555))
    
    while True:
        data, addr = sock.recvfrom(1024)
        if b'NMEA2000_DISCOVERY' in data:
            response = "192.168.1.100".encode()
            sock.sendto(response, addr)
            print(f"Responded to discovery from {addr}")

if __name__ == "__main__":
    broadcast_responder()
```

## Network Configuration

### WiFi Network Setup

The app requires:
- Same WiFi network as NMEA 2000 gateway
- Local network access enabled
- No firewall blocking ports 5555, 10110, 8080

### Firewall Rules

Ensure these ports are open:

| Port | Protocol | Purpose |
|------|----------|---------|
| 5555 | UDP | Device discovery |
| 10110 | TCP/UDP | NMEA data streaming |
| 8080 | HTTP | Gateway API |

## Usage Guide

### 1. Discovery

- Launch the app
- Tap "Scan" to discover devices
- Wait 3-5 seconds for network scan
- Devices appear in list as found

### 2. Connection

- Select a device from the list
- Connection initiates automatically
- Status shows as "Connected" in header

### 3. Data Monitoring

Once connected:
- Engine parameters update in real-time
- Fluid levels refresh automatically
- Electrical measurements display
- Position data shows navigation info

### 4. Disconnect

- Tap "Back" to return to device list
- Connection closes
- Can select another device

## Troubleshooting

### Devices Not Found

1. **Check WiFi Connection**
   - Ensure device is on same network as gateway
   - Open WiFi settings and verify connection

2. **Check Firewall**
   - Verify port 5555 is not blocked
   - Disable local firewall temporarily for testing

3. **Check Gateway**
   - Ping gateway from Android device
   - Verify gateway is running and configured
   - Check gateway logs for errors

### Connection Failed

1. **Timeout Issues**
   - Verify gateway IP address is correct
   - Check gateway is listening on port 10110
   - Reduce network latency if possible

2. **Data Not Received**
   - Verify NMEA data is being generated
   - Check gateway sends data on correct port
   - Monitor network traffic with tcpdump

### Permission Denied

1. Grant permissions:
   - Go to Settings > Apps > NMEA 2000 Monitor
   - Tap Permissions
   - Enable INTERNET and WiFi permissions

## API Configuration

### For Custom Gateways

If using a custom NMEA 2000 gateway, configure these endpoints:

**Discovery Response**
```
POST /broadcast:5555
Response: IP address as ASCII string
```

**Data Stream (TCP)**
```
TCP :10110
Stream: NMEA 0183 sentences, one per line
```

**Data Stream (UDP)**
```
UDP :10110
Packets: JSON or NMEA 0183 formatted
```

**REST API (Optional)**
```
GET /api/devices
Response: JSON array of devices
```

## Example NMEA Data

### Position Data
```
$GPRMC,123519,4807.038,N,01131.000,E,022.4,084.4,230394,003.1,W*6A
```

### GPS Fix Data
```
$GPGGA,123519,4807.038,N,01131.000,E,1,08,0.9,545.4,M,46.9,M,,*47
```

### Velocity Data
```
$GPVTG,054.7,T,034.4,M,005.5,N,010.2,K*48
```

## Advanced Configuration

### Custom Port Numbers

Edit constants in `util/Constants.kt`:
```kotlin
const val NMEA_TCP_PORT = 10110
const val NMEA_UDP_PORT = 10110
const val GATEWAY_HTTP_PORT = 8080
const val DISCOVERY_PORT = 5555
```

### Timeout Values

Adjust discovery and connection timeouts:
```kotlin
const val DISCOVERY_TIMEOUT_MS = 3000L
const val CONNECTION_TIMEOUT_MS = 10000L
```

## Performance Optimization

### For Low-End Devices

1. Reduce data update frequency
2. Limit number of monitored devices
3. Disable real-time updates when not viewing
4. Clear old data periodically

### Battery Optimization

1. Disable WiFi when not monitoring
2. Use UDP instead of TCP (less overhead)
3. Enable screen timeout
4. Close app when not in use

## Data Export

Currently supports display of data. To export:
1. Take screenshots of individual measurements
2. Connect device via USB for file transfer
3. Use Android's built-in file manager

Future versions will support:
- CSV export
- JSON export
- Data logging

## Frequently Asked Questions

### Q: How do I find my gateway's IP?

A: Use your WiFi router's admin panel or scan with a network tool like Nmap.

### Q: Does it work with cellular data?

A: No, the app requires WiFi on the local network.

### Q: Can I monitor multiple devices?

A: Yes, but you must connect to one device at a time. Switch by returning to device list.

### Q: What NMEA version is supported?

A: NMEA 2000 data is displayed. NMEA 0183 is supported as the wire protocol.

### Q: How often does data refresh?

A: Data updates as it arrives from the device, typically every 1-2 seconds.

## Reporting Issues

If you encounter problems:

1. Note the error message
2. Check the logs (adb logcat)
3. Verify network connectivity
4. Test with mock server
5. Report with:
   - Android version
   - Device model
   - Network configuration
   - Error steps to reproduce

## Legal and Safety

- Use only with devices you own or have permission to monitor
- NMEA 2000 is a proprietary protocol
- Ensure data security on shared networks
- Do not use while operating marine vessel


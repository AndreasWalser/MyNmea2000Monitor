# NMEA 2000 WiFi Monitor - Android App

A comprehensive Android application for monitoring NMEA 2000 marine devices connected via WiFi.

## Features

- **Device Discovery**: Automatically discover NMEA 2000 gateways and devices on your WiFi network
- **Real-time Monitoring**: Display real-time data from connected marine devices
- **Multiple Data Types**: Monitor engine parameters, fluid levels, electrical systems, and position data
- **NMEA 0183 Support**: Parse standard NMEA 0183 sentences
- **JSON Data Format**: Support for JSON-formatted device data from modern gateways
- **Live Updates**: Real-time updates of measurement values and device status
- **Multi-device Support**: Connect to and monitor multiple devices simultaneously

## Architecture

### Network Layer
- **Nmea2000NetworkService**: Handles WiFi connections, device discovery, and data reception
  - UDP broadcast discovery
  - TCP socket connections
  - UDP listener for continuous data streams
  - HTTP requests for REST-based gateways

### Data Parsing
- **Nmea2000Parser**: Parses various NMEA 2000 data formats
  - NMEA 0183 sentences ($GPRMC, $GPGGA, $GPVTG)
  - JSON device data
  - Engine parameters
  - Fluid levels
  - Electrical measurements
  - Position and navigation data

### UI Layer
- **Compose-based UI**: Modern Material 3 design with Jetpack Compose
- **ViewModel**: State management with Kotlin Flow
- **Device List**: Browse discovered devices with online status
- **Device Details**: View all parameters and measurements in real-time

### Data Models
- **Nmea2000Device**: Device information and associated data fields
- **DataField**: Individual measurement with name, value, and unit
- **EngineParameters**: Engine-specific metrics
- **FluidLevel**: Tank and fluid monitoring
- **ElectricalParameters**: Voltage, current, and temperature
- **PositionData**: GPS and navigation information

## Permissions Required

- `INTERNET`: Network communication
- `ACCESS_WIFI_STATE`: WiFi status monitoring
- `CHANGE_WIFI_STATE`: WiFi network management
- `ACCESS_NETWORK_STATE`: Network state detection

## How to Use

1. **Start the App**: Launch the NMEA 2000 Monitor on your Android device
2. **Connect to WiFi**: Ensure your device is on the same WiFi network as your NMEA 2000 gateway
3. **Discover Devices**: Tap the "Scan" button to discover available devices
4. **View Details**: Tap any device in the list to connect and view its real-time data
5. **Monitor**: Watch real-time updates of engine parameters, fluid levels, electrical data, and position information

## Supported NMEA 2000 Data

### Engine Data
- RPM (Revolutions per Minute)
- Boost Pressure
- Oil Pressure
- Oil Temperature
- Coolant Temperature
- Alternator Output
- Fuel Rate

### Fluid Levels
- Fuel Tank Level
- Water Tank Level
- Gray Water Level
- Custom Tank Monitoring

### Electrical Systems
- Voltage
- Current (Amps)
- Temperature

### Navigation Data
- Latitude / Longitude
- Course Over Ground
- Speed Over Ground

## Network Communication

### Device Discovery
The app sends UDP broadcasts to discover NMEA 2000 gateways:
- **Port**: 5555 (UDP)
- **Message**: "NMEA2000_DISCOVERY"
- **Timeout**: 3 seconds for responses

### Data Connection
Devices can communicate via:
- **TCP**: Port 10110 (stream of NMEA sentences)
- **UDP**: Port 10110 (JSON or NMEA data)
- **HTTP**: Port 8080 (/api/devices endpoint)

## Data Formats

### NMEA 0183 Sentences
```
$GPRMC,123519,4807.038,N,01131.000,E,022.4,084.4,230394,003.1,W*6A
$GPGGA,123519,4807.038,N,01131.000,E,1,08,0.9,545.4,M,46.9,M,,*47
$GPVTG,054.7,T,034.4,M,005.5,N,010.2,K*48
```

### JSON Format
```json
{
  "deviceName": "Engine 1",
  "engineSpeed": 2500,
  "oilTemperature": 85,
  "coolantTemperature": 92,
  "fuelLevel": 75,
  "voltage": 12.5,
  "current": 15.3
}
```

## Dependencies

- Jetpack Compose UI framework
- Kotlin Coroutines for async operations
- OkHttp for HTTP requests
- Gson for JSON parsing
- Material 3 design components

## Settings

### Compile Configuration
- **Target SDK**: 36
- **Min SDK**: 31
- **Compile SDK**: 36 (with API level 1)
- **Kotlin**: 2.2.10

## Building and Running

1. Clone the project
2. Sync Gradle files: `./gradlew build`
3. Run on device or emulator with WiFi connectivity
4. Grant required permissions when prompted

## Testing with Simulator

To test without real NMEA 2000 hardware, you can:
1. Use a NMEA 2000 gateway simulator on your network
2. Set up a mock server sending NMEA sentences via TCP/UDP
3. Use the sample data in the parser functions

## Future Enhancements

- [ ] Bluetooth LE support for direct device connections
- [ ] Historical data logging and charts
- [ ] Alarms and notifications for critical values
- [ ] Data export (CSV, JSON)
- [ ] Custom dashboard configuration
- [ ] Integration with marine weather data
- [ ] Voice alerts
- [ ] Multi-language support

## License

This project is provided as-is for educational and personal use.

## Support

For issues, questions, or feature requests, please refer to the project repository.


# NMEA 2000 WiFi Monitor - Developer Guide

## Project Overview

The NMEA 2000 WiFi Monitor is a Kotlin-based Android application built with Jetpack Compose that enables real-time monitoring of marine NMEA 2000 devices over WiFi networks.

**Technology Stack:**
- Kotlin 2.2.10
- Jetpack Compose (UI)
- Kotlin Coroutines (async operations)
- OkHttp (networking)
- GSON (JSON parsing)
- Material Design 3

## Project Structure

```
app/src/main/java/com/example/mynmea2000monitor/
├── MainActivity.kt              # Main entry point and UI composition
├── model/
│   └── Nmea2000Device.kt       # Data models for devices and parameters
├── network/
│   ├── Nmea2000NetworkService.kt # WiFi connection and discovery
│   └── Nmea2000Parser.kt        # NMEA 0183 and JSON parsing
├── viewmodel/
│   └── Nmea2000ViewModel.kt    # State management and business logic
├── ui/
│   ├── theme/                  # Material 3 theming
│   │   ├── Color.kt            # Custom colors
│   │   ├── Theme.kt            # Theme composition
│   │   └── Type.kt             # Typography
│   └── screens/                # Reusable screen components
│       ├── DeviceListScreen.kt
│       └── DeviceDetailScreen.kt
├── data/
│   └── PreferencesManager.kt   # SharedPreferences wrapper
├── util/
│   ├── Constants.kt            # App-wide constants
│   └── Extensions.kt           # Extension functions
└── sample/
    └── SampleDataProvider.kt   # Test data
```

## Core Modules

### 1. Data Models (`model/Nmea2000Device.kt`)

**Nmea2000Device**
- Primary data class representing a connected marine device
- Contains device metadata (IP, name, type, serial)
- Stores list of real-time data fields

**DataField**
- Individual measurement with name, value, and unit
- Timestamp for tracking data freshness
- Suitable for any NMEA parameter type

**Parameter Classes**
- `EngineParameters`: Engine-specific metrics
- `FluidLevel`: Tank monitoring
- `ElectricalParameters`: Voltage, current
- `PositionData`: GPS and navigation

### 2. Network Layer (`network/`)

#### Nmea2000NetworkService.kt
Handles all network communication:

**Device Discovery**
- Sends UDP broadcasts to port 5555
- Waits for gateway responses
- Returns list of discovered IP addresses
- Timeout: 3 seconds (configurable)

**Data Connection**
- TCP socket connection to NMEA port (10110)
- UDP listener for continuous streams
- HTTP GET for REST-based gateways
- Coroutine-based async operations

**Key Methods**
```kotlin
suspend fun discoverDevices(): List<String>
fun connectToDevice(ipAddress: String, onDataReceived: (String) -> Unit)
fun startUdpListener(port: Int, onDataReceived: (String) -> Unit)
fun stopListening()
fun fetchDeviceData(ipAddress: String, endpoint: String): String?
```

#### Nmea2000Parser.kt
Parses various NMEA data formats:

**NMEA 0183 Sentences**
- RMC (Recommended Minimum)
- GGA (Fix Data)
- VTG (Track and Speed)
- Extensible for additional sentence types

**JSON Format**
- Generic JSON device data parsing
- Automatic unit detection
- Field name analysis

**Parameter Parsing**
- Engine parameters from map data
- Fluid levels
- Electrical measurements

### 3. ViewModel (`viewmodel/Nmea2000ViewModel.kt`)

Manages app state using Kotlin Flow:

**State Flows**
- `devices`: List of discovered/connected devices
- `selectedDevice`: Currently active device
- `isDiscovering`: Discovery in progress
- `isConnected`: Connection status
- `errorMessage`: User-facing errors

**Key Functions**
```kotlin
fun discoverDevices()              // Scan network
fun connectToDevice(device)        // Connect and listen
fun startUdpListener()             // Listen for UDP data
fun disconnectDevice()             // Close connection
private fun updateDeviceData()     // Update device fields
```

### 4. User Interface (`ui/`)

#### Theme System (`ui/theme/`)
- Custom ocean/marine color palette
- Material 3 light and dark themes
- Tonal elevation for depth
- Accessible color contrasts

**Custom Colors**
```kotlin
val DarkBlue = Color(0xFF1B3A6B)
val OceanBlue = Color(0xFF0066CC)
val Teal = Color(0xFF00A896)
val DeepRed = Color(0xFFCC0000)
```

#### Screens
**DeviceListScreen.kt**
- List of discovered devices
- Online status indicators
- Click to connect

**DeviceDetailScreen.kt**
- Back button and device info
- Real-time data field display
- Scrollable data list
- Loading state handling

#### MainActivity
Main composition function with:
- Header with scan button
- Device list or detail view
- Error message banner
- Edge-to-edge layout

### 5. Data Persistence (`data/PreferencesManager.kt`)

SharedPreferences wrapper for app settings:
```kotlin
// Gateway settings
getGatewayIp/setGatewayIp()
getGatewayPort/setGatewayPort()

// Features
isAutoDiscoverEnabled/setAutoDiscoverEnabled()
isAutoReconnectEnabled/setAutoReconnectEnabled()

// UI & performance
isDarkThemeEnabled/setDarkThemeEnabled()
getDiscoveryTimeout/setDiscoveryTimeout()
getUpdateInterval/setUpdateInterval()

// Debugging
isLoggingEnabled/setLoggingEnabled()
```

### 6. Utilities

**Constants.kt**
- Network port numbers
- NMEA sentence prefixes
- Device types
- API endpoints

**Extensions.kt**
- Logging helpers
- WiFi utilities
- Number formatting
- String parsing
- List utilities

**SampleDataProvider.kt**
- Pre-populated test devices
- Sample NMEA sentences
- Offline testing support

## Network Architecture

### Discovery Flow
```
1. App sends UDP broadcast to port 5555
   Message: "NMEA2000_DISCOVERY"
   
2. Gateway receives and responds with IP
   
3. App collects responses (3 second timeout)
   
4. Updates device list with discovered IPs
```

### Data Flow
```
1. User selects device from list
   
2. App initiates TCP/UDP connection
   
3. Device sends NMEA sentences or JSON data
   
4. Parser processes incoming data
   
5. ViewModel updates device data fields
   
6. UI recomposes with new data
```

## Adding New Features

### New NMEA Sentence Type

1. Add sentence prefix constant to `Constants.kt`
2. Implement parser in `Nmea2000Parser.kt`
3. Return `DataField` with parsed values
4. Add unit type to `getUnitForField()`

Example:
```kotlin
private fun parseCustomSentence(sentence: String): DataField? {
    val parts = sentence.split(",")
    if (parts.size < 3) return null
    
    return DataField(
        name = "Custom Field",
        value = parts[2],
        unit = "units"
    )
}
```

### New Device Type

1. Add to data model in `Nmea2000Device.kt`
2. Extend `SampleDataProvider.kt` with sample data
3. Update UI components if needed

### New Settings Option

1. Add key to `PreferencesManager.kt`
2. Implement getter/setter methods
3. Access throughout app via dependency injection

## Testing

### Unit Tests
```bash
./gradlew testDebugUnitTest
```

### Integration Tests
```bash
./gradlew connectedAndroidTest
```

### Manual Testing

**With Mock Server**
```bash
python3 mock_nmea_server.py
```

**With Sample Data**
```kotlin
// In MainActivity
val sampleDevices = SampleDataProvider.getSampleDevices()
_devices.value = sampleDevices
```

## Performance Considerations

### Memory Management
- Close sockets after use
- Clear old data periodically
- Limit device list size

### Battery Optimization
- Use `withContext(Dispatchers.IO)` for network calls
- Implement connection pooling
- Disable updates when not visible

### Network Efficiency
- Batch updates together
- Use UDP for frequent updates
- Implement back pressure

## Error Handling

Error categories:
1. **Network Errors**: Connection failures, timeouts
2. **Parsing Errors**: Invalid data format
3. **Permission Errors**: Missing WiFi/Internet permissions
4. **Configuration Errors**: Invalid IP/port

All errors are collected in `errorMessage` StateFlow for UI display.

## Debugging

### Enable Logging
```kotlin
PreferencesManager(context).setLoggingEnabled(true)
```

### View Logs
```bash
adb logcat | grep NMEA2000
```

### Network Debugging
```bash
adb shell tcpdump -i any -s 65535 -w /sdcard/nmea.pcap
```

## Extending the Parser

### For Custom Data Formats

Implement a new parser strategy:
```kotlin
interface NmeaParser {
    fun parse(data: String): DataField?
}

class CustomNmeaParser : NmeaParser {
    override fun parse(data: String): DataField? {
        // Implementation
    }
}
```

### For Binary Protocols

Create a buffer-based parser:
```kotlin
class BinaryNmeaParser : NmeaParser {
    private val buffer = ByteArray(1024)
    // Implementation
}
```

## Gradle Configuration

### Dependencies
- Versions defined in `gradle/libs.versions.toml`
- Update via version catalog
- Use `libs.*` references in build files

### Build Types
- **Debug**: Full logging, debuggable
- **Release**: Minified, optimized

### Compile Options
- Kotlin 2.2.10
- Android 12+ (API 31)
- Java 11 compatibility

## Code Style Guidelines

### Naming Conventions
- Classes: PascalCase
- Functions: camelCase
- Constants: UPPER_SNAKE_CASE
- Private members: prefix with underscore

### Kotlin Best Practices
- Use data classes for models
- Prefer `sealed class` for enums
- Use extension functions for utilities
- Apply `@Composable` annotation correctly

### Comments
- Document public APIs
- Explain complex logic
- Use KDoc for functions
- Avoid obvious comments

## Release Checklist

- [ ] Update version in `build.gradle.kts`
- [ ] Update `README.md` with changes
- [ ] Run full test suite
- [ ] Create ProGuard rules if needed
- [ ] Test on real device
- [ ] Sign APK/AAB
- [ ] Create release notes

## Useful Commands

```bash
# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Run tests
./gradlew test

# Generate lint report
./gradlew lint

# Clean build
./gradlew clean build

# Update dependencies
./gradlew dependencies
```

## Resources

- [NMEA 0183 Standard](https://en.wikipedia.org/wiki/NMEA_0183)
- [NMEA 2000 Info](https://en.wikipedia.org/wiki/NMEA_2000)
- [Jetpack Compose Docs](https://developer.android.com/compose)
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)
- [Android Developer Guide](https://developer.android.com/docs)

## Support and Contact

For development questions or bug reports:
1. Check existing documentation
2. Review code comments
3. Run tests to isolate issues
4. Create detailed bug report with logs


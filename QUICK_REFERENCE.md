# NMEA 2000 WiFi Monitor - Quick Reference Guide

## 🚀 Quick Start (5 Minutes)

### 1. Build & Deploy
```bash
cd /Users/uex10457/AndroidStudioProjects/MyNmea2000Monitor
./gradlew assembleDebug
# Or in Android Studio: Run → Run 'app'
```

### 2. Launch App
- Tap NMEA 2000 Monitor on your Android device
- Grant WiFi and Internet permissions when prompted

### 3. Discover Devices
- Tap blue "Scan" button in header
- Wait 3-5 seconds for network scan
- Devices appear in list as found

### 4. View Data
- Tap any device in list
- Real-time data displays and updates
- Tap "Back" to return to device list

## 📱 UI Quick Reference

### Header Bar
```
[← Back] or [NMEA 2000 Monitor] [Scan] Button
   ↓
Device List           OR          Device Details
- Device 1 ●                     - Back Button
- Device 2 ●                     - Device Info
- Device 3 ○                     - Data Fields (scrollable)
- Device 4 ●
```

**Legend**: ● = Online, ○ = Offline

### Device Card
```
● Device Name
  192.168.1.100
  Gateway • SN-001-2024                    →
```

### Data Field
```
┌─────────────────────────────┐
│ Engine Speed          RPM   │
│ 2500                        │
└─────────────────────────────┘
```

## 🔌 Network Configuration

| Component | Port | Protocol | Function |
|-----------|------|----------|----------|
| **Discovery** | 5555 | UDP | Find gateways |
| **Data Stream** | 10110 | TCP/UDP | NMEA data |
| **REST API** | 8080 | HTTP | Device info |

## 📊 Supported Data

### Engine
- RPM, Oil Temp, Coolant Temp, Oil Pressure, Fuel Rate, Boost Pressure

### Tanks
- Fuel Level, Water Level, Waste Water Level (% + Liters)

### Electrical
- Voltage, Current, Temperature, State of Charge

### Navigation
- Latitude, Longitude, Course, Speed, Satellites

## 🛠️ Common Tasks

### Testing Without Hardware
```kotlin
// In MainActivity, use sample data:
val sampleDevices = SampleDataProvider.getSampleDevices()
_devices.value = sampleDevices
```

### Add Custom Port
Edit `util/Constants.kt`:
```kotlin
const val NMEA_TCP_PORT = 10110  // Change here
```

### Enable Debug Logging
```kotlin
val prefs = PreferencesManager(context)
prefs.setLoggingEnabled(true)
```

### Change Theme Color
Edit `ui/theme/Color.kt`:
```kotlin
val OceanBlue = Color(0xFF0066CC)  // Modify hex code
```

## 📋 Troubleshooting Quick Fixes

| Problem | Solution |
|---------|----------|
| No devices found | Check WiFi network, gateway powered on |
| Connection fails | Verify IP address, check port open |
| Permission error | Settings → Apps → NMEA Monitor → Permissions |
| No data received | Verify NMEA data being sent, check format |
| UI not updating | Restart app, check network connection |

## 🔍 Debug Commands

```bash
# View logs
adb logcat | grep Nmea2000

# Check device connection
adb shell ping 192.168.1.100

# Monitor network traffic
adb shell tcpdump -i any

# List running processes
adb shell pm list packages | grep nmea

# Clear app data
adb shell pm clear com.example.mynmea2000monitor
```

## 📚 Documentation Map

| Document | Purpose |
|----------|---------|
| **README.md** | Feature overview |
| **SETUP.md** | Installation & testing |
| **DEVELOPER.md** | Technical details |
| **IMPLEMENTATION_SUMMARY.md** | Complete overview |
| **FILE_MANIFEST.md** | File descriptions |
| **QUICK_REFERENCE.md** | This file |

## 🎯 File Structure (Key Files Only)

```
MyNmea2000Monitor/
├── MainActivity.kt              ← Main UI
├── network/Nmea2000NetworkService.kt  ← WiFi & connectivity
├── network/Nmea2000Parser.kt    ← Data parsing
├── viewmodel/Nmea2000ViewModel.kt     ← State management
├── ui/theme/Color.kt            ← Customize colors
└── util/Constants.kt            ← Customize ports/timeouts
```

## ⚙️ Most Common Customizations

### 1. Change Gateway IP
```kotlin
// PreferencesManager
val prefs = PreferencesManager(context)
prefs.setGatewayIp("192.168.1.100")
```

### 2. Change Port Numbers
```kotlin
// util/Constants.kt
const val NMEA_TCP_PORT = 10110
const val DISCOVERY_PORT = 5555
```

### 3. Adjust Timeouts
```kotlin
// util/Constants.kt
const val DISCOVERY_TIMEOUT_MS = 3000L      // 3 seconds
const val CONNECTION_TIMEOUT_MS = 10000L    // 10 seconds
```

### 4. Customize Colors
```kotlin
// ui/theme/Color.kt
val OceanBlue = Color(0xFF0066CC)    // Hex color code
```

### 5. Add NMEA Sentence
```kotlin
// network/Nmea2000Parser.kt
private fun parseCustomSentence(sentence: String): DataField? {
    // Parse implementation
    return DataField(name, value, unit)
}
```

## 📞 Getting Help

1. **Check Logs**: `adb logcat | grep -i nmea`
2. **Read Docs**: Start with README.md → SETUP.md → DEVELOPER.md
3. **Test Data**: Use `SampleDataProvider` for UI testing
4. **Mock Server**: Run Python NMEA simulator for testing
5. **Network**: Verify WiFi connection with `ping`

## 🔐 Important Notes

⚠️ **Permissions**: App needs INTERNET + WiFi permissions
⚠️ **Network**: Device must be on same WiFi network as gateway
⚠️ **Android**: Requires Android 12 (API 31) or higher
⚠️ **Gateway**: Must support NMEA 0183 or JSON output format

## 🎨 UI Customization Quick Tips

### Change Primary Color (Blue)
File: `ui/theme/Color.kt`
```kotlin
val DarkBlue = Color(0xFF1B3A6B)  // Header color
val OceanBlue = Color(0xFF0066CC) // Button color
```

### Change Background
File: `ui/theme/Theme.kt`
```kotlin
background = Color.White          // Light mode
background = DarkGray             // Dark mode
```

### Change Font Size
File: `MainActivity.kt`
```kotlin
style = MaterialTheme.typography.headlineSmall  // Bigger
style = MaterialTheme.typography.bodySmall      // Smaller
```

## 💾 Build Artifacts Location

```
After building, find:
Debug APK:    app/build/outputs/apk/debug/app-debug.apk
Release APK:  app/build/outputs/apk/release/app-release.apk
Bundle:       app/build/outputs/bundle/release/app-release.aab
Lint Report:  app/build/reports/lint-results-debug.html
```

## 🚀 Deployment

### To Device
```bash
./gradlew installDebug           # Install debug APK
./gradlew installRelease         # Install release APK
adb install app-debug.apk        # Manual install
```

### To Play Store
1. Sign APK with keystore
2. Build release APK/AAB
3. Upload to Google Play Console
4. Complete store listing
5. Submit for review

## 📈 Performance Tips

1. **Reduce Update Frequency**: Change `UPDATE_INTERVAL_MS`
2. **Limit Device Count**: `MAX_DEVICES = 10`
3. **Disable Logging**: `isLoggingEnabled(false)`
4. **Close Unused Sockets**: Call `disconnectDevice()`
5. **Clear Old Data**: Implement data cleanup periodically

## 🔄 Update Procedure

1. Pull latest changes: `git pull`
2. Sync Gradle: Android Studio → File → Sync Now
3. Build: `./gradlew clean build`
4. Test: Run on device
5. Deploy: `./gradlew installDebug`

## 📊 Data Format Examples

### NMEA 0183 (TCP Stream)
```
$GPRMC,123519,4807.038,N,01131.000,E,022.4,084.4,230394,003.1,W*6A
$GPGGA,123519,4807.038,N,01131.000,E,1,08,0.9,545.4,M,46.9,M,,*47
```

### JSON (HTTP/UDP)
```json
{
  "deviceName": "Engine 1",
  "engineSpeed": 2500,
  "oilTemperature": 85,
  "voltage": 13.5
}
```

## ⏱️ Typical Data Rates

| Data Type | Update Rate |
|-----------|------------|
| Engine RPM | 1-2 per second |
| Tank Levels | 1-2 per minute |
| GPS Position | 1 per second |
| Electrical | 1-2 per second |

---

## Quick Links

- **Build Command**: `./gradlew build`
- **Debug Install**: `./gradlew installDebug`
- **View Logs**: `adb logcat | grep Nmea`
- **Network Scan**: Tap "Scan" button in app
- **Sample Data**: See `SampleDataProvider.kt`
- **Constants**: Edit `util/Constants.kt`
- **Colors**: Edit `ui/theme/Color.kt`

---

**Last Updated**: April 2026
**Build Status**: ✅ Successful
**Ready for Use**: ✅ Yes


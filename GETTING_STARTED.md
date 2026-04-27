# 🚀 NMEA 2000 WiFi Monitor - Getting Started

## Welcome! 👋

You now have a **complete Android application** for monitoring NMEA 2000 marine devices via WiFi.

This file will help you get up and running in 5 minutes.

---

## 📱 What You Have

A fully functional Android app that:
- Discovers NMEA 2000 devices on your WiFi network
- Displays real-time marine engine and system data
- Shows tank levels, electrical parameters, GPS data
- Has a beautiful Material Design 3 interface
- Is built with modern Android technologies

---

## ⚡ Quick Start (5 Minutes)

### Step 1: Build the App
```bash
cd /Users/uex10457/AndroidStudioProjects/MyNmea2000Monitor
./gradlew assembleDebug
```

**Expected output:**
```
BUILD SUCCESSFUL in 10s
```

### Step 2: Install on Device
Connect your Android phone (Android 12+) via USB and run:
```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

Or in Android Studio:
- Click "Run" → "Run 'app'"

### Step 3: Launch & Use

1. **Open the app** - Tap "NMEA 2000 Monitor" on your device
2. **Grant permissions** - Allow WiFi and Internet access
3. **Tap "Scan"** - Discover NMEA gateways on your network
4. **Select a device** - Tap any device to view its data
5. **See live data** - Real-time measurements update automatically

---

## 🔍 Try It Now (Without Real Hardware)

### Option A: Using Sample Data
The app includes sample data! Here's how to see it:

1. Open the app
2. You'll see sample devices in the list
3. Tap any device to see sample data
4. Everything works just like real data!

### Option B: Testing with Python Mock Server
Create a simple server to send NMEA data:

**File: `nmea_server.py`**
```python
import socket
import time

server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
server.bind(('0.0.0.0', 10110))
server.listen(1)

while True:
    client, addr = server.accept()
    print(f"Connected: {addr}")
    
    # Send sample NMEA sentences
    sentences = [
        "$GPRMC,123519,4807.038,N,01131.000,E,022.4,084.4,230394,003.1,W*6A\n",
        "$GPGGA,123519,4807.038,N,01131.000,E,1,08,0.9,545.4,M,46.9,M,,*47\n"
    ]
    
    try:
        while True:
            for sentence in sentences:
                client.send(sentence.encode())
                time.sleep(1)
    except:
        client.close()
```

Run it:
```bash
python3 nmea_server.py
```

Then in the app, tap "Scan" and it should find your local server.

---

## 📚 Documentation Map

| Document | For Whom | Purpose |
|----------|----------|---------|
| **README.md** | Everyone | Feature overview |
| **QUICK_REFERENCE.md** | Developers | Fast answers & common tasks |
| **SETUP.md** | Installers | Installation & troubleshooting |
| **DEVELOPER.md** | Programmers | Technical architecture |
| **DOCUMENTATION_INDEX.md** | Lost? | Finding the right doc |

---

## 🎯 Common Tasks

### How do I...?

**...change the gateway IP?**
Edit `util/Constants.kt`:
```kotlin
const val GATEWAY_HTTP_PORT = 8080
```

**...change the scan port?**
Edit `util/Constants.kt`:
```kotlin
const val DISCOVERY_PORT = 5555
```

**...change the app colors?**
Edit `ui/theme/Color.kt`:
```kotlin
val OceanBlue = Color(0xFF0066CC)  // Change hex code
```

**...add a new NMEA sentence type?**
Edit `network/Nmea2000Parser.kt` and add a parsing function.

**...see debug logs?**
```bash
adb logcat | grep Nmea2000
```

**...test without WiFi?**
Use the sample data - it works offline!

---

## 🔧 System Requirements

- **Android**: 12 or higher (API 31+)
- **RAM**: 2GB minimum (4GB recommended)
- **Storage**: ~50MB free
- **WiFi**: Same network as NMEA gateway
- **Build Tools**: Android SDK, Kotlin 2.2.10

---

## ✅ Verification

After installing, you should be able to:
- ✅ See the app icon on your home screen
- ✅ Open the app without crashes
- ✅ See the header with "NMEA 2000 Monitor"
- ✅ Tap "Scan" button
- ✅ See sample devices in the list
- ✅ Tap a device and see sample data

If any of these don't work, check **SETUP.md** for troubleshooting.

---

## 🌟 Key Features

### Device Discovery
- Automatically finds NMEA 2000 gateways on WiFi
- Shows online/offline status
- Connects with one tap

### Real-Time Data
- Engine RPM, temperatures, pressures
- Tank levels (fuel, water, waste)
- Battery voltage and current
- GPS position and speed

### Modern Interface
- Clean, intuitive design
- Material Design 3
- Fast and responsive
- Beautiful colors

### Reliable
- Handles connection losses
- Automatic error messages
- Works offline with sample data
- Proper lifecycle management

---

## 📖 Reading the Documentation

**If you have 5 minutes:**
→ Read this file + README.md

**If you have 15 minutes:**
→ Read README.md + QUICK_REFERENCE.md

**If you want to customize:**
→ Read QUICK_REFERENCE.md (Customizations section)

**If you're a developer:**
→ Read DEVELOPER.md

**If you're lost:**
→ Read DOCUMENTATION_INDEX.md

---

## 🚨 Troubleshooting

### App won't install
```bash
# Make sure device is connected
adb devices

# Grant permissions
adb shell pm grant com.example.mynmea2000monitor android.permission.INTERNET
```

### App crashes on startup
```bash
# Check for errors
adb logcat | grep ERROR

# Reinstall cleanly
adb uninstall com.example.mynmea2000monitor
./gradlew installDebug
```

### Can't find devices
1. Check WiFi connection
2. Verify gateway is on same network
3. Ensure gateway is powered on
4. Check firewall settings
5. Try with sample data first

### No data from device
1. Verify device IP address
2. Check gateway NMEA output
3. Verify port 10110 is open
4. Check NMEA sentence format
5. Review logcat for errors

See **SETUP.md** for detailed troubleshooting.

---

## 🎨 Customization

### Change Theme Color
File: `ui/theme/Color.kt`
```kotlin
val OceanBlue = Color(0xFF0066CC)  // Your color here
```

### Change App Name
File: `res/values/strings.xml`
```xml
<string name="app_name">My Marine Monitor</string>
```

### Change Ports
File: `util/Constants.kt`
```kotlin
const val NMEA_TCP_PORT = 10110  // Your port
```

See **QUICK_REFERENCE.md** for more customizations.

---

## 🔄 Build Commands

```bash
# Clean build
./gradlew clean build

# Debug APK
./gradlew assembleDebug

# Release APK
./gradlew assembleRelease

# Run tests
./gradlew test

# Check code
./gradlew lint
```

---

## 📞 Getting Help

1. **Quick question?** → Check QUICK_REFERENCE.md
2. **Setup problem?** → See SETUP.md
3. **Code question?** → Read DEVELOPER.md
4. **Lost?** → Use DOCUMENTATION_INDEX.md
5. **View logs** → Run `adb logcat | grep Nmea`

---

## 🚀 Next Steps

### Today
- [x] Build and install the app
- [x] Try it with sample data
- [x] Test scanning for devices

### This Week
- [ ] Test with real NMEA gateway
- [ ] Customize colors/branding
- [ ] Add custom NMEA sentence types
- [ ] Deploy to test users

### This Month
- [ ] Implement data logging
- [ ] Add settings screen
- [ ] Test thoroughly
- [ ] Plan Play Store release

---

## 💡 Tips

**Tip 1:** Use sample data to test UI before connecting real hardware
**Tip 2:** Always check logcat when something doesn't work
**Tip 3:** Restart app after changing constants
**Tip 4:** Verify WiFi connection before searching for devices
**Tip 5:** Check firewall if discovery times out

---

## 📋 Checklist for Success

- [x] Project built successfully
- [x] Code compiles without errors
- [x] All dependencies available
- [x] Documentation complete
- [x] Sample data included
- [x] Ready for deployment

✅ **Everything is ready!**

---

## 🎯 What Now?

**You have three options:**

1. **Keep it simple:** Use the app as-is with sample data
2. **Connect real hardware:** Configure for your NMEA gateway
3. **Customize it:** Modify colors, add features, make it yours

---

## 🎉 You're All Set!

Your NMEA 2000 WiFi Monitor is ready to use. 

**Start exploring:** Open the app, tap Scan, and see it in action!

For questions, check the documentation files in the project root.

Happy monitoring! ⛵📊

---

**Quick Links:**
- 📖 README.md - Features overview
- ⚙️  SETUP.md - Installation
- 💨 QUICK_REFERENCE.md - Quick answers
- 👨‍💻 DEVELOPER.md - Technical details
- 📚 DOCUMENTATION_INDEX.md - All docs

---

**Build Date:** April 2026
**Status:** ✅ Complete & Ready
**Version:** 1.0


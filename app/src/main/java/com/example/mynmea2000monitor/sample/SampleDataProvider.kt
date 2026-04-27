package com.example.mynmea2000monitor.sample

import com.example.mynmea2000monitor.model.DataField
import com.example.mynmea2000monitor.model.Nmea2000Device

/**
 * Sample data provider for testing the app without a real NMEA 2000 device
 */
object SampleDataProvider {

    fun getSampleDevices(): List<Nmea2000Device> {
        return listOf(
            Nmea2000Device(
                id = "device_1",
                name = "Engine Monitor 1",
                ipAddress = "192.168.1.100",
                deviceType = "Engine",
                serialNumber = "SN-001-2024",
                isOnline = true,
                dataFields = getSampleEngineData()
            ),
            Nmea2000Device(
                id = "device_2",
                name = "Tank Monitor",
                ipAddress = "192.168.1.101",
                deviceType = "Tank Monitor",
                serialNumber = "SN-002-2024",
                isOnline = true,
                dataFields = getSampleTankData()
            ),
            Nmea2000Device(
                id = "device_3",
                name = "Battery System",
                ipAddress = "192.168.1.102",
                deviceType = "Battery Monitor",
                serialNumber = "SN-003-2024",
                isOnline = false,
                dataFields = getSampleBatteryData()
            ),
            Nmea2000Device(
                id = "device_4",
                name = "GPS/Navigation",
                ipAddress = "192.168.1.103",
                deviceType = "GPS",
                serialNumber = "SN-004-2024",
                isOnline = true,
                dataFields = getSamplePositionData()
            )
        )
    }

    private fun getSampleEngineData(): List<DataField> {
        return listOf(
            DataField(
                name = "Engine Speed",
                value = "2500",
                unit = "RPM",
                timestamp = System.currentTimeMillis()
            ),
            DataField(
                name = "Oil Pressure",
                value = "45",
                unit = "PSI",
                timestamp = System.currentTimeMillis()
            ),
            DataField(
                name = "Oil Temperature",
                value = "85",
                unit = "°C",
                timestamp = System.currentTimeMillis()
            ),
            DataField(
                name = "Coolant Temperature",
                value = "92",
                unit = "°C",
                timestamp = System.currentTimeMillis()
            ),
            DataField(
                name = "Boost Pressure",
                value = "1.2",
                unit = "Bar",
                timestamp = System.currentTimeMillis()
            ),
            DataField(
                name = "Fuel Rate",
                value = "35.5",
                unit = "L/h",
                timestamp = System.currentTimeMillis()
            ),
            DataField(
                name = "Alternator Output",
                value = "65",
                unit = "A",
                timestamp = System.currentTimeMillis()
            )
        )
    }

    private fun getSampleTankData(): List<DataField> {
        return listOf(
            DataField(
                name = "Fuel Tank Level",
                value = "75",
                unit = "%",
                timestamp = System.currentTimeMillis()
            ),
            DataField(
                name = "Fuel Tank Capacity",
                value = "200",
                unit = "L",
                timestamp = System.currentTimeMillis()
            ),
            DataField(
                name = "Fresh Water Tank",
                value = "85",
                unit = "%",
                timestamp = System.currentTimeMillis()
            ),
            DataField(
                name = "Fresh Water Capacity",
                value = "100",
                unit = "L",
                timestamp = System.currentTimeMillis()
            ),
            DataField(
                name = "Gray Water Tank",
                value = "45",
                unit = "%",
                timestamp = System.currentTimeMillis()
            ),
            DataField(
                name = "Black Water Tank",
                value = "25",
                unit = "%",
                timestamp = System.currentTimeMillis()
            )
        )
    }

    private fun getSampleBatteryData(): List<DataField> {
        return listOf(
            DataField(
                name = "Battery Voltage",
                value = "13.5",
                unit = "V",
                timestamp = System.currentTimeMillis()
            ),
            DataField(
                name = "Battery Current",
                value = "25",
                unit = "A",
                timestamp = System.currentTimeMillis()
            ),
            DataField(
                name = "Battery Temperature",
                value = "28",
                unit = "°C",
                timestamp = System.currentTimeMillis()
            ),
            DataField(
                name = "State of Charge",
                value = "92",
                unit = "%",
                timestamp = System.currentTimeMillis()
            ),
            DataField(
                name = "Energy Used",
                value = "45.2",
                unit = "Ah",
                timestamp = System.currentTimeMillis()
            )
        )
    }

    private fun getSamplePositionData(): List<DataField> {
        return listOf(
            DataField(
                name = "Latitude",
                value = "48.07038",
                unit = "°N",
                timestamp = System.currentTimeMillis()
            ),
            DataField(
                name = "Longitude",
                value = "11.31000",
                unit = "°E",
                timestamp = System.currentTimeMillis()
            ),
            DataField(
                name = "Speed Over Ground",
                value = "12.5",
                unit = "knots",
                timestamp = System.currentTimeMillis()
            ),
            DataField(
                name = "Course Over Ground",
                value = "245",
                unit = "°",
                timestamp = System.currentTimeMillis()
            ),
            DataField(
                name = "Satellites",
                value = "12",
                unit = "count",
                timestamp = System.currentTimeMillis()
            ),
            DataField(
                name = "HDOP",
                value = "0.9",
                unit = "m",
                timestamp = System.currentTimeMillis()
            )
        )
    }

    fun getSampleNmeaSentences(): List<String> {
        return listOf(
            "\$GPRMC,123519,4807.038,N,01131.000,E,022.4,084.4,230394,003.1,W*6A",
            "\$GPGGA,123519,4807.038,N,01131.000,E,1,08,0.9,545.4,M,46.9,M,,*47",
            "\$GPVTG,054.7,T,034.4,M,005.5,N,010.2,K*48",
            "\$GPGSA,A,3,04,05,,09,12,,,24,,,,,2.5,1.3,2.1*30",
            "\$GPGSV,2,1,08,01,40,083,46,02,17,308,41,12,07,344,39,14,22,228,45*75",
            "\$GPGSV,2,2,08,15,13,034,42,19,19,178,43,24,12,273,44,25,09,053,41*77"
        )
    }
}


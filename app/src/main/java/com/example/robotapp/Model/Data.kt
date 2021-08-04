package com.example.robotapp.Model

class Data {

    private var battery: String = ""
    private var temperature: String = ""
    private var humidity: String = ""
    private var airquality: String = ""


    constructor()
    constructor(battery: String, temperature: String, humidity: String, airquality: String) {
        this.battery = battery
        this.temperature = temperature
        this.humidity = humidity
        this.airquality = airquality
    }


    fun getBattery(): String {
        return battery
    }

    fun setBattery(battery: String) {
        this.battery = battery
    }

    fun getTemperature(): String {
        return temperature
    }

    fun setTemperature(temperature: String) {
        this.temperature = temperature
    }

    fun getHumidity(): String {
        return humidity
    }

    fun setHumidity(humidity: String) {
        this.humidity = humidity
    }

    fun getAirQuality(): String {
        return airquality
    }

    fun setAirQuality(airquality: String) {
        this.airquality = airquality
    }
}
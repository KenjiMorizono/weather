package com.example.org.weather

import android.util.Log
import com.google.gson.Gson

class ApiInterface {
    companion object {
        private val gson: Gson = Gson()

        fun GetRealTimeStats(lat: Double, lon: Double, unit: String, completionHandler: (response: RealTimeStats?) -> Unit) {

            ApiInterfaceRaw.GetRealTimeStats(lat, lon, unit, arrayListOf("temp", "feels_like", "wind_speed", "visibility", "humidity", "wind_direction", "precipitation", "precipitation_type", "cloud_cover", "fire_index", "sunrise", "sunset", "weather_code")){ response ->
                var stats = this.gson.fromJson(response, RealTimeStats::class.java)
                Log.d("ApiInterface", stats.lat.toString())
                completionHandler(stats)
            }
        }
    }
}
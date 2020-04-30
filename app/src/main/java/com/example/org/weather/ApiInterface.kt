package com.example.org.weather

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.Exception
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ApiInterface {
    companion object {
        private val gson: Gson = Gson()

        fun GetRealTimeStats(lat: Float, lon: Float, useUSMetric: Boolean, completionHandler: (response: TimeStats?) -> Unit) {
            var unit = if(useUSMetric){
                "us"
            }
            else
            {
                "si"
            }

            ApiInterfaceRaw.GetRealTimeStats(lat, lon, unit, arrayListOf("temp", "feels_like", "wind_speed", "visibility", "humidity", "wind_direction", "precipitation", "precipitation_type", "cloud_cover", "fire_index", "sunrise", "sunset", "weather_code")){ response ->
                var stats = this.gson.fromJson(response, TimeStats::class.java)
                Log.d("ApiInterface", stats.lat.toString())
                completionHandler(stats)
            }
        }

        fun GetHistoricalStats(lat: Float, lon: Float, useUSMetric: Boolean, minuteTimeStep: Int, startTime: LocalDateTime, completionHandler: (response: List<TimeStats?>) -> Unit) {
            var unit = if(useUSMetric){
                "us"
            }
            else
            {
                "si"
            }

            // make sure that the earliest they go is 7 hours back from now
            var startDateTime = startTime
            var maxStart = LocalDateTime.now().minusHours(7)
            if(startTime.isBefore(maxStart)) {
                startDateTime = maxStart
            }

            var start = startDateTime.plusHours(6).toString().format(DateTimeFormatter.ISO_DATE_TIME)
            var end = "now"

            ApiInterfaceRaw.GetHistoricalStats(lat, lon, unit, minuteTimeStep, start, end, arrayListOf("temp", "feels_like", "wind_speed", "visibility", "humidity", "wind_direction", "precipitation", "precipitation_type", "cloud_cover", "fire_index", "sunrise", "sunset", "weather_code")){ response ->
                var statsList = this.gson.fromJson<Array<TimeStats>>(response, TypeToken.getArray(TimeStats::class.java).type)
                Log.d("ApiInterface", statsList[0].lat.toString())
                completionHandler(statsList.asList())
            }
        }

        fun GetNowcastStats(lat: Float, lon: Float, useUSMetric: Boolean, minuteTimeStep: Int, endTime: LocalDateTime, completionHandler: (response: List<TimeStats?>) -> Unit) {
            var unit = if(useUSMetric){
                "us"
            }
            else
            {
                "si"
            }

            // make sure that the earliest they go is 6 hours ahead from now
            var endDateTime = endTime
            var maxEnd = LocalDateTime.now().plusHours(6)
            if(endTime.isAfter(maxEnd)) {
                endDateTime = maxEnd
            }

            var end = endDateTime.plusHours(6).toString().format(DateTimeFormatter.ISO_DATE_TIME)
            var start = "now"

            ApiInterfaceRaw.GetNowcastStats(lat, lon, unit, minuteTimeStep, start, end, arrayListOf("temp", "feels_like", "wind_speed", "visibility", "humidity", "wind_direction", "precipitation", "precipitation_type", "cloud_cover", "fire_index", "sunrise", "sunset", "weather_code")){ response ->
                var statsList = this.gson.fromJson<Array<TimeStats>>(response, TypeToken.getArray(TimeStats::class.java).type)
                Log.d("ApiInterface", statsList[0].lat.toString())
                completionHandler(statsList.asList())
            }
        }

        fun GetHourlyStats(lat: Float, lon: Float, useUSMetric: Boolean, hoursAhead: Int, completionHandler: (response: List<TimeStats?>) -> Unit) {
            var unit = if(useUSMetric){
                "us"
            }
            else
            {
                "si"
            }

            // make sure that the earliest they go is 100 hours ahead from now

            var hoursToAdd : Long = 96
            if(hoursAhead < 96) {
                if(hoursAhead >= 0){
                    hoursToAdd = hoursAhead.toLong()
                }
                else
                {
                    hoursToAdd = 0
                }
            }

            // plus 6 for local time to utc
            hoursToAdd += 6

            var end = LocalDateTime.now().plusHours(hoursToAdd).toString().format(DateTimeFormatter.ISO_DATE_TIME)
            var start = "now"

            ApiInterfaceRaw.GetHourlyStats(lat, lon, unit, start, end, arrayListOf("temp", "feels_like", "wind_speed", "visibility", "humidity", "wind_direction", "precipitation", "precipitation_type", "cloud_cover", "sunrise", "sunset", "weather_code")){ response ->
                var statsList = this.gson.fromJson<Array<TimeStats>>(response, TypeToken.getArray(TimeStats::class.java).type)
                Log.d("ApiInterface", statsList[0].lat.toString())
                completionHandler(statsList.asList())
            }
        }

        fun GetDailyStats(lat: Float, lon: Float, useUSMetric: Boolean, daysAhead: Int, completionHandler: (response: List<DailyStats?>) -> Unit) {
            var unit = if(useUSMetric){
                "us"
            }
            else
            {
                "si"
            }

            // make sure that the earliest they go is 15 days ahead from now

            var daysToAdd : Long = 14
            if(daysAhead < 14) {
                if(daysAhead >= 0){
                    daysToAdd = daysAhead.toLong()
                }
                else
                {
                    daysToAdd = 0
                }
            }

            var end = LocalDateTime.now().plusDays(daysToAdd).plusHours(6).toString().format(DateTimeFormatter.ISO_DATE_TIME)
            var start = "now"

            ApiInterfaceRaw.GetDailyStats(lat, lon, unit, start, end, arrayListOf("temp", "feels_like", "wind_speed", "visibility", "humidity", "wind_direction", "precipitation", "sunrise", "sunset", "weather_code")){ response ->
                var statsList = this.gson.fromJson<Array<DailyStats>>(response, TypeToken.getArray(DailyStats::class.java).type)
                Log.d("ApiInterface", statsList[0].lat.toString())
                completionHandler(statsList.asList())
            }
        }

        fun GetLayerPNG(type: String, zoom: Int, x: Int, y: Int, region: String, completionHandler: (response: Bitmap?) -> Unit) {

            ApiInterfaceRaw.GetLayerPNG(type, zoom, x, y, region){ response ->
                /*var i = 0
                while (i < response!!.length)
                {
                    try {
                        var response1 = response!!.substring(i)
                        var decodedString : ByteArray = Base64.decode(response1, Base64.DEFAULT)
                        //var decodedString : ByteArray = response!!.toByteArray()
                        var png : Bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                        var j = 0
                    }
                    catch (e : Exception) { i++ }
                }


                var statsList = this.gson.fromJson<Array<DailyStats>>(response, TypeToken.getArray(DailyStats::class.java).type)
                Log.d("ApiInterface", statsList[0].lat.toString())*/
                completionHandler(response)
            }
        }
    }
}
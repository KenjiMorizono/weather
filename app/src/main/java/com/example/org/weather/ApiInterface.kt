package com.example.org.weather

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.util.Base64
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.Exception
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.PI
import kotlin.math.asinh
import kotlin.math.floor
import kotlin.math.tan

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

            // make sure that the earliest they go is 96 hours ahead from now

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

        fun GetLayerPNG(lat: Float, lon: Float, zoom: Int, type: String, region: String, completionHandler: (response: Bitmap?) -> Unit) {
            var xyPair = ApiInterface.GetXYTile(lat.toDouble(), lon.toDouble(), zoom)
            ApiInterfaceRaw.GetBackgroundPNG(zoom, xyPair.first, xyPair.second){ bitmap1 ->
                ApiInterfaceRaw.GetLayerPNG(type, zoom, xyPair.first, xyPair.second, region){ bitmap2 ->
                    var bitmapFinal: Bitmap = Bitmap.createBitmap(bitmap1!!.getWidth(), bitmap1.getHeight(), bitmap1.getConfig())
                    var canvas: Canvas = Canvas(bitmapFinal)
                    canvas.drawBitmap(bitmap1, Matrix(), null)
                    canvas.drawBitmap(bitmap2!!, Matrix(), null)
                    completionHandler(bitmapFinal)
                }
            }
        }

        fun GetXYTile(lat : Double, lon: Double, zoom : Int) : Pair<Int, Int> {
            val latRad = Math.toRadians(lat)
            var xtile = floor( (lon + 180) / 360 * (1 shl zoom) ).toInt()
            var ytile = floor( (1.0 - asinh(tan(latRad)) / PI) / 2 * (1 shl zoom) ).toInt()

            if (xtile < 0) {
                xtile = 0
            }
            if (xtile >= (1 shl zoom)) {
                xtile= (1 shl zoom) - 1
            }
            if (ytile < 0) {
                ytile = 0
            }
            if (ytile >= (1 shl zoom)) {
                ytile = (1 shl zoom) - 1
            }

            return Pair(xtile, ytile)
        }
    }
}
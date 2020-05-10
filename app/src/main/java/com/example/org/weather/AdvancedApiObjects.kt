package com.example.org.weather

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class TimeStats {
    val lat : Double? = 0.0
    val lon : Double? = 0.0
    val temp : ApiDouble = ApiDouble()
    val feels_like : ApiDouble = ApiDouble()
    val wind_speed : ApiDouble = ApiDouble()
    val visibility : ApiDouble = ApiDouble()
    val humidity : ApiDouble = ApiDouble()
    val wind_direction : ApiInt = ApiInt()
    val precipitation : ApiDouble = ApiDouble()
    val precipitation_type : ApiString = ApiString()
    val cloud_cover : ApiDouble = ApiDouble()
    val fire_index : ApiDouble = ApiDouble()
    val sunrise : ApiDateTime = ApiDateTime()
    val sunset : ApiDateTime = ApiDateTime()
    val weather_code : ApiString = ApiString()
    val observation_time: ApiDateTime = ApiDateTime()
}

class DailyStats {
    val lat : Double? = 0.0
    val lon : Double? = 0.0

    val temp : Array<ApiMinMax> = arrayOf<ApiMinMax>()
    val feels_like : Array<ApiMinMax> = arrayOf<ApiMinMax>()
    val wind_speed : Array<ApiMinMax> = arrayOf<ApiMinMax>()
    val visibility : Array<ApiMinMax> = arrayOf<ApiMinMax>()
    val humidity : Array<ApiMinMax> = arrayOf<ApiMinMax>()
    val wind_direction : Array<ApiMinMax> = arrayOf<ApiMinMax>()
    val precipitation : Array<ApiMinMax> = arrayOf<ApiMinMax>()

    val sunrise : ApiDateTime = ApiDateTime()
    val sunset : ApiDateTime = ApiDateTime()
    val weather_code : ApiString = ApiString()
    val observation_time: ApiDate = ApiDate()
}
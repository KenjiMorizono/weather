package com.example.org.weather

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class TimeStats {
    val lat : Double? = 0.0
    val lon : Double? = 0.0
    val temp : ApiFloat = ApiFloat()
    val feels_like : ApiFloat = ApiFloat()
    val wind_speed : ApiFloat = ApiFloat()
    val visibility : ApiFloat = ApiFloat()
    val humidity : ApiFloat = ApiFloat()
    val wind_direction : ApiInt = ApiInt()
    val precipitation : ApiFloat = ApiFloat()
    val precipitation_type : ApiString = ApiString()
    val cloud_cover : ApiFloat = ApiFloat()
    val fire_index : ApiFloat = ApiFloat()
    val sunrise : ApiDate = ApiDate()
    val sunset : ApiDate = ApiDate()
    val weather_code : ApiString = ApiString()
    val observation_time: ApiDate = ApiDate()
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

    val sunrise : ApiDate = ApiDate()
    val sunset : ApiDate = ApiDate()
    val weather_code : ApiString = ApiString()
    val observation_time: ApiDate = ApiDate()
}
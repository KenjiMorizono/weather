package com.example.org.weather

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class RealTimeStats {
    val lat : Double? = 0.0
    val lon : Double? = 0.0
    val temp : ApiDouble = ApiDouble()
    val feels_like : ApiDouble = ApiDouble()
    val wind_speed : ApiDouble = ApiDouble()
    val visibility : ApiDouble = ApiDouble()
    val humidity : ApiDouble = ApiDouble()
    val wind_direction : ApiDouble = ApiDouble()
    val precipitation : ApiDouble = ApiDouble()
    val precipitation_type : ApiString = ApiString()
    val observation_time: ApiDate = ApiDate()
}
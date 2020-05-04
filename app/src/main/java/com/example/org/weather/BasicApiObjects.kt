package com.example.org.weather

import java.lang.Exception
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder

class ApiInt {
    val value : String? = ""
    val units : String? = ""

    fun TryGetInt() : Int? {
        var intVal : Int? = null
        try{
            if(this.value != null) {
                intVal = this.value.toInt()
            }
            else
            {
                return null
            }
        }
        catch (e : Exception) { return null }

        return intVal
    }
}

class ApiFloat {
    val value : Float? = 0.0f
    val units : String? = ""
}

class ApiString {
    val value : String? = ""
    val units : String? = ""
}

class ApiDate {
    private val value : String? = ""

    fun GetLocalDateTime() : LocalDateTime? {
        var dateTime : LocalDateTime = LocalDateTime.MIN
        try{
            dateTime = LocalDateTime.parse(this.value, DateTimeFormatter.ISO_DATE_TIME)
            dateTime = dateTime.minusHours(6)
        }
        catch (e : Exception) { return null }

        return dateTime
    }
}

class ApiMinMax {
    private val observation_time : String? = ""
    private val min : ApiFloat? = null
    private val max : ApiFloat? = null

    fun GetLocalDateTime() : LocalDateTime? {
        var dateTime : LocalDateTime = LocalDateTime.MIN
        try{
            dateTime = LocalDateTime.parse(this.observation_time, DateTimeFormatter.ISO_DATE_TIME)
            dateTime = dateTime.minusHours(6)
        }
        catch (e : Exception) { return null }

        return dateTime
    }

    fun GetMinOrMax() : ApiFloat? {
        if(this.min != null) { return this.min }
        if(this.max != null) { return this.min }
        return null
    }
}
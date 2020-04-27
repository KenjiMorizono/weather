package com.example.org.weather

import java.lang.Exception
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ApiInt {
    val value : Int? = 0
    val units : String? = ""
}

class ApiDouble {
    val value : Double? = 0.0
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
        }
        catch (e : Exception) { return null }

        return dateTime
    }
}
package com.example.org.weather

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_weather_display.*

class weatherDisplayFragment : Fragment() {
    private var info : LocationInfo? = null
    private var temperature = 0.0
    private var latitude = 0.0
    private var longitude = 0.0
    private var humidity = 0.0
    private var tempPrefix = "°"

    companion object {
        fun newInstance(locationInfo : LocationInfo) : weatherDisplayFragment {
            val displayFrag = weatherDisplayFragment()
            displayFrag.info = locationInfo
            return displayFrag
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather_display, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        this@weatherDisplayFragment.temperature = info!!.getTemperature()
        this@weatherDisplayFragment.latitude = info!!.getLatitude()
        this@weatherDisplayFragment.longitude = info!!.getLongitude()
        this@weatherDisplayFragment.humidity = info!!.getHumidity()
        updateDisplay()
    }

    fun updateDisplay(){
        if (this@weatherDisplayFragment.info!!.getUnitBoolean()){
            temperatureText.text = temperature.toString() + tempPrefix + "F"

        }
        else {
            temperatureText.text = temperature.toString() + tempPrefix + "C"

        }
        locationText.text = "(" + latitude.toString() + ", " + longitude.toString() + ")"
        humidityText.text = "Humidity: " + humidity.toString() + "%"
    }

}

package com.example.org.weather

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_weather_display.*

class weatherDisplayFragment : Fragment() {
    private var temperature = 0.0
    private var tempUnitCelsius = false
    private val tempPrefix = "°"

    companion object {
        fun newInstance(tempVal : Double, celsius : Boolean) : weatherDisplayFragment {
            val displayFrag = weatherDisplayFragment()
            displayFrag.temperature = tempVal
            displayFrag.tempUnitCelsius = celsius
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
        updateTemperature()
    }

    fun updateTemperature(){
        if (tempUnitCelsius){
            temperatureText.text = temperature.toString() + tempPrefix + "C"
        }
        else {
            temperatureText.text = temperature.toString() + tempPrefix + "F"

        }
    }

}

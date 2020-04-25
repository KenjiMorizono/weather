package com.example.org.weather

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class weatherDisplayFragment : Fragment() {
    private var temperature = 0.00f
    private var tempUnitCelsius = false
    private val tempPrefix = "°"

    companion object {
        fun newInstance(tempVal : Float, celsius : Boolean) : weatherDisplayFragment {
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
    }

}

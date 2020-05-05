package com.example.org.weather

import android.location.Address
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_weather_display.*

class weatherDisplayFragment : Fragment() {
    private var info : LocationInfo? = null
    private val tempPrefix = "°"
    private var zoom = 5

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

        if(info!!.RealTimeStats == null) {
            ApiInterface.GetRealTimeStats(info!!.getLatitude(), info!!.getLongitude(), info!!.getUnitBoolean()) { stats ->
                info!!.RealTimeStats = stats
                this.updateRealTimeDisplay()
            }
        }
        else
        {
            this.updateRealTimeDisplay()
        }

        this.btnMinus.setOnClickListener{
            if(this.zoom > 1)
            {
                this.zoom--
            }
            this.updatePicture(this.zoom)
        }

        this.btnPlus.setOnClickListener{
            if(this.zoom < 12)
            {
                this.zoom++
            }
            this.updatePicture(this.zoom)
        }

        this.updatePicture(this.zoom)
    }

    fun updateRealTimeDisplay(){

        temperatureText.text = info!!.RealTimeStats!!.temp.value.toString() + tempPrefix + info!!.RealTimeStats!!.temp.units
        var locationTextForDisplay = ""
        var addressDisplayArrayMax : Int = info!!.getLocationDescription().maxAddressLineIndex
        for(i in 0 .. addressDisplayArrayMax){
            if (i == addressDisplayArrayMax - 1){
                locationTextForDisplay += info!!.getLocationDescription().getAddressLine(i)
            }
            else {
                locationTextForDisplay += info!!.getLocationDescription().getAddressLine(i) + "\n"
            }

        }

        locationText.text = locationTextForDisplay
        humidityText.text = "Humidity: " + info!!.RealTimeStats!!.humidity.value.toString() + info!!.RealTimeStats!!.humidity.units
    }

    fun updatePicture(Zoom : Int){
        ApiInterface.GetLayerPNG(info!!.getLatitude(), info!!.getLongitude(), Zoom, "precipitation", "global"){ bitmap ->
            this.weatherIconPlaceholder.visibility = View.VISIBLE
            this.weatherIconPlaceholder.setImageBitmap(bitmap)
        }
    }

}

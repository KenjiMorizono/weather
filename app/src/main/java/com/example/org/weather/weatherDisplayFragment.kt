package com.example.org.weather

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
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

        if(info!!.DailyStats == null) {
            ApiInterface.GetDailyStats(info!!.getLatitude(), info!!.getLongitude(), info!!.getUnitBoolean(), 16) { stats ->
                info!!.DailyStats = stats
                this.updateDailyDisplay()
            }
        }
        else
        {
            this.updateDailyDisplay()
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

    fun updateDailyDisplay(){
        this.DailyPager.adapter = DailyInfoPagerAdapter(info!!.DailyStats!!, this.activity)
        var counter: Int = 0

        while(counter < info!!.DailyStats!!.size) {
            //val text_view: TextView = TextView(this.activity)
            this.DailyPager.addView(TextView(this.activity), counter)
            counter++
        }
    }

    fun updatePicture(Zoom : Int){
        ApiInterface.GetLayerPNG(info!!.getLatitude(), info!!.getLongitude(), Zoom, "precipitation", "global"){ bitmap ->
            this.weatherIconPlaceholder.visibility = View.VISIBLE
            this.weatherIconPlaceholder.setImageBitmap(bitmap)
        }
    }


    //https://www.journaldev.com/10096/android-viewpager-example-tutorial
    class DailyInfoPagerAdapter constructor(dailyStatsList: List<DailyStats>, context: Context?) : PagerAdapter() {
        var statsList: List<DailyStats>? = null
        var mContext: Context? = null

        init {
            statsList = dailyStatsList
            mContext = context
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {

            val txtDayOfWeek: TextView = TextView(this.mContext)

            // Display some text on the newly created text view
            if(position != 0) {
                var dateTime = statsList!![position].observation_time.GetLocalDate()
                if (dateTime != null) {
                    txtDayOfWeek.text = dateTime.dayOfWeek.name
                }
            }
            else
            {
                txtDayOfWeek.text = "Today"
            }

            this.setHeaderAttributes(txtDayOfWeek)

            val txtTemp: TextView = TextView(this.mContext)

            // Display some text on the newly created text view
            var tempArray = statsList!![position].temp
            if(!tempArray.isNullOrEmpty()) {
                txtTemp.text = "Temp: ${tempArray[1].GetMinOrMax()!!.value} - ${tempArray[0].GetMinOrMax()!!.value} °${tempArray[0].GetMinOrMax()!!.units}"
            }

            this.setBodyAttributes(txtTemp)

            val txtFeelsLike: TextView = TextView(this.mContext)

            // Display some text on the newly created text view
            var feelsLikeArray = statsList!![position].feels_like
            if(!feelsLikeArray.isNullOrEmpty()) {
                txtFeelsLike.text = "Feels like: ${feelsLikeArray[1].GetMinOrMax()!!.value} - ${feelsLikeArray[0].GetMinOrMax()!!.value} °${feelsLikeArray[0].GetMinOrMax()!!.units}"
            }

            this.setBodyAttributes(txtFeelsLike)

            val txtHumidity: TextView = TextView(this.mContext)

            // Display some text on the newly created text view
            var humidityArray = statsList!![position].humidity
            if(!humidityArray.isNullOrEmpty()) {
                txtHumidity.text = "Humidity: ${humidityArray[1].GetMinOrMax()!!.value} - ${humidityArray[0].GetMinOrMax()!!.value} ${humidityArray[0].GetMinOrMax()!!.units}"
            }

            this.setBodyAttributes(txtHumidity)

            val txtWindSpeed: TextView = TextView(this.mContext)

            // Display some text on the newly created text view
            var windSpeedArray = statsList!![position].wind_speed
            if(!windSpeedArray.isNullOrEmpty()) {
                txtWindSpeed.text = "Wind Speed: ${windSpeedArray[1].GetMinOrMax()!!.value} - ${windSpeedArray[0].GetMinOrMax()!!.value} ${windSpeedArray[0].GetMinOrMax()!!.units}"
            }

            this.setBodyAttributes(txtWindSpeed)

            var layout: LinearLayout = LinearLayout(this.mContext)
            layout.setBackgroundColor(Color.CYAN)
            layout.orientation = LinearLayout.VERTICAL
            layout.addView(txtDayOfWeek)
            layout.addView(txtTemp)
            layout.addView(txtFeelsLike)
            layout.addView(txtHumidity)
            layout.addView(txtWindSpeed)

            container.addView(layout, position)

            return layout
        }

        fun setHeaderAttributes(txtView: TextView)
        {
            // Set the text view font/text size
            txtView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30F)

            // Set the text view text color
            txtView.setTextColor(Color.BLACK)

            // Make the text viw text bold italic
            txtView.setTypeface(txtView.typeface, Typeface.BOLD)

            // Change the text view font
            //text_view.setTypeface(Typeface.MONOSPACE)

            // Put some padding on text view text
            //text_view.setPadding(50, 10, 10, 10)
            txtView.gravity = Gravity.CENTER_HORIZONTAL
        }

        fun setBodyAttributes(txtView: TextView)
        {
            // Set the text view font/text size
            txtView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24F)

            // Set the text view text color
            txtView.setTextColor(Color.BLACK)

            // Make the text viw text bold italic
            txtView.setTypeface(txtView.typeface, Typeface.NORMAL)

            // Change the text view font
            //text_view.setTypeface(Typeface.MONOSPACE)

            // Put some padding on text view text
            //text_view.setPadding(50, 10, 10, 10)
            txtView.gravity = Gravity.CENTER_HORIZONTAL
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view == `object`
        }

        override fun getCount(): Int {
            return this.statsList!!.size
        }
    }

}

package com.example.org.weather

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.graphics.drawable.RoundedBitmapDrawable
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

        if(info!!.HourlyStats == null) {
            ApiInterface.GetHourlyStats(info!!.getLatitude(), info!!.getLongitude(), info!!.getUnitBoolean(), 24) { stats ->
                info!!.HourlyStats = stats
                this.updateHourlyDisplay()
            }
        }
        else
        {
            this.updateHourlyDisplay()
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
                this.updatePicture(this.zoom)
            }
        }

        this.btnPlus.setOnClickListener{
            if(this.zoom < 12)
            {
                this.zoom++
                this.updatePicture(this.zoom)
            }
        }

        this.updatePicture(this.zoom)
    }

    fun updateRealTimeDisplay(){

        var locationTextForDisplay = ""
        var addressDisplayArrayMax : Int = info!!.getLocationDescription().maxAddressLineIndex
        for(i in 0 .. addressDisplayArrayMax){
            if (i == addressDisplayArrayMax - 1){
                locationTextForDisplay += info!!.getLocationDescription().locality + ", " + info!!.getLocationDescription().adminArea + " " + info!!.getLocationDescription().postalCode
            }
            else {
                locationTextForDisplay += info!!.getLocationDescription().locality + ", " + info!!.getLocationDescription().adminArea + " " + info!!.getLocationDescription().postalCode
            }

        }

        txtLocation.text = locationTextForDisplay
        txtTemp.text = info!!.RealTimeStats!!.temp.value.toString() + " " + tempPrefix + info!!.RealTimeStats!!.temp.units
        txtWeatherCode.text = info!!.RealTimeStats!!.weather_code.value!!.replace("_", " ").capitalize()
        txtFeelsLike.text = "Feels like: ${info!!.RealTimeStats!!.feels_like.value} ${tempPrefix}${info!!.RealTimeStats!!.feels_like.units}"
        txtCloudCover.text = "Cloud Cover: ${info!!.RealTimeStats!!.cloud_cover.value}${info!!.RealTimeStats!!.cloud_cover.units}"
        txtHumidity.text = "Humidity: ${info!!.RealTimeStats!!.humidity.value}${info!!.RealTimeStats!!.humidity.units}"
        txtPrecipitationType.text = "Precipitation type: ${info!!.RealTimeStats!!.precipitation_type.value} ${info!!.RealTimeStats!!.precipitation_type.units}"
        txtPrecipitation.text = "Precipitation: ${info!!.RealTimeStats!!.precipitation.value} ${info!!.RealTimeStats!!.precipitation.units}"
        txtVisibility.text = "Visibility: ${info!!.RealTimeStats!!.visibility.value} ${info!!.RealTimeStats!!.visibility.units}"
        txtWindSpeed.text = "Wind speed: ${info!!.RealTimeStats!!.wind_speed.value} ${info!!.RealTimeStats!!.wind_speed.units}"
        txtFireIndex.text = "Fire index: ${info!!.RealTimeStats!!.fire_index.value} ${info!!.RealTimeStats!!.fire_index.units}"
    }

    fun updateHourlyDisplay(){
        this.HourlyPager.adapter = HourlyInfoPagerAdapter(info!!.HourlyStats!!, this.activity)
        var counter: Int = 0

        while(counter < info!!.HourlyStats!!.size) {
            this.HourlyPager.addView(TextView(this.activity), counter)
            counter++
        }
    }

    fun updateDailyDisplay(){
        this.DailyPager.adapter = DailyInfoPagerAdapter(info!!.DailyStats!!, this.activity)
        var counter: Int = 0

        while(counter < info!!.DailyStats!!.size) {
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
    class HourlyInfoPagerAdapter constructor(hourlyStatsList: List<TimeStats>, context: Context?) : PagerAdapter() {
        var statsList: List<TimeStats>? = null
        var mContext: Context? = null

        init {
            statsList = hourlyStatsList
            mContext = context
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {

            val txtHourOfDay: TextView = TextView(this.mContext)

            // Display some text on the newly created text view
            var dateTime = statsList!![position].observation_time.GetLocalDateTime()
            if (dateTime != null) {
                if(dateTime.hour != 0 && dateTime.hour != 12) {
                    txtHourOfDay.text = if (dateTime.hour > 12) { (dateTime.hour - 12).toString() + " pm" } else { dateTime.hour.toString() + " am" }
                }
                else if(dateTime.hour == 0)
                {
                    txtHourOfDay.text = "12 am"
                }
                else
                {
                    txtHourOfDay.text = "12 pm"
                }
            }

            this.setHeaderAttributes(txtHourOfDay)

            val txtWeatherCode: TextView = TextView(this.mContext)

            // Display some text on the newly created text view
            txtWeatherCode.text = "${statsList!![position].weather_code.value!!.replace("_", " ").capitalize()}"

            this.setSmallHeaderAttributes(txtWeatherCode)

            val txtTemp: TextView = TextView(this.mContext)

            // Display some text on the newly created text view
            txtTemp.text = "Temp: ${statsList!![position].temp!!.value} °${statsList!![position].temp!!.units}"

            this.setBodyAttributes(txtTemp)

            val txtCloudCover: TextView = TextView(this.mContext)

            // Display some text on the newly created text view
            txtCloudCover.text = "Cloud cover: ${statsList!![position].cloud_cover!!.value}${statsList!![position].cloud_cover!!.units}"

            this.setBodyAttributes(txtCloudCover)

            val txtFeelsLike: TextView = TextView(this.mContext)

            // Display some text on the newly created text view
            txtFeelsLike.text = "Feels like: ${statsList!![position].feels_like!!.value} °${statsList!![position].feels_like!!.units}"

            this.setBodyAttributes(txtFeelsLike)

            val txtHumidity: TextView = TextView(this.mContext)

            // Display some text on the newly created text view
            txtHumidity.text = "Humidity: ${statsList!![position].humidity!!.value}${statsList!![position].humidity!!.units}"

            this.setBodyAttributes(txtHumidity)

            val txtWindSpeed: TextView = TextView(this.mContext)

            // Display some text on the newly created text view
            txtWindSpeed.text = "Wind Speed: ${statsList!![position].wind_speed!!.value} ${statsList!![position].wind_speed!!.units}"

            this.setBodyAttributes(txtWindSpeed)

            val txtPrecipitationType: TextView = TextView(this.mContext)

            // Display some text on the newly created text view
            txtPrecipitationType.text = "Precipitation type: ${statsList!![position].precipitation_type.value}"

            this.setBodyAttributes(txtPrecipitationType)

            val txtPrecipitation: TextView = TextView(this.mContext)

            // Display some text on the newly created text view
            txtPrecipitation.text = "Precipitation: ${if(statsList!![position].precipitation.value.toString().contains("E")){0.0} else {statsList!![position].precipitation.value}} ${statsList!![position].precipitation.units}"

            this.setBodyAttributes(txtPrecipitation)

            val txtVisibility: TextView = TextView(this.mContext)

            // Display some text on the newly created text view
            txtVisibility.text = "Visibility: ${statsList!![position].visibility.value} ${statsList!![position].visibility.units}"

            this.setBodyAttributes(txtVisibility)

            var layout: LinearLayout = LinearLayout(this.mContext)
            var bitmap: Bitmap = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888)
            var paint = Paint()
            paint.color = mContext!!.getColor(R.color.colorAccent)
            var canvas = Canvas(bitmap).drawRoundRect(0.0f, 0.0f, 1000.0f, 1000.0f, 100.0f, 100.0f, paint )
            layout.background = BitmapDrawable(Resources.getSystem(), bitmap)
            layout.orientation = LinearLayout.VERTICAL
            layout.addView(txtHourOfDay)
            if(!txtWeatherCode.text.isNullOrEmpty()) {
                layout.addView(txtWeatherCode)
            }
            if(!txtCloudCover.text.isNullOrEmpty()) {
                layout.addView(txtCloudCover)
            }
            if(!txtTemp.text.isNullOrEmpty()) {
                layout.addView(txtTemp)
            }
            if(!txtFeelsLike.text.isNullOrEmpty()) {
                layout.addView(txtFeelsLike)
            }
            if(!txtHumidity.text.isNullOrEmpty()) {
                layout.addView(txtHumidity)
            }
            if(!txtWindSpeed.text.isNullOrEmpty()) {
                layout.addView(txtWindSpeed)
            }
            if(!txtPrecipitationType.text.isNullOrEmpty()) {
                layout.addView(txtPrecipitationType)
            }
            if(!txtPrecipitation.text.isNullOrEmpty()) {
                layout.addView(txtPrecipitation)
            }
            if(!txtVisibility.text.isNullOrEmpty()) {
                layout.addView(txtVisibility)
            }

            container.addView(layout, position)

            return layout
        }

        fun setHeaderAttributes(txtView: TextView)
        {
            // Set the text view font/text size
            txtView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30F)

            // Set the text view text color
            txtView.setTextColor(Color.WHITE)

            // Make the text viw text bold italic
            txtView.setTypeface(txtView.typeface, Typeface.BOLD)

            // Change the text view font
            //text_view.setTypeface(Typeface.MONOSPACE)

            // Put some padding on text view text
            //text_view.setPadding(50, 10, 10, 10)
            txtView.gravity = Gravity.CENTER_HORIZONTAL
        }

        fun setSmallHeaderAttributes(txtView: TextView)
        {
            // Set the text view font/text size
            txtView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24F)

            // Set the text view text color
            txtView.setTextColor(Color.WHITE)

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
            txtView.setTextColor(Color.WHITE)

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
                    txtDayOfWeek.text = dateTime.dayOfWeek.name.toLowerCase().capitalize()
                }
            }
            else
            {
                txtDayOfWeek.text = "Today"
            }

            this.setHeaderAttributes(txtDayOfWeek)

            val txtWeatherCode: TextView = TextView(this.mContext)

            // Display some text on the newly created text view
            txtWeatherCode.text = "${statsList!![position].weather_code.value!!.replace("_", " ").capitalize()}"

            this.setSmallHeaderAttributes(txtWeatherCode)

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

            val txtPrecipitation: TextView = TextView(this.mContext)

            // Display some text on the newly created text view
            var precipitationArray = statsList!![position].precipitation
            if(!precipitationArray.isNullOrEmpty()) {
                txtPrecipitation.text = "Precipitation: ${if(precipitationArray[0].GetMinOrMax()!!.value.toString().contains("E")){0.0} else {precipitationArray[0].GetMinOrMax()!!.value}} ${precipitationArray[0].GetMinOrMax()!!.units}"
            }

            this.setBodyAttributes(txtPrecipitation)

            val txtSunrise: TextView = TextView(this.mContext)

            // Display some text on the newly created text view
            var dateTimeAM = statsList!![position].sunrise.GetLocalDateTime()
            if(dateTimeAM != null) {
                txtSunrise.text = "Sunrise: ${if (dateTimeAM.hour > 12) { dateTimeAM.hour - 12 } else { dateTimeAM.hour }}:${dateTimeAM.minute.toString().padStart(2, '0')} am"
            }

            this.setBodyAttributes(txtSunrise)

            val txtSunset: TextView = TextView(this.mContext)

            // Display some text on the newly created text view
            var dateTimePM = statsList!![position].sunset.GetLocalDateTime()
            if(dateTimePM != null) {
                txtSunset.text = "Sunset: ${if(dateTimePM.hour > 12) {dateTimePM.hour - 12} else {dateTimePM.hour}}:${dateTimePM.minute.toString().padStart(2, '0')} pm"
            }

            this.setBodyAttributes(txtSunset)

            var layout: LinearLayout = LinearLayout(this.mContext)
            var bitmap: Bitmap = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888)
            var paint = Paint()
            paint.color = mContext!!.getColor(R.color.colorAccent)
            var canvas = Canvas(bitmap).drawRoundRect(0.0f, 0.0f, 1000.0f, 1000.0f, 100.0f, 100.0f, paint )
            layout.background = BitmapDrawable(Resources.getSystem(), bitmap)
            layout.orientation = LinearLayout.VERTICAL
            layout.addView(txtDayOfWeek)
            if(!txtWeatherCode.text.isNullOrEmpty()) {
                layout.addView(txtWeatherCode)
            }
            if(!txtTemp.text.isNullOrEmpty()) {
                layout.addView(txtTemp)
            }
            if(!txtFeelsLike.text.isNullOrEmpty()) {
                layout.addView(txtFeelsLike)
            }
            if(!txtHumidity.text.isNullOrEmpty()) {
                layout.addView(txtHumidity)
            }
            if(!txtWindSpeed.text.isNullOrEmpty()) {
                layout.addView(txtWindSpeed)
            }
            if(!txtPrecipitation.text.isNullOrEmpty()) {
                layout.addView(txtPrecipitation)
            }
            if(!txtSunrise.text.isNullOrEmpty())
            {
                layout.addView(txtSunrise)
            }
            if(!txtSunset.text.isNullOrEmpty())
            {
                layout.addView(txtSunset)
            }

            container.addView(layout, position)

            return layout
        }

        fun setHeaderAttributes(txtView: TextView)
        {
            // Set the text view font/text size
            txtView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30F)

            // Set the text view text color
            txtView.setTextColor(Color.WHITE)

            // Make the text viw text bold italic
            txtView.setTypeface(txtView.typeface, Typeface.BOLD)

            txtView.gravity = Gravity.CENTER_HORIZONTAL
        }

        fun setSmallHeaderAttributes(txtView: TextView)
        {
            // Set the text view font/text size
            txtView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24F)

            // Set the text view text color
            txtView.setTextColor(Color.WHITE)

            // Make the text viw text bold italic
            txtView.setTypeface(txtView.typeface, Typeface.BOLD)

            txtView.gravity = Gravity.CENTER_HORIZONTAL
        }

        fun setBodyAttributes(txtView: TextView)
        {
            // Set the text view font/text size
            txtView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24F)

            // Set the text view text color
            txtView.setTextColor(Color.WHITE)

            // Make the text viw text bold italic
            txtView.setTypeface(txtView.typeface, Typeface.NORMAL)

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

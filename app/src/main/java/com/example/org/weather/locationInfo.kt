package com.example.org.weather

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat.requestPermissions
import java.util.*

class LocationInfo (context : Context, mainAct : MainActivity, unitBoolean : Boolean){
    // https://stackoverflow.com/questions/1513485/how-do-i-get-the-current-gps-location-programmatically-in-android

    private var latitude = 360.0
    private var longitude = 360.0
    var RealTimeStats : TimeStats? = null
    var HistoricalStats : List<TimeStats>? = null
    var NowcastStats : List<TimeStats>? = null
    var HourlyStats : List<TimeStats>? = null
    var DailyStats : List<TimeStats>? = null
    private var tempUnitFahrenheit = unitBoolean
    private var mContext = context
    private var mAct = mainAct
    private var locationDescription : Address? = null
    private val REQUEST_PERMISSION_LOCATION = 255
    private var locationManager = (mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager)

    private var locationListener = object : LocationListener {

        override fun onLocationChanged(location: Location?) {
            if (location != null) {
                latitude = location.latitude
                longitude = location.longitude

                var geo = Geocoder(mAct)
                locationDescription = geo.getFromLocation(latitude, longitude, 1)[0]
                Log.d("ADDRESS", locationDescription.toString())

                mAct.spinner!!.visibility = View.GONE

                mAct.supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.fragContainer,
                        weatherDisplayFragment.newInstance(this@LocationInfo)
                    )
                    .commit()
            }
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?){

        }

        override fun onProviderEnabled(provider: String?) {

        }

        override fun onProviderDisabled(provider: String?) {

        }

    }

    fun updateLocationInfo(){
        var gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        Log.d("INFO", "GPS is " + gpsEnabled.toString())

        if (!gpsEnabled){
            Log.i("WARNING", "GPS NOT AVAILABLE REQUESTING GPS ENABLE...")
            buildAlertNoGps()
            gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        }

        if (gpsEnabled){
            if (!checkPermissions(locationManager)){
                getPermission()
            }
            else {
                getLocationInfo()
            }
        }
    }

    fun getLocationInfo(){
        try {
            var location = locationManager.getLastKnownLocation(Context.LOCATION_SERVICE)

            if (location != null && location.time > Calendar.getInstance().timeInMillis - 2 * 60 * 100){
                latitude = location.latitude
                longitude = location.longitude

            }
            else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 500.0f, locationListener)
            }

        }
        catch (e: SecurityException){
            Log.d("ERROR", "Security Exception even after receiving location permission")
        }
    }

    fun buildAlertNoGps(){
        var builder = AlertDialog.Builder(mContext, R.style.Theme_AppCompat_Dialog_Alert)

        val positiveButtonClick = { dialog: DialogInterface, which: Int ->
            mContext.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))

        }

        val negativeButtonClick = { dialog: DialogInterface, which: Int ->
            dialog.cancel()
        }

        builder.setMessage("Your GPS is disabled, do you want to enable it?")
            .setCancelable(false)
            .setPositiveButton("Yes", DialogInterface.OnClickListener(function = positiveButtonClick))
            .setNegativeButton("No", DialogInterface.OnClickListener(function = negativeButtonClick))


    }

    fun checkPermissions(manager : LocationManager): Boolean{ // For when GPS is enabled
        // https://stackoverflow.com/questions/36280564/how-to-solve-gps-location-provider-security-exception-in-android
        if (mContext.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            return true
        }

        return false
    }

    private fun getPermission(){
        requestPermissions(
            mAct,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_PERMISSION_LOCATION
        )

    }

    fun logInfo(){
        Log.i("INFO", "Latitude: " + latitude.toString())
        Log.i("INFO", "Longitude: " + longitude.toString())

    }

    fun getLatitude() : Double{

        return latitude
    }

    fun getLongitude() : Double {

        return longitude
    }

    fun getUnitBoolean() : Boolean {

        return tempUnitFahrenheit
    }

    fun getRequestPermissionCode() : Int{

        return REQUEST_PERMISSION_LOCATION
    }
    fun setLocationDescription(addressInfo : Address){

        this.locationDescription = addressInfo
        latitude = locationDescription!!.latitude
        longitude = locationDescription!!.longitude
    }

    fun getLocationDescription() : Address{

        return locationDescription!!
    }

    fun setUseFahrenheit(bool : Boolean){
        this@LocationInfo.tempUnitFahrenheit = bool
        this.resetLocationInfo()
    }

    private fun resetLocationInfo()
    {
        this.RealTimeStats = null
        this.HistoricalStats = null
        this.NowcastStats = null
        this.HourlyStats = null
        this.DailyStats = null

        mAct.supportFragmentManager.beginTransaction()
            .replace(
                R.id.fragContainer,
                weatherDisplayFragment.newInstance(this@LocationInfo)
            )
            .commit()
    }
}
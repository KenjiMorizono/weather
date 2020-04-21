package com.example.org.weather

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    // https://stackoverflow.com/questions/1513485/how-do-i-get-the-current-gps-location-programmatically-in-android
    private var gpsEnabled = false
    private val REQUEST_PERMISSION_LOCATION = 255
    private var latitude = 0.0
    private var longitude = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        var locationManager = (getSystemService(Context.LOCATION_SERVICE) as LocationManager)

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Log.i("WARNING", "GPS NOT AVAILABLE REQUESTING GPS ENABLE...")
            buildAlertNoGps()
            gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        }
        else {
            gpsEnabled = true
        }

        if (gpsEnabled){
            if (!checkPermissions(locationManager)){
                requestPermissions()

            }
            else {
                requestPermissions() // Still need this to ensure that the location updates for some reason
                try {
                    var location = locationManager.getLastKnownLocation(Context.LOCATION_SERVICE)
                    var locationListener = object : LocationListener{
                        override fun onLocationChanged(location: Location?) {
                            if (location != null){
                                latitude = location.latitude
                                longitude = location.longitude
                                locationManager.removeUpdates(this)
                            }

                        }

                        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?){

                        }

                        override fun onProviderEnabled(provider: String?) {

                        }

                        override fun onProviderDisabled(provider: String?) {

                        }

                    }
                    if (location != null && location.time > Calendar.getInstance().timeInMillis - 2 * 60 * 100){
                        latitude = location.latitude
                        longitude = location.longitude

                    }
                    else {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0.0f, locationListener)
                    }

                }
                catch (e: SecurityException){
                    Log.d("ERROR", "Security Exception even after receiving location permission")
                }
            }
        }

        fab.setOnClickListener { view ->
            helloWorldTextView.text = latitude.toString()
        }
    }

    fun buildAlertNoGps(){
        var builder = AlertDialog.Builder(this, R.style.Theme_AppCompat_Dialog_Alert)

        val positiveButtonClick = { dialog: DialogInterface, which: Int ->
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))

        }

        val negativeButtonClick = {dialog: DialogInterface, which: Int ->
            dialog.cancel()
        }

        builder.setMessage("Your GPS is disabled, do you want to enable it?")
            .setCancelable(false)
            .setPositiveButton("Yes", DialogInterface.OnClickListener(function = positiveButtonClick))
            .setNegativeButton("No", DialogInterface.OnClickListener(function = negativeButtonClick))


    }

    fun checkPermissions(manager : LocationManager): Boolean{ // For when GPS is enabled
        // https://stackoverflow.com/questions/36280564/how-to-solve-gps-location-provider-security-exception-in-android
        var manager = (getSystemService(Context.LOCATION_SERVICE) as LocationManager)

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            return true
        }

        return false
    }

    private fun requestPermissions(){
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_PERMISSION_LOCATION
        )

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray){
        if (requestCode == REQUEST_PERMISSION_LOCATION){
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)){
                // Permission to use location is granted, do the same thing as if it was already set
                try {
                    var locationManager = (getSystemService(Context.LOCATION_SERVICE) as LocationManager)
                    var location = locationManager.getLastKnownLocation(Context.LOCATION_SERVICE)
                    var locationListener = object : LocationListener{
                        override fun onLocationChanged(location: Location?) {
                            if (location != null){
                                latitude = location.latitude
                                longitude = location.longitude
                                locationManager.removeUpdates(this)
                            }

                        }

                        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?){

                        }

                        override fun onProviderEnabled(provider: String?) {

                        }

                        override fun onProviderDisabled(provider: String?) {

                        }

                    }
                    if (location != null && location.time > Calendar.getInstance().timeInMillis - 2 * 60 * 100){
                        latitude = location.latitude
                        longitude = location.longitude

                    }
                    else {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0.0f, locationListener)
                    }

                }
                catch (e: SecurityException){
                    Log.d("ERROR", "Security Exception even after receiving location permission")
                }

            }
        }
    }

    fun getEnteredLocation(zipCode: Int){ // For getting location based on ZIP code entered in entry field


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
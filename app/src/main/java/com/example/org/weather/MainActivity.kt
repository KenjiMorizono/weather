package com.example.org.weather

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    // https://stackoverflow.com/questions/1513485/how-do-i-get-the-current-gps-location-programmatically-in-android
    private var celsius = false
    private var infoContainer : LocationInfo? = null

    private var locationManager : LocationManager? = null
    private val REQUEST_LOCATION: Int = 1

    //define the listener
    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            Log.d("location:", location.latitude.toString() + " " + location.longitude.toString())
            retrievedLocation = true
        }
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        fix-global-variable-update-issues
        infoContainer = LocationInfo(this, this)
        infoContainer!!.updateLocationInfo()
        infoContainer!!.logInfo()

        fab.setOnClickListener { view ->
            infoContainer!!.logInfo()

        }
    }
  
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray){
        if (requestCode == infoContainer!!.getRequestPermissionCode()){
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)){
                // Permission to use location is granted, do the same thing as if it was already set
                infoContainer!!.getLocationInfo()

            }
        }
    }*/

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
            R.id.celsiusOption -> {
                celsius = true
                true
            }
            R.id.fahrenheitOption -> {
                celsius = false
                true
            }
            else ->
                super.onOptionsItemSelected(item)
        }
    }
}
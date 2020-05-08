package com.example.org.weather

import android.Manifest
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import androidx.core.app.ActivityCompat
import androidx.core.graphics.scale
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.time.LocalDateTime


class MainActivity : AppCompatActivity() {
    // https://stackoverflow.com/questions/1513485/how-do-i-get-the-current-gps-location-programmatically-in-android
    private var infoContainer : LocationInfo? = null
    private var preferences : SharedPreferences? = null
    private val prefName = "PREFS"
    var spinner: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        spinner = findViewById(R.id.progressBar1) as ProgressBar
        preferences = PreferenceManager.getDefaultSharedPreferences(this)
        infoContainer = LocationInfo(this, this, loadUnitPreferenceFromFile())
        infoContainer!!.updateLocationInfo()

        /*fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()

            ApiInterface.GetRealTimeStats(40.0, -105.0, true) { stats ->
                if(stats != null){
                    Log.d("Main Call", stats.observation_time.GetLocalDateTime().toString())
                }
            }

            ApiInterface.GetHistoricalStats(40.0, -105.0, true, 60, LocalDateTime.now().minusHours(7)) { statsList ->
                if(statsList != null){
                    Log.d("Main Call", statsList[0]!!.observation_time.GetLocalDateTime().toString())
                }
            }

            ApiInterface.GetNowcastStats(40.0, -105.0, true, 60, LocalDateTime.now().plusHours(6)) { statsList ->
                if(statsList != null){
                    Log.d("Main Call", statsList[0]!!.observation_time.GetLocalDateTime().toString())
                }
            }

            ApiInterface.GetHourlyStats(40.0, -105.0, true, 100) { statsList ->
                if(statsList != null){
                    Log.d("Main Call", statsList[0]!!.observation_time.GetLocalDateTime().toString())
                }
            }

            ApiInterface.GetDailyStats(40.0, -105.0, true, 14) { statsList ->
                if(statsList != null){
                    Log.d("Main Call", statsList[0]!!.temp[0].GetMinOrMax()!!.value.toString())
                }
            }

            ApiInterface.GetLayerPNG(40.0, -105.0,1, "precipitation", "global") { bitmap ->
                if(bitmap != null){
                    //this.image_view_bitmap.setImageBitmap(bitmap)
                }
            }
        }*/
      infoContainer!!.logInfo()
    }

override fun onStop() {
        super.onStop()
        saveUnitPreferenceToFile()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray){
        if (requestCode == infoContainer!!.getRequestPermissionCode()){
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)){
                // Permission to use location is granted, do the same thing as if it was already set
                infoContainer!!.getLocationInfo()

            }
            else
            {
                spinner!!.visibility = View.GONE
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragContainer, zipCodeEntryFragment.newInstance(infoContainer!!))
                    .commit()
            }
        }
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
            R.id.celsiusOption -> {
                infoContainer!!.setUseFahrenheit(false)
                true
            }
            R.id.fahrenheitOption -> {
                infoContainer!!.setUseFahrenheit(true)
                true
            }
            R.id.zipCodeEntryOption -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragContainer, zipCodeEntryFragment.newInstance(infoContainer!!)).commit()
                true
            }
            else ->
                super.onOptionsItemSelected(item)
        }
    }

    fun saveUnitPreferenceToFile(){
        var unitPrefUs = infoContainer!!.getUnitBoolean()
        val editor = preferences!!.edit()
        editor.putBoolean(prefName, unitPrefUs)
        editor.apply()

    }

    fun loadUnitPreferenceFromFile() : Boolean{
        var unitPrefUs = preferences!!.getBoolean(prefName, true)
        Log.d("unitPrefUS", unitPrefUs.toString())

        return unitPrefUs
    }
}

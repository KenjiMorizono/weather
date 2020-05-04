package com.example.org.weather
import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.core.app.ActivityCompat
import androidx.core.graphics.scale
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.time.LocalDateTime

class MainActivity : AppCompatActivity() {
    // https://stackoverflow.com/questions/1513485/how-do-i-get-the-current-gps-location-programmatically-in-android
    private var celsius = false
    private var infoContainer : LocationInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        infoContainer = LocationInfo(this, this)
        infoContainer!!.updateLocationInfo()
        infoContainer!!.logInfo()

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()

            ApiInterface.GetRealTimeStats(40.0f, -105.0f, true) { stats ->
                if(stats != null){
                    Log.d("Main Call", stats.observation_time.GetLocalDateTime().toString())
                }
            }

            ApiInterface.GetHistoricalStats(40.0f, -105.0f, true, 60, LocalDateTime.now().minusHours(7)) { statsList ->
                if(statsList != null){
                    Log.d("Main Call", statsList[0]!!.observation_time.GetLocalDateTime().toString())
                }
            }

            ApiInterface.GetNowcastStats(40.0f, -105.0f, true, 60, LocalDateTime.now().plusHours(6)) { statsList ->
                if(statsList != null){
                    Log.d("Main Call", statsList[0]!!.observation_time.GetLocalDateTime().toString())
                }
            }

            ApiInterface.GetHourlyStats(40.0f, -105.0f, true, 100) { statsList ->
                if(statsList != null){
                    Log.d("Main Call", statsList[0]!!.observation_time.GetLocalDateTime().toString())
                }
            }

            ApiInterface.GetDailyStats(40.0f, -105.0f, true, 14) { statsList ->
                if(statsList != null){
                    Log.d("Main Call", statsList[0]!!.temp[0].GetMinOrMax()!!.value.toString())
                }
            }

            ApiInterface.GetLayerPNG(40.0f, -105.0f,1, "precipitation", "global") { bitmap ->
                if(bitmap != null){
                    this.image_view_bitmap.setImageBitmap(bitmap)
                }
            }
        }
      infoContainer!!.logInfo()
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray){
        if (requestCode == infoContainer!!.getRequestPermissionCode()){
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)){
                // Permission to use location is granted, do the same thing as if it was already set
                infoContainer!!.getLocationInfo()

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
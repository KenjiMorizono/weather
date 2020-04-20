package com.example.org.weather

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.util.Log
import androidx.appcompat.app.AlertDialog

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        var locationmanager = (getSystemService(Context.LOCATION_SERVICE) as LocationManager)
        if (!locationmanager.isProviderEnabled(LocationManager.GPS_PROVIDER)){

            Log.i("WARNING", "GPS NOT AVAILABLE REQUESTING GPS ENABLE...")
            buildAlertNoGps()

        }

        fab.setOnClickListener { view ->

        }
    }

    fun buildAlertNoGps(){
        var builder = AlertDialog.Builder(this, R.style.Theme_AppCompat_Dialog_Alert)
        builder.setMessage("Your GPS is disabled, do you want to enable it?")
            .setCancelable(false)
            .setPositiveButton("Yes", DialogInterface.OnClickListener(function = positiveButtonClick))
            .setNegativeButton("No", DialogInterface.OnClickListener(function = negativeButtonClick))


    }

    val positiveButtonClick = { dialog: DialogInterface, which: Int ->
        startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
    }

    val negativeButtonClick = {dialog: DialogInterface, which: Int ->
        dialog.cancel()
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

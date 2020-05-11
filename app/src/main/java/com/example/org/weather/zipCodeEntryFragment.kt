package com.example.org.weather


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.location.Geocoder
import android.util.Log
import android.view.Menu
import kotlinx.android.synthetic.main.fragment_zip_code_entry.*
import java.lang.Exception

class zipCodeEntryFragment : Fragment() {
    private var info : LocationInfo? = null

    companion object {
        fun newInstance(locationInfo : LocationInfo) : zipCodeEntryFragment {
            val displayFrag = zipCodeEntryFragment()
            displayFrag.info = locationInfo
            return displayFrag
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_zip_code_entry, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.clear()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var geo = Geocoder(activity)
        zipCodeSubmitButton.setOnClickListener {
            try {
                var address = geo.getFromLocationName(zipCodeEditText.text.toString(), 1)[0]
                Log.d("Address: ", address.toString())
                info!!.setLocationDescription(address)
                info!!.setLatitude(address.latitude)
                info!!.setLongitude(address.longitude)
                info!!.resetLocationInfo()
                var act = activity as MainActivity
                act.supportFragmentManager.beginTransaction().replace(R.id.fragContainer, weatherDisplayFragment.newInstance(info!!)).commit()
            }
            catch (e : Exception) { zipCodeEditText.error = "Please enter a valid Zip Code." }


        }

        cancelButton.setOnClickListener {
            var act = activity as MainActivity

            act.supportFragmentManager.beginTransaction().replace(R.id.fragContainer, weatherDisplayFragment.newInstance(info!!)).commit()
        }
    }
}

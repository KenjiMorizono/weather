package com.example.org.weather

import android.util.Log
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.StringRequest

class ApiInterfaceRaw {
    companion object {
        val TAG = "ApiInterfaceRaw"
        val apikey = "0rbNpCSqq2OAZ7b16EUoZ3Vv3se1OILI"
        val climacellURL = "https://api.climacell.co/v3"
        val realTimeURL = "/weather/realtime?"

        fun GetRealTimeStats(lat: Double, lon: Double, unit: String, fields: List<String>, completionHandler: (response: String?) -> Unit) {

            val path = "${realTimeURL}lat=${lat}&lon=${lon}&unit_system=${unit}&fields=${fields.joinToString(",")}"
            var str: String? = null
            ApiInterfaceRaw.get(path) { response ->
                Log.d("ApiInterfaceRaw", response + "")
                completionHandler(response)
            }
        }

        // from https://www.varvet.com/blog/kotlin-with-volley/

        fun get(path: String, completionHandler: (response: String?) -> Unit) {
            val stringReq = object : StringRequest(Method.GET, climacellURL + path,
                Response.Listener<String> { response ->
                    Log.d(TAG, "/post request OK! Response: $response")
                    completionHandler(response)
                },
                Response.ErrorListener { error ->
                    VolleyLog.e(TAG, "/post request fail! Error: ${error.message}")
                    completionHandler(null)
                }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    headers.put("accept", "application/json")
                    headers.put("apikey", apikey)
                    return headers
                }
            }

            BackendVolley.instance?.addToRequestQueue(stringReq, TAG)
        }
    }
}
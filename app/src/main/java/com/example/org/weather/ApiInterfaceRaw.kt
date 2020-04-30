package com.example.org.weather

import android.graphics.Bitmap
import android.util.Log
import android.widget.ImageView
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.StringRequest
import java.time.LocalDateTime

class ApiInterfaceRaw {
    companion object {
        val TAG = "ApiInterfaceRaw"
        val apikey = "0rbNpCSqq2OAZ7b16EUoZ3Vv3se1OILI"
        val climacellURL = "https://api.climacell.co/v3"
        val realTimeURL = "/weather/realtime?"
        val historicalURL = "/weather/historical/climacell?"
        val nowcastURL = "/weather/nowcast?"
        val hourlyForcastURL = "/weather/forecast/hourly?"
        val dailyForcastURL = "/weather/forecast/daily?"
        val layersURL = "/weather/layers"

        fun GetRealTimeStats(lat: Float, lon: Float, unit: String, fields: List<String>, completionHandler: (response: String?) -> Unit) {

            val path = "${realTimeURL}lat=${lat}&lon=${lon}&unit_system=${unit}&fields=${fields.joinToString(",")}"

            ApiInterfaceRaw.get(path) { response ->
                Log.d("ApiInterfaceRaw", response)
                completionHandler(response)
            }
        }

        fun GetHistoricalStats(lat: Float, lon: Float, unit: String, minuteTimeStep: Int, startTime: String, endTime: String, fields: List<String>, completionHandler: (response: String?) -> Unit) {

            val path = "${historicalURL}lat=${lat}&lon=${lon}&unit_system=${unit}&timestep=${minuteTimeStep}&start_time=${startTime}&end_time=${endTime}&fields=${fields.joinToString(",")}"

            ApiInterfaceRaw.get(path) { response ->
                Log.d("ApiInterfaceRaw", response)
                completionHandler(response)
            }
        }

        fun GetNowcastStats(lat: Float, lon: Float, unit: String, minuteTimeStep: Int, startTime: String, endTime: String, fields: List<String>, completionHandler: (response: String?) -> Unit) {

            val path = "${nowcastURL}lat=${lat}&lon=${lon}&unit_system=${unit}&timestep=${minuteTimeStep}&start_time=${startTime}&end_time=${endTime}&fields=${fields.joinToString(",")}"

            ApiInterfaceRaw.get(path) { response ->
                Log.d("ApiInterfaceRaw", response)
                completionHandler(response)
            }
        }

        fun GetHourlyStats(lat: Float, lon: Float, unit: String, startTime: String, endTime: String, fields: List<String>, completionHandler: (response: String?) -> Unit) {

            val path = "${hourlyForcastURL}lat=${lat}&lon=${lon}&unit_system=${unit}&start_time=${startTime}&end_time=${endTime}&fields=${fields.joinToString(",")}"

            ApiInterfaceRaw.get(path) { response ->
                Log.d("ApiInterfaceRaw", response)
                completionHandler(response)
            }
        }

        fun GetDailyStats(lat: Float, lon: Float, unit: String, startTime: String, endTime: String, fields: List<String>, completionHandler: (response: String?) -> Unit) {

            val path = "${dailyForcastURL}lat=${lat}&lon=${lon}&unit_system=${unit}&start_time=${startTime}&end_time=${endTime}&fields=${fields.joinToString(",")}"

            ApiInterfaceRaw.get(path) { response ->
                Log.d("ApiInterfaceRaw", response)
                completionHandler(response)
            }
        }

        fun GetLayerPNG(type: String, zoom: Int, x: Int, y: Int, region: String, completionHandler: (response: Bitmap?) -> Unit) {

            val path = "${layersURL}/${type}/now/${zoom}/${x}/${y}.png?region=${region}"

            ApiInterfaceRaw.getPNG(path) { response ->
                Log.d("ApiInterfaceRaw", response!!.byteCount.toString())
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

        // from https://stackoverflow.com/questions/41104831/how-to-download-an-image-by-using-volley/41112901
        fun getPNG(path: String, completionHandler: (response: Bitmap?) -> Unit) {
            val stringReq = object : ImageRequest(climacellURL + path,
                Response.Listener<Bitmap?> { response ->
                    Log.d(TAG, "/post request OK! Response: $response")
                    completionHandler(response)
                }, 1000, 1000, ImageView.ScaleType.FIT_XY, Bitmap.Config.ALPHA_8,
                Response.ErrorListener { error ->
                    VolleyLog.e(TAG, "/post request fail! Error: ${error.message}")
                    completionHandler(null)
                }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    headers.put("accept", "image/png")
                    headers.put("apikey", apikey)
                    return headers
                }
            }

            BackendVolley.instance?.addToRequestQueue(stringReq, TAG)
        }
    }
}
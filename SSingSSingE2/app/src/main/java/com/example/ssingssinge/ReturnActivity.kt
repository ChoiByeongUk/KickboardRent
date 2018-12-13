package com.example.ssingssinge

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.ssingssinge.Data.Location
import com.example.ssingssinge.ShowKickboardListActivity.webserver
import kotlinx.android.synthetic.main.activity_return.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class ReturnActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_return)

        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val now = Date()

        returnTimeText.text = "반납일시 : ${sdf.format(now)}"

        homeButton.setOnClickListener {
            finish()
        }


        var runnable: Runnable = Runnable {
            val requestUrl = "http://192.168.0.17:8080/api/scooters/return"
            try {
                val url = URL(requestUrl)

                val conn = url.openConnection() as HttpURLConnection
                if (conn != null) {
                    val pref = getSharedPreferences("globalPref", Context.MODE_PRIVATE)
                    val accessToken = pref?.getString("accessToken", "null")

                    conn.connectTimeout = 10000
                    conn.requestMethod = "PUT"
                    conn.setRequestProperty("Content-Type", "application/json")
                    conn.setRequestProperty(
                        "Authorization",
                        accessToken
                    )
                    conn.doInput = true
                    conn.doOutput = true

                    var requestBody: String = """
                        {
                            "reservation_id": ${intent.getIntExtra("reservation_id", -1)},
                            "location": {
                            "latitude": ${123.123},
                            "longitude": ${123.321},
                            "location_name": "경북대 북문"
                            }
                        }

                    """.trimIndent()

                    Log.d("request body : ", requestBody)
                    val output = DataOutputStream(conn.outputStream)
                    output.write(requestBody.toByteArray())
                    output.flush()
                    output.close()

                    var resCode = conn.responseCode
                    Log.d("return response", resCode.toString())
                }
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: JSONException) {
                e.printStackTrace()
            }

        }
        var thread:Thread = Thread(runnable)
        thread.start()
    }
}

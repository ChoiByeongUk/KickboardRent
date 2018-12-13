package com.example.ssingssinge

import android.content.Context
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.ssingssinge.Data.Kickboard
import com.example.ssingssinge.Data.Location
import kotlinx.android.synthetic.main.activity_reserve.*
import org.json.JSONException
import org.json.JSONObject
import java.io.DataOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

/*
    TODO : 선택한 킥보드를 예약 할 수 있는 기능 구현 필요
 */
class ReserveActivity : AppCompatActivity() {

    val webServer = "http://10.0.2.2:8080/api/reservations"
    var reservationStatus = false
    var resCode:Int = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reserve)

        var kickboard_id: Int = intent.getIntExtra("kickboard_id", 0)
        var latitude: Double = intent.getDoubleExtra("return_location_latitude", 0.0)
        var longitude: Double = intent.getDoubleExtra("return_location_longitude", 0.0)
        var location_name: String = intent.getStringExtra("return_location_location_name")
        var start_time:String = intent.getStringExtra("reservation_time_start_time")
        var end_time:String = intent.getStringExtra("reservation_time_end_time")

        var requestBody: String = """{
            |"kickboard_id": ${kickboard_id},
            |"return_location": {
            |   "latitude": ${latitude},
            |   "longitude": ${longitude},
            |   "location_name": "${location_name}"
            |},
            |"reservation_time": {
            |   "start_time": "${start_time}",
            |   "end_time": "${end_time}"
            |}
        }""".trimMargin()

        Log.d("request Body", requestBody)

        AsyncTask.execute {
            val requestUrl = webServer
            try {
                val url = URL(requestUrl)

                val conn = url.openConnection() as HttpURLConnection
                if (conn != null) {
                    conn.connectTimeout = 10000
                    conn.requestMethod = "POST"
                    conn.setRequestProperty(
                        "Authorization",
                        getSharedPreferences("globalPref", Context.MODE_PRIVATE).getString("accessToken", null)
                    )
                    conn.setRequestProperty("Content-Type", "application/json")
                    conn.doInput = true
                    conn.doOutput = true

                    val output = DataOutputStream(conn.outputStream)
                    output.write(requestBody.toString().toByteArray())
                    output.flush()
                    output.close()

                    resCode = conn.responseCode
                    Log.d("Login Res Code : ", "" + resCode)
                    if (resCode == 201)
                        reservationStatus = true

                    reservationStatus = true
                }
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

        while (reservationStatus == false) {
        }

        if(reservationStatus == true) {
            if(resCode == 201) {
                setResult(MainActivity.OK)
                finish()
            } else {
                setResult(MainActivity.FAIL)
                finish()
            }
        }
    }
}

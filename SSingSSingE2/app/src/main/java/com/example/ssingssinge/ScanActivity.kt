package com.example.ssingssinge

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.ssingssinge.Data.Kickboard
import com.example.ssingssinge.Data.Location
import com.example.ssingssinge.MainActivity.Companion.RETURN
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import org.json.JSONArray
import org.json.JSONException
import org.w3c.dom.Text
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class ScanActivity : AppCompatActivity() {
    lateinit var serialNumberText:TextView
    lateinit var startLocationText:TextView
    lateinit var endLocationText:TextView
    lateinit var startTimeText:TextView
    lateinit var endTimeText:TextView
    lateinit var handler:Handler
    var reservationID: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        handler = Handler()
        startLocationText = findViewById(R.id.startLocationText) as TextView
        endLocationText = findViewById(R.id.endLocationText) as TextView
        startTimeText = findViewById(R.id.startTimeText) as TextView
        endTimeText = findViewById(R.id.endTimeText) as TextView

        var returnButton: Button = findViewById(R.id.returnButton) as Button
        returnButton.setOnClickListener {
            var intent = Intent(this, ReturnActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            intent.putExtra("reservation_id", reservationID)
            startActivityForResult(intent, RETURN)
        }

        serialNumberText = findViewById(R.id.serialNumberText)
        IntentIntegrator(this).initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == RETURN) {
            finish()
        }
        else if(requestCode == IntentIntegrator.REQUEST_CODE) {
            var result:IntentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            if(result == null) {
                Toast.makeText(this, "실패", Toast.LENGTH_LONG).show()
            } else {
                serialNumberText.text = result.contents + "대여중"
                getReservationData(result.contents)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }


    }

    fun getReservationData(scanSerial: String) {
        val webserver = "http://192.168.0.17:8080/api/users/"

        val pref = getSharedPreferences("globalPref", Context.MODE_PRIVATE)
        val username = pref?.getString("username", "null")
        val accessToken = pref?.getString("accessToken", "null")

        var runnable:Runnable = Runnable {
            var requestUrl = "${webserver}$username/reservations/available"

            Log.d("Request uri", requestUrl)
            try {
                val url = URL(requestUrl)

                val conn = url.openConnection() as HttpURLConnection

                if (conn != null) {
                    conn.connectTimeout = 10000
                    conn.requestMethod = "GET"
                    conn.setRequestProperty("Content-Type", "application/json")
                    conn.setRequestProperty(
                        "Authorization",
                        accessToken
                    )
                    Log.d(
                        "access token",
                        accessToken
                    )
                    conn.doInput = true

                    val isr = InputStreamReader(conn.inputStream, "UTF-8")
                    val reader = BufferedReader(isr)
                    var tempStr: String
                    val builder = StringBuilder()

                    tempStr = reader.readText()
                    builder.append(tempStr + "\n")

                    isr.close()
                    reader.close()

                    val result = builder.toString()
                    Log.d("result : ", result)

                    val jsonArray = JSONArray(result)

                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)

                        val scooterObject = jsonObject.getJSONObject("scooter")
                        Log.d("Scooter : ", scooterObject.toString())

                        reservationID = jsonObject.getInt("id")
                        var id:Int = scooterObject.getInt("id")
                        var manufacture:String = scooterObject.getJSONObject("kickboard_manufacture").getString("name")
                        var modelname:String = scooterObject.getString("kickboard_modelname")
                        var serial:String = scooterObject.getString("kickboard_serial")
                        var state:String = scooterObject.getString("kickboard_state")

                        var locationJSON = jsonObject.getJSONObject("location")
                        var startLocationLatitude = locationJSON.getJSONObject("rentalLocation").getDouble("latitude")
                        var startLocationLongitude = locationJSON.getJSONObject("rentalLocation").getDouble("longitude")
                        var startLocationName = locationJSON.getJSONObject("rentalLocation").getString("location_name")

                        var endLocationLatitude = locationJSON.getJSONObject("returnLocation").getDouble("latitude")
                        var endLocationLongitude = locationJSON.getJSONObject("returnLocation").getDouble("longitude")
                        var endLocationName = locationJSON.getJSONObject("returnLocation").getString("location_name")

                        var timeJSON = jsonObject.getJSONObject("reservation_time")
                        var startTime = timeJSON.getString("startTime")
                        var endTime = timeJSON.getString("endTime")

                        if(serial == scanSerial) {
                            handler.post{
                                startLocationText.text = "출발지 : $startLocationName"
                                endLocationText.text = "목적지 : $endLocationName"
                                startTimeText.text = "시작시간 : $startTime"
                                endTimeText.text = "반납시간 : $endTime"
                            }
                            break
                        }
                    }
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

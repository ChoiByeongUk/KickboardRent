package com.example.ssingssinge

import android.content.Context
import android.content.Intent.getIntent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.ssingssinge.Data.Kickboard
import com.example.ssingssinge.Data.KickboardAdapter
import com.example.ssingssinge.Data.Location
import com.example.ssingssinge.R.id.*
import com.example.ssingssinge.ShowKickboardListActivity.MODE_PRIVATE
import com.example.ssingssinge.ShowKickboardListActivity.webserver
import kotlinx.android.synthetic.main.fragment_show_reserved_kickboard.*
import org.jetbrains.anko.support.v4.defaultSharedPreferences
import org.json.JSONArray
import org.json.JSONException
import org.w3c.dom.Text
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


/*
    TODO: DB에서 예약내역 조회해서 리스트뷰에 추가
 */
class ShowReservedKickboardFragment : Fragment() {
    var reservationIds: ArrayList<Int> = ArrayList<Int>()
    var kickboards:ArrayList<Kickboard> = ArrayList<Kickboard>()
    var returnLocations: ArrayList<Location> = ArrayList<Location>()
    var startTimes: ArrayList<String> = ArrayList<String>()
    var endTimes: ArrayList<String> = ArrayList<String>()
    var states: ArrayList<String> = ArrayList<String>()
    var nowIndex:Int = 0
    var maxSize:Int = 0

    lateinit var kickboardModelText:TextView
    lateinit var kickboardSerialText:TextView
    lateinit var startDateText:TextView
    lateinit var endDateText:TextView
    lateinit var startLocationText:TextView
    lateinit var endLocationText:TextView
    lateinit var mainActivity: MainActivity
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mainActivity = activity as MainActivity
    }

    override fun onDetach() {
        super.onDetach()
        kickboards.clear()
        returnLocations.clear()
        startTimes.clear()
        endTimes.clear()
        states.clear()
        nowIndex = 0
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_show_reserved_kickboard, container, false)

        val pref = activity?.getSharedPreferences("globalPref", Context.MODE_PRIVATE)
        val username = pref?.getString("username", "null")
        val accessToken = pref?.getString("accessToken", "null")
        init(username, accessToken)

        kickboardModelText = rootView.findViewById(R.id.kickboardModelText) as TextView
        kickboardSerialText = rootView.findViewById(R.id.kickboardSerialText) as TextView
        startDateText = rootView.findViewById(R.id.startDateText) as TextView
        endDateText = rootView.findViewById(R.id.endDateText) as TextView
        startLocationText = rootView.findViewById(R.id.startLocationText) as TextView
        endLocationText = rootView.findViewById(R.id.endLocationText) as TextView

        showView()

        val prevButton = rootView.findViewById(R.id.prevButton) as Button
        val nextButton = rootView.findViewById(R.id.nextButton) as Button
        val cancelButton = rootView.findViewById(R.id.cancelButton) as Button

        prevButton.setOnClickListener {
            if(nowIndex == 0) nowIndex = maxSize-1
            else nowIndex--

            showView()
        }

        nextButton.setOnClickListener {
            if(nowIndex >= maxSize-1) nowIndex = 0
            else nowIndex++

            showView()
        }

        cancelButton.setOnClickListener {
            cancelReservation(accessToken, reservationIds[nowIndex])

            maxSize--
            kickboards.removeAt(nowIndex)
            returnLocations.removeAt(nowIndex)
            reservationIds.removeAt(nowIndex)
            startTimes.removeAt(nowIndex)
            endTimes.removeAt(nowIndex)
            states.removeAt(nowIndex)
            nowIndex = 0

            showView()
        }

        return rootView
    }

    fun cancelReservation(accessToken: String?, reservationId: Int) {
        var waiting = false
        AsyncTask.execute {
            var requestUrl = "http://10.0.2.2:8080/api/reservations/" + reservationId

            try {

                val url = URL(requestUrl)

                val conn = url.openConnection() as HttpURLConnection

                if (conn != null) {
                    conn.connectTimeout = 10000
                    conn.requestMethod = "DELETE"
                    conn.setRequestProperty("Content-Type", "application/json")
                    conn.setRequestProperty("Authorization", accessToken)
                    conn.doInput = true

                    var resCode:Int = conn.responseCode
                    Log.d("Delete result: ", resCode.toString())
                    waiting = true
                }
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

        while (waiting == false) {
        }
    }


    fun showView() {
        Log.d("index", nowIndex.toString())
        if(maxSize > 0) {
            kickboardModelText.text = "모델명:" + "\n" + kickboards[nowIndex].kickboard_modelname
            kickboardSerialText.text = "고유번호 : ${kickboards[nowIndex].kickboard_serial}"


            startDateText.text = "시작시간 : ${startTimes[nowIndex]}"
            endDateText.text = "반납시간 : ${endTimes[nowIndex]}"

            startLocationText.text = "출발위치 : ${kickboards[nowIndex].kickboard_location.locationName}"
            endLocationText.text = "반납위치 : ${returnLocations[nowIndex].locationName}"
        } else {
            kickboardModelText.text = "모델명:" + "\n"
            kickboardSerialText.text = "고유번호 : "


            startDateText.text = "시작시간 : "
            endDateText.text = "반납시간 : "

            startLocationText.text = "출발위치 : "
            endLocationText.text = "반납위치 : "
        }
    }

    fun init(username: String?, accessToken: String?) {
        val webserver = "http://10.0.2.2:8080/api/users/"
        var waiting = false

        AsyncTask.execute {
            var requestUrl = "${webserver}$username/reservations"

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

                        reservationIds.add(jsonObject.getInt("id"))
                        val scooterObject = jsonObject.getJSONObject("scooter")
                        Log.d("Scooter : ", scooterObject.toString())
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

                        var status = jsonObject.getString("reservation_state")

                        var kickboard = Kickboard(id, manufacture, modelname, serial, state, Location(startLocationLatitude, startLocationLongitude, startLocationName))
                        kickboards.add(kickboard)
                        returnLocations.add(Location(endLocationLatitude, endLocationLongitude, endLocationName))
                        startTimes.add(startTime)
                        endTimes.add(endTime)
                        states.add(status)
                        maxSize++
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            waiting = true
        }

        while (waiting == false) {
        }
    }
}

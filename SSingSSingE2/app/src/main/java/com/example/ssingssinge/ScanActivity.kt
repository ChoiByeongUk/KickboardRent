package com.example.ssingssinge

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.ssingssinge.MainActivity.Companion.RETURN
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import com.journeyapps.barcodescanner.CaptureActivity
import org.jetbrains.anko.*
import org.json.JSONArray
import org.json.JSONException
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.jar.Manifest

class ScanActivity : AppCompatActivity(), OnMapReadyCallback {
    private val REQUEST_ACCESS_FINE_LOCATION = 1000
    lateinit var serialNumberText: TextView
    lateinit var startLocationText:TextView
    lateinit var endLocationText:TextView
    lateinit var startTimeText:TextView
    lateinit var endTimeText:TextView
    lateinit var handler: Handler
    var reservationID: Int = -1
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: MyLocationCallBack
    private val polylineOptions = PolylineOptions().width(5f).color(Color.RED)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
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

        locationInit()
        IntentIntegrator(this)?.setOrientationLocked(false)
        IntentIntegrator(this)?.setBeepEnabled(false)
        IntentIntegrator(this).initiateScan()
    }

    private fun locationInit() {
        fusedLocationProviderClient = FusedLocationProviderClient(this)
        locationCallback = MyLocationCallBack()
        locationRequest = LocationRequest()

        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 5000
    }

    override fun onResume() {
        super.onResume()
        permissionCheck(cancel = {
            showPermissionInfoDialog()
        }, ok = {
            addLocationListener()
        })
    }

    @SuppressLint("MissingPermission")
    private fun addLocationListener() {
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    private fun permissionCheck(cancel: () -> Unit, ok: () -> Unit) {
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                cancel()
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_ACCESS_FINE_LOCATION)
            }
        } else {
            ok()
        }
    }

    private fun showPermissionInfoDialog() {
        alert("현재 위치 정보를 얻으려면 위치 권한이 필요합니다", "위치접근") {
            yesButton {
                ActivityCompat.requestPermissions(this@ScanActivity, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_ACCESS_FINE_LOCATION)
            }
            noButton {  }
        }.show()
    }

    override fun onPause() {
        super.onPause()
        removeLocationListener()
    }

    private fun removeLocationListener() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            REQUEST_ACCESS_FINE_LOCATION    ->  {
                if((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    addLocationListener()
                } else {
                    toast("권한 거부됨")
                }
                return
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

            if (requestCode == RETURN) {
                finish()
            } else if (requestCode == IntentIntegrator.REQUEST_CODE) {
                var result: IntentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
                if (result == null) {
                    Toast.makeText(this, "실패", Toast.LENGTH_LONG).show()
                } else {
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
                                endLocationText.text = "목적지 : 경북대 북문"
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

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    inner class MyLocationCallBack: LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            super.onLocationResult(locationResult)

            val location = locationResult?.lastLocation
            location?.run {
                val latLng = LatLng(latitude, longitude)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f))
                polylineOptions.add(latLng)
                mMap.addPolyline(polylineOptions)
            }
        }
    }
}

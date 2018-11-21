package com.example.ssingssinge

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.constraint.ConstraintLayout
import android.support.design.widget.Snackbar
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.widget.Toast
import com.example.ssingssinge.Data.Kickboard
import com.example.ssingssinge.Manager.LoginManager
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.defaultSharedPreferences
import org.jetbrains.anko.selector
import org.jetbrains.anko.startActivity
import java.nio.channels.Selector

/*
    TODO : 예약기능 구현 필요(ReserveActivity)
    TODO : QR코드를 이용한 예약 -> 대여 상태로 변경 기능 필요
    TODO : PersonalInformationActivity 구현 필요
    TODO : 로그인, 회원가입 기능 DB와 연동 필요
 */
class MainActivity : AppCompatActivity() {

    companion object {
        const val LOGINJOIN = 1
        const val SHOWLIST = 2
        const val RESERVE = 3
        const val OK = 100
        const val FAIL = 200
        const val BACK_PRESSED = 1000
    }

    private val findKickboardFragment:FindKickboardFragment = FindKickboardFragment()
    private val startKickboardFragment:StartKickboardFragment = StartKickboardFragment()
    private val personalInformationFragment:PersonalInformationFragment = PersonalInformationFragment()
    private lateinit var mainLayout:ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainLayout = findViewById(R.id.layout_main) as ConstraintLayout

        val loginManager: LoginManager = LoginManager.getInstance()!!

        //우선 킥보드 조회화면을 먼저 출력
        supportFragmentManager.beginTransaction().replace(R.id.container, FindKickboardFragment()).commit()

        tabs.addOnTabSelectedListener(Selector())

        //로그인 되지 않았다면 로그인이나 회원가입부터 시작
        /*if(!loginManager.isLogin()) {
            val intent = Intent(this, LoginJoinActivity::class.java)
            startActivityForResult(intent, LOGINJOIN)
        }*/
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode) {
            LOGINJOIN   ->  {
                if(resultCode == LoginManager.LOGINOK) {
                    val pref = defaultSharedPreferences
                    Snackbar.make(mainLayout, "로그인 성공\nid : ${pref.getString("email", "null")}", Snackbar.LENGTH_SHORT).show()
                } else if(resultCode == LoginManager.JOINOK) {
                    val pref = PreferenceManager.getDefaultSharedPreferences(this)
                    Snackbar.make(mainLayout, "회원가입 성공\nid : ${pref.getString("username", "null")}", Snackbar.LENGTH_SHORT).show()
                } else {
                    val intent = Intent(this, LoginJoinActivity::class.java)
                    startActivityForResult(intent, LOGINJOIN)
                }
            }

            SHOWLIST    ->  {
                if(resultCode == OK) {
                    var kickboard_id = data?.getIntExtra("kickboard_id", 0)!!
                    var latitude = data?.getDoubleExtra("return_location_latitude", 0.0)!!
                    var longitude = data?.getDoubleExtra("return_location_longitude", 0.0)!!
                    var location_name = data?.getStringExtra("return_location_location_name")!!
                    var start_time = data?.getStringExtra("reservation_time_start_time")!!
                    var end_time = data?.getStringExtra("reservation_time_end_time")!!
                    reserve(kickboard_id, latitude, longitude, location_name, start_time, end_time)// TODO : 예약기능 구현 필요
                } else {
                    Toast.makeText(applicationContext, "Error", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun showKickboardList(location:String, kickboardType: String, startDate:String, endDate: String) {
        val intent:Intent = Intent(this, ShowKickboardListActivity::class.java)
        intent.putExtra("location", location)
        intent.putExtra("kickboardType", kickboardType)
        intent.putExtra("startDate", startDate)
        intent.putExtra("endDate", endDate)
        startActivityForResult(intent, SHOWLIST)
    }

    // TODO : 예약기능 구현 필요
    fun reserve(kickboard_id: Int, latitude: Double, longitude: Double, location_name: String, start_time: String, end_time: String) {
        val intent:Intent = Intent(this, ReserveActivity::class.java)

        intent.putExtra("kickboard_id", kickboard_id)
        intent.putExtra("return_location_latitude", latitude)
        intent.putExtra("return_location_longitude", longitude)
        intent.putExtra("return_location_location_name", location_name)
        intent.putExtra("reservation_time_start_time", start_time)
        intent.putExtra("reservation_time_end_time", end_time)
        startActivityForResult(intent, RESERVE)
    }

    fun searchReservedKickboard() {
        val showReservedKickboardFragment:Fragment = ShowReservedKickboardFragment()
        supportFragmentManager.beginTransaction().replace(R.id.container, showReservedKickboardFragment).commit()
    }

    fun cancelReservation() {
        val cancelReservationFragment:Fragment = CancelReservationFragment()
        supportFragmentManager.beginTransaction().replace(R.id.container, cancelReservationFragment).commit()
    }


    //탭 사용을 위한 클래스
    inner class Selector: TabLayout.OnTabSelectedListener {
        override fun onTabReselected(p0: TabLayout.Tab?) {

        }

        override fun onTabUnselected(p0: TabLayout.Tab?) {

        }

        override fun onTabSelected(tab: TabLayout.Tab?) {
            var position:Int = tab?.position!!
            lateinit var selected:Fragment

            when(position) {
                0   ->  {
                    selected = findKickboardFragment
                }
                1   ->  {
                    selected = startKickboardFragment
                }
                2   ->  {
                    selected = personalInformationFragment
                }
            }

            supportFragmentManager.beginTransaction().replace(R.id.container, selected).commit()
        }
    }
}

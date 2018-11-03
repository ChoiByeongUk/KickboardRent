package com.example.ssingssinge

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.Snackbar
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.widget.Toast
import com.example.ssingssinge.Manager.LoginManager
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.selector
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {

    companion object {
        const val LOGINJOIN = 1
        const val SHOWLIST = 2
        const val RESERVE = 3
        const val OK = 100
        const val FAIL = 200
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

        supportFragmentManager.beginTransaction().replace(R.id.container, FindKickboardFragment()).commit()

        tabs.addOnTabSelectedListener(Selector())


        //로그인 되지 않았다면 로그인이나 회원가입부터 시작
        if(!loginManager.isLogin()) {
            val intent = Intent(this, LoginJoinActivity::class.java)
            startActivityForResult(intent, LOGINJOIN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode) {
            LOGINJOIN   ->  {
                if(resultCode == LoginManager.LOGINOK) {
                    Snackbar.make(mainLayout, "로그인 성공\nid : ${data?.getStringExtra("id")}", Snackbar.LENGTH_SHORT).show()
                } else {
                    val intent = Intent(this, LoginJoinActivity::class.java)
                    startActivityForResult(intent, LOGINJOIN)
                }
            }

            SHOWLIST    ->  {
                if(resultCode == OK) {
                    reserve(data?.getLongExtra("SerialNumber", 0)!!)
                } else {
                    Toast.makeText(applicationContext, "Error", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun showKickboardList(location:String, hourForReserve: Int) {
        val intent:Intent = Intent(this, ShowKickboardListActivity::class.java)
        intent.putExtra("location", location)
        intent.putExtra("hour", hourForReserve)
        startActivityForResult(intent, SHOWLIST)
    }

    fun reserve(serialNumber: Long) {
        val intent:Intent = Intent(this, ReserveActivity::class.java)
        intent.putExtra("SerialNumber", serialNumber)
        startActivityForResult(intent, RESERVE)
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

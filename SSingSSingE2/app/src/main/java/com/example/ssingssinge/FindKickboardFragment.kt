package com.example.ssingssinge

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import kotlinx.android.synthetic.main.fragment_find_kickboard.*
import org.jetbrains.anko.analogClock
import org.jetbrains.anko.digitalClock
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk25.coroutines.onItemSelectedListener
import org.jetbrains.anko.support.v4.find
import org.jetbrains.anko.themedAnalogClock
import java.text.SimpleDateFormat
import java.util.*

/*
    TODO : 킥보드 정보를 DB에서 불러와야 함
    TODO : 설정한 시간에 따라 예약가능한 킥보드들만 목록에 보여줘야 함
*/

//장소, 시간에 따라 예약가능한 킥보드목록을 조회
class FindKickboardFragment : Fragment(), OnItemSelectedListener {

    lateinit var mainActivity:MainActivity
    private var clickedItem = -1

    var locations: Array<String> = arrayOf("전체", "경북대학교 정문", "경북대학교 북문")
    var kickboardType: Array<String> = arrayOf("Xiaomi Mijia", "Ninebot Series", "Icabot Kurrus")

    var findLocation = locations[0]
    var startHour:Int = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    var startMinute:Int = Calendar.getInstance().get(Calendar.MINUTE)
    var endHour:Int = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    var endMinute:Int = Calendar.getInstance().get(Calendar.MINUTE)
    var findKickboardType = kickboardType[0]

    lateinit var rootView:View

    val hours: Array<Int> = arrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23)
    val minutes: Array<Int> = arrayOf(0, 10, 20, 30, 40, 50)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_find_kickboard, container, false)

        //장소 선택을 위한 스피너
        val locationSpinner: Spinner = rootView.findViewById(R.id.spinner)
        var spinnerAdapter: ArrayAdapter<String> = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, locations)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        locationSpinner.adapter = spinnerAdapter
        locationSpinner.onItemSelectedListener = this

        val kickboardTypeSpinner: Spinner = rootView.findViewById(R.id.kickboardTypeSpinner) as Spinner
        var kickboardTypeSpinnerAdaper: ArrayAdapter<String> = ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, kickboardType)
        kickboardTypeSpinnerAdaper.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        kickboardTypeSpinner.adapter = kickboardTypeSpinnerAdaper
        kickboardTypeSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                findKickboardType = kickboardType[position]
            }
        }

        //대여시작시간, 분 선택을 위한 스피너
        var startHourAdapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, hours)
        var startMinuteAdapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, minutes)
        val startHourSpinner = rootView.findViewById(R.id.startHourSpinner) as Spinner
        val startMinuteSpinner = rootView.findViewById(R.id.startMinuteSpinner) as Spinner
        startHourSpinner.adapter = startHourAdapter
        startMinuteSpinner.adapter = startMinuteAdapter

        startHourSpinner?.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                startHour = position
            }
        }

        startMinuteSpinner?.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                startMinute = position*10
            }
        }

        //종료시작시간, 분 선택을 위한 스피너
        var endHourAdapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, hours)
        var endMinuteAdapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, minutes)
        val endHourSpinner = rootView.findViewById(R.id.endHourSpinner) as Spinner
        val endMinuteSpinner = rootView.findViewById(R.id.endMinuteSpinner) as Spinner
        endHourSpinner.adapter = endHourAdapter
        endMinuteSpinner.adapter = endMinuteAdapter

        endHourSpinner?.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                endHour = position
            }
        }

        endMinuteSpinner?.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                endMinute = position*10
            }
        }

        val search: Button = rootView.findViewById(R.id.search)
        search.setOnClickListener {
            if(findLocation != null) {
                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                val startDate = Date()
                startDate.hours = startHour
                startDate.minutes = startMinute

                val endDate = Date()
                endDate.hours = endHour
                endDate.minutes = endMinute

                mainActivity.showKickboardList(findLocation, findKickboardType, sdf.format(startDate), sdf.format(endDate)) // TODO : 장소, 시간에 따라 DB에서 이용가능한 킥보드만 골라서 보여줘야 함
            } else {
                Toast.makeText(context, "back", Toast.LENGTH_SHORT).show()
            }
        }
        return rootView
    }

    override fun onAttach(context: Context?) {
        mainActivity = activity as MainActivity
        super.onAttach(context)
    }
    /*
        스피너 사용을 위한 메소드
     */
    override fun onNothingSelected(parent: AdapterView<*>?) {
    }
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        findLocation = locations[position]
    }
}
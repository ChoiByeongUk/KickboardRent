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
import org.jetbrains.anko.sdk25.coroutines.onItemSelectedListener
import org.jetbrains.anko.support.v4.find
import org.jetbrains.anko.themedAnalogClock

/*
    TODO : 킥보드 정보를 DB에서 불러와야 함
    TODO : 설정한 시간에 따라 예약가능한 킥보드들만 목록에 보여줘야 함
*/

//장소, 시간에 따라 예약가능한 킥보드목록을 조회
class FindKickboardFragment : Fragment(), OnItemSelectedListener {

    lateinit var mainActivity:MainActivity
    private var clickedItem = -1
    lateinit var findLocation:String
    var findHour:Int = -1
    var findMinute: Int = -1
    var locations: Array<String> = arrayOf("전체", "경북대학교 정문", "경북대학교 북문")
    lateinit var rootView:View
    lateinit var hourAdapter: ArrayAdapter<Int>
    lateinit var minuteAdapter: ArrayAdapter<Int>
    val hours: Array<Int> = arrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23)
    val minutes: Array<Int> = arrayOf(0, 10, 20, 30, 40, 50)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_find_kickboard, container, false)

        //장소 선택을 위한 스피너
        val spinner: Spinner = rootView.findViewById(R.id.spinner)
        var spinnerAdapter: ArrayAdapter<String> = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, locations)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter
        spinner.onItemSelectedListener = this

        hourAdapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, hours)
        minuteAdapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, minutes)
        val hourSpinner = rootView.findViewById(R.id.hourSpinner) as Spinner
        val minuteSpinner = rootView.findViewById(R.id.minuteSpinner) as Spinner
        hourSpinner.adapter = hourAdapter
        minuteSpinner.adapter = minuteAdapter
        hourSpinner?.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                findHour = position
            }
        }

        minuteSpinner?.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                findMinute = position
            }
        }


        val search: Button = rootView.findViewById(R.id.search)
        search.setOnClickListener {
            if(findLocation != null) {
                mainActivity.showKickboardList(findLocation, findHour, findMinute) // TODO : 장소, 시간에 따라 DB에서 이용가능한 킥보드만 골라서 보여줘야 함
            } else {
                Toast.makeText(context, "위치를 선택하세요", Toast.LENGTH_SHORT).show()
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

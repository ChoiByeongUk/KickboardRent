package com.example.ssingssinge

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.fragment_find_kickboard.*
import org.jetbrains.anko.sdk25.coroutines.onItemSelectedListener
import org.jetbrains.anko.support.v4.find


class FindKickboardFragment : Fragment(), AdapterView.OnItemSelectedListener {

    lateinit var mainActivity:MainActivity
    private var clickedItem = -1
    lateinit var findLocation:String
    var findHour:Int = 0
    var locations: Array<String> = arrayOf("전체", "경북대학교 정문", "경북대학교 북문")
    lateinit var rootView:View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_find_kickboard, container, false)

        val spinner: Spinner = rootView.findViewById(R.id.spinner)
        var spinnerAdapter: ArrayAdapter<String> = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, locations)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter
        spinner.onItemSelectedListener = this

        val timePicker: TimePicker = rootView.findViewById(R.id.timePicker)
        timePicker.setOnTimeChangedListener { view, hourOfDay, minute ->
            findHour = hourOfDay
        }

        val search: Button = rootView.findViewById(R.id.search)
        search.setOnClickListener {
            if(findLocation != null) {
                mainActivity.showKickboardList(findLocation, findHour)
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

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        findLocation = locations[position]
    }
}

package com.example.ssingssinge

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import kotlinx.android.synthetic.main.fragment_personal_information.*

/*
    TODO : 예약 내역, 예약취소등 기능 구현 필요
 */

class PersonalInformationFragment : Fragment() {
    lateinit var rootView:View
    lateinit var mainActivity: MainActivity
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mainActivity = activity as MainActivity
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_personal_information, container, false)

        val buttonAccident = rootView.findViewById(R.id.buttonAccident) as Button

        buttonAccident.setOnClickListener {
            var intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:02-0000-0000")
            startActivity(intent)
        }

        val buttonReserveList = rootView.findViewById(R.id.buttonReserveList) as Button

        buttonReserveList.setOnClickListener {
            mainActivity.searchReservedKickboard()
        }

        val buttonCancel = rootView.findViewById(R.id.buttonCancelReserve) as Button

        buttonCancel.setOnClickListener {
            mainActivity.cancelReservation()
        }



        return rootView
    }
}

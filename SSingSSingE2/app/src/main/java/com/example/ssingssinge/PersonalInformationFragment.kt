package com.example.ssingssinge

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/*
    TODO : 예약 내역, 예약취소등 기능 구현 필요
 */

class PersonalInformationFragment : Fragment() {
    lateinit var rootView:View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_personal_information, container, false)

        return rootView
    }
}

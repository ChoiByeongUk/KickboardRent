package com.example.ssingssinge

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.jetbrains.anko.support.v4.defaultSharedPreferences

class ShowReservedKickboardFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_show_reserved_kickboard, container, false)

        var textview = rootView.findViewById(R.id.reserved_kickboard_textView) as TextView

        val pref = defaultSharedPreferences
        textview.text = "${pref.getString("email", "null")}의 예약목록"

        return rootView
    }
}

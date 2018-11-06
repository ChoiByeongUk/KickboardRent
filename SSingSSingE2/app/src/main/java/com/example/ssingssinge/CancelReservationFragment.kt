package com.example.ssingssinge

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_cancel_reservation.*
import org.jetbrains.anko.support.v4.defaultSharedPreferences

class CancelReservationFragment : Fragment() {

    private lateinit var mainActivity:MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mainActivity = activity as MainActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var rootView = inflater.inflate(R.layout.fragment_cancel_reservation, container, false)

        val pref = defaultSharedPreferences

        val textView = rootView.findViewById(R.id.forcancel_kickboard_list_textView) as TextView

        textView.text = "${pref.getString("email", "null")}이 취소할 수 있는 목록"

        return rootView
    }
}

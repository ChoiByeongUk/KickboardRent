package com.example.ssingssinge

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.ssingssinge.Data.KickboardAdapter
import org.jetbrains.anko.support.v4.defaultSharedPreferences


/*
    TODO: DB에서 예약내역 조회해서 리스트뷰에 추가
 */
class ShowReservedKickboardFragment : Fragment() {
    lateinit private var kickboardAdapter: KickboardAdapter;

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_show_reserved_kickboard, container, false)

        var textview = rootView.findViewById(R.id.textView) as TextView

        val pref = defaultSharedPreferences
        textview.text = "${pref.getString("email", "null")}의 예약목록"

        kickboardAdapter = KickboardAdapter(context)


        return rootView
    }
}

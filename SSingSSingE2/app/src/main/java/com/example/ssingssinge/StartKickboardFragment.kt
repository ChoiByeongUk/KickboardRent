package com.example.ssingssinge

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/*
    TODO : QR코드를 이용해서 예약상태의 킥보드를 대여할 수 있는 기능 구현 필요
 */

class StartKickboardFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_start_kickboard, container, false)

        return rootView
    }
}

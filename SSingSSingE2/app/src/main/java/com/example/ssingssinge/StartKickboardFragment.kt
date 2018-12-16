package com.example.ssingssinge

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import org.json.JSONException
import org.json.JSONObject
import android.widget.Toast



/*
    TODO : QR코드를 이용해서 예약상태의 킥보드를 대여할 수 있는 기능 구현 필요
 */

class StartKickboardFragment : Fragment() {
    lateinit var qrScan:IntentIntegrator
    lateinit var mainActivity: MainActivity

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mainActivity = activity as MainActivity
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_start_kickboard, container, false)

        var scanButton = rootView.findViewById(R.id.scanButton) as Button
        scanButton.setOnClickListener {
            mainActivity.scan()

        }
        return rootView
    }
}

package com.example.ssingssinge

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Button
import android.widget.Toast
import com.example.ssingssinge.Data.User
import com.example.ssingssinge.Manager.LoginManager
import kotlinx.android.synthetic.main.activity_join.*
import kotlinx.android.synthetic.main.activity_join.view.*
import org.jetbrains.anko.email
import java.util.*

/*
    TODO : 이메일 인증 구현 필요(웹서버)
 */
class JoinActivity : AppCompatActivity() {

    private var emailAuthed = false
    private var randomCode: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        joinButton.setOnClickListener {
            val name = nameText.text.toString()
            val username = usernameText.text.toString()
            val email = emailText.text.toString()
            val password = passwordText.text.toString()

            var user: User = User(name, username, email, password)
            val loginManager = LoginManager.getInstance()

            joinButton.text = "진행중"
            if (loginManager.join(user)) {
                joinButton.text="회원가입"
                setResult(LoginManager.JOINOK)
                val pref = PreferenceManager.getDefaultSharedPreferences(this)
                val editor = pref.edit()

                editor.putString("username", username)
                    .apply()

                finish()
            } else {
                joinButton.text="회원가입"
                setResult(LoginManager.JOINFAILED)
            }
        }
    }
}

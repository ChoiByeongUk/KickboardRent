package com.example.ssingssinge

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import com.example.ssingssinge.Data.User
import com.example.ssingssinge.Manager.LoginManager
import kotlinx.android.synthetic.main.activity_join.*

/*
    TODO : 사용자가 회원가입을 요청하면 DB에 저장해야 함
 */
class JoinActivity : AppCompatActivity() {

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

            if(loginManager.join(user)) {
                setResult(LoginManager.JOINOK)
                val pref = PreferenceManager.getDefaultSharedPreferences(this)
                val editor = pref.edit()

                editor.putString("username", username)
                    .apply()

                finish()
            } else {
                setResult(LoginManager.JOINFAILED)
            }
        }
    }
}

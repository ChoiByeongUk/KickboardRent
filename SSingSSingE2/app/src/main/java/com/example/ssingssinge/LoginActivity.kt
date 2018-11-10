package com.example.ssingssinge

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.widget.Button
import android.widget.TextView
import com.example.ssingssinge.Data.User
import com.example.ssingssinge.Manager.LoginManager
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.defaultSharedPreferences

/*
    TODO : 로그인 요청이 있으면 DB에서 정보를 읽어와 로그인 시도를 해야 함
 */
class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginButton = findViewById(R.id.loginButton) as Button

        loginButton.setOnClickListener {
            val email = emailText.text.toString()
            val password = passwordText.text.toString()

            val user:User = User( "null", "null", email, password)

            loginButton.text = "진행중"
            if(LoginManager.getInstance().login(user)) { // TODO : login메소드 변경 필요
                loginButton.text="로그인"
                val pref = defaultSharedPreferences
                val editor = pref.edit()

                editor.putString("email", email).apply()

                setResult(LoginManager.LOGINOK)
                finish()
            } else {
                loginButton.text="로그인"
                setResult(LoginManager.LOGINFAILED)
                Snackbar.make(activity_login, "로그인 실패", Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}

package com.example.ssingssinge

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.ssingssinge.Manager.LoginManager
import kotlinx.android.synthetic.main.activity_login_join.*
import org.jetbrains.anko.startActivity

// 로그인이나 회원가입을 선택할 수 있는 액티비티
class LoginJoinActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_join)

        loginButton.setOnClickListener {
            val intent: Intent = Intent(this, LoginActivity::class.java)
            startActivityForResult(intent, LoginManager.LOGIN)
        }

        joinButton.setOnClickListener {
            val intent: Intent = Intent(this, JoinActivity::class.java)
            startActivityForResult(intent, LoginManager.JOIN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode) {
            LoginManager.LOGIN  ->  {
                if(resultCode == LoginManager.LOGINOK) {
                    val intent = Intent()
                    intent.putExtra("id", data?.getStringExtra("id"))
                    setResult(LoginManager.LOGINOK, intent)
                    finish()
                } else {
                    setResult(LoginManager.LOGINFAILED) // 로그인 실패인 경우에는 계속해서 로그인 시도 가능하도록 함
                }
            }

            LoginManager.JOIN   ->  {
                if(resultCode == LoginManager.JOINOK) {
                    setResult(LoginManager.JOINOK)
                    finish()
                }
            }
        }
    }
}

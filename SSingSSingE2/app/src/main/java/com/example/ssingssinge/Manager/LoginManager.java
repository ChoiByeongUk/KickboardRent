package com.example.ssingssinge.Manager;

import com.example.ssingssinge.Data.User;

/*
    TODO : 로그인, 회원가입 DB와 연동되도록 구현 필요
 */
public class LoginManager {

    public static final int LOGIN = 1;
    public static final int LOGINOK = 100;
    public static final int LOGINFAILED=200;
    public static final int JOIN = 2;
    public static final int JOINOK = 101;
    public static final int JOINFAILED = 201;

    private static User user;
    private static boolean loginStatus;

    private static LoginManager loginManager = null;

    private LoginManager() { this.loginStatus = false; this.user = null; }
    public static LoginManager getInstance() {
        if(loginManager == null) {
            loginManager = new LoginManager();
            return loginManager;
        } else {
            return loginManager;
        }
    }

    //    TODO : 회원가입 기능 구현 필요(DB 연동)
    public boolean join() {
        return true;
    }


    //     TODO : 로그인 기능 구현 필요(DB 연동)
    public boolean login(User user) {
        this.user = user;
        if(user.getEmail().equals("test") && user.getPassword().equals("test")) {
            loginStatus = true;
        }
        return loginStatus;
    }

    public User getUser() {
        return user;
    }

    public boolean isLogin() {
        return loginStatus;
    }
}

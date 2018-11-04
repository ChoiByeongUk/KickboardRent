package com.example.ssingssinge.Manager;

import android.os.AsyncTask;
import android.util.Log;
import com.example.ssingssinge.Data.User;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

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

    private static boolean loginStatus;
    private static boolean joinStatus;
    private static String webServer = "http://10.0.2.2:5000";

    private static LoginManager loginManager = null;

    private LoginManager() { this.loginStatus = false; }

    public static LoginManager getInstance() {
        if(loginManager == null) {
            loginManager = new LoginManager();
            return loginManager;
        } else {
            return loginManager;
        }
    }

    //    TODO : 회원가입 기능 구현 필요(DB 연동)
    public boolean join(final User user) {
        joinStatus = false;
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                String requestUrl = webServer + "/join";
                try {
                    URL url = new URL(requestUrl);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    if(conn != null) {
                        conn.setConnectTimeout(10000);
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Content-Type", "application/json");
                        conn.setDoInput(true);
                        conn.setDoOutput(true);

                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("name", user.getName());
                        jsonObject.put("username", user.getUsername());
                        jsonObject.put("email", user.getEmail());
                        jsonObject.put("password", user.getPassword());

                        DataOutputStream output = new DataOutputStream(conn.getOutputStream());
                        output.write(jsonObject.toString().getBytes());
                        output.flush();
                        output.close();

                        int resCode = conn.getResponseCode();
                        Log.d("Join Res Code : ", "" + resCode);
                        if(resCode == 200)
                            joinStatus = true;
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return joinStatus;
    }


    //     TODO : 로그인 기능 구현 필요(DB 연동)
    public boolean login(final User user) {
        loginStatus = false;
        String email = user.getEmail();
        String password = user.getPassword();

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                String requestUrl = webServer + "/login";
                try {
                    URL url = new URL(requestUrl);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    if (conn != null) {
                        conn.setConnectTimeout(10000);
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Content-Type", "application/json");
                        conn.setDoInput(true);
                        conn.setDoOutput(true);

                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("email", user.getEmail());
                        jsonObject.put("password", user.getPassword());

                        DataOutputStream output = new DataOutputStream(conn.getOutputStream());
                        output.write(jsonObject.toString().getBytes());
                        output.flush();
                        output.close();

                        int resCode = conn.getResponseCode();
                        Log.d("Login Res Code : ", "" + resCode);
                        if (resCode == 200)
                            loginStatus = true;

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return loginStatus;
    }

    public boolean isLogin() {
        return loginStatus;
    }

    public void logout() {loginStatus = false;}
}
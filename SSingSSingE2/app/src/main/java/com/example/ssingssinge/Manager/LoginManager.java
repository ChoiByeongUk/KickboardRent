package com.example.ssingssinge.Manager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import com.example.ssingssinge.Data.User;
import com.example.ssingssinge.R;
import com.google.gson.JsonObject;
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
    private static String webServer = "http://10.0.2.2:8080/api";
    private static boolean process;
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
        process = false;

        AsyncTask.execute(
                new Runnable() {
            @Override
            public void run() {
                String requestUrl = webServer + "/auth/signup";
                Log.d("request Uri", requestUrl);
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
                        jsonObject.put("password", user.getPassword());

                        Log.d("request", jsonObject.toString());
                        OutputStream output = conn.getOutputStream();
                        output.write(jsonObject.toString().getBytes());
                        output.flush();

                        int resCode = conn.getResponseCode();
                        Log.d("Join result ", resCode+"");

                        output.close();
                        /*InputStreamReader isr = new InputStreamReader(conn.getInputStream(), "UTF-8");
                        BufferedReader reader = new BufferedReader(isr);
                        String tempStr;
                        StringBuilder builder = new StringBuilder();

                        while((tempStr = reader.readLine()) != null) {
                            builder.append(tempStr + "\n");
                        }
                        isr.close();
                        reader.close();

                        String result = builder.toString();
                        Log.d("result : ", result);*/

                        process = true;
                        joinStatus = true;
                        Log.d("Finished", "Join finished");
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

        while(process == false) {

        }
        return joinStatus;
    }


    //     TODO : 로그인 기능 구현 필요(DB 연동)
    public String login(final User user) {
        loginStatus = false;
        process = false;
        String email = user.getEmail();
        String password = user.getPassword();
        final String[] accessToken = new String[1];

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                String requestUrl = webServer + "/auth/signin";
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
                        jsonObject.put("username", user.getUsername());
                        jsonObject.put("password", user.getPassword());

                        DataOutputStream output = new DataOutputStream(conn.getOutputStream());
                        output.write(jsonObject.toString().getBytes());
                        output.flush();
                        output.close();

                        InputStreamReader isr = new InputStreamReader(conn.getInputStream(), "UTF-8");
                        BufferedReader reader = new BufferedReader(isr);
                        String tempStr;
                        StringBuilder builder = new StringBuilder();

                        while((tempStr = reader.readLine()) != null) {
                            builder.append(tempStr + "\n");
                        }

                        isr.close();
                        reader.close();

                        String result = builder.toString();
                        JSONObject resultObject = new JSONObject(result);

                        Log.d("result ", resultObject.toString());

                        accessToken[0] = resultObject.getString("tokenType") + " " + resultObject.getString("accessToken");

                        loginStatus = true;
                        process = true;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        while(process == false) { }

        return accessToken[0];
    }

    public boolean isLogin() {
        return loginStatus;
    }

    public void logout() {loginStatus = false;}
}
package com.example.lanyatest;


import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Person extends AppCompatActivity {


    //String TAG=MainActivity.class.getCanonicalName();
    String msg;

    private TextView usname;
    private TextView usphone;
    //private TextView usmoney;
    //private TextView uspassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        usname=(TextView)findViewById(R.id.tv_name);
        usphone=(TextView)findViewById(R.id.tv_phone);
        //usmoney=(TextView)findViewById(R.id.tv_money);


        new Thread(getRun).start();
        //new Thread(getRun).start();
    }

    Runnable getRun=new Runnable() {

        @Override
        public void run() {
            Log.d(msg, "你好");
            sendRequestWithOkHttp();
        }

    };

    private void sendRequestWithOkHttp(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    SharedPreferences sp=getSharedPreferences("sp_login",0);
                    String usephone=sp.getString("login_phone","");
                    //Log.d(TAG,"phone is"+usephone);

                    OkHttpClient client=new OkHttpClient();
                    RequestBody requestBody=new FormBody.Builder()
                            .add("tel",usephone)
                            .build();
                    Request request=new Request.Builder()
                            .url("http://39.98.42.159:8080/AndroidTest/user/finduser?")
                            .post(requestBody)
                            .build();
                    Response response=client.newCall(request).execute();
                    String responseData=response.body().string();
                    showResponse(responseData);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void showResponse(final String response){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //usname.setText(response);
                try{
                    JSONObject jsonObject=new JSONObject(response);
                    String name=jsonObject.getString("uname");
                    String phone=jsonObject.getString("tel");
                    String money=jsonObject.getString("idmoney");

                    usname.setText(name);
                    usphone.setText(phone);
                    //usmoney.setText(money);

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }
}

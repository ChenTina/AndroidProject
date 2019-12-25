package com.example.lanyatest;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Looper;
import android.print.PrinterId;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    //存储用户信息进SharedPreference
    private EditText etPhone;
    private CheckBox cbRememberPwd;

    private Boolean bPwdSwitch=false;
    private EditText etPwd;

    String TAG=SignActivity.class.getCanonicalName();
    String msg;

    private EditText usephone;
    private EditText usepass;
    private HashMap<String,String>stringHashMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //密码明文切换图标
        final ImageView ivPwdSwitch=findViewById(R.id.iv_pwd_switch);
        etPwd=findViewById(R.id.et_pwd);

        //绑定对象
        etPhone=findViewById(R.id.et_phone);
        cbRememberPwd=findViewById(R.id.rm_pwd);

        String spFileName=getResources()
                .getString(R.string.shared_preferences_file_name);
        String phoneKey=getResources()
                .getString(R.string.login_phone);
        String passwordKey=getResources()
                .getString(R.string.login_password);
        String rmpasswordKey=getResources()
                .getString(R.string.login_remember_password);

        SharedPreferences spFile=getSharedPreferences(
                spFileName,
                Context.MODE_PRIVATE
        );

        String phone=spFile.getString(phoneKey,null);
        String password=spFile.getString(passwordKey,null);
        Boolean rememberPassword=spFile.getBoolean(rmpasswordKey,
                false);
        if (phone!=null&&!TextUtils.isEmpty(phone)){
            etPhone.setText(phone);
        }

        if (password!=null&&!TextUtils.isEmpty(password)){
            etPwd.setText(password);
        }

        cbRememberPwd.setChecked(rememberPassword);

        ivPwdSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bPwdSwitch=!bPwdSwitch;
                if (bPwdSwitch){
                    ivPwdSwitch.setImageResource(R.drawable.ic_visibility_black_24dp);
                    etPwd.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }else{
                    ivPwdSwitch.setImageResource(R.drawable.ic_visibility_off_black_24dp);
                    etPwd.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD |
                            InputType.TYPE_CLASS_TEXT);
                    etPwd.setTypeface(Typeface.DEFAULT);
                }
            }
        });
        cbRememberPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String spFileName=getResources()
                        .getString(R.string.shared_preferences_file_name);
                String phoneKey=getResources()
                        .getString(R.string.login_phone);
                String passwordKey=getResources()
                        .getString(R.string.login_password);
                String rmpasswordKey=getResources()
                        .getString(R.string.login_remember_password);

                SharedPreferences spFile=getSharedPreferences(
                        spFileName,
                        Context.MODE_PRIVATE
                );
                SharedPreferences.Editor editor=spFile.edit();

                if (cbRememberPwd.isChecked()){
                    String password=etPwd.getText().toString();
                    String phone=etPhone.getText().toString();

                    editor.putString(phoneKey,phone);
                    editor.putString(passwordKey,password);
                    editor.putBoolean(rmpasswordKey,true);
                    editor.apply();
                }else{
                    editor.remove(phoneKey);
                    editor.remove(passwordKey);
                    editor.remove(rmpasswordKey);
                    editor.apply();
                }
            }
        });
        //注册按钮
        final Button button=findViewById(R.id.bt_sign);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(com.example.lanyatest.LoginActivity.this,SignActivity.class);
                startActivity(intent);
            }
        });

        usephone=(EditText)findViewById(R.id.et_phone);
        usepass=(EditText)findViewById(R.id.et_pwd);
        stringHashMap=new HashMap<>();
    }
    public void loginGET(View view){

        //stringHashMap.put("uname",usename.getText().toString());
        /*stringHashMap.put("tel",usephone.getText().toString());
        stringHashMap.put("password",usepass.getText().toString());
        new Thread(getRun).start();*/
        Intent intent=new Intent();
        intent.setClass(com.example.lanyatest.LoginActivity.this,MainActivity   .class);
        startActivity(intent);

    }

    Runnable getRun=new Runnable() {
        @Override
        public void run() {
            Log.d(msg,"你好");
            requestGet(stringHashMap);

            /*Intent intent=new Intent();
            intent.setClass(com.example.smartlock.LoginActivity.this,MainActivity.class);
            startActivity(intent);*/
        }
    };


    private void requestGet(HashMap<String,String> paramsMap){
        try{
            Log.e(TAG,"哈哈哈哈哈");
            for(java.util.Map.Entry<String,String> kk : paramsMap.entrySet()){
                Log.e(TAG,kk.getKey()+" "+kk.getValue());
            }
            String baseUrl="http://39.98.42.159:8080/AndroidTest/user/login?";
            StringBuilder tempParams=new StringBuilder();
            int pos=0;
            for(String key:paramsMap.keySet()){
                if (pos>0){
                    tempParams.append("&");
                }
                tempParams.append(String.format("%s=%s",key, URLEncoder.encode(paramsMap.get(key),"utf-8")));
                pos++;
            }

            Log.e(TAG,"hin8yi"+tempParams.toString());
            String requestUrl = baseUrl + tempParams.toString();
            // 新建一个URL对象
            URL url = new URL(requestUrl);
            // 打开一个HttpURLConnection连接
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            // 设置连接主机超时时间
            urlConn.setConnectTimeout(5 * 1000);
            //设置从主机读取数据超时
            urlConn.setReadTimeout(5 * 1000);
            // 设置是否使用缓存  默认是true
            urlConn.setUseCaches(true);
            // 设置为Post请求
            urlConn.setRequestMethod("GET");
            //urlConn设置请求头信息
            //设置请求中的媒体类型信息。
            urlConn.setRequestProperty("Content-Type", "application/json");
            //设置客户端与服务连接类型
            urlConn.addRequestProperty("Connection", "Keep-Alive");
            // 开始连接
            urlConn.connect();
            // 判断请求是否成功
            if (urlConn.getResponseCode() == 200) {
                // 获取返回的数据
                String result = streamToString(urlConn.getInputStream());
                Log.e(TAG, "Get方式请求成功，result--->" + result);
                if (result.equals("-1"))
                {
                    Looper.prepare();
                    Toast toast=Toast.makeText(getApplicationContext(),"此账号不存在，请重新输入",Toast.LENGTH_LONG);
                    toast.show();
                    Looper.loop();
                }
                else if(result.equals("-2"))
                {
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(),
                            "密码错误，请重新输入",Toast.LENGTH_LONG).show();
                    Looper.loop();
                }else {
                    Intent intent=new Intent();
                    intent.setClass(com.example.lanyatest.LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            } else {
                Log.e(TAG, "Get方式请求失败");
            }
            // 关闭连接
            urlConn.disconnect();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }


    /**
     * 将输入流转换成字符串
     *
     * @param is 从网络获取的输入流
     * @return
     */
    public String streamToString(InputStream is) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            baos.close();
            is.close();
            byte[] byteArray = baos.toByteArray();
            return new String(byteArray);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }
}


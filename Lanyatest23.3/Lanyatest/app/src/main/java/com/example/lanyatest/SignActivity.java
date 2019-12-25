package com.example.lanyatest;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

public class SignActivity extends AppCompatActivity {

    String TAG=SignActivity.class.getCanonicalName();
    String msg;
    private EditText usename;
    private EditText usenumber;
    private EditText password;
    private EditText idcard;
    //private RadioButton radiosex;
    private HashMap<String,String>stringHashMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        usename=(EditText)findViewById(R.id.name);
        usenumber=(EditText)findViewById(R.id.number);
        password=(EditText)findViewById(R.id.et_pwd);
        idcard=(EditText)findViewById(R.id.id_card);
        //radiosex=(RadioButton)findViewById(R.id.radioMale);
        stringHashMap=new HashMap<>();

        // intent=new Intent();
        //intent.setClass(com.example.smartlock.SignActivity.this,LoginActivity.class);
        //startActivity(intent);

        RadioGroup radioGroup=(RadioGroup)findViewById(R.id.radio);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radiobutton=(RadioButton)findViewById(checkedId);
                stringHashMap.put("sex",radiobutton.getText().toString());
            }
        });

    }

    public void signGET(View view){

        stringHashMap.put("uname",usename.getText().toString());
        stringHashMap.put("tel",usenumber.getText().toString());
        stringHashMap.put("password",password.getText().toString());
        stringHashMap.put("card",idcard.getText().toString());
        //stringHashMap.put("sex",radiosex.toString());
        //stringHashMap.put("sex",)
        new Thread(getRun).start();

    }

    Runnable getRun=new Runnable() {
        @Override
        public void run() {
            Log.d(msg,"你好");
            requestGet(stringHashMap);

            Intent intent=new Intent();
            intent.setClass(com.example.lanyatest.SignActivity.this,LoginActivity.class);
            startActivity(intent);
        }
    };

    private void requestGet(HashMap<String,String> paramsMap){
        try{
            Log.e(TAG,"哈哈哈哈哈");
            for(java.util.Map.Entry<String,String> kk : paramsMap.entrySet()){
                Log.e(TAG,kk.getKey()+" "+kk.getValue());
            }
            String baseUrl="http://39.98.42.159:8080/AndroidTest/user/insertUser?";
            StringBuilder tempParams=new StringBuilder();
            int pos=0;
            for(String key:paramsMap.keySet()){
                if (pos>0){
                    tempParams.append("&");
                }
                tempParams.append(String.format("%s=%s",key, URLEncoder.encode(paramsMap.get(key),"utf-8")));
                pos++;
            }

            Log.e(TAG,"paramsnrkhnjhtn"+tempParams.toString());
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


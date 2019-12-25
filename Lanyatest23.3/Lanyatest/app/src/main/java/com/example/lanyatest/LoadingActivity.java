package com.example.lanyatest;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class LoadingActivity extends AppCompatActivity {
    private Dialog mDialog;
    private Dialog mWeiboDialog;
    private Button btn_show_weibo_loading;
    private Button btn_show_thrid_loading;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    DialogThridUtils.closeDialog(mDialog);
                    WeiboDialogUtils.closeDialog(mWeiboDialog);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_open_lock);
//        btn_show_weibo_loading = (Button) findViewById(R.id.btn_show_weibo_loading);
//        btn_show_thrid_loading = (Button) findViewById(R.id.btn_show_thrid_loading);
//        btn_show_weibo_loading.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mWeiboDialog = WeiboDialogUtils.createLoadingDialog(LoadingActivity.this, "加载中...");
//                mHandler.sendEmptyMessageDelayed(1, 2000);
//            }
//        });
//
//        btn_show_thrid_loading.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mDialog = DialogThridUtils.showWaitDialog(LoadingActivity.this, "加载中...", false, true);
//                mHandler.sendEmptyMessageDelayed(1, 2000);
//            }
//        });
        Loanding();
    }

    public void Loanding(){
        mDialog = DialogThridUtils.showWaitDialog(LoadingActivity.this, "开锁中...", false, true);
        mHandler.sendEmptyMessageDelayed(1, 5000);

    }


}

package com.example.lanyatest;




import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;

import java.util.UUID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView Home;
    //private TextView Order;
    private TextView User;

    private FrameLayout ly_content;
    private UserFragment f1,f3;

    private OpenLockFragment openlock;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);


        setContentView(R.layout.activity_main);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.show(openlock);
        bindView();

    }

    //UI组件初始化与事件绑定

    private void bindView() {
        Home = (TextView) this.findViewById(R.id.iv_home);

        User = (TextView) this.findViewById(R.id.iv_user);
        ly_content = (FrameLayout) findViewById(R.id.fragment_container);


        Home.setOnClickListener(this);

        User.setOnClickListener(this);

    }

    //重置所有文本的选中状态
    public void selected() {
        Home.setSelected(false);
        User.setSelected(false);
    }

    //隐藏所有Fragment
    public void hideAllFragment(FragmentTransaction transaction) {
        if (f1 != null) {
            transaction.hide(f1);
        }

//        if (openlock != null) {
//            transaction.hide(openlock);
//        }

    }




    @Override
    public void onClick(View v) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideAllFragment(transaction);
        switch (v.getId()) {
            case R.id.iv_home:
                selected();
                Home.setSelected(true);
                if (openlock == null) {
                    openlock = new OpenLockFragment();
                    transaction.add(R.id.fragment_container, openlock);
                } else {
                    transaction.show(openlock);
                }
                break;

            case R.id.iv_user:

                selected();
                User.setSelected(true);
                if (openlock != null) {
                    transaction.hide(openlock);
                }
                if (f1 == null) {
                    f1 = new UserFragment();
                    transaction.add(R.id.fragment_container, f1);
                } else {
                    transaction.show(f1);
                }
                break;


        }


        transaction.commit();
    }
}


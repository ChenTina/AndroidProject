package com.example.lanyatest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class UserFragment extends Fragment implements View.OnClickListener{

    @BindView(R.id.iv_connect)
    TextView connect;
    @BindView(R.id.iv_broken)
    TextView broken;
    @BindView(R.id.iv_setting_user_avatar)
    ImageView person;
    @BindView(R.id.tv_username)
    TextView username;

    Unbinder unbinder;


    //@Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        unbinder = ButterKnife.bind(this, view);
        connect.setOnClickListener(this);
        broken.setOnClickListener(this);
        person.setOnClickListener(this);
        username.setOnClickListener(this);
        return view;
    }

    //点击跳转拨打电话页面

    public void onClick(View v){
        switch (v.getId()) {
            case R.id.iv_broken:
                Intent intent = new Intent(getActivity(),BrokenActivity.class);
                startActivity(intent);
                break;

            case R.id.iv_connect:
                Intent callback = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "18778841677"));
                startActivity(callback);
                break;

            case R.id.iv_setting_user_avatar:
            case R.id.user_name:
                Intent intent2 = new Intent(getActivity(),Person.class);
                startActivity(intent2);


        }

    }


}
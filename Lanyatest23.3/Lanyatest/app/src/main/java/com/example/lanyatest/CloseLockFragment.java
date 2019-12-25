package com.example.lanyatest;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
public class CloseLockFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.bt_lock)
    Button lock;
    @BindView(R.id.iv_lock)
    ImageView Ivmage;
//    @BindView(R.id.bt_lock)
//    Button lock;

    Unbinder unbinder;
    private Boolean IvSwp = false;
    private Boolean openlock=false;
    private Boolean clocklock=false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_close_lock, container, false);

        //init();
        unbinder = ButterKnife.bind(this, view);
        lock.setOnClickListener(this);
        //lock.setOnClickListener(this);
        return view;
    }

//    private void init() {
//        unlock.setVisibility(View.VISIBLE);
//        lock.setVisibility(View.GONE);
//    }


    public void onClick(View v) {
//            Ivmage.setImageResource(R.mipmap.ic_unlock);
            //unlock.setVisibility(View.GONE);
//            Intent intent1 = new Intent(getActivity(),LoadingActivity.class);
//            startActivity(intent1);
            //openlock=true;
            Intent intent = new Intent(getActivity(),BoothActivity2.class);
            startActivity(intent);
    }


}


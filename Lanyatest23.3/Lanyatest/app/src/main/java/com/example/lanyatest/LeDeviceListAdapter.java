package com.example.lanyatest;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class LeDeviceListAdapter extends BaseAdapter {

    private List<BluetoothDevice> deviceList;
    private Context mContext;

    public LeDeviceListAdapter(Context context) {
        mContext = context;
        deviceList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return deviceList.size();
    }

    @Override
    public BluetoothDevice getItem(int i) {
        return deviceList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {






        return view;
    }

    public void addDevice(BluetoothDevice bluetoothDevice) {
        if (deviceList != null) {
            deviceList.add(bluetoothDevice);
        }
    }
}

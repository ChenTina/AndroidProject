package com.example.lanyatest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class BoothActivity extends AppCompatActivity {

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothDevice  mBluetoothDevice;

    private static final int REQUEST_ENABLE_BT = 1111;
    private TextView mBluetoothState;
    private boolean mScanning;
    private Handler mHandler;
    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;
    private LeDeviceListAdapter mLeDeviceListAdapter;
    private ListView mBleDevice;

    //服务UUID
    public static final UUID RX_SERVICE_UUID = UUID.fromString("0000fff0-0000-1000-8000-00805f9b34fb");
    //服务里特征值的UUID
    public static final UUID RX_CHATACTERISTIC_UUID = UUID.fromString("0000fff6-0000-1000-8000-00805f9b34fb");
    // random number
    private static final int REQUEST_ACCESS_COARSE_LOCATION = 1022;

    private int mConnectionState = STATE_DISCONNECTED;

    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;
    private final static String TAG = BoothActivity.class.getSimpleName();
    public final static String ACTION_GATT_CONNECTED =
            "com.lanyatest.ble.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED =
            "com.lanyatest.ble.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED =
            "com.lanyatest.ble.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE =
            "com.lanyatest.ble.ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA =
            "com.lanyatest.ble.EXTRA_DATA";
    private BluetoothGatt mBluetoothGatt;
    private SharedPreferences.Editor Editor;
    private String CHECKSTRING="";

    private Boolean openlock=true;
    private Boolean closelock=false;
    private Button bt_lock;

//    public BoothActivity(boolean openlock, boolean closelock) {
//        this.openlock = openlock;
//        this.closelock = closelock;
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_close_lock);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_ACCESS_COARSE_LOCATION);
        initView();
        checkIsSupportBLE();
        initReceiver();
        initEvent();


        cleareshareData();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
        Date date = new Date(System.currentTimeMillis());
        SharedPreferences.Editor editor = getSharedPreferences("starttime", MODE_PRIVATE).edit();
        editor.putString("starttime", simpleDateFormat.format(date));
        editor.apply();
        //开始时间显示
        TextView start_time = (TextView) findViewById(R.id.start_time);
        SharedPreferences getstarttime = getSharedPreferences("starttime", MODE_PRIVATE);
        start_time.setText("开锁时间："+" "+getstarttime.getString("starttime", ""));
    }



    protected void cleareshareData() {
        // TODO Auto-generated method stub

        SharedPreferences.Editor editor1 = getSharedPreferences("starttime",MODE_PRIVATE).edit();
        //SharedPreferences.Editor editor2 = getSharedPreferences("endtime",MODE_PRIVATE).edit();


        editor1.clear();
        editor1.commit();

        //editor2.clear();
       // editor2.commit();
    }

    //连接蓝牙设备，通过mGattCallback回调
    private void initEvent() {
        open();
    }
    public void open() {
//        connect("C8:FD:19:9C:CA:86");
        connect("78:DB:2F:B2:BD:DC");
    }
    private void  connect(final String address) {


        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (device == null) {
            Log.d(TAG, "Device not found.  Unable to connect.");

        }
        mBluetoothGatt = device.connectGatt(this, true, mGattCallback);

    }
    private void initView() {
        mBluetoothState = findViewById(R.id.bt_unlock);
//        mBleDevice = findViewById(R.id.bleDevice);
        bt_lock = findViewById(R.id.bt_lock);
        bt_lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BoothActivity.this,BoothActivity2.class);
                startActivity(intent);
                finish();
            }
        });
        mLeDeviceListAdapter = new LeDeviceListAdapter(this);
        mHandler = new Handler();
    }

    private void initReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mReceiver, filter);
    }

    private void checkIsSupportBLE() {
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        } else {
            obtainBluetoothAdapter();
            checkIsEnable();
        }
    }

    private void checkIsEnable() {
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            /*Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);*/
            mBluetoothAdapter.enable();

        }
    }


    //    开启BLE
    private void obtainBluetoothAdapter() {
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        //通过uuid来扫描指定设备
        scanLeDeviceByUUID();
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                        BluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        mBluetoothState.setText("Bluetooth off");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        mBluetoothState.setText("Turning Bluetooth off...");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        mBluetoothState.setText("Bluetooth on");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        mBluetoothState.setText("Turning Bluetooth on...");
                        break;
                }
            }
        }
    };



    //通过UUID来扫描设备
    private void scanLeDeviceByUUID() {
        mBluetoothAdapter.startLeScan(new UUID[]{RX_SERVICE_UUID}, mLeScanCallback);
    }

    //    扫描结果回调
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi,
                             byte[] scanRecord) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLeDeviceListAdapter.addDevice(device);
                    mLeDeviceListAdapter.notifyDataSetChanged();
                    mBleDevice.setAdapter(mLeDeviceListAdapter);
                }
            });
        }

    };

    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {


        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            String intentAction;
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                //到此步，连接已经建立

                Log.i("connect:", " -------------------connected have success!---------------------");

                intentAction = ACTION_GATT_CONNECTED;
                mConnectionState = STATE_CONNECTED;
                broadcastUpdate(intentAction);
                Log.i(TAG, "Connected to GATT server.");
                Log.i(TAG, "Attempting to start service discovery:----------mBluetoothGatt.discoverServices()----------" +
                        mBluetoothGatt.discoverServices());



            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                //断开连接
                intentAction = ACTION_GATT_DISCONNECTED;
                mConnectionState = STATE_DISCONNECTED;
                Log.i(TAG, "Disconnected from GATT server.");
                broadcastUpdate(intentAction);
            }
        }

        @Override
        // New services discovered
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);

                mBluetoothGatt.getService(RX_SERVICE_UUID);
                boolean check = writeByteToBleDevice_1();
                if(check){
                    //关闭连接，然后释放gatt服务
//                    disconnect();
                }else
                    Log.i("writeERROR", "--------------------------------ERROR-------------------------: ");


                //调用写入函数，写入成功则返回true
//                if(CHECKSTRING=="00AU01"){
//                    boolean check = writeByteToBleDevice_1();
//                    if(check){
//                        //关闭连接，然后释放gatt服务
//                        disconnect();
//                    }else
//                        Log.i("writeERROR", "--------------------------------ERROR-------------------------: ");
//
//                }else if (CHECKSTRING=="00AU02"){
//                    boolean check = writeByteToBleDevice_2();
//                    if(check){
//                        //关闭连接，然后释放gatt服务
//                        disconnect();
//                    }else
//                        Log.i("writeERROR", "--------------------------------ERROR-------------------------: ");
//
//                }
//
//            } else {
//                Log.w(TAG, "onServicesDiscovered received: " + status);
            }
        }


        //发送指令成功后关闭连接
        public void disconnect(){
            if(mBluetoothAdapter == null || mBluetoothGatt ==null){
                return;
            }
            mBluetoothGatt.disconnect();
            //关闭连接后，释放gatt资源
            closeGattServer();
        }


        //关闭连接后释放Gattserver
        public void closeGattServer(){
            if (mBluetoothGatt==null){
                return;
            }
            mBluetoothGatt.close();
            mBluetoothGatt = null;
        }


        //获取服务,
        public BluetoothGattService getService(UUID uuid) {
            if (mBluetoothAdapter == null || mBluetoothGatt == null) {
                Log.i(TAG, "getService: BluetoothAdapter not initialized");
                return null;
            }
            return mBluetoothGatt.getService(uuid);
        }


        //获取特征
        private BluetoothGattCharacteristic getCharcteristic(String serviceUUID, String characteristicUUID) {

            //得到服务对象
            BluetoothGattService service = getService(UUID.fromString(serviceUUID));  //调用上面获取服务的方法

            if (service == null) {
                Log.i(TAG, "getCharcteristic: -------------Can not find 'BluetoothGattService");
                return null;
            }

            //得到此服务结点下Characteristic对象
            final BluetoothGattCharacteristic gattCharacteristic = service.getCharacteristic(UUID.fromString(characteristicUUID));
            if (gattCharacteristic != null) {
                return gattCharacteristic;
            } else {
                Log.i(TAG, "getCharcteristic: Can not find 'BluetoothGattCharacteristic");
                return null;
            }
        }


        @Override
        // Result of a characteristic read operation
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic,
                                         int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            }
        }
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
            broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
        }

    };


    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }


    private void broadcastUpdate(final String action, final BluetoothGattCharacteristic characteristic) {
        final Intent intent = new Intent(action);

        // This is special handling for the Heart Rate Measurement profile. Data
        // parsing is carried out as per profile specifications.
        if (RX_CHATACTERISTIC_UUID.equals(characteristic.getUuid())) {
            int flag = characteristic.getProperties();
            int format = -1;
            if ((flag & 0x01) != 0) {
                format = BluetoothGattCharacteristic.FORMAT_UINT16;
                Log.d(TAG, "Heart rate format UINT16.");
            } else {
                format = BluetoothGattCharacteristic.FORMAT_UINT8;
                Log.d(TAG, "Heart rate format UINT8.");
            }
            final int heartRate = characteristic.getIntValue(format, 1);
            Log.d(TAG, String.format("Received heart rate: %d", heartRate));
            intent.putExtra(EXTRA_DATA, String.valueOf(heartRate));
        } else {
            // For all other profiles, writes the data formatted in HEX.
            final byte[] data = characteristic.getValue();
            if (data != null && data.length > 0) {
                final StringBuilder stringBuilder = new StringBuilder(data.length);
                for (byte byteChar : data)

                    //以十六进制的形式输出
                    stringBuilder.append(String.format("%02X ", byteChar));
//                intent.putExtra(EXTRA_DATA, new String(data) + "\n" + stringBuilder.toString());
                intent.putExtra(EXTRA_DATA, new String(data) );

            }
        }
        sendBroadcast(intent);
    }


    //向BLE设备写数据，发送指令，实现开关锁
    public boolean writeByteToBleDevice_1() {
        BluetoothGattService mBluetoothGattService = mBluetoothGatt.getService(RX_SERVICE_UUID);
        if (mBluetoothGattService == null) {
            Log.i(TAG, "mBluetoothGattService is null");
            return false;
        }

        BluetoothGattCharacteristic characteristic = mBluetoothGattService.getCharacteristic(RX_CHATACTERISTIC_UUID);
        if (characteristic == null) {

            return false;
        }
        //写入指令 00AU02,开锁
        boolean b = characteristic.setValue("00AU01");


        //本地存储数据
//            SharedPreferences userSeting = getSharedPreferences("setting",Activity.MODE_PRIVATE);
//            SharedPreferences.Editor editor = userSeting.edit();
//            editor.putString("CHAR","00AU01");
//            editor.commit();

        return b && mBluetoothGatt.writeCharacteristic(characteristic);
    }


    public boolean writeByteToBleDevice_2() {
        BluetoothGattService mBluetoothGattService = mBluetoothGatt.getService(RX_SERVICE_UUID);
        if (mBluetoothGattService == null) {
            Log.i(TAG, "mBluetoothGattService is null");
            return false;
        }

        BluetoothGattCharacteristic characteristic = mBluetoothGattService.getCharacteristic(RX_CHATACTERISTIC_UUID);
        if (characteristic == null) {

            return false;
        }
        //写入指令 00AU02,开锁
        boolean b = characteristic.setValue("00AU02");


        //本地存储数据
//        SharedPreferences userSeting = getSharedPreferences("setting",Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = userSeting.edit();
//        editor.putString("CHAR","00AU02");
//        editor.commit();

        return b && mBluetoothGatt.writeCharacteristic(characteristic);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }
}

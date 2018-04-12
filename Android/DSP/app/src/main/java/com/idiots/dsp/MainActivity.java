package com.idiots.dsp;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private BluetoothAdapter mBluetoothAdapter;
    private Button onOffBtn, discoverableBtn, devicesBtn, sendBtn, startBtn;
    private EditText editText;

    private BluetoothConnectionService mBluetoothConnectionService;

    private static final UUID MY_UUID_INSECURE = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private BluetoothDevice mBTDevice;

    private ArrayList<BluetoothDevice> mBTDevices = new ArrayList<>();
    private DeviceListAdapter mDeviceListAdapter;
    private ListView listView;

    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if(mBluetoothAdapter.ACTION_STATE_CHANGED.equals(action)){
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, mBluetoothAdapter.ERROR);

                switch(state){
                    case BluetoothAdapter.STATE_ON:

                        break;
                    case BluetoothAdapter.STATE_OFF:

                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:

                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:

                        break;
                }
            }
        }
    };

    private final BroadcastReceiver mBroadcastReceiver2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if(mBluetoothAdapter.ACTION_STATE_CHANGED.equals(action)){
                final int mode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, mBluetoothAdapter.ERROR);

                switch(mode){
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE:

                        break;
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:

                        break;
                    case BluetoothAdapter.SCAN_MODE_NONE:

                        break;
                    case BluetoothAdapter.STATE_CONNECTING:

                        break;
                    case BluetoothAdapter.STATE_CONNECTED:

                        break;
                }
            }
        }
    };
    private BroadcastReceiver mBroadcastReceiver3 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if(action.equals(BluetoothDevice.ACTION_FOUND)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                mBTDevices.add(device);

                mDeviceListAdapter = new DeviceListAdapter(context, R.layout.device_adapter_view, mBTDevices);
                listView.setAdapter(mDeviceListAdapter);
            }
        }
    };

    private BroadcastReceiver mBroadcastReceiver4 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if(action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)){

                BluetoothDevice mDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                if(mDevice.getBondState() == BluetoothDevice.BOND_BONDED){

                    mBTDevice = mDevice;

                }else if(mDevice.getBondState() == BluetoothDevice.BOND_BONDING){

                }else if(mDevice.getBondState() == BluetoothDevice.BOND_NONE){

                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
        unregisterReceiver(mBroadcastReceiver2);
        unregisterReceiver(mBroadcastReceiver3);
        unregisterReceiver(mBroadcastReceiver4);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        onOffBtn = findViewById(R.id.main_on_off_btn);
        discoverableBtn = findViewById(R.id.main_discoverable);
        devicesBtn = findViewById(R.id.main_devices);
        listView = findViewById(R.id.deviceList);
        sendBtn = findViewById(R.id.main_send_btn);
        editText = findViewById(R.id.edit_text);
        startBtn = findViewById(R.id.main_start_btn);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(mBroadcastReceiver4, filter);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBTConnection(mBTDevice, MY_UUID_INSECURE);
                msg("Connection Started");
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byte[] bytes = editText.getText().toString().getBytes(Charset.defaultCharset());
                mBluetoothConnectionService.write(bytes);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mBluetoothAdapter.cancelDiscovery();

                onOffBT();

                String deviceName = mBTDevices.get(position).getName();
                String devideAddress = mBTDevices.get(position).getAddress();

                if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2){
                    mBTDevices.get(position).createBond();

                    mBTDevice = mBTDevices.get(position);
                    mBluetoothConnectionService = new BluetoothConnectionService(MainActivity.this);
                }

                msg(deviceName+" Selected");

                startBTConnection(mBTDevice, MY_UUID_INSECURE);
                msg("Connection Started");
            }
        });

        onOffBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOffBT();
            }
        });

        discoverableBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
                startActivity(discoverableIntent);

                IntentFilter intentFilter = new IntentFilter(mBluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
                registerReceiver(mBroadcastReceiver2, intentFilter);
            }
        });

        devicesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBTDevices.clear();
                mBTDevices.addAll(mBluetoothAdapter.getBondedDevices());
                if(mBluetoothAdapter.isDiscovering()){
                    mBluetoothAdapter.cancelDiscovery();

                    checkBTPermission();

                    mBluetoothAdapter.startDiscovery();
                    IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                    registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);
                }
                if(!mBluetoothAdapter.isDiscovering()){
                    checkBTPermission();

                    mBluetoothAdapter.startDiscovery();
                    IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                    registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);
                }

                msg("Searching");
            }
        });
    }

    public void startBTConnection(BluetoothDevice device, UUID uuid){
        mBluetoothConnectionService.startClient(device, uuid);
    }

    private void checkBTPermission(){
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            int permissionCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            if(permissionCheck != 0){
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001);
            }
        }
    }

    private void onOffBT(){
        if(mBluetoothAdapter == null){

        }else if(!mBluetoothAdapter.isEnabled()){
            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBTIntent);

            IntentFilter intentFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReceiver, intentFilter);
        }

        /*
        if(mBluetoothAdapter.isEnabled()){
            mBluetoothAdapter.disable();

            IntentFilter intentFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReceiver, intentFilter);
        }
        */
    }

    private void msg(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
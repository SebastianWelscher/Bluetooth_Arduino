package com.example.bluetoothchat;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Set;

class BluetoothHelper extends Thread{

    Activity _activity;

    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    Boolean _isActive;
    ListView _deviceList;

    private CommsThread commsThread;


    private final static String UUID = "00001101-0000-1000-8000-00805F9B34FB";   // Serial UUID

    public static  String EXTRA_ADDRESS = "device_address";

    BluetoothHelper(Activity activity,ListView deviceList){
        this._activity = activity;
        this._deviceList = deviceList;
    }

    public final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress();
            }
        }
    };

    public void pairedDevicesList(){
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        ArrayList list = new ArrayList();

        if(pairedDevices.size() > 0){
            for(BluetoothDevice device : pairedDevices){
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress();
                list.add(deviceName + "\n" + deviceHardwareAddress);
            }
        }
        final ArrayAdapter adapter = new ArrayAdapter(_activity, android.R.layout.simple_list_item_1,list);
        _deviceList.setAdapter(adapter);
        _deviceList.setOnItemClickListener(myClicklistener);

    }

    private AdapterView.OnItemClickListener myClicklistener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String info = ((TextView) view).getText().toString();
            String address = info.substring(info.length() -17);

            Intent i = new Intent(_activity, ledControl.class);
            i.putExtra(EXTRA_ADDRESS,address);
            _activity.startActivity(i);
        }
    };
}
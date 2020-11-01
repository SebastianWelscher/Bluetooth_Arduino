package com.example.bluetoothchat;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    Switch bluetoothSwitch;
    Boolean isActive;
    TextView status;
    ListView deviceList;
    public static  String EXTRA_ADDRESS = "device_address";

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
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

    private void pairedDevicesList(){
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        ArrayList list = new ArrayList();

        if(pairedDevices.size() > 0){
            for(BluetoothDevice device : pairedDevices){
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress();
                list.add(deviceName + "\n" + deviceHardwareAddress);
            }
        }
       final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,list);
        deviceList.setAdapter(adapter);
        deviceList.setOnItemClickListener(myClicklistener);

    }

    private AdapterView.OnItemClickListener myClicklistener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String info = ((TextView) view).getText().toString();
            String address = info.substring(info.length() -17);

            Intent i = new Intent(MainActivity.this, ledControl.class);
            i.putExtra(EXTRA_ADDRESS,address);
            startActivity(i);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isActive = false;

        if(bluetoothAdapter == null){
            // not implemented yet
        }

        deviceList = (ListView)findViewById(R.id.deviceList);

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver,filter);

        status = (TextView)findViewById(R.id.statusText);
        status.setText("aus");
        bluetoothSwitch = (Switch)findViewById(R.id.bluetooth_switch);

        bluetoothSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    isActive = true;
                    status.setText("an");
                    if(!bluetoothAdapter.isEnabled()){
                        Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBTIntent,0);
                    }
                }
                else{
                    isActive = false;
                    status.setText("aus");
                    if(bluetoothAdapter.isEnabled()){
                       bluetoothAdapter.disable();
                    }
                }
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
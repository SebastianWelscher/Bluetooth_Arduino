package com.example.bluetoothchat;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Switch bluetoothSwitch;
    Button searchButton;
    Boolean isActive;
    TextView status;
    ListView deviceList;
    BluetoothHelper bluetoothHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isActive = false;

        deviceList = (ListView)findViewById(R.id.deviceList);

        bluetoothHelper = new BluetoothHelper(this,deviceList);
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(bluetoothHelper.receiver,filter);

        status = (TextView)findViewById(R.id.statusText);
        status.setText("aus");
        bluetoothSwitch = (Switch)findViewById(R.id.bluetooth_switch);

        searchButton = (Button)findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 bluetoothHelper.pairedDevicesList();
            }
        });

        if(bluetoothHelper.bluetoothAdapter.isEnabled()){
            bluetoothSwitch.setChecked(true);
        }else{
            bluetoothSwitch.setChecked(false);
        }

        bluetoothSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    isActive = true;
                    status.setText("an");
                    if(!bluetoothHelper.bluetoothAdapter.isEnabled()){
                        Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBTIntent,1);
                    }
                }
                else{
                    isActive = false;
                    status.setText("aus");
                    if(bluetoothHelper.bluetoothAdapter.isEnabled()){
                        bluetoothHelper.bluetoothAdapter.disable();
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(bluetoothHelper.receiver);
    }
}
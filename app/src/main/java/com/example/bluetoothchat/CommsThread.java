package com.example.bluetoothchat;

import android.app.Activity;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class CommsThread extends Thread {

    final BluetoothSocket bluetoothSocket;
    final InputStream inputStream;
    final OutputStream outputStream;

    public CommsThread(BluetoothSocket socket){
        this.bluetoothSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        try{
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        }catch (IOException e){
            Log.d("CommsThread", e.getLocalizedMessage());
        }
        inputStream = tmpIn;
        outputStream = tmpOut;
    }

    public void run(){

        byte[] buffer = new byte[1024];

        int bytes;

        while (true){
            try {
                bytes = inputStream.read(buffer);


            }catch (IOException e){

            }
        }
    }
}

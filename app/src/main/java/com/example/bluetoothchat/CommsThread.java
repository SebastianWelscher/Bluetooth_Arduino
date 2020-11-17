package com.example.bluetoothchat;

import android.app.Activity;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class CommsThread extends Thread {

    private final BluetoothSocket bluetoothSocket;
    private final InputStream inputStream;
    private final OutputStream outputStream;
    private final Handler mHandler;


    public CommsThread(BluetoothSocket socket, Handler handler){
        this.bluetoothSocket = socket;
        this.mHandler = handler;
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
                bytes = inputStream.available();
                if (bytes != 0) {
                    bytes = inputStream.read(buffer, 0, bytes);
                    mHandler.obtainMessage().sendToTarget();

                }
                }catch(IOException e){
                    e.printStackTrace();
                    break;
                }
        }
    }

    public void write(String input){
        byte[] bytes = input.getBytes();
        try {
            outputStream.write(bytes);
        }catch (IOException e){
        }
    }

    public void cancel(){
        try {
            bluetoothSocket.close();
        }catch (IOException e){

        }
    }
}
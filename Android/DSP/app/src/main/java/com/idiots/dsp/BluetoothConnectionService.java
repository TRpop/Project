package com.idiots.dsp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.UUID;

/**
 * Created by KimHJ on 2018-03-10.
 */

public class BluetoothConnectionService {

    private Context mContext;
    private final BluetoothAdapter mBluetoothAdapter;

    private static final UUID MY_UUID_INSECURE = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    private AcceptThread mAcceptThread;
    private ConnectThread mConnectThread;
    private BluetoothDevice mmDevice;
    private UUID deviceUUID;

    private ConnectedThread mConnectedThread;


    public BluetoothConnectionService(Context context){
        mContext = context;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        start();
    }

    private class AcceptThread extends Thread{
        private BluetoothServerSocket mmServerSocket;

        public AcceptThread(){
            BluetoothServerSocket tmp = null;

            try {
                tmp = mBluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord("DSP", MY_UUID_INSECURE);
            } catch (IOException e) {
                e.printStackTrace();
            }

            mmServerSocket = tmp;
        }

        @Override
        public void run() {
            super.run();

            BluetoothSocket socket = null;

            try {
                socket = mmServerSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(socket != null){
                connected(socket, mmDevice);
            }
        }

        public void cancel(){
            try {
                mmServerSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class ConnectThread extends Thread{
        private BluetoothSocket mmSocket;

        public ConnectThread(BluetoothDevice device, UUID uuid){
            mmDevice = device;
            deviceUUID = uuid;
        }

        @Override
        public void run() {
            super.run();

            BluetoothSocket tmp = null;

            try {
                tmp = mmDevice.createRfcommSocketToServiceRecord(deviceUUID);
            } catch (IOException e) {
                e.printStackTrace();
            }

            mmSocket = tmp;

            mBluetoothAdapter.cancelDiscovery();

            try {
                mmSocket.connect();
            } catch (IOException e) {
                e.printStackTrace();

                try {
                    mmSocket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

            connected(mmSocket, mmDevice);
        }

        public void cancel(){
            try {
                mmSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void start(){

        if(mConnectThread != null){
            mConnectThread.cancel();
            mConnectThread = null;
        }

        if (mAcceptThread == null) {
            mAcceptThread = new AcceptThread();
            mAcceptThread.start();
        }
    }

    public void startClient(BluetoothDevice device, UUID uuid){
        mConnectThread = new ConnectThread(device, uuid);
        mConnectThread.start();
    }

    private class ConnectedThread extends Thread{
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket){

            mmSocket = socket;
            InputStream tmpInStream = null;
            OutputStream tmpOutStream = null;


            try {
                tmpInStream = mmSocket.getInputStream();
                tmpOutStream = mmSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            mmInStream = tmpInStream;
            mmOutStream = tmpOutStream;
        }

        @Override
        public void run() {
            super.run();

            byte[] buffer = new byte[1024];

            int bytes;

            while(true){
                try {
                    bytes = mmInStream.read(buffer);
                    String incomingMessage = new String(buffer, 0, bytes);
                    Log.d("read", incomingMessage);
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }

            }
        }

        public void write(byte[] bytes){
            String text = new String(bytes, Charset.defaultCharset());

            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void cancel(){
            try {
                mmSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void connected(BluetoothSocket mmSocket, BluetoothDevice mmDevice) {
        mConnectedThread = new ConnectedThread(mmSocket);
        mConnectedThread.start();
    }

    public void write(byte[] out){
        mConnectedThread.write(out);
    }
}

package com.idiots.gorail;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by MYHOME on 2018-03-26.
 */

public class ConnectedTask extends AsyncTask<Void, String, Boolean> {

    private static final String TAG = "ConnectedTask";

    private InputStream mInputStream = null;
    private OutputStream mOutputStream = null;
    private BluetoothSocket mBluetoothSocket = null;

    private ArrayAdapter<String> mConversationArrayAdapter;

    private String mConnectedDeviceName = null;

    private Context mContext;

    public volatile boolean receiving;

    ConnectedTask(BluetoothSocket socket, String mConnectedDeviceName, ArrayAdapter<String> mConversationArrayAdapter, Context context){

        this.mConnectedDeviceName = mConnectedDeviceName;
        this.mConversationArrayAdapter = mConversationArrayAdapter;
        mBluetoothSocket = socket;
        this.receiving = false;
        try {
            mInputStream = mBluetoothSocket.getInputStream();
            mOutputStream = mBluetoothSocket.getOutputStream();
        } catch (IOException e) {
            Log.e(TAG, "socket not created", e );
        }

        Log.d( TAG, "connected to "+mConnectedDeviceName);
        MainActivity.mConnectionStatus.setText(mConnectedDeviceName);
        MainActivity.q = new Fifo<>();
        this.mContext = context;
    }


    @Override
    protected Boolean doInBackground(Void... params) {

        byte [] readBuffer = new byte[1024];
        int readBufferPosition = 0;


        while (true) {

            if ( isCancelled() ) return false;

            try {

                int bytesAvailable = mInputStream.available();

                if(bytesAvailable > 0) {

                    byte[] packetBytes = new byte[bytesAvailable];

                    mInputStream.read(packetBytes);

                    for(int i=0;i<bytesAvailable;i++) {

                        byte b = packetBytes[i];
                        if(b == '\n')
                        {
                            byte[] encodedBytes = new byte[readBufferPosition];
                            System.arraycopy(readBuffer, 0, encodedBytes, 0,
                                    encodedBytes.length);
                            String recvMessage = new String(encodedBytes, "UTF-8");

                            readBufferPosition = 0;

                            Log.d(TAG, "recv message: " + recvMessage);
                            publishProgress(recvMessage);
                        }
                        else
                        {
                            readBuffer[readBufferPosition++] = b;
                        }
                    }
                }
            } catch (IOException e) {

                Log.e(TAG, "disconnected", e);
                return false;
            }
        }
    }

    @Override
    protected void onProgressUpdate(String... recvMessage) {

        if(recvMessage[0].contains("@#")){
            receiving = true;
            MainActivity.q.clear();
            MainActivity.q.append(recvMessage[0]);
            Intent intent = new Intent("BT_RECEIVE_CHANGED");
            intent.putExtra("RECEIVING", receiving);
            mContext.sendBroadcast(intent);

            if(recvMessage[0].contains("&*")) {
                receiving = false;
                Intent intent1 = new Intent("BT_RECEIVE_CHANGED");
                intent1.putExtra("RECEIVING", receiving);
                mContext.sendBroadcast(intent1);
            }

        }
        else if(recvMessage[0].contains("&*")) {
            receiving = false;
            MainActivity.q.append(recvMessage[0]);
            Intent intent = new Intent("BT_RECEIVE_CHANGED");
            intent.putExtra("RECEIVING", receiving);
            mContext.sendBroadcast(intent);
        }
        else{
            MainActivity.q.append(recvMessage[0]);
        }

        //TODO
        //mConversationArrayAdapter.insert(mConnectedDeviceName + ": " + recvMessage[0], 0);
    }

    @Override
    protected void onPostExecute(Boolean isSucess) {
        super.onPostExecute(isSucess);

        if ( !isSucess ) {


            closeSocket();
            Log.d(TAG, "Device connection was lost");
            MainActivity.isConnectionError = true;
            //showErrorDialog("Device connection was lost");
        }
    }

    @Override
    protected void onCancelled(Boolean aBoolean) {
        super.onCancelled(aBoolean);

        closeSocket();
    }

    void closeSocket(){

        try {

            mBluetoothSocket.close();
            Log.d(TAG, "close socket()");

        } catch (IOException e2) {

            Log.e(TAG, "unable to close() " +
                    " socket during connection failure", e2);
        }
    }

    void write(String msg){

        msg += "\n";

        try {
            mOutputStream.write(msg.getBytes());
            mOutputStream.flush();
        } catch (IOException e) {
            Log.e(TAG, "Exception during send", e );
        }
    }
}
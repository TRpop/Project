package com.idiots.gorail;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by MYHOME on 2018-03-26.
 */

public class ConnectTask extends AsyncTask<Void, Void, Boolean> {

    private static final String TAG = "ConnectTask";

    private BluetoothSocket mBluetoothSocket = null;
    private BluetoothDevice mBluetoothDevice = null;
    private BluetoothAdapter mBluetoothAdapter = null;

    //private ConnectedTask mConnectedTask = null;

    private String mConnectedDeviceName = null;

    private Context mContext;

    private ArrayAdapter<String> mConversationArrayAdapter;

    ConnectTask(BluetoothDevice bluetoothDevice, BluetoothAdapter mBluetoothAdapter, ConnectedTask mConnectedTask, ArrayAdapter<String> mConversationArrayAdapter, Context context) {
        this.mBluetoothAdapter = mBluetoothAdapter;
        this.mConversationArrayAdapter = mConversationArrayAdapter;
        //this.mConnectedTask = mConnectedTask;

        mBluetoothDevice = bluetoothDevice;
        mConnectedDeviceName = bluetoothDevice.getName();

        //SPP
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

        try {
            mBluetoothSocket = mBluetoothDevice.createRfcommSocketToServiceRecord(uuid);
            Log.d( TAG, "create socket for "+mConnectedDeviceName);

        } catch (IOException e) {
            Log.e( TAG, "socket create failed " + e.getMessage());
        }

        MainActivity.mConnectionStatus.setText("connecting...");
        this.mContext = context;
    }


    @Override
    protected Boolean doInBackground(Void... params) {

        // Always cancel discovery because it will slow down a connection
        mBluetoothAdapter.cancelDiscovery();

        // Make a connection to the BluetoothSocket
        try {
            // This is a blocking call and will only return on a
            // successful connection or an exception
            mBluetoothSocket.connect();
        } catch (IOException e) {
            // Close the socket
            try {
                mBluetoothSocket.close();
            } catch (IOException e2) {
                Log.e(TAG, "unable to close() " +
                        " socket during connection failure", e2);
            }

            return false;
        }

        return true;
    }


    @Override
    protected void onPostExecute(Boolean isSucess) {

        if ( isSucess ) {
            connected(mBluetoothSocket);
        }
        else{

            MainActivity.isConnectionError = true;
            Log.d( TAG,  "Unable to connect device");
            //showErrorDialog("Unable to connect device");
        }
    }

    private void connected( BluetoothSocket socket) {
        MainActivity.mConnectedTask = new ConnectedTask(socket, mConnectedDeviceName, mConversationArrayAdapter, mContext);
        MainActivity.mConnectedTask.execute();
    }

    public String getmConnectedDeviceName() {
        return mConnectedDeviceName;
    }
}
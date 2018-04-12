package idiots.com.bluetoothtest2;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by KimHJ on 2018-03-10.
 */

public class DeviceListAdapter extends ArrayAdapter<BluetoothDevice> {

    private LayoutInflater mLayoutInflater;
    private ArrayList<BluetoothDevice> mDevices;
    private int resource;


    public DeviceListAdapter(@NonNull Context context, int resource, ArrayList<BluetoothDevice> devices) {
        super(context, resource, devices);

        this.mDevices = devices;
        this.resource = resource;
        this.mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        convertView = mLayoutInflater.inflate(resource, null);

        BluetoothDevice device = mDevices.get(position);

        if(device != null){
            TextView deviceName = convertView.findViewById(R.id.tvDeviceName);
            TextView deviceAddress = convertView.findViewById(R.id.tvDeviceAddress);

            if(deviceName != null){
                deviceName.setText(device.getName());
            }
            if(deviceAddress != null){
                deviceAddress.setText(device.getAddress());
            }
        }

        return convertView;
    }
}

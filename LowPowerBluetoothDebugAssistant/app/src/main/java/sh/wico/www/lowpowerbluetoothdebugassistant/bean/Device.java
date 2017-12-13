package sh.wico.www.lowpowerbluetoothdebugassistant.bean;

import android.bluetooth.BluetoothDevice;

/**
 * Created by HYW on 2017/12/7.
 */

public class Device {

    private BluetoothDevice bluetoothDevice;
    private int rssi;

    public Device(){}

    public Device(BluetoothDevice bluetoothDevice, int rssi) {
        this.bluetoothDevice = bluetoothDevice;
        this.rssi = rssi;
    }

    public BluetoothDevice getBluetoothDevice() {
        return bluetoothDevice;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Device) {
            return bluetoothDevice.equals(((Device) obj).getBluetoothDevice());
        }
        return false;
    }

    public int getRssi() {
        return rssi;
    }
}

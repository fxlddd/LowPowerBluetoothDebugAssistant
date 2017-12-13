package sh.wico.www.lowpowerbluetoothdebugassistant;

import android.app.Application;
import android.bluetooth.BluetoothGattCharacteristic;



import java.util.ArrayList;
import java.util.List;

import sh.wico.www.lowpowerbluetoothdebugassistant.bean.Service;

/**
 * Created by HYW on 2017/12/11.
 */

public class MyApplication extends Application {

    private final List<Service> services = new ArrayList<>();
    private final List<BluetoothGattCharacteristic> characteristics = new ArrayList<>();

    public static Service.SERVICE_TYPE serviceType;

    private boolean clearflag;
    private BluetoothGattCharacteristic characteristic;

    public boolean isClearflag() {
        return clearflag;
    }

    public void setClearflag(boolean clearflag) {
        this.clearflag = clearflag;
    }

    public void setServices(List<Service> services) {
        this.services.clear();
        this.services.addAll(services);
    }

    public List<Service> getServices() {
        return services;
    }

    public List<BluetoothGattCharacteristic> getCharacteristics() {
        return characteristics;
    }

    public void setCharacteristics(List<BluetoothGattCharacteristic> characteristics) {
        this.characteristics.clear();
        this.characteristics.addAll(characteristics);
    }

    public BluetoothGattCharacteristic getCharacteristic() {
        return characteristic;
    }

    public void setCharacteristic(BluetoothGattCharacteristic characteristic) {
        this.characteristic = characteristic;
    }
}

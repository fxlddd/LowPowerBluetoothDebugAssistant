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

    private boolean clearflag;

    public boolean isClearflag() {
        return clearflag;
    }

    public void setClearflag(boolean clearflag) {
        this.clearflag = clearflag;
    }

    public enum  SERVICE_TYPE {
        TYPE_USER_DEBUG, TYPE_NUMBER,TYPE_STR,TYPE_OTHER;
    }

    private final List<Service> services = new ArrayList<>();
    private final List<BluetoothGattCharacteristic> characteristics = new ArrayList<>();

    private BluetoothGattCharacteristic characteristic;

    public static SERVICE_TYPE serviceType;

    public void setServices(List<Service> services) {
        this.services.clear();
        this.services.addAll(services);
    }

    public List<Service> getServices() {
        return services;
    }
}

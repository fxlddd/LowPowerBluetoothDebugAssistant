package sh.wico.www.lowpowerbluetoothdebugassistant.bean;

import android.bluetooth.BluetoothGattService;

/**
 * Created by Administrator on 2015-11-20.
 */
public class Service {
    private String name;
    private BluetoothGattService service;

    public enum  SERVICE_TYPE {
        TYPE_USER_DEBUG, TYPE_NUMBER,TYPE_STR,TYPE_OTHER;
    }

    public Service() {
    }

    public Service(String name, BluetoothGattService service) {
        this.name = name;
        this.service = service;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BluetoothGattService getBGService() {
        return service;
    }

    public void setBGService(BluetoothGattService service) {
        this.service = service;
    }
}

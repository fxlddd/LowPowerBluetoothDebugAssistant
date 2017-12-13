package sh.wico.www.lowpowerbluetoothdebugassistant;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import sh.wico.www.lowpowerbluetoothdebugassistant.adapter.DeviceAdapter;
import sh.wico.www.lowpowerbluetoothdebugassistant.bean.Device;
import sh.wico.www.lowpowerbluetoothdebugassistant.bean.Service;
import sh.wico.www.lowpowerbluetoothdebugassistant.service.BluetoothLeService;
import sh.wico.www.lowpowerbluetoothdebugassistant.utils.GattAttributes;
import sh.wico.www.lowpowerbluetoothdebugassistant.utils.Utils;

public class MainActivity extends AppCompatActivity {

    private static BluetoothAdapter bluetoothAdapter;
    private final List<Device>  deviceList = new ArrayList<>();

    private FloatingActionButton startScanBtn;
    private FloatingActionButton stopScanBtn;

    private RecyclerView deviceRecycler;
    private RecyclerView.LayoutManager layoutManager;
    private DeviceAdapter deviceAdapter;

    private String curDevName;
    private String curDevMac;

    private boolean isScanning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkBleSupportAndInitialize();
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initBroadcastReceiver();
        initService();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 关闭连接的设备
        disconnectDevice();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(gattUpdateReceiver);
    }

    private void initService() {
        Intent gattServiceIntent = new Intent(getApplicationContext(), BluetoothLeService.class);
        startService(gattServiceIntent);
    }

    public void initView() {
        startScanBtn = findViewById(R.id.start_scan_button);
        stopScanBtn = findViewById(R.id.stop_scan_button);

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        deviceAdapter = new DeviceAdapter(deviceList);
        deviceAdapter.setItemClickListener(new DeviceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (isScanning) stopScan();
                connectDevice(deviceList.get(position).getBluetoothDevice());
            }
        });
        deviceRecycler = findViewById(R.id.bluetooth_device_recyler);
        deviceRecycler.setLayoutManager(layoutManager);
        deviceRecycler.setAdapter(deviceAdapter);
    }

    public void onClick(View view) {
        if (view.getId() == R.id.start_scan_button) {
            startScan();
        }
        else if (view.getId() == R.id.stop_scan_button) {
            stopScan();
        }
    }

    /**
     * 检查蓝牙是否可用，并且初始化蓝牙
     */
    private void checkBleSupportAndInitialize() {
        // 检查设备是否支持蓝牙
        if (!getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "设备不支持蓝牙", Toast.LENGTH_LONG).show();
            return;
        }
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
        if (bluetoothManager == null) {
            // 设备不支持蓝牙
            Toast.makeText(this, "设备不支持蓝牙", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.enable();
        }
    }

    private void startScan() {
        isScanning = true;
        startScanBtn.setVisibility(View.GONE);
        stopScanBtn.setVisibility(View.VISIBLE);
        deviceList.clear();
        deviceAdapter.notifyDataSetChanged();
        bluetoothAdapter.startLeScan(leScanCallback);
    }

    private void stopScan() {
        isScanning =false;
        stopScanBtn.setVisibility(View.GONE);
        startScanBtn.setVisibility(View.VISIBLE);
        bluetoothAdapter.stopLeScan(leScanCallback);
    }

    private BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice bluetoothDevice, final int rssi, byte[] scanRecord) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Device Device = new Device(bluetoothDevice, rssi);
                    if(deviceList.contains(Device))
                        return;
                    deviceList.add(Device);
                    System.out.println(deviceList.size());
                    deviceAdapter.notifyDataSetChanged();
                }
            });
        }
    };


    //
    private void connectDevice(BluetoothDevice bluetoothDevice) {
        curDevMac = bluetoothDevice.getAddress();
        curDevName = bluetoothDevice.getName();
        if (BluetoothLeService.getConnectionState() != BluetoothLeService.STATE_DISCONNECTED) {
            BluetoothLeService.disconnect();
        }
        System.out.println("begin connect" + curDevName + curDevMac);
        BluetoothLeService.connect(curDevMac, curDevName, this);
    }

    private void disconnectDevice() {
        BluetoothLeService.disconnect();
    }

    private void initBroadcastReceiver() {
        registerReceiver(gattUpdateReceiver, Utils.makeGattUpdateIntentFilter());
    }

    private final BroadcastReceiver gattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                System.out.println("连接成功");
                BluetoothLeService.discoverServices();
            }
            else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                prepareGattService(BluetoothLeService.getSupportedGattServices());
            }
            else if (action.equals(BluetoothLeService.ACTION_GATT_DISCONNECTED)) {

            }
        }
    };

    private void prepareGattService(List<BluetoothGattService> gattServers) {
        prepareData(gattServers);

        Intent intent = new Intent(this, ServicesActivity.class);
        intent.putExtra("dev_name", curDevName);
        intent.putExtra("dev_mac", curDevMac);
        startActivity(intent);
    }

    private void prepareData(List<BluetoothGattService> bleGattServices) {
        if (bleGattServices == null) {
            return;
        }
        List<Service> services = new ArrayList<>();
        for (BluetoothGattService bleGattService : bleGattServices) {
            String uuid = bleGattService.getUuid().toString();
            if (uuid.equals(GattAttributes.GENERIC_ACCESS_SERVICE) || uuid.equals(GattAttributes.GENERIC_ATTRIBUTE_SERVICE))
                continue;
            String name = GattAttributes.lookup(uuid, "unknowService");
            Service service = new Service(name, bleGattService);
            services.add(service);
        }
        ((MyApplication) getApplication()).setServices(services);
    }

}

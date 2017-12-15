package sh.wico.www.lowpowerbluetoothdebugassistant;

import android.bluetooth.BluetoothGattService;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import sh.wico.www.lowpowerbluetoothdebugassistant.adapter.ServiceAdapter;
import sh.wico.www.lowpowerbluetoothdebugassistant.bean.Service;
import sh.wico.www.lowpowerbluetoothdebugassistant.utils.GattAttributes;

public class ServicesActivity extends AppCompatActivity {

    private List<Service> services = new ArrayList<>();     // 蓝牙设备的服务列表
    private RecyclerView recyclerView;                      // 展示服务列表
    private RecyclerView.LayoutManager layoutManager;       // 展示服务列表的布局管理器
    private ServiceAdapter adapter;                         // 蓝牙设备服务的适配器
    private MyApplication myApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);
        myApplication = (MyApplication) getApplication();
        services.addAll(myApplication.getServices());       // 从 MyApplication 中获取服务列表
        initView();                                         // 初始化展示服务的列表
    }

    /**
     * 初始化展示服务的列表
     */
    private void initView() {
        recyclerView = findViewById(R.id.services_ry);
        adapter = new ServiceAdapter(services);
        adapter.setItemClickListener(new ServiceAdapter.OnItemClickListener() {     // 为类表中的每一个服务项添加点击事件的监听
            @Override
            public void onItemClick(View view, int position) {
                Service service = services.get(position);                           // 获取点击的条目对应的 Service
                BluetoothGattService bgService = service.getBGService();            // 获取服务中封装的蓝牙服务
                myApplication.setCharacteristics(bgService.getCharacteristics());   // 将服务中的 Characteristics 放到 MyApplication 中
                Intent intent = new Intent(ServicesActivity.this, CharacteristicsActivity.class);
                String bgServiceUuid = bgService.getUuid().toString();
                if (GattAttributes.USR_SERVICE.equals(bgServiceUuid)) {             // 记录服务类型
                    intent = new Intent(ServicesActivity.this, DebugActivity.class);
                    intent.putExtra("is_user_service", true);
                    MyApplication.serviceType = Service.SERVICE_TYPE.TYPE_USER_DEBUG;
                }
                else if (GattAttributes.BATTERY_SERVICE.equals(bgServiceUuid)
                        || GattAttributes.RGB_LED_SERVICE_CUSTOM.equals(bgServiceUuid)) {
                    MyApplication.serviceType = Service.SERVICE_TYPE.TYPE_NUMBER;
                }
                else {
                    MyApplication.serviceType = Service.SERVICE_TYPE.TYPE_OTHER;
                }
                startActivity(intent);                                              // 跳转 Activity
            }
        });
        recyclerView.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
    }


}

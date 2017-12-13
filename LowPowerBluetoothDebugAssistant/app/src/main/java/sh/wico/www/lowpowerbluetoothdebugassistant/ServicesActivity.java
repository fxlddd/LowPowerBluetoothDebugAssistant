package sh.wico.www.lowpowerbluetoothdebugassistant;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import sh.wico.www.lowpowerbluetoothdebugassistant.adapter.ServiceAdapter;
import sh.wico.www.lowpowerbluetoothdebugassistant.bean.Service;

public class ServicesActivity extends AppCompatActivity {

    private List<Service> services = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ServiceAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);
        services.addAll(((MyApplication)getApplication()).getServices());
        for (Service service : services) {
            System.out.println(service.getName());
        }
        initView();
    }

    private void initView() {
        recyclerView = findViewById(R.id.services_ry);
        adapter = new ServiceAdapter(services);
        recyclerView.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(this, LinearLayout.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
    }


}

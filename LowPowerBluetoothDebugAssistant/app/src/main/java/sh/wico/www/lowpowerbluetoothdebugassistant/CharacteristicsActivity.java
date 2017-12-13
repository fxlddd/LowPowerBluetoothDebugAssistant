package sh.wico.www.lowpowerbluetoothdebugassistant;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import sh.wico.www.lowpowerbluetoothdebugassistant.adapter.CharacteristicsAdapter;

public class CharacteristicsActivity extends AppCompatActivity {


    private List<BluetoothGattCharacteristic> characteristics;
    private MyApplication myApplication;

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private CharacteristicsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_characteristics);
        myApplication = (MyApplication) getApplication();
        characteristics = myApplication.getCharacteristics();
        if (characteristics == null) characteristics = new ArrayList<>();
        initRecyclerView();
    }

    private void initRecyclerView() {
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        adapter = new CharacteristicsAdapter(characteristics);
        adapter.setItemClickListener(new CharacteristicsAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View View, int position) {
                myApplication.setCharacteristic(characteristics.get(position));
                Intent intent = new Intent(CharacteristicsActivity.this, GattDetailActivity.class);
                startActivity(intent);
            }
        });
        recyclerView = findViewById(R.id.characteristics_ry);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

}

package sh.wico.www.lowpowerbluetoothdebugassistant;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import sh.wico.www.lowpowerbluetoothdebugassistant.adapter.MessagesAdapter;
import sh.wico.www.lowpowerbluetoothdebugassistant.bean.Message;
import sh.wico.www.lowpowerbluetoothdebugassistant.service.BluetoothLeService;
import sh.wico.www.lowpowerbluetoothdebugassistant.utils.Constants;
import sh.wico.www.lowpowerbluetoothdebugassistant.utils.Utils;

public class DebugActivity extends AppCompatActivity {

    private RecyclerView messagesRV;
    private RecyclerView.LayoutManager messagesLM;
    private EditText inputET;
    private ImageButton sendIB;
    private MessagesAdapter messagesAdapter;
    private List<Message> messages = new ArrayList<>();

    private MyApplication myApplication;
    private BluetoothGattCharacteristic notifyCharacteristic;
    private BluetoothGattCharacteristic writeCharacteristic;


    private BroadcastReceiver gattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Bundle extras = intent.getExtras();
            if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                if (extras.containsKey(Constants.EXTRA_BYTE_VALUE) && extras.containsKey(Constants.EXTRA_BYTE_UUID_VALUE)) {
                    byte[] data = intent.getByteArrayExtra(Constants.EXTRA_BYTE_VALUE);
                    Message message = new Message(formatMsgContent(data), Message.MessageType.RECEIVED);
                    notifyAdapter(message);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);
        myApplication = (MyApplication) getApplication();
        initView();
        initCharacteristics();
        prepareBroadcastDataNotify();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(gattUpdateReceiver, Utils.makeGattUpdateIntentFilter());
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(gattUpdateReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopBroadcastDataNotify();
    }

    private void initView() {
        inputET = findViewById(R.id.input_et);
        sendIB = findViewById(R.id.send_ib);
        sendIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeData();
            }
        });

        messagesLM = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        messagesAdapter = new MessagesAdapter(messages);
        messagesRV = findViewById(R.id.messages_rv);
        messagesRV.setLayoutManager(messagesLM);
        messagesRV.setAdapter(messagesAdapter);
    }

    private void initCharacteristics() {
        List<BluetoothGattCharacteristic> bluetoothGattCharacteristics = myApplication.getCharacteristics();
        notifyCharacteristic = bluetoothGattCharacteristics.get(0);
        writeCharacteristic = bluetoothGattCharacteristics.get(1);
    }

    private void prepareBroadcastDataNotify() {
        BluetoothLeService.setCharacteristicNotification(notifyCharacteristic, true);
    }

    private void stopBroadcastDataNotify() {
        BluetoothLeService.setCharacteristicNotification(notifyCharacteristic, false);
    }

    private void notifyAdapter(Message message) {
        System.out.println(message.getContent());
        messages.add(message);
        messagesAdapter.notifyItemInserted(messages.size() - 1);
        messagesRV.smoothScrollToPosition(messagesAdapter.getItemCount() - 1);
    }

    public String formatMsgContent(byte[] data) {
        return "HEX" + Utils.ByteArraytoHex(data) + " (ASC II : " + Utils.byteToASCII(data) + ")";
    }

    private void writeData(){
        String data = inputET.getText().toString();

        System.out.println("write data : " + data);
        if (TextUtils.isEmpty(data)) {
            System.out.println("input is empty");
            Toast.makeText(this, "input is empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (Utils.isAtCmd(data)) {
            data += "\r\n";
        }
        try {
            byte[] bytes = data.getBytes("US-ASCII");
            writeDataBytes(bytes);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Message message = new Message(data, Message.MessageType.SEND);
        notifyAdapter(message);
    }

    private void writeDataBytes(byte[] bytes) {
        BluetoothLeService.writeCharacteristicGattDb(writeCharacteristic, bytes);
    }

}

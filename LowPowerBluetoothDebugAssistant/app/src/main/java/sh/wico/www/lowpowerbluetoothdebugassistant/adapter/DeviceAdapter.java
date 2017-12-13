package sh.wico.www.lowpowerbluetoothdebugassistant.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



import java.util.List;

import sh.wico.www.lowpowerbluetoothdebugassistant.R;
import sh.wico.www.lowpowerbluetoothdebugassistant.bean.Device;

/**
 * Created by HYW on 2017/12/7.
 */

public class DeviceAdapter extends RecyclerView.Adapter implements View.OnClickListener{

    private List<Device> deviceList;
    private OnItemClickListener itemClickListener;

    public DeviceAdapter(List<Device> deviceList) {
        this.deviceList = deviceList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_device, parent, false);
        view.setOnClickListener(this);  //
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        Device device = deviceList.get(position);
        viewHolder.deviceNameTv.setText("device : " + device.getBluetoothDevice().getName());
        viewHolder.deviceSignalTv.setText("signal : " + device.getRssi() + "dBm");
        viewHolder.deviceMacTv.setText("mac : " +device.getBluetoothDevice().getAddress());
        viewHolder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return deviceList == null ? 0 : deviceList.size();
    }

    @Override
    public void onClick(View v) {
        if (itemClickListener != null) {
            itemClickListener.onItemClick(v, (int) v.getTag());
        }
    }

    // 钩子函数用于监听到点击后调用
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView deviceNameTv;
        TextView deviceSignalTv;
        TextView deviceMacTv;


        public ViewHolder(View itemView) {
            super(itemView);
            deviceNameTv = itemView.findViewById(R.id.device_name_tv);
            deviceSignalTv = itemView.findViewById(R.id.device_signal_tv);
            deviceMacTv = itemView.findViewById(R.id.device_mac_tv);
        }
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}

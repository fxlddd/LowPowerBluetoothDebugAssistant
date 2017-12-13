package sh.wico.www.lowpowerbluetoothdebugassistant.adapter;

import android.bluetooth.BluetoothGattCharacteristic;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import sh.wico.www.lowpowerbluetoothdebugassistant.R;
import sh.wico.www.lowpowerbluetoothdebugassistant.utils.GattAttributes;
import sh.wico.www.lowpowerbluetoothdebugassistant.utils.Utils;

/**
 * Created by HYW on 2017/12/13.
 */

public class CharacteristicsAdapter extends RecyclerView.Adapter implements View.OnClickListener{

    private List<BluetoothGattCharacteristic> characteristics;
    private OnItemClickListener itemClickListener;

    public CharacteristicsAdapter(List<BluetoothGattCharacteristic> characteristics) {
        this.characteristics = characteristics;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_characteristic, parent, false);   // 将列表试图转化成View
        view.setOnClickListener(this);      // 为视图添加点击事件的监听
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        BluetoothGattCharacteristic characteristic = characteristics.get(position);
        String characteristicName = GattAttributes.lookup(characteristic.getUuid().toString(), characteristic.getUuid().toString());
        String characteristicProperties = Utils.getPorperties(holder.itemView.getContext(), characteristic);
        viewHolder.characteristicNameTv.setText(characteristicName);
        viewHolder.characteristicPropertiesTv.setText(characteristicProperties);
        viewHolder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return characteristics.size();
    }

    @Override
    public void onClick(View v) {
        if (itemClickListener != null) {
            itemClickListener.OnItemClick(v, (int)v.getTag());
        }
    }

    public interface OnItemClickListener {
        void OnItemClick (View View, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView characteristicNameTv;
        TextView characteristicPropertiesTv;

        public ViewHolder(View itemView) {
            super(itemView);
            characteristicNameTv = itemView.findViewById(R.id.characteristic_name_tv);
            characteristicPropertiesTv = itemView.findViewById(R.id.characteristic_properties_tv);
        }

    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}

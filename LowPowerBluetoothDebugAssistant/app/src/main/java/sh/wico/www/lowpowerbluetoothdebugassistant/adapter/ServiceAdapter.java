package sh.wico.www.lowpowerbluetoothdebugassistant.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



import java.util.List;

import sh.wico.www.lowpowerbluetoothdebugassistant.R;
import sh.wico.www.lowpowerbluetoothdebugassistant.bean.Service;

/**
 * Created by HYW on 2017/12/12.
 * 用于将服务展示到界面
 */

public class ServiceAdapter extends RecyclerView.Adapter implements View.OnClickListener {

    private List<Service> services;                     // 服务列表
    private OnItemClickListener itemClickListener;      // 点击事件监听


    public ServiceAdapter(List<Service> services) {
        this.services = services;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service, parent, false);
        view.setOnClickListener(this);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder)holder;
        Service service = services.get(position);
        viewHolder.serviceNameTv.setText(service.getName());
        viewHolder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return services == null ? 0 : services.size();
    }

    @Override
    public void onClick(View v) {
        if (itemClickListener != null) {
            itemClickListener.onItemClick(v, (int)v.getTag());
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView serviceNameTv;

        public ViewHolder(View itemView) {
            super(itemView);
            serviceNameTv = itemView.findViewById(R.id.service_name);
        }
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

}

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
 */

public class ServiceAdapter extends RecyclerView.Adapter implements View.OnClickListener{

    private List<Service> services;

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

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView serviceNameTv;

        public ViewHolder(View itemView) {
            super(itemView);
            serviceNameTv = itemView.findViewById(R.id.service_name);
        }
    }

}

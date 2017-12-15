package sh.wico.www.lowpowerbluetoothdebugassistant.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sh.wico.www.lowpowerbluetoothdebugassistant.R;
import sh.wico.www.lowpowerbluetoothdebugassistant.bean.Message;

/**
 * Created by HYW on 2017/12/14.
 */

public class MessagesAdapter extends RecyclerView.Adapter {

    private List<Message> messages;

    public MessagesAdapter(List<Message> messages) {
        this.messages = messages == null ? new ArrayList<Message>() : messages;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        Message message = messages.get(position);
        if (message.getMessageType() == Message.MessageType.SEND) {
            viewHolder.messageTVRight.setText(message.getContent());
            viewHolder.messageLLRight.setVisibility(View.VISIBLE);
            viewHolder.messageLLLeft.setVisibility(View.GONE);
        }
        else {
            viewHolder.messageTVLeft.setText(message.getContent());
            viewHolder.messageLLLeft.setVisibility(View.VISIBLE);
            viewHolder.messageLLRight.setVisibility(View.GONE);
        }
        viewHolder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView messageTVLeft;
        TextView messageTVRight;
        LinearLayout messageLLLeft;
        LinearLayout messageLLRight;

        public ViewHolder(View itemView) {
            super(itemView);
            messageTVLeft = itemView.findViewById(R.id.message_tv_left);
            messageTVRight = itemView.findViewById(R.id.message_tv_right);
            messageLLLeft = itemView.findViewById(R.id.message_ll_left);
            messageLLRight = itemView.findViewById(R.id.message_ll_right);
        }
    }


}

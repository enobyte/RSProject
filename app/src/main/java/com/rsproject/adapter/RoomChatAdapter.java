package com.rsproject.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rsproject.R;
import com.rsproject.encapsule.ListChatItem;

import java.util.List;

/**
 * Created by Eno on 3/29/2016.
 */
public class RoomChatAdapter extends RecyclerView.Adapter<RoomChatAdapter.ChatHolder> {
    private List<ListChatItem> itemData;
    View itemLayoutView;
    ViewGroup mParent;
    //Activity activity;

    public RoomChatAdapter(List<ListChatItem> itemData) {
        //this.activity = activity;
        this.itemData = itemData;
    }

    @Override
    public ChatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mParent             = parent;
        itemLayoutView      = LayoutInflater.from(parent.getContext()).inflate(R.layout.textroom_activity, parent, false);
        ChatHolder chatHolder = new ChatHolder(itemLayoutView);
        return chatHolder;
    }

    @Override
    public void onBindViewHolder(ChatHolder holder, int position) {
        final ListChatItem item = itemData.get(position);
        holder.leftSMS.setText(item.getContent());
        if (item != null) {
            if (item.getLevel().equalsIgnoreCase("1")) {
                holder.leftSMS.setGravity(Gravity.RIGHT);
                holder.linerChat.setGravity(Gravity.RIGHT);
                holder.relativeLayout.setGravity(Gravity.RIGHT);
                holder.relativeLayout.setBackgroundResource(R.drawable.speech_bubble_out);
            } else {
                holder.leftSMS.setGravity(Gravity.LEFT);
                holder.linerChat.setGravity(Gravity.LEFT);
                holder.relativeLayout.setGravity(Gravity.LEFT);
                holder.relativeLayout.setBackgroundResource(R.drawable.speech_bubble_in);
            }
        }
    }


    @Override
    public int getItemCount() {
        return itemData.size();
    }

    public class ChatHolder extends RecyclerView.ViewHolder {
        TextView leftSMS, dateSMS;
        LinearLayout linerChat;
        RelativeLayout relativeLayout;

        public ChatHolder(View itemView) {
            super(itemView);
            leftSMS         = (TextView) itemView.findViewById(R.id.leftSMS);
            dateSMS         = (TextView) itemView.findViewById(R.id.tgljam);
            linerChat       = (LinearLayout) itemView.findViewById(R.id.linierChat);
            relativeLayout  = (RelativeLayout) itemView.findViewById(R.id.rtextroom);
        }
    }

}
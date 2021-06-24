package com.example.app;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class RoomViewHolder extends RecyclerView.ViewHolder{
    TextView chatroom_title;
    TextView chatroom_lastmeesage;
    TextView chatroom_timestamp;

    public RoomViewHolder(View itemView){
        super(itemView);
        chatroom_title = (TextView)itemView.findViewById(R.id.chatroom_title);
        chatroom_lastmeesage = (TextView)itemView.findViewById(R.id.chatroom_lastMessage);
        chatroom_timestamp = (TextView)itemView.findViewById(R.id.chatroom_timestamp);

    }
}

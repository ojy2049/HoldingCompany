package com.example.app;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ChatViewHolder extends RecyclerView.ViewHolder{
    TextView username;
    TextView msg;
    TextView timestamp;
    LinearLayout linearLayoutDest;
    LinearLayout linearLayoutmain;

    ChatViewHolder(View itemView)
    {
        super(itemView);

        username= itemView.findViewById(R.id.username);
        timestamp= itemView.findViewById(R.id.timestamp);
        msg= itemView.findViewById(R.id.msg);
        linearLayoutDest=itemView.findViewById(R.id.linearlayout_destination);
        linearLayoutmain=itemView.findViewById(R.id.linearlayout_main);

    }
}

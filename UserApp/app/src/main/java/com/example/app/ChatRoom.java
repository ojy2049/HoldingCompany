package com.example.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

public class ChatRoom extends Fragment {
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd hh:mm");

    String dUid =null;
    FirebaseDatabase firebaseDatabase;
    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment1, container, false);

        firebaseDatabase = FirebaseDatabase.getInstance();
        RecyclerView recyclerView = view.findViewById(R.id.chatfragment_recyclerview);
        recyclerView.setAdapter(new RoomAdapter());
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));


        return view;
    }

    class RoomAdapter extends RecyclerView.Adapter<RoomViewHolder> {
        private List<Chatmodel> chatmodel = new ArrayList<>();
        private String uid;
        private ArrayList<String> dUser=new ArrayList<>();;

        ChatUser chatuser;
        public RoomAdapter() {

            getUid();
        }

        private void getUid() {
            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            firebaseDatabase.getReference().child("chatrooms").orderByChild("users/" + uid).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    chatmodel.clear();
                    for (DataSnapshot item : dataSnapshot.getChildren()) {
                        chatmodel.add(item.getValue(Chatmodel.class));
                    }
                    notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        public RoomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.room_view, parent, false);
            RoomViewHolder viewHolder = new RoomViewHolder(view);

            return viewHolder;

        }

        public void onBindViewHolder(RoomViewHolder viewHolder, int position) {

            for (String user : chatmodel.get(position).users.keySet()) {
                if (!user.equals(uid)) {
                    dUid = user;
                    Log.d("hello", dUid);
                    dUser.add(dUid);
                }
            }
            firebaseDatabase.getReference().child("users").child(dUid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    chatuser =  dataSnapshot.getValue(ChatUser.class);


                    viewHolder.chatroom_title.setText(chatuser.userName);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            Map<String, Chatmodel.Comment> commentMap = new TreeMap<>(Collections.reverseOrder());
            commentMap.putAll(chatmodel.get(position).comments);
            String lastMessageKey = (String) commentMap.keySet().toArray()[0];
            viewHolder.chatroom_lastmeesage.setText(chatmodel.get(position).comments.get(lastMessageKey).message);

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(view.getContext(), ChatActivity.class);
                    intent.putExtra("frienduid", dUser.get(position));
                    startActivity(intent);

                }
            });

            long unixTime = (long) chatmodel.get(position).comments.get(lastMessageKey).timestamp;
            Date date = new Date(unixTime);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
            viewHolder.chatroom_timestamp.setText(simpleDateFormat.format(date));

        }

        @Override
        public int getItemCount() {
            return chatmodel.size();
        }
    }
}

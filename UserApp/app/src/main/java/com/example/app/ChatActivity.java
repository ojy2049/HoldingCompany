package com.example.app;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {
    public String Roomuid;
    private String myuid;
    public String frienduid;
    private float rating;

    public static Context Context_main;
    RecyclerView recyclerView;
    Button button, btn_submit, btn_cancel;
    EditText editText;
    Toolbar toolbar;
    FirebaseDatabase firebaseDatabase;
    Dialog dialog;
    RatingBar ratingBar;


    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyy.MM.dd HH:mm");
    private Log Lod;

    public void onBackPressed() {
        Intent intent = new Intent(ChatActivity.this, MainActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK

                | Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acivity_chat);

        Context_main=this;
        myuid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        frienduid = getIntent().getStringExtra("frienduid");        //채팅 상대
        //Log.d("chatchat", frienduid);

        recyclerView = findViewById(R.id.chat_recyler_view);
        button=findViewById(R.id.btn_send);
        editText = findViewById(R.id.chat_Etext);

        toolbar= findViewById(R.id.chat_toolbar);
        setSupportActionBar(toolbar);

        firebaseDatabase = FirebaseDatabase.getInstance();

        if(editText.getText().toString() == null) button.setEnabled(false);
        else button.setEnabled(true);
        checkChatRoom();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Chatmodel chatModel = new Chatmodel();
                chatModel.users.put(myuid,true);
                chatModel.users.put(frienduid,true);

                if(Roomuid == null){
                    button.setEnabled(false);
                    firebaseDatabase.getReference().child("chatrooms").push().setValue(chatModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            checkChatRoom();
                        }
                    });
                }else{
                    sendMsgToDataBase();
                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.chat_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.call:
                Call();
                return true;
            case R.id.leave_room:
                Show_Dialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //작성한 메시지를 데이터베이스에 보낸다.
    private void sendMsgToDataBase()
    {
        if(!editText.getText().toString().equals(""))
        {
            Chatmodel.Comment comment = new Chatmodel.Comment();
            comment.uid = myuid;
            comment.message = editText.getText().toString();
            comment.timestamp = ServerValue.TIMESTAMP;
            firebaseDatabase.getReference().child("chatrooms").child(Roomuid).child("comments").push().setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    editText.setText("");
                }
            });
        }
    }

    private void checkChatRoom()
    {
        //자신 key == true 일때 chatModel 가져온다.
        firebaseDatabase.getReference().child("chatrooms").orderByChild("users/"+myuid).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()) //나, 상대방 id 가져온다.
                {
                    Chatmodel chatmodel = dataSnapshot.getValue(Chatmodel.class);
                    if(chatmodel.users.containsKey(frienduid)){
                        Roomuid = dataSnapshot.getKey();
                        button.setEnabled(true);

                        //동기화
                        recyclerView.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
                        recyclerView.setAdapter(new chatAdapter());

                        //메시지 보내기
                        sendMsgToDataBase();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    class chatAdapter extends RecyclerView.Adapter<ChatViewHolder>
    {
        List<Chatmodel.Comment> msgdata;
        ChatUser User;

        public chatAdapter()
        {
            msgdata= new ArrayList<>();
            getFriendUid();

        }
        private void getFriendUid()
        {
            firebaseDatabase.getReference().child("users").child(frienduid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User = snapshot.getValue(ChatUser.class);
                    getMessageList();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }

        private void getMessageList()
        {
            FirebaseDatabase.getInstance().getReference().child("chatrooms").child(Roomuid).child("comments").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    msgdata.clear();

                    for(DataSnapshot dataSnapshot : snapshot.getChildren())
                    {
                        msgdata.add(dataSnapshot.getValue(Chatmodel.Comment.class));
                    }
                    notifyDataSetChanged();

                    recyclerView.scrollToPosition(msgdata.size()-1);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) { }
            });
        }

        @Override
        public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            Context context = parent.getContext();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            //뷰홀더 객체 생성
            View view = inflater.inflate(R.layout.message, parent, false);
            ChatViewHolder viewHolder = new ChatViewHolder(view);

            return viewHolder;
        }
        public void onBindViewHolder(ChatViewHolder viewHolder, int position)
        {

            if(msgdata.get(position).uid.equals(myuid)){
                viewHolder.msg.setText(msgdata.get(position).message);
                viewHolder.msg.setBackgroundResource(R.drawable.rightbubble);
                viewHolder.msg.setTextSize(20);
                viewHolder.linearLayoutDest.setVisibility(View.INVISIBLE);
                viewHolder.linearLayoutmain.setGravity(Gravity.RIGHT);
            }
            else{

                viewHolder.msg.setText(msgdata.get(position).message);
                viewHolder.msg.setBackgroundResource(R.drawable.leftbubble);
                viewHolder.msg.setTextSize(20);
                viewHolder.username.setText(User.userName);
                viewHolder.linearLayoutDest.setVisibility(View.VISIBLE);
                viewHolder.linearLayoutmain.setGravity(Gravity.LEFT);
            }

            viewHolder.timestamp.setText(getDateTime(position));

        }

        public String getDateTime(int position)
        {
            long unixTime=(long) msgdata.get(position).timestamp;
            Date date = new Date(unixTime);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
            String time = simpleDateFormat.format(date);
            return time;
        }

        @Override
        public int getItemCount() {
            return msgdata.size();
        }

    }
    private void Call(){
        retrofitservice retrofit = new retrofitservice();
        Call<Result> call = retrofit.api.getCall(frienduid);
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (response.body() != null && response.isSuccessful()) {
                    Result result = response.body();
                    System.out.println(result.getMessage());
                    if (result.getMessage().equals("ok")) {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+result.getPhone()));
                        startActivity(intent);
                    }
                    else
                        System.out.println("NO라니");
                } else {
                    System.out.println("오류1");
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                System.out.println("오류2");
            }

        });
    }
    private void Show_Dialog(){
        dialog=new Dialog(ChatActivity.this);
        dialog.setContentView(R.layout.pop_dialog);

        ratingBar =dialog.findViewById(R.id.pop_ratingbar);
        btn_submit=dialog.findViewById(R.id.btn_submit);
        btn_cancel=dialog.findViewById(R.id.btn_cancel);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                rating = v;
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("별점은"+rating);
                ratingbar_submit();
                dialog.dismiss();
                finish();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });
        dialog.setCanceledOnTouchOutside(false);

        dialog.show();
    }
    private void ratingbar_submit(){
        retrofitservice retrofit = new retrofitservice();

        Call<Result> call = retrofit.api.rating(frienduid,myuid,rating);
        System.out.println("uid는" +frienduid +" "+ myuid+" "+ rating);
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (response.body() != null && response.isSuccessful()) {
                    System.out.println("성공");
                } else {
                    System.out.println("오류1");
                }
            }
            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                System.out.println("오류2");
            }

        });
    }


}

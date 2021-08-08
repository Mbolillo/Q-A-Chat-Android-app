package com.example.questionsandanswerschat.Chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.questionsandanswerschat.Chat.Adapter.MessagesAdapter;
import com.example.questionsandanswerschat.Chat.Model.ChatInfo;
import com.example.questionsandanswerschat.Chat.Model.UserInfo;
import com.example.questionsandanswerschat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConversationPage extends AppCompatActivity {

    private CircleImageView image_profile;
    private TextView username;

    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    private Intent intent;
    private ImageButton btSend;
    private EditText txtSend;
    private Toolbar toolbar;

    MessagesAdapter messagesAdapter;
    List<ChatInfo> chatInfos;

    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_page);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);



        image_profile = findViewById(R.id.image_profile);
        username = findViewById(R.id.fetchedName);
        btSend = findViewById(R.id.btSend);
        txtSend = findViewById(R.id.txtSend);

        intent = getIntent();
        final String userId = intent.getStringExtra("userId");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = txtSend.getText().toString();

                if (!message.equals("")){
                    sendMessage(firebaseUser.getUid(), userId, message);
                }else{
                    Toast.makeText(ConversationPage.this, "Please send a non empty message", Toast.LENGTH_SHORT).show();
                }
                txtSend.setText("");
            }
        });



        reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                UserInfo userInfo = dataSnapshot.getValue(UserInfo.class);
                username.setText(userInfo.getUsername());
                if(userInfo.getImageUrl().equals("default")){
                    image_profile.setImageResource(R.mipmap.ic_launcher);
                } else{
                    Glide.with(ConversationPage.this).load(userInfo.getImageUrl()).into(image_profile);
                }

                readMessage(firebaseUser.getUid(), userId, userInfo.getImageUrl());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void sendMessage(String sender, final String receiver, String message){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        ////////////////////////

        ////////////////////////
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);

        reference.child("Chats").push().setValue(hashMap);


        ///////////////////////////////////////
           //Add user to chat fragment
        final DatabaseReference refChat = FirebaseDatabase.getInstance().getReference("ChatList")
                .child(firebaseUser.getUid()).child(receiver);

        refChat.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    refChat.child("id").setValue(receiver);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /////////////////////////////////////////
    }

    private void readMessage (final String myId, final String userId, final String imageUrl){
        chatInfos = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatInfos.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ChatInfo chatInfo = snapshot.getValue(ChatInfo.class);
                    if(chatInfo.getReceiver().equals(myId) && chatInfo.getSender().equals(userId) || chatInfo.getReceiver().equals(userId) && chatInfo.getSender().equals(myId)){
                        chatInfos.add(chatInfo);
                    }

                    messagesAdapter = new MessagesAdapter(ConversationPage.this, chatInfos, imageUrl);
                    recyclerView.setAdapter(messagesAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}

package com.example.questionsandanswerschat.Chat.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.questionsandanswerschat.Chat.ConversationPage;
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

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {

    private Context context;
    private List<UserInfo> userInfos;
    private boolean isChat;

    String lastMessage;

    public UsersAdapter (Context context,List<UserInfo>userInfos,boolean isChat){
        this.context = context;
        this.userInfos = userInfos;
        this.isChat = isChat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.useritems, parent, false);
        return new UsersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final UserInfo user = userInfos.get(position);
        holder.username.setText(user.getUsername());

        if(user.getImageUrl().equals("default")){
            holder.image_profile.setImageResource(R.mipmap.ic_launcher);
        }else {
            Glide.with(context).load(user.getImageUrl()).into(holder.image_profile);
        }


        if (isChat){
            lastMessage(user.getId(), holder.lastMessage);
        }else{
            holder.lastMessage.setVisibility(View.GONE);
        }

       holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(context, ConversationPage.class);
               intent.putExtra("userId", user.getId());
               context.startActivity(intent);
           }
       });
    }

    @Override
    public int getItemCount() {

        return userInfos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView username;
        public ImageView image_profile;
        private TextView lastMessage;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.et_username);
            image_profile = itemView.findViewById(R.id.image_profile);
            lastMessage = itemView.findViewById(R.id.lastMessage);
        }
    }

    //It will check the last message that has been sent

    private void lastMessage(final String userId, final TextView lastMsg){
        lastMessage = "default";
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ChatInfo chatInfo = snapshot.getValue(ChatInfo.class);
                    if(chatInfo.getReceiver().equals(firebaseUser.getUid()) && chatInfo.getSender().equals(userId) ||
                    chatInfo.getReceiver().equals(userId) && chatInfo.getSender().equals(firebaseUser.getUid())){

                        lastMessage = chatInfo.getMessage();
                    }
                }

                switch (lastMessage){
                    case "default":
                        lastMsg.setText("No messages"); //If never started a conversation
                        break;

                    default:
                        lastMsg.setText(lastMessage);
                        break;
                }

                lastMessage = "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}

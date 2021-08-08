package com.example.questionsandanswerschat.Chat.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.questionsandanswerschat.Chat.Model.ChatInfo;
import com.example.questionsandanswerschat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {

    public static final int message_left=0;
    public static final int message_right=1;

    private Context context;
    private List<ChatInfo> chatInfos;
    private String imageUrl;

    FirebaseUser firebaseUser;

    public MessagesAdapter (Context context,List<ChatInfo>chatInfos, String imageUrl){
        this.context = context;
        this.chatInfos = chatInfos;
        this.imageUrl = imageUrl;
    }

    @NonNull
    @Override
    public MessagesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == message_right) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_chat_right, parent, false);
            return new MessagesAdapter.ViewHolder(view);
        }else{
            View view = LayoutInflater.from(context).inflate(R.layout.item_chat_left, parent, false);
            return new MessagesAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesAdapter.ViewHolder holder, int position) {

        ChatInfo chat = chatInfos.get(position);
        holder.message_show.setText(chat.getMessage());

        if(imageUrl.equals("default")){
            holder.image_profile.setImageResource(R.mipmap.ic_launcher);
        }else{
            Glide.with(context).load(imageUrl).into(holder.image_profile);
        }
    }

    @Override
    public int getItemCount() {

        return chatInfos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView message_show;
        public ImageView image_profile;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            message_show = itemView.findViewById(R.id.message_show);
            image_profile = itemView.findViewById(R.id.image_profile);
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(chatInfos.get(position).getSender().equals(firebaseUser.getUid())){
            return message_right;
        }else{
            return message_left;
        }
    }
}

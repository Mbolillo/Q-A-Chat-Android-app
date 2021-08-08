package com.example.questionsandanswerschat.Game.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.questionsandanswerschat.R;


public class ChatFragmentGame extends Fragment {

    public static ChatFragmentGame newInstance(){
        ChatFragmentGame chatFragmentGame = new ChatFragmentGame();
        return chatFragmentGame;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myFragment = inflater.inflate(R.layout.fragment_chat_game,container,false);

        return myFragment;

    }
}

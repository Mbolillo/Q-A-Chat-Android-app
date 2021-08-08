package com.example.questionsandanswerschat.Game;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.questionsandanswerschat.Game.GlobalVariables.GlobalVariables;
import com.example.questionsandanswerschat.Game.Model.Question;
import com.example.questionsandanswerschat.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;

public class Play extends AppCompatActivity {

    Button btPlay;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Questions");

        btPlay = findViewById(R.id.bt_play);
        btPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Play.this, InGame.class);
                startActivity(intent);
                finish();
            }
        });

        loadQuestion(GlobalVariables.IdCategory);

    }

    private void loadQuestion(String idCategory) {

        //Clear list if there is old questions

        if (GlobalVariables.questionsList.size() >0)
            GlobalVariables.questionsList.clear();


        databaseReference.orderByChild("CategoryId").equalTo(idCategory).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){

                    Question question = postSnapshot.getValue(Question.class);
                    GlobalVariables.questionsList.add(question);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Get Random List
        Collections.shuffle(GlobalVariables.questionsList);
    }
}

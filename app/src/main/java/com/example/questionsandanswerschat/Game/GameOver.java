package com.example.questionsandanswerschat.Game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.questionsandanswerschat.MainActivity;
import com.example.questionsandanswerschat.R;

public class GameOver extends AppCompatActivity {

    Button btPlayAgain;
    TextView scoreTotal, questionTotal;
    ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);


        scoreTotal = findViewById(R.id.ScoreTotal);
        questionTotal = findViewById(R.id.QuestionTotal);
        progressBar = findViewById(R.id.progressBarFinish);
        btPlayAgain = findViewById(R.id.btPlayAgain);

        btPlayAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameOver.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //Here we'll get the data from bundle and will set to view

        Bundle extra = getIntent().getExtras();
        if(extra != null){
            int score = extra.getInt("Score");
            int totalQuestion = extra.getInt("Total");
            int correct = extra.getInt("Correct");

            scoreTotal.setText((String.format("Score : %d",score)));
            questionTotal.setText(String.format("Correct : %d / %d",correct,totalQuestion));

            progressBar.setMax(totalQuestion);
            progressBar.setProgress(correct);

        }
    }
}

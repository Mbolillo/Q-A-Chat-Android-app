package com.example.questionsandanswerschat.Game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.questionsandanswerschat.Game.GlobalVariables.GlobalVariables;
import com.example.questionsandanswerschat.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class InGame extends AppCompatActivity implements View.OnClickListener{

    final static long interval = 1000;
    final static long timeOut = 9000;
    int progressValue = 0;
    int index=0, score=0, thisQuestion=0, totalQuestion, correctAnswer;
    ProgressBar progressBar;
    CountDownTimer countDownTimer;
    ImageView imageQuestion;
    TextView textQuestion, textScore, textQuestionNumber;
    Button btA, btB, btC, btD;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_game);



        progressBar = findViewById(R.id.progressBar);
        textQuestion = findViewById(R.id.textQuestion);
        textScore = findViewById(R.id.score);
        textQuestionNumber = findViewById(R.id.questionsTotal);
        imageQuestion = findViewById(R.id.imageQuestion);

        btA = findViewById(R.id.btA);
        btB = findViewById(R.id.btB);
        btC = findViewById(R.id.btC);
        btD = findViewById(R.id.btD);

        btA.setOnClickListener(this);
        btB.setOnClickListener(this);
        btC.setOnClickListener(this);
        btD.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        countDownTimer.cancel();

        //If there are still questions to finish the list
        if (index < totalQuestion) {

            Button clickedButton = (Button) v;

            //If user chose correct answer
            if (clickedButton.getText().equals(GlobalVariables.questionsList.get(index).getCorrectAnswer())){

                score+= 10;
                correctAnswer++;
                showQuestion(++index); //User goes to the next question
            }
            //If user chose wrong answer
            else if(!clickedButton.getText().equals(GlobalVariables.questionsList.get(index).getCorrectAnswer())){
                score+= 0;
                showQuestion(++index);
            }

            else{

                Intent intent = new Intent(this, GameOver.class );
                Bundle bundle = new Bundle();
                bundle.putInt("Score", score);
                bundle.putInt("Total", totalQuestion);
                bundle.putInt("Correct", correctAnswer);

                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
            textScore.setText(String.format("%d",score));
        }
    }

    private void showQuestion(int index) {

        if (index < totalQuestion){

            thisQuestion++;
            textQuestionNumber.setText(String.format("%d/%d",thisQuestion,totalQuestion));
            progressBar.setProgress(0);
            progressValue = 0;

            //If question is an image
            if (GlobalVariables.questionsList.get(index).getIsImageQuestion().equals("true")){

                Picasso.get().load(GlobalVariables.questionsList.get(index).getQuestion()).into(imageQuestion);

                imageQuestion.setVisibility(View.VISIBLE);
                textQuestion.setVisibility(View.INVISIBLE);

            }else   {
                    //if question is text instead of image
                textQuestion.setText(GlobalVariables.questionsList.get(index).getQuestion());
                imageQuestion.setVisibility(View.INVISIBLE);
                textQuestion.setVisibility(View.VISIBLE);
            }

            btA.setText(GlobalVariables.questionsList.get(index).getAnswerA());
            btB.setText(GlobalVariables.questionsList.get(index).getAnswerB());
            btC.setText(GlobalVariables.questionsList.get(index).getAnswerC());
            btD.setText(GlobalVariables.questionsList.get(index).getAnswerD());

            countDownTimer.start();
        }

        //If this is the final question
        else {

            Intent intent = new Intent(this, GameOver.class );
            Bundle bundle = new Bundle();
            bundle.putInt("Score", score);
            bundle.putInt("Total", totalQuestion);
            bundle.putInt("Correct", correctAnswer);

            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        totalQuestion = GlobalVariables.questionsList.size();

        countDownTimer = new CountDownTimer(timeOut,interval) {
            @Override
            public void onTick(long miniSeconds) {
                progressBar.setProgress(progressValue);
                progressValue++;

            }

            @Override
            public void onFinish() {
                countDownTimer.cancel();
                showQuestion(++index);
            }
        };

        showQuestion(index);

    }
}

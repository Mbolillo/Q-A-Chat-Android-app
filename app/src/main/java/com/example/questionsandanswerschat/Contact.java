package com.example.questionsandanswerschat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Contact extends AppCompatActivity {
    private EditText editTextTo;
    private EditText editTextSubject;
    private EditText getEditTextMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        editTextTo = findViewById(R.id.edit_text_to);
        editTextSubject = findViewById(R.id.edit_text_subject);
        getEditTextMessage = findViewById(R.id.edit_text_message);

        Button buttonSend = findViewById(R.id.btn_send);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMail();
            }
        });
    }

    private void sendMail(){
        String recipientList = editTextTo.getText().toString();
        String[] recipients = recipientList.split(",");

        String subject = editTextSubject.getText().toString();
        String message = getEditTextMessage.getText().toString();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, recipients);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);

        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, "Choose and email client"));

    }
}

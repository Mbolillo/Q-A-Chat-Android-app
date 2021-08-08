package com.example.questionsandanswerschat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordReset extends AppCompatActivity {

    private EditText et_sendEmail;
    private Button bt_resetPassword;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Password Reset");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        et_sendEmail = findViewById(R.id.et_resetEmail);
        bt_resetPassword = findViewById(R.id.bt_resetEmail);

        firebaseAuth = FirebaseAuth.getInstance();
        bt_resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = et_sendEmail.getText().toString();

                if(email.equals("")){
                    Toast.makeText(PasswordReset.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                }else{
                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){
                                Toast.makeText(PasswordReset.this, "We have sent an email to "+ email, Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(PasswordReset.this, Login.class));
                            }else{
                                String error = task.getException().getMessage();
                                Toast.makeText(PasswordReset.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}

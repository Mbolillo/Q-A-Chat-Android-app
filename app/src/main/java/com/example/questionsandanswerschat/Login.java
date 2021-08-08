package com.example.questionsandanswerschat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.questionsandanswerschat.Game.GlobalVariables.GlobalVariables;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class Login extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btLogin;
    private TextView tvRegister,tvResetPassword;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference reference;
    private FirebaseUser firebaseUser;



    //User gets directly to the main page(evenThough the app was closed) unless the user clicked on logOut
    @Override
    protected void onStart() {
        super.onStart();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //check if user is null
        if(firebaseUser !=null){
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btLogin = findViewById(R.id.bt_login);
        tvRegister = findViewById(R.id.tv_Register);
        tvResetPassword = findViewById(R.id.password_forgot);

        tvResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            startActivity(new Intent(Login.this, PasswordReset.class));
            }
        });



        firebaseAuth = FirebaseAuth.getInstance();

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_Email = etEmail.getText().toString();
                String txt_Password = etPassword.getText().toString();

                if(TextUtils.isEmpty(txt_Email) || TextUtils.isEmpty(txt_Password)){
                    Toast.makeText(Login.this,"Please fill all fields", Toast.LENGTH_SHORT).show();
                }else{
                    firebaseAuth.signInWithEmailAndPassword(txt_Email, txt_Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Intent intent = new Intent(Login.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                Toast.makeText(Login.this, "User logged", Toast.LENGTH_SHORT).show();

                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(Login.this,"Email/Password are wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });


        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });
    }
}

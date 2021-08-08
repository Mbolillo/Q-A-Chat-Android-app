package com.example.questionsandanswerschat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.ToolbarWidgetWrapper;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.internal.InternalTokenProvider;

import java.util.HashMap;

public class Register extends AppCompatActivity {

    private EditText etUsername, etEmail, etPassword;
    private Button btRegister;
    private TextView tvLogin;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUsername = findViewById(R.id.et_username);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btRegister = findViewById(R.id.btRegister);
        tvLogin = findViewById(R.id.tv_Login);


        firebaseAuth = FirebaseAuth.getInstance();

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_Username = etUsername.getText().toString();
                String txt_Email = etEmail.getText().toString();
                String txt_Password = etPassword.getText().toString();

                if(TextUtils.isEmpty(txt_Username) || TextUtils.isEmpty(txt_Email) || TextUtils.isEmpty(txt_Password)){
                    Toast.makeText(Register.this,"Please fill all fields", Toast.LENGTH_SHORT).show();
                }else if (txt_Password.length() <5){
                    Toast.makeText(Register.this,"Your password must have at least 5 characters", Toast.LENGTH_SHORT).show();
                }else{
                    Registration(txt_Username, txt_Email, txt_Password);
                }
            }
        });


        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, Login.class));
            }
        });
    }

    private void Registration(final String username, final String email , String password){
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    assert user != null;
                    String userId = user.getUid();

                    reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("id", userId);
                    hashMap.put("username", username);
                    hashMap.put("imageUrl", "default");
                    hashMap.put("email", email);

                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Intent intent = new Intent(Register.this, MainActivity.class);
                                Toast.makeText(Register.this, "User registered", Toast.LENGTH_SHORT).show();
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }else {
                    Toast.makeText(Register.this, "Can't register with this email or password", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}

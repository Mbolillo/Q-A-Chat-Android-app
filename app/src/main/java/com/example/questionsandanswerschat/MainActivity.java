package com.example.questionsandanswerschat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.questionsandanswerschat.Chat.ChatSection;
import com.example.questionsandanswerschat.Chat.Model.UserInfo;
import com.example.questionsandanswerschat.Game.Fragments.CategoryFragment;
import com.example.questionsandanswerschat.Game.Fragments.ChatFragmentGame;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private CircleImageView image_profile;
    private TextView username;
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        image_profile = findViewById(R.id.image_profile);
        username = findViewById(R.id.fetchedName);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        //this will retrieve the username from the database and profile image and gets displayed on the toolbar
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                UserInfo userInfo = dataSnapshot.getValue(UserInfo.class);
                username.setText(userInfo.getUsername());
                if (userInfo.getImageUrl().equals("default")) {
                    image_profile.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Glide.with(MainActivity.this).load(userInfo.getImageUrl()).into(image_profile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ////Setting fragment for Button navigation located at the bottom of the activity main
        bottomNavigationView = findViewById((R.id.navigation));
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment fragmentSelected = null;

                switch (item.getItemId()){

                    case R.id.categories:
                        fragmentSelected = CategoryFragment.newInstance();
                        break;
                }
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, fragmentSelected);
                fragmentTransaction.commit();
                return true;
            }
        });

        setDefaultFragment();

        bottomNavigationView = findViewById((R.id.navigation));
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                switch (item.getItemId()){

                    case R.id.chat:
                        startActivity(new Intent(MainActivity.this, ChatSection.class));
                        break;
                }
                return true;
            }
        });

    }

        //This will replace the frameLayout to the customized one
    private void setDefaultFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, CategoryFragment.newInstance());
        fragmentTransaction.commit();
    }

    //UpperMenu
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu, menu);
            return true;
        }


        //When the user selects an item from the options menu (including action items in the app bar), the system calls your activity's onOptionsItemSelected() method
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.donations:
                startActivity(new Intent(MainActivity.this, Donations.class));
                break;
            case R.id.contact:
                startActivity(new Intent(MainActivity.this, Contact.class));
                break;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, Login.class));
                finish();

                return true;
        }
        return false;
    }



}

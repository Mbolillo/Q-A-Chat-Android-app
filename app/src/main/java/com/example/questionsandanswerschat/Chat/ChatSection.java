package com.example.questionsandanswerschat.Chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.questionsandanswerschat.Chat.Fragments.FragmentChat;
import com.example.questionsandanswerschat.Chat.Fragments.UserProfileFragment;
import com.example.questionsandanswerschat.Chat.Fragments.UsersFragment;
import com.example.questionsandanswerschat.Donations;
import com.example.questionsandanswerschat.Login;
import com.example.questionsandanswerschat.Chat.Model.UserInfo;
import com.example.questionsandanswerschat.MainActivity;
import com.example.questionsandanswerschat.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatSection extends AppCompatActivity {


    private CircleImageView image_profile;
    private TextView username;

    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_section);


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
                    Glide.with(ChatSection.this).load(userInfo.getImageUrl()).into(image_profile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        tabLayout = findViewById(R.id.tabLayout);
        viewPager= findViewById(R.id.viewPager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPagerAdapter.AddFragment(new FragmentChat(), "Chat");
        viewPagerAdapter.AddFragment(new UsersFragment(),"Users");
        viewPagerAdapter.AddFragment(new UserProfileFragment(),"Profile");

        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);


    }
    //Upper menu
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
                startActivity(new Intent(ChatSection.this, Donations.class));
                break;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ChatSection.this, Login.class));
                finish();

                return true;
        }

        return false;
    }

    ////////////////////////////////////////////////////////////VIDEO4
    class ViewPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        ViewPagerAdapter(FragmentManager fragmentManager){
            super(fragmentManager);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void AddFragment(Fragment fragment, String title){
            fragments.add(fragment);
            titles.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }
    ///////////////////////////////////////////////////////////////////
}

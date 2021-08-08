package com.example.questionsandanswerschat.Chat.Fragments;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.questionsandanswerschat.Chat.Model.UserInfo;
import com.example.questionsandanswerschat.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class UserProfileFragment extends Fragment {

    private TextView userName;
    private CircleImageView profileImage;

    private DatabaseReference reference;
    private FirebaseUser firebaseUser;

    //This will allow to upload images to user profile
    private StorageReference storageReference;
    private static final int imageRequest = 1;
    private Uri uriImage;
    private StorageTask<UploadTask.TaskSnapshot> uploadTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        userName = view.findViewById(R.id.et_username);
        profileImage = view.findViewById(R.id.image_profile);

        storageReference = FirebaseStorage.getInstance().getReference("uploads");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(isAdded()) {
                    UserInfo userInfo = dataSnapshot.getValue(UserInfo.class);
                    userName.setText(userInfo.getUsername());

                    if (userInfo.getImageUrl().equals("default")) {
                        profileImage.setImageResource(R.mipmap.ic_launcher);
                    } else {
                        Glide.with(getContext()).load(userInfo.getImageUrl()).into(profileImage);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage();
                
            }
        });

        return view;
    }

    private void openImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, imageRequest);
    }

    private String getExtensionFile(Uri uri){
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage(){

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Uploading");
        progressDialog.show();

        if(uriImage != null){
            final StorageReference referenceFile = storageReference.child(System.currentTimeMillis() + getExtensionFile(uriImage));
        uploadTask = referenceFile.putFile(uriImage);
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if(!task.isSuccessful()){
                    throw task.getException();
                }
                return referenceFile.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            Uri downloadUri = task.getResult();
                            String mUri = downloadUri.toString();

                            reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

                            HashMap<String, Object> map = new HashMap<>();
                            map.put("imageUrl", mUri);
                            reference.updateChildren(map);

                            progressDialog.dismiss();

                        }else{
                            Toast.makeText(getContext(), "Upload Failed", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
            });
        }else{
            Toast.makeText(getContext(), "Image not selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == imageRequest && resultCode == RESULT_OK && data != null && data.getData() != null){

            uriImage = data.getData();

            if (uploadTask != null && uploadTask.isInProgress()){

                Toast.makeText(getContext(), "Upload in progress", Toast.LENGTH_SHORT).show();
            }else{

                uploadImage();
            }
        }
    }
}


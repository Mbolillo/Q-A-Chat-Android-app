package com.example.questionsandanswerschat.Game.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.questionsandanswerschat.Game.GlobalVariables.GlobalVariables;
import com.example.questionsandanswerschat.Game.Interface.ItemClickListener;
import com.example.questionsandanswerschat.Game.Model.Category;
import com.example.questionsandanswerschat.Game.Play;
import com.example.questionsandanswerschat.Game.ViewHolder.CategoryViewHolder;
import com.example.questionsandanswerschat.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


public class CategoryFragment extends Fragment {

    private RecyclerView categoryList;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseRecyclerAdapter<Category, CategoryViewHolder> recyclerAdapter;
    View myFragment;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;



    public static CategoryFragment newInstance() {
        CategoryFragment categoryFragment = new CategoryFragment();
        return categoryFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Category");


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        myFragment = inflater.inflate(R.layout.fragment_category, container, false);

        categoryList = myFragment.findViewById(R.id.categoryList);
        categoryList.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(container.getContext());
        categoryList.setLayoutManager(layoutManager);

        loadCategories();

        return myFragment;

    }

    //This will fetch the data from firebase in order to display images and text into recycler view
    private void loadCategories() {

        recyclerAdapter = new FirebaseRecyclerAdapter<Category, CategoryViewHolder>(Category.class,R.layout.category_list,CategoryViewHolder.class,databaseReference) {
            @Override
            protected void populateViewHolder(CategoryViewHolder viewHolder, final Category model, int i) {

                viewHolder.categoryName.setText(model.getName());
                Picasso.get().load(model.getImage()).into(viewHolder.categoryImage);

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(getActivity(), String.format("%s | %s",recyclerAdapter.getRef(position).getKey(),model.getName()), Toast.LENGTH_SHORT).show();
                        Intent playGame = new Intent(getActivity(), Play.class);
                        GlobalVariables.IdCategory = recyclerAdapter.getRef(position).getKey();
                        startActivity(playGame);
                    }
                });

            }
        };
        recyclerAdapter.notifyDataSetChanged();
        categoryList.setAdapter(recyclerAdapter);




    }


}
    

package com.example.assignment.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.assignment.R;
import com.example.assignment.adapter.PostPhotoAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class DetailPostFragment extends Fragment {
    TextView title, description, address;
    String postTitle;
    private List<String> postPhotoList;
    private RecyclerView recyclerView;
    private PostPhotoAdapter postPhotoAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_detail_post, container, false);
        title = root.findViewById(R.id.Post_detail_title);
        description = root.findViewById(R.id.Post_detail_description);
        address = root.findViewById(R.id.Post_detail_address);
        postTitle = getArguments().getString("postTitle");
        return root;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Posts").child(postTitle).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    title.setText(postTitle);
                    description.setText(snapshot.child("description").getValue().toString());
                    address.setText(snapshot.child("address").getValue().toString());
                } catch (Exception e) {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        recyclerView = view.findViewById(R.id.Post_detail_images);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        postPhotoList = new ArrayList<String>();
        postPhotoAdapter = new PostPhotoAdapter(getContext(), postPhotoList);
        recyclerView.setAdapter(postPhotoAdapter);
        readPostImages();
        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final NavController navController = Navigation.findNavController(v);
                Bundle bundle = new Bundle();
                bundle.putString("address", address.getText().toString());
                navController.navigate(R.id.action_navigation_detailPost_to_navigation_map, bundle);
            }
        });
    }


    public void readPostImages() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Posts").child(postTitle).child("images").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postPhotoList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    String post = snapshot1.child("postImage").getValue(String.class);
                    postPhotoList.add(post);
                }
                postPhotoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
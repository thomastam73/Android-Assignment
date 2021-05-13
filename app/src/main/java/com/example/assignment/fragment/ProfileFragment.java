package com.example.assignment.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.assignment.LoginSession;
import com.example.assignment.R;
import com.example.assignment.adapter.PostAdapter;
import com.example.assignment.adapter.UserPostAdapter;
import com.example.assignment.model.Post;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {
    Button logout, edit;
    de.hdodenhof.circleimageview.CircleImageView profile_icon;
    TextView profile_name;
    String userID;
    private RecyclerView recyclerView;
    private UserPostAdapter postAdapter;
    private List<Post> postList;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        logout = root.findViewById(R.id.logout);
        edit = root.findViewById(R.id.EditProfile);
        profile_icon = root.findViewById(R.id.profile_icon);
        profile_name = root.findViewById(R.id.userName);
        userID = LoginSession.getUserID(getContext());
        getUserInf();
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginSession.clearData(getContext());
            }
        });


        return root;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final NavController navController = Navigation.findNavController(view);
        recyclerView = view.findViewById(R.id.ownPostList);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        postList = new ArrayList<>();
        postAdapter = new UserPostAdapter(getContext(), postList);
        recyclerView.setAdapter(postAdapter);
        readPost();

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_navigation_profile_to_navigation_editProfile);
            }
        });
    }

    public void getUserInf() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Users").child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    Glide.with(getView()).load(snapshot.child("icon").getValue()).into(profile_icon);
                    profile_name.setText(snapshot.child("name").getValue().toString());
                } catch (Exception e) {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void readPost() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    postList.clear();
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        String postUserID = snapshot1.child("author").getValue().toString();
                        String userID = LoginSession.getUserID(getContext());
                        if (postUserID.equals(userID)) {
                            Post post = snapshot1.getValue(Post.class);
                            postList.add(post);
                        }
                    }
                    postAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
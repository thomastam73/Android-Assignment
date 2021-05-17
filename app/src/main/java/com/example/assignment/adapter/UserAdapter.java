package com.example.assignment.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.assignment.service.LoginSession;
import com.example.assignment.model.User;
import com.example.assignment.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    public Context context;
    public List<User> userList;

    public UserAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView icon;
        public TextView name, email;
        public Button follow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.user_icon);
            name = itemView.findViewById(R.id.search_user_name);
            email = itemView.findViewById(R.id.search_user_email);
            follow = itemView.findViewById(R.id.btn_follow);
        }
    }

    private void addNotification(String userid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(userid);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userid", LoginSession.getUserID(context));
        hashMap.put("text", "is following you");
        hashMap.put("postTitle", "");
        reference.push().setValue(hashMap);
    }

    private void isFollowing(String userId, Button button) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Follow").child(LoginSession.getUserID(context)).child("following");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(userId).exists()) {
                    button.setText("following");
                } else {
                    button.setText("follow");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = userList.get(position);
        Glide.with(context).load(user.getIcon()).into(holder.icon);
        holder.name.setText(user.getName());
        holder.email.setText(user.getEmail());
        holder.follow.setVisibility(View.VISIBLE);
        isFollowing(user.getId(), holder.follow);
        if (user.getId().equals(LoginSession.getUserID(context))) {
            holder.follow.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("userID", user.getId());
                Navigation.findNavController(v).navigate(R.id.action_navigation_search_to_navigation_other_user_profile, bundle);
            }
        });

        holder.follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference following = FirebaseDatabase.getInstance().getReference().child("Users").child(LoginSession.getUserID(context)).child("following");
                DatabaseReference follower = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getId()).child("follower");

                if (holder.follow.getText().toString().equals("follow")) {
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(LoginSession.getUserID(context))
                            .child("following").child(user.getId()).setValue(true);
                    following.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Long totalLikes = (Long) dataSnapshot.getValue();
                            following.setValue(totalLikes + 1);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    FirebaseDatabase.getInstance().getReference().child("Follow").child(user.getId())
                            .child("follower").child(LoginSession.getUserID(context)).setValue(true);
                    follower.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Long totalLikes = (Long) dataSnapshot.getValue();
                            follower.setValue(totalLikes + 1);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    addNotification(user.getId());
                } else {
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(LoginSession.getUserID(context))
                            .child("following").child(user.getId()).removeValue();
                    following.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Long totalLikes = (Long) dataSnapshot.getValue();
                            following.setValue(totalLikes - 1);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(user.getId())
                            .child("follower").child(LoginSession.getUserID(context)).removeValue();
                    follower.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Long totalLikes = (Long) dataSnapshot.getValue();
                            follower.setValue(totalLikes - 1);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
    }

}

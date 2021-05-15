package com.example.assignment.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.assignment.LoginSession;
import com.example.assignment.model.Post;
import com.example.assignment.model.User;
import com.example.assignment.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    public Context context;
    public List<Post> postList;

    public PostAdapter(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView cover, user_icon;
        public TextView username, title, postDate;
        public View layout;
        public Button like;
        private float x1, x2, y1, y2;


        @SuppressLint("ClickableViewAccessibility")
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    final NavController navController = Navigation.findNavController(v);
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            x1 = event.getX();
                            y1 = event.getY();
                            break;
                        case MotionEvent.ACTION_UP:
                            x2 = event.getX();
                            y2 = event.getY();
                            if (x1 < x2) {
                                navController.navigate(R.id.action_navigation_post_to_navigation_land_mark);
                            } else if (x1 > x2) {
                                navController.navigate(R.id.action_navigation_post_to_navigation_search);
                            }
                            break;
                    }
                    return true;
                }
            });

            username = itemView.findViewById(R.id.user_name);
            user_icon = itemView.findViewById(R.id.user_icon);
            cover = itemView.findViewById(R.id.own_post_cover);
            title = itemView.findViewById(R.id.own_post_title);
            postDate = itemView.findViewById(R.id.own_post_date);
            like = itemView.findViewById(R.id.btn_like);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = postList.get(position);
        String postUserID = post.getAuthor();
        Glide.with(context).load(post.getCover()).into(holder.cover);
        holder.title.setText(post.getTitle());
        holder.postDate.setText(post.getPostDate());
        UserInfo(holder.user_icon, holder.username, postUserID);
        isLiked(post.getTitle(), holder.like);
        holder.cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final NavController navController = Navigation.findNavController(v);
                Bundle bundle = new Bundle();
                bundle.putString("postTitle", post.getTitle());
                navController.navigate(R.id.action_navigation_post_to_navigation_detailPost, bundle);
            }
        });

        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference liked = FirebaseDatabase.getInstance().getReference().child("Posts").child(post.getTitle()).child("like");
                if (holder.like.getText().toString().equals("Like")) {
                    FirebaseDatabase.getInstance().getReference().child("Like").child(post.getTitle())
                            .child("liked").child(LoginSession.getUserID(context)).setValue(true);
                    liked.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Long totalLikes = (Long) dataSnapshot.getValue();
                            liked.setValue(totalLikes + 1);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {
                    FirebaseDatabase.getInstance().getReference().child("Like").child(post.getTitle())
                            .child("liked").child(LoginSession.getUserID(context)).removeValue();
                    liked.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Long totalLikes = (Long) dataSnapshot.getValue();
                            liked.setValue(totalLikes - 1);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
    }

    private void isLiked(String postTitle, Button button) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Like").child(postTitle).child("liked");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(LoginSession.getUserID(context)).exists()) {
                    button.setText("Liked");
                } else {
                    button.setText("Like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void UserInfo(ImageView user_icon, TextView username, String userID) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userID);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                Glide.with(context).load(user.getIcon()).into(user_icon);
                user_icon.getLayoutParams().width = 100;
                user_icon.getLayoutParams().height = 100;
                username.setText(user.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}

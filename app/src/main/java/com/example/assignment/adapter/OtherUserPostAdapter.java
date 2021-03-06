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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.assignment.model.Post;
import com.example.assignment.model.User;
import com.example.assignment.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class OtherUserPostAdapter extends RecyclerView.Adapter<OtherUserPostAdapter.ViewHolder> {
    public Context context;
    public List<Post> postList;

    public OtherUserPostAdapter(Context context, List<Post> postList) {
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
        private float x1, x2, y1, y2;


        @SuppressLint("ClickableViewAccessibility")
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.user_name);
            user_icon = itemView.findViewById(R.id.user_icon);
            cover = itemView.findViewById(R.id.own_post_cover);
            title = itemView.findViewById(R.id.own_post_title);
            postDate = itemView.findViewById(R.id.own_post_date);
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
        holder.cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final NavController navController = Navigation.findNavController(v);
                Bundle bundle = new Bundle();
                bundle.putString("postTitle", post.getTitle());
                navController.navigate(R.id.action_navigation_other_user_profile_to_navigation_detailPost, bundle);
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

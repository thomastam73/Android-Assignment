package com.example.assignment.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
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
import com.example.assignment.R;

import java.util.List;

public class UserPostAdapter extends RecyclerView.Adapter<UserPostAdapter.ViewHolder> {
    public Context context;
    public List<Post> postList;

    public UserPostAdapter(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_post_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView cover;
        public TextView title, postDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cover = itemView.findViewById(R.id.own_post_cover);
            title = itemView.findViewById(R.id.own_post_title);
            postDate = itemView.findViewById(R.id.own_post_date);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = postList.get(position);

        Glide.with(context).load(post.getCover()).into(holder.cover);
        holder.title.setText(post.getTitle());
        holder.postDate.setText(post.getPostDate());
        holder.cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final NavController navController = Navigation.findNavController(v);
                Bundle bundle = new Bundle();
                bundle.putString("postTitle", post.getTitle());
                navController.navigate(R.id.action_navigation_profile_to_navigation_detailPost, bundle);
            }
        });


    }

}

package com.example.assignment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.assignment.R;

import java.util.List;

public class PostPhotoAdapter extends RecyclerView.Adapter<PostPhotoAdapter.ViewHolder> {
    public Context context;
    public List<String> postPhotoList;

    public PostPhotoAdapter(Context context, List<String> postPhotoList) {
        this.context = context;
        this.postPhotoList = postPhotoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_photo_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public int getItemCount() {
        return postPhotoList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.post_images);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String post = postPhotoList.get(position);
        Glide.with(context).load(post).into(holder.image);
    }
}

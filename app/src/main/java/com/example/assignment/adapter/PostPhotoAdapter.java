package com.example.assignment.adapter;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import com.example.assignment.DisplayToast;
import com.example.assignment.model.Post;
import com.example.assignment.model.User;
import com.example.assignment.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
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

package com.example.assignment.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.assignment.service.DisplayToast;
import com.example.assignment.service.LoginSession;
import com.example.assignment.R;
import com.example.assignment.model.Post;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class AddFragment extends Fragment {
    private static final int PICK_IMAGES = 1;
    private static final int PICK_IMAGE = 2;
    Button add;
    ImageButton cover, images;
    EditText title, address, description;
    ArrayList<Uri> imageList = new ArrayList<Uri>();
    private Uri imagesUri;
    private Uri imageUri;
    private Uri coverUri;
    private String currentDate;
    private int uploadCount = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_add, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final NavController navController = Navigation.findNavController(view);
        add = view.findViewById(R.id.AddPostSubmit);
        cover = view.findViewById(R.id.AddPostCover);
        images = view.findViewById(R.id.AddPostImage);
        title = view.findViewById(R.id.AddPostTitleText);
        address = view.findViewById(R.id.AddPostAddressText);
        description = view.findViewById(R.id.AddPostDescription);
        String userID = LoginSession.getUserID(getContext());
        Date c = Calendar.getInstance().getTime();

        @SuppressLint("SimpleDateFormat") SimpleDateFormat curFormatter = new SimpleDateFormat("dd/MM/yyyy");
        currentDate = curFormatter.format(c);

        cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE);
            }
        });

        images.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, PICK_IMAGES);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StorageReference PostImages = FirebaseStorage.getInstance().getReference().child("PostImage").child(title.getText().toString());
                StorageReference PostCover = FirebaseStorage.getInstance().getReference().child("PostCover").child(title.getText().toString());
                DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Posts").child(title.getText().toString());
                db.setValue(new Post(title.getText().toString(), address.getText().toString(), description.getText().toString(), userID, currentDate,0));
                StorageReference coverName = PostCover.child("Cover" + coverUri.getLastPathSegment());
                coverName.putFile(coverUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        coverName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String url = String.valueOf(uri);
                                CoverSaveToDB(url, db);
                            }
                        });
                    }
                });
                for (uploadCount = 0; uploadCount < imageList.size(); uploadCount++) {
                    Uri IndividualImage = imageList.get(uploadCount);
                    StorageReference imageName = PostImages.child("Image" + IndividualImage.getLastPathSegment());
                    imageName.putFile(IndividualImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String url = String.valueOf(uri);
                                    ImageSaveToDB(url, db);
                                }
                            });
                        }
                    });
                }
                navController.navigate(R.id.action_navigation_add_to_navigation_post);
                DisplayToast.displayToast("Upload Success", getContext());
            }
        });
    }


    private void ImageSaveToDB(String url, DatabaseReference db) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("postImage", url);
        db.child("images").push().setValue(hashMap);
    }

    private void CoverSaveToDB(String url, DatabaseReference db) {
        db.child("cover").setValue(url);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGES) {
            if (resultCode == RESULT_OK) {
                if (data.getClipData() != null) {
                    int countClipData = data.getClipData().getItemCount();
                    int currentImageSelect = 0;
                    imageUri = data.getClipData().getItemAt(0).getUri();
                    images.setImageURI(imageUri);
                    while (currentImageSelect < countClipData) {
                        imagesUri = data.getClipData().getItemAt(currentImageSelect).getUri();
                        imageList.add(imagesUri);
                        currentImageSelect += 1;
                    }
                } else {

                }
            }
        } else if (requestCode == PICK_IMAGE) {
            if (resultCode == RESULT_OK) {
                coverUri = Objects.requireNonNull(data).getData();
                cover.setImageURI(coverUri);
            }
        }
    }
}
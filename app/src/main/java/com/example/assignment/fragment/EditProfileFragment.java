package com.example.assignment.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.example.assignment.DisplayToast;
import com.example.assignment.LoginSession;
import com.example.assignment.R;
import com.example.assignment.model.Post;
import com.example.assignment.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;


public class EditProfileFragment extends Fragment {
    String userID;
    de.hdodenhof.circleimageview.CircleImageView icon;
    EditText name, age, description;
    Button submit;
    Uri iconUri;
    StorageReference iconName;
    private static final int PERMISSION_FILE = 3;
    private static final int ACCESS_FILE = 4;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        userID = LoginSession.getUserID(getContext());
        icon = root.findViewById(R.id.EditIcon);
        name = root.findViewById(R.id.EditName);
        age = root.findViewById(R.id.EditAge);
        description = root.findViewById(R.id.EditDescription);
        submit = root.findViewById(R.id.EditSubmit);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final NavController navController = Navigation.findNavController(view);
        getUserInf();
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_FILE);
                } else {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_PICK);
                    startActivityForResult(Intent.createChooser(intent, "image pick"), ACCESS_FILE);
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
                db.child("name").setValue(name.getText().toString());
                db.child("age").setValue(Integer.parseInt(String.valueOf(age.getText())));
                db.child("description").setValue(description.getText().toString());
                if (iconUri != null) {
                    StorageReference userIcon = FirebaseStorage.getInstance().getReference().child("Icon").child(userID);
                    iconName = userIcon.child("iconImage_" + iconUri.getLastPathSegment());
                    iconName.putFile(iconUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            iconName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    db.child("icon").setValue(uri.toString());
                                }
                            });
                        }
                    });
                }
                DisplayToast.displayToast("Update success",getContext());
                navController.navigate(R.id.action_navigation_editProfile_to_navigation_profile);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACCESS_FILE && resultCode == Activity.RESULT_OK && data != null & data.getData() != null) {
            Uri uri = data.getData();
            CropImage.activity(uri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setCropShape(CropImageView.CropShape.OVAL)
                    .setActivityTitle("Crop Image")
                    .setFixAspectRatio(true)
                    .setCropMenuCropButtonTitle("Done")
                    .start(getContext(), this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = result.getUri();
                Log.d("uri", uri.toString());
                iconUri = uri;
                icon.setImageURI(uri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                DisplayToast.displayToast(error.getMessage(), getContext());
            }
        }
    }

    public void getUserInf() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Users").child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    Glide.with(getView()).load(snapshot.child("icon").getValue()).into(icon);
                    icon.getLayoutParams().height = 300;
                    icon.getLayoutParams().width = 300;
                    name.setText(snapshot.child("name").getValue().toString());
                    age.setText(snapshot.child("age").getValue().toString());
                    description.setText(snapshot.child("description").getValue().toString());
                }catch (Exception e){}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
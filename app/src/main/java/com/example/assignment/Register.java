package com.example.assignment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.assignment.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Objects;

public class Register extends AppCompatActivity {

    Activity context = this;
    Button submit;
    ImageButton icon;
    EditText email, password, repeatPassword;
    FirebaseAuth mAuth;
    Uri iconUri;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        submit = findViewById(R.id.register_submit);
        email = findViewById(R.id.register_edit_email);
        password = findViewById(R.id.register_edit_password);
        repeatPassword = findViewById(R.id.register_edit_repeat_password);
        icon = findViewById(R.id.register_icon);
        mAuth = FirebaseAuth.getInstance();
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Users");

        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            db.child(mAuth.getUid()).setValue(new User(mAuth.getUid(), "","", email.getText().toString(), "", 0, 0, 0));
                            StorageReference UserIcon = FirebaseStorage.getInstance().getReference().child("Icon").child(mAuth.getUid());
                            StorageReference coverName = UserIcon.child("IconImage" + iconUri.getLastPathSegment());
                            coverName.putFile(iconUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    coverName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String url = String.valueOf(uri);
                                            IconSaveToDB(url, db);
                                        }
                                    });
                                }
                            });
                            Intent intent = new Intent();
                            intent.setClass(Register.this, Login.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                            DisplayToast.displayToast("Success", context);
                        } else {
                            DisplayToast.displayToast("Register fail", context);
                        }
                    }
                });
            }
        });
    }

    private void IconSaveToDB(String url, DatabaseReference db) {
        HashMap<String, String> hashMap1 = new HashMap<>();
        hashMap1.put("coverImage", url);
        db.child(mAuth.getUid()).child("Icon").push().setValue(hashMap1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1){
            if (resultCode == RESULT_OK) {
                iconUri = Objects.requireNonNull(data).getData();
                icon.setImageURI(iconUri);
            }
        }
    }
}
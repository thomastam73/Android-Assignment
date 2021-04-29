package com.example.assignment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {

    Activity context = this;
    Button submit;
    EditText email, password, repeatPassword;
    FirebaseAuth mAuth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        submit = findViewById(R.id.register_submit);
        email = findViewById(R.id.register_edit_email);
        password = findViewById(R.id.register_edit_password);
        repeatPassword = findViewById(R.id.register_edit_repeat_password);

        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.message, (ViewGroup) findViewById(R.id.custom_toast));
        TextView message = layout.findViewById(R.id.toast_message);
        final Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 10);
        toast.setDuration(Toast.LENGTH_SHORT);


        mAuth = FirebaseAuth.getInstance();


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent();
                            intent.setClass(Register.this, Login.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                            message.setText("Success");
                        } else {
                            message.setText("Register fail");
                        }
                        toast.setView(layout);
                        toast.show();
                    }
                });
            }
        });
    }
}
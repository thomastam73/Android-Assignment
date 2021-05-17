package com.example.assignment.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.assignment.MainActivity;
import com.example.assignment.R;
import com.example.assignment.service.DisplayToast;
import com.example.assignment.service.FirebaseInstanceIdService;
import com.example.assignment.service.LoginSession;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;

import java.util.concurrent.Executor;

public class Login extends AppCompatActivity {
    Activity context = this;
    Button submit, register;
    EditText email, password;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        submit = findViewById(R.id.login_submit);
        register = findViewById(R.id.login_register);
        email = findViewById(R.id.login_edit_email);
        password = findViewById(R.id.login_edit_password);
        mAuth = FirebaseAuth.getInstance();


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            LoginSession.setDataLogin(Login.this, true);
                            FirebaseUser user = mAuth.getCurrentUser();
                            LoginSession.setUserID(Login.this, user.getUid());
                            LoginSession.setUserEmail(Login.this, user.getEmail());
                            Intent intent = new Intent();
                            intent.setClass(Login.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                            DisplayToast.displayToast("Hello, " + user.getEmail(), context);
                        } else {
                            DisplayToast.displayToast("Oh, " + task.getException(), context);
                        }
                    }
                });

            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Login.this, Register.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        BiometricManager biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate()) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                DisplayToast.displayToast("Have finger print", this);
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                DisplayToast.displayToast("Sensor not available", this);
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                DisplayToast.displayToast("No sensor", this);
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                DisplayToast.displayToast("No finger print", this);
                break;
        }

        Executor executor = ContextCompat.getMainExecutor(this);
        BiometricPrompt biometricPrompt = new BiometricPrompt(Login.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                startActivity(new Intent(Login.this, MainActivity.class));
                DisplayToast.displayToast("Hello, " + LoginSession.getUserEmail(context), context);
                finish();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });

        final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Login")
                .setDescription("Use finger print to login")
                .setNegativeButtonText("Cancel")
                .build();
        if (LoginSession.getDataLogin(this)) {
            biometricPrompt.authenticate(promptInfo);

        }
    }
}
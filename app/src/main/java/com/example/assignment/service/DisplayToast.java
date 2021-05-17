package com.example.assignment.service;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.assignment.R;

public class DisplayToast {
    public DisplayToast() {
    }

    public static void displayToast(String text, Context context) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.message, null);
        TextView message = layout.findViewById(R.id.toast_message);
        final Toast toast = new Toast(context.getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 10);
        toast.setDuration(Toast.LENGTH_SHORT);
        message.setText(text);
        toast.setView(layout);
        toast.show();
    }

}

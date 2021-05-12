package com.example.assignment.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.assignment.LoginSession;
import com.example.assignment.R;


public class EditProfileFragment extends Fragment {
    private String userID;
    ImageView icon;
    EditText name, age, description;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        userID = LoginSession.getUserID(getContext());
        icon = root.findViewById(R.id.)
        return root;
    }


}
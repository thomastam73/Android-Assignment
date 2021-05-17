package com.example.assignment.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.assignment.R;


public class LandMarkInfFragment extends Fragment {
    TextView name;
    TextView x, y;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_land_mark_inf, container, false);
        name = root.findViewById(R.id.LocationName);
        x = root.findViewById(R.id.X);
        y = root.findViewById(R.id.Y);
        name.setText(getArguments().getString("landmarkName"));
        x.setText((int) getArguments().getDouble("latitude"));
        x.setText((int) getArguments().getDouble("longitude"));

        return root;
    }
}
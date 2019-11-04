package com.yoseph.re_mind.ui.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yoseph.re_mind.R;
import com.yoseph.re_mind.data.User;

public class OverviewFragment extends Fragment {


    public OverviewFragment() {
        // Required empty public constructor.
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Get a Firebase Realtime Database reference.
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference root = database.getReference();

        User user = new User(1, "Yehyun", "Ryu");
        root.child("users").child(String.valueOf(user.id)).setValue(user);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_overview, container, false);
    }

}

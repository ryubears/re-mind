package com.yoseph.re_mind.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.yoseph.re_mind.R;
import com.yoseph.re_mind.data.TaskContent;

public class BottomSheetFragment extends BottomSheetDialogFragment {

    private ImageButton addItemButton;
    private EditText addItemEditText;

    private LinearLayout noAlertLayout;
    private LinearLayout timeAlertLayout;
    private LinearLayout locationAlertLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bottom_sheet, container, false);

        addItemButton   = view.findViewById(R.id.add_reminder_button);
        addItemEditText = view.findViewById(R.id.add_reminder_edittext);

        noAlertLayout = view.findViewById(R.id.no_alert_layout);
        timeAlertLayout = view.findViewById(R.id.time_alert_layout);
        locationAlertLayout = view.findViewById(R.id.location_alert_layout);

        addItemButton.setOnClickListener(view1 -> {
            // add the addItemEditText to the Task content
            String newTask = addItemEditText.getText().toString();
            if (newTask.isEmpty()) {
                return;
            } else {
                TaskContent.addItem(new TaskContent.TaskItem("6",newTask,  "", 0));
            }
        });

        noAlertLayout.setOnClickListener(view12 -> {
            noAlertLayout.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bottom_sheet_item_background));
            timeAlertLayout.setBackground(null);
            locationAlertLayout.setBackground(null);
        });

        timeAlertLayout.setOnClickListener(view13 -> {
            noAlertLayout.setBackground(null);
            timeAlertLayout.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bottom_sheet_item_background));
            locationAlertLayout.setBackground(null);
        });

        locationAlertLayout.setOnClickListener(view14 -> {
            noAlertLayout.setBackground(null);
            timeAlertLayout.setBackground(null);
            locationAlertLayout.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bottom_sheet_item_background));
        });

        return view;
    }





}

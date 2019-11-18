package com.yoseph.re_mind.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.yoseph.re_mind.R;
import com.yoseph.re_mind.data.TaskContent;
import com.yoseph.re_mind.ui.interfaces.CallBackListener;

public class BottomSheetFragment extends BottomSheetDialogFragment {

    private int id= 100;
    private int alertType = TaskContent.TYPE_GENERAL;

    private ImageButton addItemButton;
    private EditText addItemEditText;
    private EditText descriptionEditText;

    private LinearLayout noAlertLayout;
    private LinearLayout timeAlertLayout;
    private LinearLayout locationAlertLayout;

    private CallBackListener callBackListener;

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
        descriptionEditText = view.findViewById(R.id.add_reminder_description_edittext);

        noAlertLayout = view.findViewById(R.id.no_alert_layout);
        timeAlertLayout = view.findViewById(R.id.time_alert_layout);
        locationAlertLayout = view.findViewById(R.id.location_alert_layout);

        addItemButton.setOnClickListener(view1 -> {
            // add the addItemEditText to the Task content
            int id = 100 + (int)(Math.random() * ((1000 - 100) + 1));
            String newTask = addItemEditText.getText().toString();
            String details = descriptionEditText.getText().toString();
            TaskContent.TaskItem taskItem = new TaskContent.TaskItem(String.valueOf(id), newTask, "No Description", alertType,null);
            if (newTask.isEmpty()) {
                return;
            } else {
                if (details != null && !details.isEmpty()) {
                    taskItem.details = details;
                }
                TaskContent.addItem(taskItem);

                if(callBackListener != null) {
                    callBackListener.onCallBack();
                }

                this.dismiss();
            }
        });

        noAlertLayout.setOnClickListener(view12 -> {
            noAlertLayout.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bottom_sheet_item_background));
            timeAlertLayout.setBackground(null);
            locationAlertLayout.setBackground(null);

            alertType = TaskContent.TYPE_GENERAL;
        });

        timeAlertLayout.setOnClickListener(view13 -> {
            noAlertLayout.setBackground(null);
            timeAlertLayout.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bottom_sheet_item_background));
            locationAlertLayout.setBackground(null);

            alertType = TaskContent.TYPE_GENERAL;
        });

        locationAlertLayout.setOnClickListener(view14 -> {
            noAlertLayout.setBackground(null);
            timeAlertLayout.setBackground(null);
            locationAlertLayout.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bottom_sheet_item_background));

            alertType = TaskContent.TYPE_LOCATION;
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getActivity() instanceof CallBackListener) {
            callBackListener = (CallBackListener) getActivity();
        }
    }
}

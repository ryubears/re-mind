package com.yoseph.re_mind.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.yoseph.re_mind.R;
import com.yoseph.re_mind.data.TaskContent;

public class BottomSheetFragment extends BottomSheetDialogFragment {

    private ImageButton addItemButton;
    private EditText addItemEditText;

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

        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // add the addItemEditText to the Task content
                String newTask = addItemEditText.getText().toString();
                if (newTask == null || newTask.isEmpty()) {
                    return;
                } else {
                    TaskContent.addItem(new TaskContent.TaskItem("6",newTask,  "", 0));
                }

                

            }
        });

        return view;
    }





}

package com.yoseph.re_mind.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.yoseph.re_mind.R;

/**
 * <p>A fragment that shows a list of items as a modal bottom sheet.</p>
 * <p>You can show this modal bottom sheet from your activity like this:</p>
 * <pre>
 *     TypeItemBottomSheetListDialogFragment.newInstance(30).show(getSupportFragmentManager(), "dialog");
 * </pre>
 */
public class TypeItemBottomSheetListDialogFragment extends BottomSheetDialogFragment {

    public static final String TEXT = "TEXT";

    private static final String ARG_NAME = "text_name";

    private String title;

    private EditText editView;

    // TODO: Customize parameters
    public static TypeItemBottomSheetListDialogFragment newInstance(String textName) {
        final TypeItemBottomSheetListDialogFragment fragment = new TypeItemBottomSheetListDialogFragment();
        final Bundle args = new Bundle();
        args.putString(ARG_NAME, textName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(ARG_NAME);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_typeitembottomsheet_list_dialog, container, false);

        editView = view.findViewById(R.id.dialog_text);
        editView.setHint(title);
        editView.setOnEditorActionListener((v1, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {

                Intent intent = new Intent();
                intent.putExtra(TEXT, editView.getText().toString());

                // pass intent to target fragment
                Fragment fragment = getTargetFragment();
                fragment.onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                dismiss();

                return true;
            }
            return false;
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        editView.post(() -> {
            editView.requestFocus();
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(editView, InputMethodManager.SHOW_IMPLICIT);
        });
    }

}

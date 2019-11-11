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

    private EditText edit;

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
        View v = inflater.inflate(R.layout.fragment_typeitembottomsheet_list_dialog, container, false);
        ((TextView) v.findViewById(R.id.dialog_title)).setText(title);

        edit = v.findViewById(R.id.dialog_text);
        edit.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
//                    submit_btn.performClick();

                    Intent intent = new Intent();
                    intent.putExtra(TEXT, edit.getText().toString());

                    // pass intent to target fragment
                    Fragment f = getTargetFragment();
                    f.onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                    dismiss();

                    return true;
                }
                return false;
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        edit.post(new Runnable() {
            @Override
            public void run() {
                edit.requestFocus();
                InputMethodManager imgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imgr.showSoftInput(edit, InputMethodManager.SHOW_IMPLICIT);

            }
        });
    }

}

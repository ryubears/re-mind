package com.yoseph.re_mind.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.yoseph.re_mind.R;
import com.yoseph.re_mind.ui.activities.TaskDetailActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailButtonFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetailButtonFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailButtonFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_TITLE = "title";
    private static final String ARG_ICON = "icon";
    private static final String ARG_VALUE = "value";
    private static final String ARG_ACTION = "action";

    private String title;
    private String value;
    private int icon;
    private int action;

    private TaskDetailActivity mListener;

    public DetailButtonFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param title Parameter 1.
     * @param icon Parameter 2.
     * @return A new instance of fragment DetailButtonFragment.
     */
    public static DetailButtonFragment newInstance(String title, String value, int icon, int action) {
        DetailButtonFragment fragment = new DetailButtonFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_VALUE, value);
        args.putInt(ARG_ICON, icon);
        args.putInt(ARG_ACTION, action);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(ARG_TITLE);
            value = getArguments().getString(ARG_VALUE);
            icon = getArguments().getInt(ARG_ICON);
            action = getArguments().getInt(ARG_ACTION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_detail_button, container, false);
        v.setOnClickListener(this);
        if (title != null) {
            ((TextView) v.findViewById(R.id.button_title)).setText(title);
        }
        if (value != null) {
            ((TextView) v.findViewById(R.id.button_value)).setText(value);
        }
        if (icon != 0) {
            ((ImageView) v.findViewById(R.id.button_icon)).setImageResource(icon);
        }
        return v;
    }


    public void onButtonPressed() {
        if (mListener != null) {
            mListener.onFragmentInteraction(action);
        }
    }

    public void setValueRender(String value) {
        ((TextView) this.getView().findViewById(R.id.button_value)).setText(value);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TaskDetailActivity) {
            mListener = (TaskDetailActivity) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must be run in TaskDetailActivity");
        }
    }

    @Override
    public void onClick(View v) {
        //do what you want to do when button is clicked
        onButtonPressed();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(int type);
    }

}

package com.yoseph.re_mind.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.yoseph.re_mind.R;
import com.yoseph.re_mind.data.TaskContent;
import com.yoseph.re_mind.ui.activities.TaskDetailActivity;

import java.util.Date;

/**
 * A fragment representing a single Task detail screen.
 */
public class TaskDetailFragment extends Fragment {

    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private TaskContent.TaskItem mItem;

    private DetailButtonFragment dueDateButton;
    private DetailButtonFragment setLocationButton;
    private DetailButtonFragment repeatButton;
    private DetailButtonFragment shareButton;
    private DetailButtonFragment setCategoryButton;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TaskDetailFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = TaskContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);

            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.title);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.task_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.task_detail)).setText(mItem.details);
        }

        if (savedInstanceState == null){
            dueDateButton = DetailButtonFragment.newInstance("Due Date", mItem.dueDate, R.drawable.event, TaskDetailActivity.SET_DATE);
            setLocationButton = DetailButtonFragment.newInstance("Set Location", mItem.location, R.drawable.location, TaskDetailActivity.SET_LOCATION);
            repeatButton = DetailButtonFragment.newInstance("Repeat", mItem.repeat, R.drawable.repeat, TaskDetailActivity.SET_REPEAT);
            shareButton = DetailButtonFragment.newInstance("Shared With", mItem.share, R.drawable.share, TaskDetailActivity.SET_SHARE);
            setCategoryButton = DetailButtonFragment.newInstance("Set Category", mItem.category, R.drawable.category, TaskDetailActivity.SET_CATEGORY);

            //add child fragment
            getChildFragmentManager()
                    .beginTransaction()
                    .add(R.id.actionButtons,  dueDateButton)
                    .add(R.id.actionButtons, setLocationButton)
                    .add(R.id.actionButtons, repeatButton)
                    .add(R.id.actionButtons, shareButton)
                    .add(R.id.actionButtons, setCategoryButton)
                    .commit();
        }


        return rootView;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == TaskDetailActivity.SET_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.DATE);
            mItem.setDueDate(date.toString());
            dueDateButton.setValueRender(mItem.dueDate);
        }

        if (requestCode == TaskDetailActivity.SET_CATEGORY) {
            String result = (String) data.getSerializableExtra(SetCategoryListDialogFragment.SELECTION);
            mItem.setCategory(result);
            setCategoryButton.setValueRender(result);
        }

        if (requestCode == TaskDetailActivity.SET_LOCATION) {
            String result = (String) data.getSerializableExtra(SetCategoryListDialogFragment.SELECTION);
            mItem.setLocation(result);
            setLocationButton.setValueRender(result);
        }

        if (requestCode == TaskDetailActivity.SET_REPEAT) {
            String result = (String) data.getSerializableExtra(SetCategoryListDialogFragment.SELECTION);
            mItem.setRepeat(result);
            repeatButton.setValueRender(result);
        }

        if (requestCode == TaskDetailActivity.SET_SHARE) {
            String result = (String) data.getSerializableExtra(DatePickerFragment.DATE);
            mItem.setShare(result);
            shareButton.setValueRender(result);
        }
    }
}

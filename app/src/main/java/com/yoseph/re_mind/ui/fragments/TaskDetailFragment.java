package com.yoseph.re_mind.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yoseph.re_mind.R;
import com.yoseph.re_mind.data.CategoryContent;
import com.yoseph.re_mind.data.TaskContent;
import com.yoseph.re_mind.ui.activities.TaskDetailActivity;
import com.yoseph.re_mind.ui.interfaces.TaskDetailCallBackListener;

import java.util.Date;
import java.util.List;

/**
 * A fragment representing a single Task detail screen.
 */
public class TaskDetailFragment extends Fragment implements TaskDetailCallBackListener {

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

    private ChipGroup chips;
    private RecyclerView subItemsViews;
    private TextView emptyView;

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

        emptyView = rootView.findViewById(R.id.empty_view);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.task_detail)).setText(mItem.details);
        }

        if (savedInstanceState == null){
            dueDateButton = DetailButtonFragment.newInstance("Due Date", mItem.dueDate, R.drawable.event, TaskDetailActivity.SET_DATE);
            setLocationButton = DetailButtonFragment.newInstance("Set Location", mItem.location, R.drawable.location, TaskDetailActivity.SET_LOCATION);
            repeatButton = DetailButtonFragment.newInstance("Repeat", mItem.repeat, R.drawable.repeat, TaskDetailActivity.SET_REPEAT);
            shareButton = DetailButtonFragment.newInstance("Shared With", mItem.share, R.drawable.share, TaskDetailActivity.SET_SHARE);
            setCategoryButton = DetailButtonFragment.newInstance("Set Category", mItem.category != null ?  mItem.category.title : "", R.drawable.category, TaskDetailActivity.SET_CATEGORY);

            //add child fragment
            getChildFragmentManager()
                    .beginTransaction()
                    .add(R.id.actionButtons,  dueDateButton)
                    .add(R.id.actionButtons, setLocationButton)
                    .add(R.id.actionButtons, repeatButton)
                    .add(R.id.actionButtons, shareButton)
                    .add(R.id.actionButtons, setCategoryButton)
                    .commit();

            subItemsViews = rootView.findViewById(R.id.sub_task_list);
            assert subItemsViews != null;
            setupRecyclerView(subItemsViews);

            chips = rootView.findViewById(R.id.quick_add_chips);
            for(int index = 0; index < chips.getChildCount(); index++) {
                View nextChild = chips.getChildAt(index);

                if (nextChild instanceof Chip) {
                    nextChild.setOnClickListener(view -> {
                        mItem.subList.add(((Chip) view).getText().toString());
                        chips.removeView(nextChild);
                        subItemsViews.getAdapter().notifyDataSetChanged();
                        onCallBack(null);
                    });
                }
            }
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
            if (data.hasExtra(SetCategoryListDialogFragment.CREATE_NEW)) {
                DialogFragment dialogFragment = TypeItemBottomSheetListDialogFragment.newInstance("Add new sub task");
                dialogFragment.setTargetFragment(this, TaskDetailActivity.SET_CATEGORY);
                dialogFragment.show(this.getFragmentManager(), "typeNewCategory");
            } else {

                String result = (String) data.getSerializableExtra(SetCategoryListDialogFragment.TEXT);

                for (CategoryContent.CategoryItem item: CategoryContent.CATEGORIES) {
                    if (item.title.equals(result)) {
                        mItem.setCategory(item);
                        setCategoryButton.setValueRender(result);
                        return;
                    }
                }

                CategoryContent.CategoryItem item = new CategoryContent.CategoryItem(result, R.drawable.category);
                CategoryContent.CATEGORIES.add(item);
                mItem.setCategory(item);

                setCategoryButton.setValueRender(result);
            }

        }

        if (requestCode == TaskDetailActivity.SET_LOCATION) {
            if (data.hasExtra(SetCategoryListDialogFragment.CREATE_NEW)) {
                DialogFragment dialogFragment = TypeItemBottomSheetListDialogFragment.newInstance("Search for a location");
                dialogFragment.setTargetFragment(this, TaskDetailActivity.SET_LOCATION);
                dialogFragment.show(this.getFragmentManager(), "typeNewLocation");
            } else {
                String result = (String) data.getSerializableExtra(SetCategoryListDialogFragment.TEXT);
                mItem.setLocation(result);
                setLocationButton.setValueRender(result);
            }
        }

        if (requestCode == TaskDetailActivity.SET_REPEAT) {
            String result = (String) data.getSerializableExtra(SetCategoryListDialogFragment.TEXT);
            mItem.setRepeat(result);
            repeatButton.setValueRender(result);
        }

        if (requestCode == TaskDetailActivity.SET_SHARE) {
            String result = (String) data.getSerializableExtra(TypeItemBottomSheetListDialogFragment.TEXT);
            mItem.setShare(result);
            shareButton.setValueRender(result);

            // Add reminder to shared list.
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference root = database.getReference("users");
            String[] resultSplit = result.split("@");
            root.child(resultSplit[0] + "/shared/0").setValue(mItem);
        }

        if (requestCode == TaskDetailActivity.ADD_SUB_TASK) {
            String result = (String) data.getSerializableExtra(TypeItemBottomSheetListDialogFragment.TEXT);
            mItem.subList.add(result);
            subItemsViews.getAdapter().notifyDataSetChanged();
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new TaskDetailFragment.SimpleItemRecyclerViewAdapter(mItem.subList, this));
    }

    @Override
    public void onCallBack(String title) {
        if (mItem.subList.size() == 0) {
            emptyView.setVisibility(View.VISIBLE);
            subItemsViews.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            subItemsViews.setVisibility(View.VISIBLE);
        }

        if (title != null) {
            Chip chip = new Chip(chips.getContext());
            chip.setText(title);
            chip.setClickable(true);
            chip.setCheckable(false);
            chip.setOnClickListener(view -> {
                mItem.subList.add(((Chip) view).getText().toString());
                chips.removeView(chip);
                subItemsViews.getAdapter().notifyDataSetChanged();
                onCallBack(null);
            });
            chips.addView(chip);
        }
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<TaskDetailFragment.SimpleItemRecyclerViewAdapter.ViewHolder> {
        private List<String> mValues;
        private TaskDetailCallBackListener callBackListener;
//        private final View.OnClickListener mOnClickListener = view -> {
//            TaskContent.TaskItem item = (TaskContent.TaskItem) view.getTag();
//
//            Context context = view.getContext();
//            Intent intent = new Intent(context, TaskDetailActivity.class);
//            intent.putExtra(TaskDetailFragment.ARG_ITEM_ID, item.id);
//
//            context.startActivity(intent);
//        };

        SimpleItemRecyclerViewAdapter(List<String> items, TaskDetailCallBackListener callBackListener) {
            mValues = items;
            this.callBackListener = callBackListener;
        }

        @Override
        public TaskDetailFragment.SimpleItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.sub_items, parent, false);
            return new TaskDetailFragment.SimpleItemRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final TaskDetailFragment.SimpleItemRecyclerViewAdapter.ViewHolder holder, int position) {
            holder.mTitle.setText(mValues.get(position));

            holder.itemView.setTag(mValues.get(position));
            holder.mId = position;
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mTitle;
            final RadioButton mButton;
            int mId;

            public void onSelect(RadioButton mButton) {
                mValues.remove(mId);
                mButton.setChecked(false);

                notifyDataSetChanged();
                callBackListener.onCallBack(mTitle.getText().toString());
            }

            ViewHolder(View view) {
                super(view);
                mTitle = view.findViewById(R.id.title);
                mButton = view.findViewById(R.id.radioButton);
                mButton.setOnClickListener(view1 -> onSelect(mButton));
            }
        }
    }
}

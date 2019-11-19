package com.yoseph.re_mind.ui.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yoseph.re_mind.R;
import com.yoseph.re_mind.data.TaskContent;
import com.yoseph.re_mind.ui.activities.TaskDetailActivity;

import java.util.List;

public class OverviewFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private RecyclerView recyclerView;
    private SimpleItemRecyclerViewAdapter recyclerViewAdapter;

    public OverviewFragment() {
        // Required empty public constructor.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_overview, container, false);

        View recyclerView = rootView.findViewById(R.id.task_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        Spinner spinner = rootView.findViewById(R.id.filter_spinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.filter, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(this);

        return rootView;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new OverviewFragment.SimpleItemRecyclerViewAdapter(TaskContent.getItems()));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        String filter_title = parent.getItemAtPosition(pos).toString();

        switch (filter_title) {
            case "All":
                TaskContent.setFilter(-1);
            case "Homework":
                TaskContent.setFilter(0);
            case "Event":
                TaskContent.setFilter(1);
            case "Shopping List":
                TaskContent.setFilter(2);
        }
        SimpleItemRecyclerViewAdapter.updateList();
        recyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }


    public void addFirebaseListeners(String userId) {
        // Get a Firebase Realtime Database reference.
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference root = database.getReference("users/" + userId + "/shared");

        // Add Listener
        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                TaskContent.TaskItem taskItem = dataSnapshot.getValue(TaskContent.TaskItem.class);
                TaskContent.addItem(taskItem);
                recyclerViewAdapter.notifyItemInserted(TaskContent.ITEMS.size() - 1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                TaskContent.TaskItem taskItem = dataSnapshot.getValue(TaskContent.TaskItem.class);
                TaskContent.addItem(taskItem);
                recyclerViewAdapter.notifyItemInserted(TaskContent.ITEMS.size() - 1);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void refreshRecyclerView() {

        recyclerViewAdapter.notifyDataSetChanged();
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<OverviewFragment.SimpleItemRecyclerViewAdapter.ViewHolder> {
        private static List<TaskContent.TaskItem> mValues;
        private final View.OnClickListener mOnClickListener = view -> {
            TaskContent.TaskItem item = (TaskContent.TaskItem) view.getTag();

            Context context = view.getContext();
            Intent intent = new Intent(context, TaskDetailActivity.class);

            intent.putExtra(TaskDetailFragment.ARG_ITEM_ID, item.id);

            context.startActivity(intent);
        };

        SimpleItemRecyclerViewAdapter(List<TaskContent.TaskItem> items) {

            mValues = items;
        }

        public static void updateList() {
            mValues = TaskContent.getItems();
        }

        @Override
        public OverviewFragment.SimpleItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.task_list_content, parent, false);
            return new OverviewFragment.SimpleItemRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final OverviewFragment.SimpleItemRecyclerViewAdapter.ViewHolder holder, int position) {
            holder.mTitle.setText(mValues.get(position).title);
            holder.mDetailView.setText(mValues.get(position).details);

            holder.mIconView.setImageResource(TaskContent.getIconForType(mValues.get(position).type));

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
            holder.mId = position;
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mTitle;
            final TextView mDetailView;
            final ImageView mIconView;
            final RadioButton mButton;
            int mId;

            public void onSelect(RadioButton mButton) {
                mValues.remove(mId);
                mButton.setChecked(false);
                notifyItemRemoved(mId);
                notifyItemRangeChanged(mId, getItemCount());
            }

            ViewHolder(View view) {
                super(view);
                mDetailView = view.findViewById(R.id.detail);
                mTitle = view.findViewById(R.id.title);
                mIconView = view.findViewById(R.id.iconView);
                mButton = view.findViewById(R.id.radioButton);
                mButton.setOnClickListener(view1 -> onSelect(mButton));
            }
        }
    }
}

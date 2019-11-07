package com.yoseph.re_mind.ui.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.yoseph.re_mind.R;
import com.yoseph.re_mind.data.TaskContent;
import com.yoseph.re_mind.ui.activities.TaskDetailActivity;

import java.util.List;

public class OverviewFragment extends Fragment {

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

        return rootView;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new OverviewFragment.SimpleItemRecyclerViewAdapter(TaskContent.ITEMS));
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<OverviewFragment.SimpleItemRecyclerViewAdapter.ViewHolder> {
        private List<TaskContent.TaskItem> mValues;
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
                notifyDataSetChanged();
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

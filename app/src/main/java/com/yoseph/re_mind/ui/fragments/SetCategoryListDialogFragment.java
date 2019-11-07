package com.yoseph.re_mind.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.yoseph.re_mind.R;

/**
 * <p>A fragment that shows a list of items as a modal bottom sheet.</p>
 * <p>You can show this modal bottom sheet from your activity like this:</p>
 * <pre>
 *     SetCategoryListDialogFragment.newInstance(30).show(getSupportFragmentManager(), "dialog");
 * </pre>
 */
public class SetCategoryListDialogFragment extends BottomSheetDialogFragment {
    public static final String SELECTION = "CATEGORY";

    private static final String ARG_TITLE = "title";
    private static final String ARG_DESCRIPTION = "description";
    private static final String ARG_ICON = "icon";
    private static final String ARG_ITEMS = "items";
    private static final String ARG_ICONS = "icons";

    private String[] items;
    private int[] icons;


    public static SetCategoryListDialogFragment newInstance(String title, String description, int icon, String[] items, int[] icons) {
        final SetCategoryListDialogFragment fragment = new SetCategoryListDialogFragment();
        final Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_DESCRIPTION, description);
        args.putInt(ARG_ICON, icon);
        args.putStringArray(ARG_ITEMS, items);
        args.putIntArray(ARG_ICONS, icons);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setcategory_list_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        final RecyclerView recyclerView =  view.findViewById(R.id.list);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        items = getArguments().getStringArray(ARG_ITEMS);
        icons = getArguments().getIntArray(ARG_ICONS);
        recyclerView.setAdapter(new SetCategoryAdapter(items, icons));

        ((TextView) view.findViewById(R.id.sheet_title)).setText(getArguments().getString(ARG_TITLE));
        ((TextView) view.findViewById(R.id.sheet_description)).setText(getArguments().getString(ARG_DESCRIPTION));
        ((ImageView) view.findViewById(R.id.sheet_icon)).setImageResource(getArguments().getInt(ARG_ICON));
    }


    private class ViewHolder extends RecyclerView.ViewHolder {

        final TextView text;
        final ImageView icon;

        ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            // TODO: Customize the item layout
            super(inflater.inflate(R.layout.fragment_setcategory_list_dialog_item, parent, false));
            text = itemView.findViewById(R.id.option_text);
            icon = itemView.findViewById(R.id.option_icon);
            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra(SELECTION, items[getAdapterPosition()]);

                    // pass intent to target fragment
                    Fragment f = getTargetFragment();
                    f.onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                    dismiss();
                }
            });
        }

    }

    private class SetCategoryAdapter extends RecyclerView.Adapter<ViewHolder> {

        private final String[] mItems;
        private final int[] mIcons;

        SetCategoryAdapter(String[] items, int[] icons) {
            mItems = items;
            mIcons = icons;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.text.setText(mItems[position]);
            holder.icon.setImageResource(mIcons[position]);
        }

        @Override
        public int getItemCount() {
            return mItems.length;
        }

    }

}

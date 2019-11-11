package com.yoseph.re_mind.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.yoseph.re_mind.R;
import com.yoseph.re_mind.data.CategoryContent;
import com.yoseph.re_mind.ui.fragments.DatePickerFragment;
import com.yoseph.re_mind.ui.fragments.DetailButtonFragment;
import com.yoseph.re_mind.ui.fragments.OverviewFragment;
import com.yoseph.re_mind.ui.fragments.SetCategoryListDialogFragment;
import com.yoseph.re_mind.ui.fragments.TaskDetailFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * An activity representing a single Task detail screen. This
 * activity is only used on narrow width devices.
 */
public class TaskDetailActivity extends AppCompatActivity implements DetailButtonFragment.OnFragmentInteractionListener {

    public static final int SET_DATE = 0;
    public static final int SET_LOCATION = 1;
    public static final int SET_REPEAT = 2;
    public static final int SET_SHARE = 3;
    public static final int SET_CATEGORY = 4;

    private TaskDetailFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        Toolbar toolbar = findViewById(R.id.detail_toolbar);
        Chip chip = findViewById(R.id.chip);
        ListView lv = findViewById(R.id.subItems);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        final List<String> sub_list = new ArrayList<>();
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, sub_list);
        lv.setAdapter(arrayAdapter);

        chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sub_list.add((String) chip.getText());
                lv.setVisibility(view.VISIBLE);
                arrayAdapter.notifyDataSetChanged();
            }
        });

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(TaskDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(TaskDetailFragment.ARG_ITEM_ID));
            fragment = new TaskDetailFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.task_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            navigateUpTo(new Intent(this, OverviewFragment.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(int type){

        DialogFragment f = null;
        if (type == SET_DATE){
            f = new DatePickerFragment();
        } else if (type == SET_SHARE) {

        } else if (type == SET_CATEGORY) {
            f = SetCategoryListDialogFragment.newInstance("Set Category", "For filtering actions", R.drawable.category, CategoryContent.getTitles(), CategoryContent.getIcons());
        } else if (type == SET_LOCATION) {
            String[] optionTitles = new String[] { "Grocery", "Pharmacy", "Home Improvement", "Search" };
            int[] optionIcons = new int[] { R.drawable.category, R.drawable.event, R.drawable.category, R.drawable.add_black };
            f = SetCategoryListDialogFragment.newInstance("Set Location", "Be reminded when you travel nearby", R.drawable.location, optionTitles, optionIcons);
        }

        if (f != null) {
            f.setTargetFragment(fragment, type);
            f.show(getSupportFragmentManager(), "buttonAction");
        }

    }
}

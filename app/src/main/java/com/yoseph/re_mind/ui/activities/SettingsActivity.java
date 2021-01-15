package com.yoseph.re_mind.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;

import com.yoseph.re_mind.R;

public class SettingsActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Add back arrow to toolbar.
        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle(getString(R.string.menu_settings));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle arrow click here.
        if (item.getItemId() == android.R.id.home) {
            // Close this activity and return to preview activity.
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}

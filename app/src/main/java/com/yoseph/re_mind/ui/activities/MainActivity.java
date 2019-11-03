package com.yoseph.re_mind.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.yoseph.re_mind.R;
import com.yoseph.re_mind.ui.fragments.BottomSheetFragment;
import com.yoseph.re_mind.ui.fragments.MapFragment;
import com.yoseph.re_mind.ui.fragments.OverviewFragment;

public class MainActivity extends AppCompatActivity {

    // View references.
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private ImageButton fab;

    // Index to keep track of current fragment.
    public static int navItemIndex = 0;

    // Toolbar titles for each nav menu item.
    private String[] activityTitles;

    // Tags used to identify fragments.
    private static final String TAG_OVERVIEW = "overview";
    private static final String TAG_MAP = "map";
    public static String CURRENT_TAG = TAG_OVERVIEW;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get view references.
        drawerLayout = findViewById(R.id.main_drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.nav_toolbar);
        fab = findViewById(R.id.nav_fab);

        // Initialize toolbar and fab.
        setSupportActionBar(toolbar);
        setFloatingActionButton();

        // Disable title.
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Initialize handler for transitioning between fragments.
        handler = new Handler();

        // Load toolbar titles from string resources.
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        // Initialize navigation menu.
        setUpNavigationView();

        // When user returns to the app after backgrounding it.
        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_OVERVIEW;
            loadFragment();
        }

    }

    private void setFloatingActionButton() {
        // using BottomSheetDialogFragment
        final BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
            }
        });
    }

    private void loadFragment() {
        // Select appropriate nav menu item.
        selectNavMenu();

        // Set toolbar title.
        setToolbarTitle();

        // If user select the current navigation menu again, just close navigation drawer.
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawerLayout.closeDrawers();
            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging when switching between navigation menus.
        // So using runnable, the fragment is loaded with cross fade effect.
        Runnable pendingRunnable = new Runnable() {
            @Override
            public void run() {
                // Update the main content by replacing fragments.
                Fragment fragment = getFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.nav_fragment, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };
        handler.post(pendingRunnable);

        // Close drawer on item click.
        drawerLayout.closeDrawers();

        // Refresh toolbar menu.
        invalidateOptionsMenu();
    }

    private Fragment getFragment() {
        switch (navItemIndex) {
            case 0:
                return new OverviewFragment();
            case 1:
                return new MapFragment();
            default:
                return null;
        }
    }

    private void setToolbarTitle() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(activityTitles[navItemIndex]);
        }
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setUpNavigationView() {
        // Setting Navigation View Item Selected Listener to handle the item click of the navigation menu.
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_overview:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_OVERVIEW;
                        break;
                    case R.id.nav_map:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_MAP;
                        break;
                    case R.id.nav_settings:
                        startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_about_us:
                        startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_privacy_policy:
                        startActivity(new Intent(MainActivity.this, PrivacyPolicyActivity.class));
                        drawerLayout.closeDrawers();
                        return true;
                    default:
                        navItemIndex = 0;
                }

                // Check if the item is in checked state or not, if not make it in checked state.
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadFragment();
                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        // Set the ActionBarDrawerToggle to drawer layout.
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        // Calling sync state is necessary or else your hamburger icon wont show up.
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
            return;
        }

        if (navItemIndex != 0) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_OVERVIEW;
            loadFragment();
            return;
        }

        super.onBackPressed();
    }

}

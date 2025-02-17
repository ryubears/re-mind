package com.yoseph.re_mind.ui.activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yoseph.re_mind.R;
import com.yoseph.re_mind.receiver.NotificationActionReceiver;
import com.yoseph.re_mind.ui.fragments.BottomSheetFragment;
import com.yoseph.re_mind.ui.fragments.MapFragment;
import com.yoseph.re_mind.ui.fragments.OverviewFragment;
import com.yoseph.re_mind.ui.fragments.TaskDetailFragment;
import com.yoseph.re_mind.ui.interfaces.CallBackListener;

public class MainActivity extends AppCompatActivity implements CallBackListener {

    private static final String USER_YEHYUN = "ryuxx115";
    private static final String USER_DONGHA = "kangx637";
    private static String userId;

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

    // For dealing with transition between fragments.
    private Handler handler;

    // Notification constants.
    public static final int NOTIFICATION_ID = 1337;
    private static final String CHANNEL_ID = "re:mind";
    private static final String CHANNEL_NAME = "re:mind";
    private static final String CHANNEL_DESCRIPTION = "Contextual Reminder Notifications";

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
        setProfileButton();
        setProfileButtonNavHeader();

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

        // Create an explicit intent for opening TaskDetailActivity.
        Intent backIntent = new Intent(this, MainActivity.class);
        backIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Intent intent = new Intent(this, TaskDetailActivity.class);
        intent.putExtra(TaskDetailFragment.ARG_ITEM_ID, "1");
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 17, new Intent[] {backIntent, intent}, PendingIntent.FLAG_ONE_SHOT);

        // Set long click listener for faking notification.
        createNotificationChannel();
        ImageView toolbarIcon = findViewById(R.id.toolbar_icon);
        toolbarIcon.setOnLongClickListener(view -> {

            Intent actionIntent = new Intent(this, NotificationActionReceiver.class);
            actionIntent.putExtra("notificationId", NOTIFICATION_ID);
            PendingIntent actionPendingIntent = PendingIntent.getBroadcast(this, NOTIFICATION_ID, actionIntent, PendingIntent.FLAG_CANCEL_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.logo_imperialred)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.marker_icon))
                    .setContentTitle("Drop off take home midterm")
                    .setContentText("Location: Keller Hall")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .addAction(R.drawable.complete, "Complete", actionPendingIntent)
                    .addAction(R.drawable.snooze, "Snooze", actionPendingIntent)
                    .setAutoCancel(true);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            // NotificationId is a unique int for each notification that you must define.
            notificationManager.notify(NOTIFICATION_ID, builder.build());

            return true;
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference root = database.getReference("id");
        root.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long id = (long) dataSnapshot.getValue();
                if (id % 2 == 0) {
                    userId = USER_YEHYUN;
                    Toast.makeText(getApplicationContext(), "User: ryuxx115", Toast.LENGTH_LONG).show();
                } else {
                    userId = USER_DONGHA;
                    Toast.makeText(getApplicationContext(), "User: kangx637", Toast.LENGTH_LONG).show();
                }

                OverviewFragment overviewFragment = (OverviewFragment) getSupportFragmentManager().findFragmentByTag(TAG_OVERVIEW);
                overviewFragment.addFirebaseListeners(userId);

                root.setValue(id + 1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = CHANNEL_NAME;
            String description = CHANNEL_DESCRIPTION;
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void setFloatingActionButton() {
        // Using BottomSheetDialogFragment.
        final BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
        fab.setOnClickListener(view -> bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag()));
    }

    private void setProfileButton() {
        final ImageButton button = findViewById(R.id.profile_button);
        button.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, ProfileActivity.class)));
    }

    private void setProfileButtonNavHeader() {
        final NavigationView nav_header = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        header.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
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
        Runnable pendingRunnable = () -> {
            // Update the main content by replacing fragments.
            Fragment fragment = getFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            fragmentTransaction.replace(R.id.nav_fragment, fragment, CURRENT_TAG);
            fragmentTransaction.commitAllowingStateLoss();
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
        navigationView.setNavigationItemSelectedListener(menuItem -> {
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onCallBack() {
        if (navItemIndex == 0) {
            OverviewFragment overviewFragment = (OverviewFragment) getSupportFragmentManager().findFragmentByTag(CURRENT_TAG);
            overviewFragment.refreshRecyclerView();
        }
    }
}

package com.example.finalprojectandroid;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class MainActivity
        extends AppCompatActivity
        implements
        ListFragment.OnMovieSelectedListener {

    public static final String CHANNEL_ID =
            "movies_channel";

    private DetailsFragment
            detailsFragment;

    private ViewPager
            viewPager;


    private final
    ActivityResultLauncher<String>
            notificationPermissionLauncher =

            registerForActivityResult(

                    new ActivityResultContracts
                            .RequestPermission(),

                    isGranted -> {

                    }
            );


    @Override
    protected void onCreate(
            Bundle savedInstanceState
    ) {

        super.onCreate(
                savedInstanceState
        );

        setContentView(
                R.layout.activity_main
        );


        /*
         * Notifications
         */
        createNotificationChannel();

        requestNotificationPermission();


        /*
         * Toolbar
         */
        Toolbar toolbar =
                findViewById(
                        R.id.toolbar
                );

        setSupportActionBar(
                toolbar
        );


        /*
         * ViewPager + TabLayout
         */
        viewPager =
                findViewById(
                        R.id.viewPager
                );

        TabLayout tabLayout =
                findViewById(
                        R.id.tabLayout
                );


        ListFragment listFragment =
                new ListFragment();

        detailsFragment =
                new DetailsFragment();


        ViewPagerAdapter adapter =
                new ViewPagerAdapter(
                        getSupportFragmentManager()
                );


        adapter.addFragment(
                listFragment,
                "Movies"
        );

        adapter.addFragment(
                detailsFragment,
                "Details"
        );


        viewPager.setAdapter(
                adapter
        );


        tabLayout.setupWithViewPager(
                viewPager
        );
    }


    /*
     * Passing Data Between Fragments
     */
    @Override
    public void onMovieSelected(
            Movie movie,
            boolean openDetails
    ) {

        if (detailsFragment != null) {

            detailsFragment
                    .updateDetails(
                            movie
                    );
        }


        /*
         * Move to Details tab
         * only when user clicks movie
         */
        if (openDetails
                && viewPager != null) {

            viewPager.setCurrentItem(
                    1
            );
        }
    }


    /*
     * Notification
     */
    public void showNotification(
            String title,
            String message
    ) {


        /*
         * Android 13+
         */
        if (Build.VERSION.SDK_INT
                >= Build.VERSION_CODES.TIRAMISU) {

            if (ContextCompat
                    .checkSelfPermission(
                            this,
                            Manifest.permission
                                    .POST_NOTIFICATIONS
                    )
                    != PackageManager
                    .PERMISSION_GRANTED) {

                return;
            }
        }


        /*
         * Open app when user
         * clicks notification
         */
        Intent intent =
                new Intent(
                        this,
                        MainActivity.class
                );

        intent.setFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP
                        |
                        Intent.FLAG_ACTIVITY_SINGLE_TOP
        );


        PendingIntent pendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                                |
                                PendingIntent.FLAG_IMMUTABLE
                );


        NotificationCompat.Builder
                builder =

                new NotificationCompat
                        .Builder(
                        this,
                        CHANNEL_ID
                )

                        .setSmallIcon(
                                android.R.drawable
                                        .ic_dialog_info
                        )

                        .setContentTitle(
                                title
                        )

                        .setContentText(
                                message
                        )

                        .setContentIntent(
                                pendingIntent
                        )

                        .setAutoCancel(
                                true
                        )

                        .setPriority(
                                NotificationCompat
                                        .PRIORITY_DEFAULT
                        );


        NotificationManager manager =
                (NotificationManager)
                        getSystemService(
                                NOTIFICATION_SERVICE
                        );


        /*
         * Different notification IDs
         */
        int notificationId =
                (int)
                        System.currentTimeMillis();


        manager.notify(
                notificationId,
                builder.build()
        );
    }


    /*
     * Notification Permission
     */
    private void
    requestNotificationPermission() {

        if (Build.VERSION.SDK_INT
                >= Build.VERSION_CODES.TIRAMISU) {

            if (ContextCompat
                    .checkSelfPermission(
                            this,
                            Manifest.permission
                                    .POST_NOTIFICATIONS
                    )
                    != PackageManager
                    .PERMISSION_GRANTED) {

                notificationPermissionLauncher
                        .launch(
                                Manifest.permission
                                        .POST_NOTIFICATIONS
                        );
            }
        }
    }


    /*
     * Notification Channel
     */
    private void
    createNotificationChannel() {

        if (Build.VERSION.SDK_INT
                >= Build.VERSION_CODES.O) {

            NotificationChannel channel =
                    new NotificationChannel(

                            CHANNEL_ID,

                            "Movies Notifications",

                            NotificationManager
                                    .IMPORTANCE_DEFAULT
                    );


            channel.setDescription(
                    "Notifications for movie loading status"
            );


            NotificationManager manager =
                    getSystemService(
                            NotificationManager.class
                    );


            if (manager != null) {

                manager.createNotificationChannel(
                        channel
                );
            }
        }
    }


    /*
     * Options Menu
     */
    @Override
    public boolean
    onCreateOptionsMenu(
            Menu menu
    ) {

        menu.add(
                "About"
        );

        menu.add(
                "Help"
        );

        return true;
    }


    @Override
    public boolean
    onOptionsItemSelected(
            MenuItem item
    ) {

        String title =
                item
                        .getTitle()
                        .toString();


        if (title.equals(
                "About"
        )) {

            showNotification(

                    "Movie Finder",

                    "Final Project - Mobile App Development 2"
            );

            return true;
        }


        if (title.equals(
                "Help"
        )) {

            showNotification(

                    "Help",

                    "Search for movies and tap any movie to see details."
            );

            return true;
        }


        return super
                .onOptionsItemSelected(
                        item
                );
    }
}
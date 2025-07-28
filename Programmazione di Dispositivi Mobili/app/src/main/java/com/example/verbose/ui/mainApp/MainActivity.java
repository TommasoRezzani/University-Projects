package com.example.verbose.ui.mainApp;

import static android.icu.text.DateFormat.getPatternInstance;
import static com.example.verbose.Constants.LOCAL_STORAGE;
import static com.example.verbose.Constants.NOTIFICATION_CHANNEL_ID;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.verbose.R;
import com.example.verbose.model.Appointment;
import com.example.verbose.model.Notification;
import com.example.verbose.repository.NotificationsRepository;
import com.example.verbose.sources.IAppointmentsCallback;
import com.example.verbose.sources.LocalNotificationsDataSource;
import com.example.verbose.util.ServiceLocator;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static BadgeDrawable notificationsBadge;
    private static final String TAG = MainActivity.class.getSimpleName();
    private View shadow;


    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // FCM SDK (and your app) can post notifications.
                } else {
                    // TODO: Inform user that that your app will not show notifications.
                }
            });

    private void askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }

    private void askFileAccessPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) ==
                    PackageManager.PERMISSION_GRANTED) {
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.READ_MEDIA_IMAGES)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_GRANTED) {
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }
    }

    private void createNotificationChannel(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "VERBOSE", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Verbose main notification channel");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        askNotificationPermission();
        createNotificationChannel();
        askFileAccessPermission();

        if(isFirstLaunch()){
            ServiceLocator.getInstance().getUsersRepository().getUserPendingRequests(new IAppointmentsCallback() {
                @Override
                public void onSuccess(List<Appointment> appointments) {
                    appointments.forEach(appointment -> {
                        appointment.getRepetitionInfo().getName();
                        Notification notification = new Notification(
                                new Date().getTime(),
                                getString(R.string.appointment_request_title, appointment.getRepetitionInfo().getName()),
                                getString(
                                        R.string.appointment_request_content,
                                        appointment.getClientInfo().getUsername(),
                                        getPatternInstance("yyMMd").format(appointment.getDate()),
                                        String.valueOf(appointment.getTimeSlot()),
                                        appointment.getRequest()),
                                Notification.NotificationType.APPOINTMENT_REQUEST,
                                appointment.getId()
                        );
                        ServiceLocator.getInstance().getNotificationRepository(getApplicationContext()).insertNotification(notification);
                    });

                    if(!appointments.isEmpty() && MainActivity.getNotificationsBadge() != null){
                        BadgeDrawable badgeDrawable = MainActivity.getNotificationsBadge();
                        badgeDrawable.setVisible(true);
                        badgeDrawable.setNumber(appointments.size());
                    }
                }

                @Override
                public void onFailure(Throwable throwable) {
                    Log.d(TAG, throwable.toString());
                }
            });
        }

        setContentView(R.layout.activity_main);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().
                findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        NavController navController = navHostFragment.getNavController();
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);

        NavigationUI.setupWithNavController(bottomNav, navController);
        
        notificationsBadge = bottomNav.getOrCreateBadge(R.id.notificationsFragment);
        NotificationsRepository notificationsRepository = new NotificationsRepository(getApplicationContext());
        LocalNotificationsDataSource.INotificationCountCallback callback = new LocalNotificationsDataSource.INotificationCountCallback() {
            @Override
            public void onSuccess(int count) {
                notificationsBadge.setVisible(count != 0);
                notificationsBadge.setNumber(count);
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        };
        notificationsRepository.getNotificationsCount(callback);


        shadow = findViewById(R.id.bottom_nav_shadow);

        navController.addOnDestinationChangedListener((navController1, navDestination, bundle) -> {
            if (    navDestination.getId() == R.id.newRepetitionFragment ||
                    navDestination.getId() == R.id.impostazioniProfiloFragment ||
                    navDestination.getId() == R.id.modificaProfiloFragment ||
                    navDestination.getId() == R.id.lroProfiloFragment ||
                    navDestination.getId() == R.id.addAppointmentFragment ||
                    navDestination.getId() == R.id.reviewListFragment){
                bottomNav.setVisibility(View.GONE);
                shadow.setVisibility(View.GONE);
            }else {
                bottomNav.setVisibility(View.VISIBLE);
                shadow.setVisibility(View.VISIBLE);
            }
        });

        //TODO remove
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(registrationId -> {
            String deviceId = registrationId.split(":")[0];
            ServiceLocator.getInstance().getUsersRepository().updateUserRegistrationToken(deviceId, registrationId);
        }).addOnFailureListener(e -> Log.d(TAG, e.getLocalizedMessage()));
    }

    private boolean isFirstLaunch() {
        boolean isFirst = getSharedPreferences(LOCAL_STORAGE, MODE_PRIVATE).getBoolean("first_launch", true);
        if(isFirst)
            getSharedPreferences(LOCAL_STORAGE, MODE_PRIVATE).edit().putBoolean("first_launch", false).apply();
        return isFirst;
    }

    public static BadgeDrawable getNotificationsBadge(){
        return notificationsBadge;
    }
}
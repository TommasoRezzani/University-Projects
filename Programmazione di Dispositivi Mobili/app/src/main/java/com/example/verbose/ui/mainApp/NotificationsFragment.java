package com.example.verbose.ui.mainApp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.verbose.R;
import com.example.verbose.adapter.NotificationAdapter;
import com.example.verbose.model.Notification;
import com.example.verbose.ui.ViewModels.NotificationsViewModel;
import com.example.verbose.ui.ViewModels.NotificationsViewModelFactory;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NotificationsFragment extends Fragment {
    private static final String TAG = NotificationsFragment.class.getSimpleName();

    private ConstraintLayout notificationLayout;
    private ConstraintLayout noNotificationLayout;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private BottomNavigationView bottomNavigationView;

    public NotificationsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_notifications, container, false);

        bottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView);

        notificationLayout = rootView.findViewById(R.id.notification_layout);
        noNotificationLayout = rootView.findViewById(R.id.no_notification_layout);

        swipeRefreshLayout = rootView.findViewById(R.id.notification_refresh_layout);

        recyclerView = rootView.findViewById(R.id.notifications_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        NotificationsViewModel notificationsViewModel = new ViewModelProvider(this, new NotificationsViewModelFactory(requireActivity().getApplicationContext())).get(NotificationsViewModel.class);

        if(!notificationsViewModel.getNotificationsLiveData().hasObservers()) {
            notificationsViewModel.getNotificationsLiveData().observe(getViewLifecycleOwner(), listResult -> {
                if(listResult.getData().isEmpty()){
                    notificationLayout.setVisibility(View.GONE);
                    noNotificationLayout.setVisibility(View.VISIBLE);
                } else {
                    notificationLayout.setVisibility(View.VISIBLE);
                    noNotificationLayout.setVisibility(View.GONE);

                    NotificationAdapter notificationAdapter = new NotificationAdapter(listResult.getData());

                    notificationAdapter.setOnItemDeletedListener(notification -> {
                        notificationsViewModel.deleteNotification(notification);
                        BadgeDrawable badge = bottomNavigationView.getOrCreateBadge(R.id.notificationsFragment);
                        badge.setNumber(badge.getNumber() - 1);
                        badge.setVisible(badge.getNumber() > 0);
                    });
                    notificationAdapter.setOnRequestAnsweredListener(notificationsViewModel::answerRequest);

                    recyclerView.setAdapter(notificationAdapter);
                }
                swipeRefreshLayout.setRefreshing(false);
            });
        }

        notificationsViewModel.getNotifications();

        swipeRefreshLayout.setOnRefreshListener(notificationsViewModel::getNotifications);

        return rootView;
    }
}
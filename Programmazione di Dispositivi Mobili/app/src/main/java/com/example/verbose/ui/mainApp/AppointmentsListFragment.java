package com.example.verbose.ui.mainApp;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.verbose.R;
import com.example.verbose.adapter.AppointmentAdapter;
import com.example.verbose.adapter.AppointmentHolder.ReceivedAppointmentHolder;
import com.example.verbose.adapter.AppointmentHolder.SentAppointmentHolder;
import com.example.verbose.adapter.ClosedAppointmentAdapter;
import com.example.verbose.adapter.ReceivedAppointmentAdapter;
import com.example.verbose.adapter.SentAppointmentAdapter;
import com.example.verbose.model.Appointment;
import com.example.verbose.model.Result;
import com.example.verbose.ui.ViewModels.AppointmentListViewModel;
import com.example.verbose.ui.dialog.DeleteDialog;
import com.example.verbose.ui.dialog.ReviewDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

public class AppointmentsListFragment extends Fragment implements AppointmentAdapter.OnClickListener,AppointmentAdapter.OnDeleteListener, AppointmentAdapter.OnCloseListener, AppointmentAdapter.OnLeaveReviewListener {
    private static final String TAG = AppointmentsListFragment.class.getSimpleName();

    private TabLayout tabLayout;

    private RecyclerView recyclerView;
    private TextView noAppointmentsPresent;
    private SwipeRefreshLayout swipeRefreshLayout;

    public AppointmentsListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_appointments_list, container, false);

        swipeRefreshLayout = rootView.findViewById(R.id.appointments_list_refresh_layout);

        recyclerView = rootView.findViewById(R.id.appointments_list_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

        tabLayout = rootView.findViewById(R.id.tabLayout);

        noAppointmentsPresent = rootView.findViewById(R.id.no_appointments_present);

        AppointmentListViewModel appointmentListViewModel = new ViewModelProvider(this).get(AppointmentListViewModel.class);

        appointmentListViewModel.getSentAppointmentsLiveData().observe(getViewLifecycleOwner(), listResult -> {
            if(listResult.isSuccess()) {
                SentAppointmentAdapter appointmentAdapter = new SentAppointmentAdapter(listResult.getData());
                appointmentAdapter.setOnDeleteListener(this);
                appointmentAdapter.setOnClickListener(this);
                recyclerView.setAdapter(appointmentAdapter);
                displayMissingDataView(!listResult.getData().isEmpty());

            } else {
                Throwable throwable = listResult.getThrowable();
                if(throwable instanceof UnknownHostException || throwable instanceof SocketTimeoutException){
                    displayLoadingErrorMessage();
                }
            }
            swipeRefreshLayout.setRefreshing(false);
        });

        appointmentListViewModel.getRecvAppointmentsLiveData().observe(getViewLifecycleOwner(), listResult -> {
            if(listResult.isSuccess()) {
                ReceivedAppointmentAdapter appointmentAdapter = new ReceivedAppointmentAdapter(listResult.getData());
                appointmentAdapter.setOnClickListener(this);
                appointmentAdapter.setOnDeleteListener(this);
                appointmentAdapter.setOnCloseListener(this);
                recyclerView.setAdapter(appointmentAdapter);
                displayMissingDataView(!listResult.getData().isEmpty());
            } else {
                Throwable throwable = listResult.getThrowable();
                if(throwable instanceof UnknownHostException || throwable instanceof SocketTimeoutException){
                    displayLoadingErrorMessage();
                }
            }
            swipeRefreshLayout.setRefreshing(false);
        });

        appointmentListViewModel.getClosedAppointmentsLiveData().observe(getViewLifecycleOwner(), listResult -> {
            if(listResult.isSuccess()){
                ClosedAppointmentAdapter appointmentAdapter = new ClosedAppointmentAdapter(listResult.getData());
                appointmentAdapter.setOnLeaveReviewListener(this);
                recyclerView.setAdapter(appointmentAdapter);
                displayMissingDataView(!listResult.getData().isEmpty());
            } else {
                Throwable throwable = listResult.getThrowable();
                if(throwable instanceof UnknownHostException || throwable instanceof SocketTimeoutException){
                    displayLoadingErrorMessage();
                }
            }
            swipeRefreshLayout.setRefreshing(false);
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                appointmentListViewModel.setTabIndex(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        appointmentListViewModel.getTabIndexLiveData().observe(getViewLifecycleOwner(), tabIndex -> {
            AppointmentAdapter appointmentAdapter = null;

            switch (tabIndex){
                case 0:
                    if(!appointmentListViewModel.getSentAppointmentsLiveData().isInitialized()){
                        appointmentListViewModel.getSentAppointments();
                    } else {
                        Result<List<Appointment>> prevResult = appointmentListViewModel.getSentAppointmentsLiveData().getValue();
                        if (prevResult.isSuccess())
                            appointmentAdapter = new SentAppointmentAdapter(appointmentListViewModel.getSentAppointmentsLiveData().getValue().getData());
                        displayMissingDataView(prevResult.isSuccess() && !prevResult.getData().isEmpty());
                    }
                    break;
                case 1:
                    tabLayout.selectTab(tabLayout.getTabAt(tabIndex));
                    if(!appointmentListViewModel.getRecvAppointmentsLiveData().isInitialized()){
                        appointmentListViewModel.getRecvAppointments();
                    } else {
                        Result<List<Appointment>> prevResult = appointmentListViewModel.getRecvAppointmentsLiveData().getValue();
                        if(prevResult.isSuccess())
                            appointmentAdapter = new ReceivedAppointmentAdapter(appointmentListViewModel.getRecvAppointmentsLiveData().getValue().getData());
                        displayMissingDataView(prevResult.isSuccess() && !prevResult.getData().isEmpty());
                    }
                    break;
                case 2:
                    tabLayout.selectTab(tabLayout.getTabAt(tabIndex));
                    if(!appointmentListViewModel.getClosedAppointmentsLiveData().isInitialized()){
                        appointmentListViewModel.getClosedAppointments();
                    } else {
                        Result<List<Appointment>> prevResult = appointmentListViewModel.getClosedAppointmentsLiveData().getValue();
                        if (prevResult.isSuccess())
                            appointmentAdapter = new ClosedAppointmentAdapter(appointmentListViewModel.getClosedAppointmentsLiveData().getValue().getData());
                        displayMissingDataView(prevResult.isSuccess() && !prevResult.getData().isEmpty());
                    }
                    break;
            }

            try {
                appointmentAdapter.setOnDeleteListener(this);
                if(appointmentAdapter instanceof SentAppointmentAdapter || appointmentAdapter instanceof ReceivedAppointmentAdapter)
                    appointmentAdapter.setOnClickListener(this);
                if(appointmentAdapter instanceof ReceivedAppointmentAdapter)
                    appointmentAdapter.setOnCloseListener(this);
                if(appointmentAdapter instanceof ClosedAppointmentAdapter)
                    appointmentAdapter.setOnLeaveReviewListener(this);
                recyclerView.setAdapter(appointmentAdapter);
            }catch (NullPointerException ignored) {}
        });

        swipeRefreshLayout.setOnRefreshListener(() -> {
            switch (appointmentListViewModel.getTabIndexLiveData().getValue()){
                case 0:
                    appointmentListViewModel.getSentAppointments();
                    break;
                case 1:
                    appointmentListViewModel.getRecvAppointments();
                    break;
                case 2:
                    appointmentListViewModel.getClosedAppointments();
                    break;
            }
        });

        return rootView;
    }

    @Override
    public void onDelete(View v, Appointment target) {
        Log.d(TAG, target.getRequest());
        DeleteDialog deleteDialog = new DeleteDialog(getString(R.string.delete_confirmation_string), true);
        AppointmentListViewModel appointmentListViewModel = new ViewModelProvider(this).get(AppointmentListViewModel.class);

        deleteDialog.setDialogListener(new DeleteDialog.DialogListener() {
            @Override
            public void onDialogPositiveClick(DialogInterface dialog, int which) {
                appointmentListViewModel.deleteAppointment(target).observe(getViewLifecycleOwner(), listResult -> {
                    SentAppointmentAdapter sentAppointmentAdapter = new SentAppointmentAdapter(listResult.getData());
                    sentAppointmentAdapter.setOnClickListener(AppointmentsListFragment.this);
                    sentAppointmentAdapter.setOnDeleteListener(AppointmentsListFragment.this);
                    recyclerView.setAdapter(sentAppointmentAdapter);
                });
            }

            @Override
            public void onDialogNegativeClick(DialogInterface dialog, int which) {
            }
        });
        deleteDialog.show(requireActivity().getSupportFragmentManager(), "DELETE APPOINTMENT DIALOG");
    }

    @Override
    public void onClose(View v, Appointment target) {
        AppointmentListViewModel appointmentListViewModel = new ViewModelProvider(this).get(AppointmentListViewModel.class);
        appointmentListViewModel.closeAppointment(target);
    }

    @Override
    public void onLeaveReview(View v, Appointment target) {
        AppointmentListViewModel appointmentListViewModel = new ViewModelProvider(this).get(AppointmentListViewModel.class);

        ReviewDialog reviewDialog;
        if(target.getReview().isPresent()) {
            reviewDialog = new ReviewDialog(getString(R.string.leave_review), target.getReview().get().getContent(), target.getReview().get().getVote());
        }
        else {
            reviewDialog = new ReviewDialog(getString(R.string.leave_review));
        }
        reviewDialog.setDialogListener(new ReviewDialog.DialogListener() {
            @Override
            public void onDialogPositiveClick(DialogInterface dialog, int which, String reviewText, float vote) {
                Log.d(TAG, reviewText);
                appointmentListViewModel.sendReview(target, reviewText, vote);
            }

            @Override
            public void onDialogNegativeClick(DialogInterface dialog, int which) {}
        });
        reviewDialog.show(requireActivity().getSupportFragmentManager(), "Review Dialog");
    }

    public void displayLoadingErrorMessage(){
        displayMissingDataView(false);
        Snackbar.make(requireView(), getString(R.string.no_connection_error), Snackbar.LENGTH_LONG).show();
    }

    public void displayMissingDataView(boolean showRecyclerView){
        if(!showRecyclerView) {
            recyclerView.setVisibility(View.GONE);
            noAppointmentsPresent.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            noAppointmentsPresent.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v, Appointment target) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(target.getConferenceLink()));
            startActivity(intent);
        } catch (NullPointerException ignored){}
    }
}
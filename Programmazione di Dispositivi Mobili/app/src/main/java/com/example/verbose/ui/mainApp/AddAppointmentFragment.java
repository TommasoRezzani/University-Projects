package com.example.verbose.ui.mainApp;

import static android.icu.text.DateFormat.getPatternInstance;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.verbose.R;
import com.example.verbose.adapter.ReviewAdapter;
import com.example.verbose.adapter.TimeSlotAdapter;
import com.example.verbose.model.AuthUser;
import com.example.verbose.model.Repetition;
import com.example.verbose.ui.ViewModels.AddAppointmentViewModel;
import com.example.verbose.ui.ViewModels.AddAppointmentViewModelFactory;
import com.example.verbose.ui.dialog.DeleteDialog;
import com.example.verbose.ui.dialog.RequestDialog;
import com.example.verbose.util.GlideApp;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Date;


public class AddAppointmentFragment extends Fragment {
    private static final String TAG = AddAppointmentFragment.class.getSimpleName();

    private TextView username;
    private TextView firstName;
    private TextView bio;
    private ImageView profilePicture;

    private RecyclerView gridView;
    private RecyclerView recyclerView;
    private TextView reviewsHeader;
    private TextView dateText;
    private FloatingActionButton prevDateButton;
    private FloatingActionButton nextDateButton;
    private ConstraintLayout gridConstraintLayout;
    private ConstraintLayout loadingConstraintLayout;

    public AddAppointmentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Repetition repetition;
        assert getArguments() != null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            repetition = getArguments().getSerializable("repetition", Repetition.class);
        } else {
            repetition = (Repetition) getArguments().getSerializable("repetition");
        }


        Log.d(TAG, repetition.getName());

        AddAppointmentViewModel addAppointmentViewModel =
                new ViewModelProvider(
                        this,
                        new AddAppointmentViewModelFactory(repetition)
                ).get(AddAppointmentViewModel.class);

        View rootView = inflater.inflate(R.layout.fragment_add_appointment, container, false);

        //UI relativa alle informazioni dell'owner della ripetizione
        username = rootView.findViewById(R.id.profile_username_text_view);
        firstName = rootView.findViewById(R.id.firstLastName);
        bio = rootView.findViewById(R.id.profile_bio_text_view);
        profilePicture = rootView.findViewById(R.id.profile_picture);


        dateText = rootView.findViewById(R.id.date_text);
        prevDateButton = rootView.findViewById(R.id.prev_date_button);
        nextDateButton = rootView.findViewById(R.id.next_date_button);
        gridConstraintLayout = rootView.findViewById(R.id.grid_constraint_layout);
        loadingConstraintLayout = rootView.findViewById(R.id.loading_constraint_layout);
        reviewsHeader = rootView.findViewById(R.id.reviews_header_text);

        recyclerView = rootView.findViewById(R.id.review_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

        gridView = rootView.findViewById(R.id.time_grid_view);
        gridView.setLayoutManager(new GridLayoutManager(requireActivity(), 4));

        addAppointmentViewModel.getOwnerInfo().observe(getViewLifecycleOwner(), authUserResult -> {
            AuthUser owner = authUserResult.getData();
            username.setText(owner.getUsername());
            firstName.setText(owner.getFirstName() + " " + owner.getLastName());
            bio.setText(owner.getBio());

            GlideApp.with(this)
                    .load(owner.getProfilePicture())
                    .placeholder(R.drawable.baseline_account_circle_24)
                    .circleCrop()
                    .into(profilePicture);
        });

        addAppointmentViewModel.getReviews().observe(getViewLifecycleOwner(), listResult -> {
            if(listResult.getData().size() == 0) {
                recyclerView.setVisibility(View.GONE);
                reviewsHeader.setVisibility(View.GONE);
            } else {
                ReviewAdapter reviewAdapter = new ReviewAdapter(listResult.getData());
                recyclerView.setAdapter(reviewAdapter);
            }
        });

        addAppointmentViewModel.getCurrentDateLiveData().observe(getViewLifecycleOwner(), date -> {
            gridConstraintLayout.setVisibility(View.GONE);
            loadingConstraintLayout.setVisibility(View.VISIBLE);
            addAppointmentViewModel.getSlotsByDate(date).observe(getViewLifecycleOwner(), listResult -> {
                TimeSlotAdapter timeSlotAdapter = new TimeSlotAdapter(listResult.getData());
                timeSlotAdapter.setOnSlotClickedListener((v, slot) -> {
                    RequestDialog genericDialog = new RequestDialog("Conferma appuntamento");
                    genericDialog.setDialogListener(new RequestDialog.DialogListener() {
                        @Override
                        public void onDialogPositiveClick(DialogInterface dialog, int which, String requestText) {
                            addAppointmentViewModel.createAppointment(slot, requestText);
                        }

                        @Override
                        public void onDialogNegativeClick(DialogInterface dialog, int which) {

                        }
                    });

                    genericDialog.show(requireActivity().getSupportFragmentManager(), "Confirm dialog");
                });
                gridView.setAdapter(timeSlotAdapter);
                dateText.setText(getPatternInstance("yyyyMMd").format(date));
                gridConstraintLayout.setVisibility(View.VISIBLE);
                loadingConstraintLayout.setVisibility(View.GONE);
            });
        });

        prevDateButton.setOnClickListener(v -> {
            addAppointmentViewModel.prevDate();
        });

        nextDateButton.setOnClickListener(v -> {
            addAppointmentViewModel.nextDate();
        });

        addAppointmentViewModel.getResultLiveData().observe(getViewLifecycleOwner(), appointmentResult -> {
            String text;
            if(appointmentResult.isSuccess()){
                text = getString(R.string.registration_completed_title);
            } else {
                String detail = appointmentResult.getThrowable().getMessage();
                text = getString(R.string.registration_failed_text, detail);
            }
            DeleteDialog deleteDialog = new DeleteDialog(text, false, true, false);
            deleteDialog.setDialogListener(new DeleteDialog.DialogListener() {
                @Override
                public void onDialogPositiveClick(DialogInterface dialog, int which) {

                }

                @Override
                public void onDialogNegativeClick(DialogInterface dialog, int which) {

                }
            });
            deleteDialog.show(requireActivity().getSupportFragmentManager(), "delete dialog");
        });

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date currentDate = calendar.getTime();

        dateText.setText(getPatternInstance("yyyyMMd").format(currentDate));
        addAppointmentViewModel.setDate(currentDate);

        return rootView;
    }
}
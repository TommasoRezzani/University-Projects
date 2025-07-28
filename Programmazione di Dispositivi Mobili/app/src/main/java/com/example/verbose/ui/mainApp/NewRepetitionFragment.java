package com.example.verbose.ui.mainApp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.verbose.R;
import com.example.verbose.adapter.SelectableCategoryAdapter;
import com.example.verbose.adapter.SingleSelectableCategoryAdapter;
import com.example.verbose.model.Category;
import com.example.verbose.model.Repetition;
import com.example.verbose.model.Result;
import com.example.verbose.model.SelectableCategory;
import com.example.verbose.ui.ViewModels.CategoriesViewModel;
import com.example.verbose.ui.ViewModels.CategoriesViewModelFactory;
import com.example.verbose.ui.ViewModels.NewRepetitionViewModel;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class NewRepetitionFragment extends Fragment {

    private final static String TAG = NewRepetitionFragment.class.getSimpleName();

    private ConstraintLayout constraintLayout;
    private TextInputLayout titleInputLayout;
    private TimePicker pickerStart;
    private TimePicker pickerEnd;
    private RecyclerView recyclerView;
    private TextView timeError;
    private TextView categoryError;
    private CardView cardView;

    public NewRepetitionFragment() {
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
        View rootView =  inflater.inflate(R.layout.fragment_new_repetition, container, false);

        constraintLayout = rootView.findViewById(R.id.new_rep_constraint_layout);
        titleInputLayout = rootView.findViewById(R.id.title_input_layout);
        pickerStart = rootView.findViewById(R.id.spinner_start);
        pickerEnd = rootView.findViewById(R.id.spinner_end);
        timeError = rootView.findViewById(R.id.wrong_hour_error_text);
        cardView = rootView.findViewById(R.id.cardView);
        categoryError = rootView.findViewById(R.id.missing_category_text);

        pickerStart.setIs24HourView(true);
        pickerEnd.setIs24HourView(true);

        recyclerView =  rootView.findViewById(R.id.category_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        CategoriesViewModel categoriesViewModel = new ViewModelProvider(this, new CategoriesViewModelFactory(requireActivity().getApplicationContext())).get(CategoriesViewModel.class);

        categoriesViewModel.getCategories().observe(this.getViewLifecycleOwner(), setResult -> {
            if(setResult.isSuccess()){
                Set<SelectableCategory> categories = setResult.getData().stream().map(SelectableCategory::new).collect(Collectors.toSet());

                SingleSelectableCategoryAdapter mAdapter = new SingleSelectableCategoryAdapter(new ArrayList<>(categories));
                recyclerView.setAdapter(mAdapter);
            }else{
                Log.d(TAG, setResult.getThrowable().toString());
            }
        });

        NewRepetitionViewModel newRepetitionViewModel = new ViewModelProvider(this).get(NewRepetitionViewModel.class);

        rootView.findViewById(R.id.send_repetition_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO fix
                String title = titleInputLayout.getEditText().getText().toString().trim();
                int startHour = pickerStart.getHour();
                int endHour = pickerEnd.getHour();
                Optional<Category> selectedCategory = ((SingleSelectableCategoryAdapter) recyclerView.getAdapter()).getSelectedCategory();

                if(title.isEmpty()){
                    titleInputLayout.setError(getResources().getString(R.string.missing_title_error));
                    titleInputLayout.setErrorEnabled(true);
                    return;
                } else {
                    titleInputLayout.setErrorEnabled(false);
                }
                if(startHour >= endHour){
                    ConstraintSet constraintSet = new ConstraintSet();
                    constraintSet.clone(constraintLayout);
                    constraintSet.connect(R.id.cardView, ConstraintSet.TOP, R.id.wrong_hour_error_text, ConstraintSet.BOTTOM, 8);
                    constraintSet.applyTo(constraintLayout);

                    timeError.setVisibility(View.VISIBLE);
                    return;
                } else {
                    ConstraintSet constraintSet = new ConstraintSet();
                    constraintSet.clone(constraintLayout);
                    constraintSet.connect(R.id.cardView, ConstraintSet.TOP, R.id.textView9, ConstraintSet.BOTTOM, 16);
                    constraintSet.applyTo(constraintLayout);
                    timeError.setVisibility(View.GONE);
                }
                if(!selectedCategory.isPresent()){
                    ConstraintSet constraintSet = new ConstraintSet();
                    constraintSet.clone(constraintLayout);
                    constraintSet.connect(R.id.category_recycler_view, ConstraintSet.TOP, R.id.missing_category_text, ConstraintSet.BOTTOM, 8);
                    constraintSet.applyTo(constraintLayout);

                    categoryError.setVisibility(View.VISIBLE);
                    return;
                } else {
                    ConstraintSet constraintSet = new ConstraintSet();
                    constraintSet.clone(constraintLayout);
                    constraintSet.connect(R.id.category_recycler_view, ConstraintSet.TOP, R.id.textView10, ConstraintSet.BOTTOM, 8);
                    constraintSet.applyTo(constraintLayout);

                    categoryError.setVisibility(View.GONE);
                }

                Repetition repetition = new Repetition(title, startHour, endHour, selectedCategory.get());
                newRepetitionViewModel.createRepetition(repetition).observe(getViewLifecycleOwner(), new Observer<Result<Repetition>>() {
                    @Override
                    public void onChanged(Result<Repetition> repetitionResult) {
                        if (repetitionResult.isSuccess()) {
                            Navigation.findNavController(requireView()).navigateUp();
                        } else {
                            //TODO display error
                        }
                        newRepetitionViewModel.getCreationResult().removeObserver(this);
                    }
                });
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
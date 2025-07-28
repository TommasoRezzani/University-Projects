package com.example.verbose.ui.mainApp;

import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.verbose.R;
import com.example.verbose.adapter.ReviewAdapter;
import com.example.verbose.model.Repetition;
import com.example.verbose.ui.ViewModels.ReviewListViewModel;
import com.example.verbose.ui.ViewModels.ReviewListViewModelFactory;

public class ReviewListFragment extends Fragment {
    private RecyclerView recyclerView;
    private TextView emptyText;

    public ReviewListFragment() {
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

        View rootView = inflater.inflate(R.layout.fragment_review_list, container, false);

        emptyText = rootView.findViewById(R.id.no_review_text);
        emptyText.setText(R.string.no_reviews_text);
        recyclerView = rootView.findViewById(R.id.review_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

        ReviewListViewModel reviewListViewModel = new ViewModelProvider(this, new ReviewListViewModelFactory(repetition)).get(ReviewListViewModel.class);
        reviewListViewModel.getReviews().observe(getViewLifecycleOwner(), listResult -> {
            if(listResult.getData().isEmpty()){
                recyclerView.setVisibility(View.GONE);
                emptyText.setVisibility(View.VISIBLE);
            } else {
                ReviewAdapter reviewAdapter = new ReviewAdapter(listResult.getData());
                recyclerView.setAdapter(reviewAdapter);
                recyclerView.setVisibility(View.VISIBLE);
                emptyText.setVisibility(View.GONE);
            }
        });

        return rootView;
    }
}
package com.example.verbose.ui.mainApp;

import static android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.verbose.R;
import com.example.verbose.adapter.RepetitionAdapter;
import com.example.verbose.model.Repetition;
import com.example.verbose.ui.ViewModels.SearchViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.net.SocketTimeoutException;
import java.util.Comparator;

public class HomeFragment extends Fragment implements RepetitionAdapter.OnRepetitionClickListener, Comparator<Repetition> {
    private static final String TAG = HomeFragment.class.getSimpleName();

    private TextView greetingText;
    private RecyclerView recyclerView;
    private TextInputLayout textInputLayout;
    private FloatingActionButton floatingActionButton;

    private SwipeRefreshLayout swipeRefreshLayout;

    private SearchViewModel searchViewModel;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.searchViewModel = new ViewModelProvider(requireActivity()).get(SearchViewModel.class);
        searchViewModel.getFavorites();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        floatingActionButton = rootView.findViewById(R.id.floatingActionButton);
        textInputLayout = rootView.findViewById(R.id.search_input);
        swipeRefreshLayout = rootView.findViewById(R.id.home_refresh_layout);
        recyclerView = rootView.findViewById(R.id.repetitions_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if(searchViewModel.getSearchQuery() != null) {
            textInputLayout.getEditText().setText(searchViewModel.getSearchQuery());
        }

        searchViewModel.getFavoritesLiveData().observe(getViewLifecycleOwner(), listResult -> {
            if(listResult.isSuccess()) {
                RepetitionAdapter adapter = new RepetitionAdapter(listResult.getData(), this);
                adapter.setOnRepetitionClickListener(HomeFragment.this);
                recyclerView.setAdapter(adapter);
                swipeRefreshLayout.setRefreshing(false);
            } else {
                Throwable throwable = listResult.getThrowable();
                if(throwable instanceof SocketTimeoutException){
                    //TODO
                }
            }
        });

        searchViewModel.getSearchResultsLiveData().observe(getViewLifecycleOwner(), listResult -> {
            if(listResult.isSuccess()){
                RepetitionAdapter adapter = new RepetitionAdapter(listResult.getData(), this);
                adapter.setOnRepetitionClickListener(HomeFragment.this);
                recyclerView.setAdapter(adapter);
                swipeRefreshLayout.setRefreshing(false);
            } else {
                Throwable throwable = listResult.getThrowable();
                if(throwable instanceof SocketTimeoutException){
                    //TODO
                }
            }
        });

        swipeRefreshLayout.setOnRefreshListener(() -> {
            if(Boolean.TRUE.equals(searchViewModel.showFavorites().getValue())){
                searchViewModel.getFavorites();
            }else {
                searchViewModel.search(searchViewModel.getSearchQuery());
            }
        });

        floatingActionButton.setOnClickListener(v -> {
            Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_newRepetitionFragment);
        });

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        greetingText = requireActivity().findViewById(R.id.greeting_text);

        searchViewModel.getLoggedUserMutableLiveData().observe(getViewLifecycleOwner(), authUserResult -> {
            greetingText.setText(getResources().getString(R.string.greetings_text, authUserResult.getData().getUsername()));
            authUserResult.handle();
        });

        textInputLayout.getEditText().setOnEditorActionListener((v, actionId, event) -> {
            if(actionId == IME_ACTION_SEARCH){
                Log.d(TAG, v.getText().toString());
                String searchQuery = v.getText().toString();

                if(!searchQuery.isEmpty()) {
                    searchViewModel.search(v.getText().toString());
                }

                return true;
            }
            return false;
        });
    }

    @Override
    public void onClick(View v, Repetition target) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("repetition", target);

        Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_addAppointmentFragment, bundle);
    }


    @Override
    public int compare(Repetition o1, Repetition o2) {
        return Float.compare(o2.getVote(), o1.getVote());
    }
}
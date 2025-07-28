package com.example.verbose.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.verbose.R;
import com.example.verbose.adapter.SelectableCategoryAdapter;
import com.example.verbose.model.AuthUser;
import com.example.verbose.model.Category;
import com.example.verbose.model.Result;
import com.example.verbose.model.SelectableCategory;
import com.example.verbose.ui.ViewModels.AuthUserViewModel;
import com.example.verbose.ui.ViewModels.CategoriesViewModel;
import com.example.verbose.ui.ViewModels.CategoriesViewModelFactory;
import com.example.verbose.ui.mainApp.MainActivity;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

public class CategoriesFragment extends Fragment {
    private static final String TAG = CategoriesFragment.class.getSimpleName();

    private RecyclerView recyclerView;

    private Button sendCategoriesButton;

    public CategoriesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        AuthUser authUser;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            authUser = getArguments().getSerializable("partial_user", AuthUser.class);
        } else {
            authUser = (AuthUser) getArguments().getSerializable("partial_user");
        }

        View rootView = inflater.inflate(R.layout.fragment_categories, container, false);

        recyclerView = rootView.findViewById(R.id.categories_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        AuthUserViewModel authUserViewModel = new ViewModelProvider(this).get(AuthUserViewModel.class);
        CategoriesViewModel categoriesViewModel = new ViewModelProvider(this, new CategoriesViewModelFactory(requireActivity().getApplicationContext())).get(CategoriesViewModel.class);

        categoriesViewModel.getCategories().observe(this.getViewLifecycleOwner(), setResult -> {
            if(setResult.isSuccess()){
                Set<SelectableCategory> categories = setResult.getData().stream().map(SelectableCategory::new).map(category -> {
                    Category castCategory = category;
                    Log.d(TAG, String.valueOf(authUser.getPreferences().contains(castCategory)));
                    if(authUser.getPreferences().contains(category))
                        category.setSelected(true);
                    return category;
                }).collect(Collectors.toSet());

                SelectableCategoryAdapter mAdapter = new SelectableCategoryAdapter(new ArrayList<>(categories));
                recyclerView.setAdapter(mAdapter);
            }else{
                Log.d(TAG, setResult.getThrowable().toString());
            }
        });

        authUserViewModel.getUserLiveData().observe(this.getViewLifecycleOwner(), userResult -> {
            if(userResult.getEventType() == Result.EventType.UPDATE_USER && !userResult.isHandled()){
                if (userResult.isSuccess()) {
                    Intent intent = new Intent(requireContext(), MainActivity.class);
                    startActivity(intent);
                    requireActivity().finish();
                } else {
                    Log.d(TAG, userResult.getData().toString());
                }
                userResult.handle();
            }
        });

        sendCategoriesButton = rootView.findViewById(R.id.button_send_categories);
        sendCategoriesButton.setOnClickListener(v -> {
            SelectableCategoryAdapter adapter = ((SelectableCategoryAdapter) recyclerView.getAdapter());
            Set<Category> selected =  adapter.getCategories().stream()
                    .filter(SelectableCategory::isSelected)
                    .collect(Collectors.toSet());

            authUser.setPreferences(selected);

            authUserViewModel.updateData(authUser);
        });

        return rootView;
    }
}
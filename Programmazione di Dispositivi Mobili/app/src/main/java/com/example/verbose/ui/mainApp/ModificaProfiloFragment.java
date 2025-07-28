package com.example.verbose.ui.mainApp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.verbose.R;
import com.example.verbose.adapter.SelectableCategoryAdapter;
import com.example.verbose.model.AuthUser;
import com.example.verbose.model.Category;
import com.example.verbose.model.SelectableCategory;
import com.example.verbose.ui.ViewModels.CategoriesViewModel;
import com.example.verbose.ui.ViewModels.CategoriesViewModelFactory;
import com.example.verbose.ui.ViewModels.ProfileViewModel;
import com.example.verbose.ui.ViewModels.ProfileViewModelFactory;
import com.example.verbose.ui.login.CategoriesFragment;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;


public class ModificaProfiloFragment extends Fragment {
    private TextInputLayout usernameLayout;
    private TextInputLayout firstNameLayout;
    private TextInputLayout lastNameLayout;
    private TextInputLayout bioLayout;
    private Button sendEdit;
    private String username;
    private String firstName;
    private String lastName;
    private String bio;
    private RecyclerView recyclerView;
    private static final String TAG = CategoriesFragment.class.getSimpleName();

    public ModificaProfiloFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        assert getArguments() != null;
        AuthUser currentUser;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            currentUser = getArguments().getSerializable("user_profile", AuthUser.class);
        } else {
            currentUser = (AuthUser) getArguments().getSerializable("user_profile");
        }

        View rootView = inflater.inflate(R.layout.fragment_modifica_profilo, container, false);

        ProfileViewModel profileViewModel = new ViewModelProvider(this, new ProfileViewModelFactory(getContext())).get(ProfileViewModel.class);
        profileViewModel.getUserLiveData().observe(getViewLifecycleOwner(), authUserResult -> Navigation.findNavController(requireView()).popBackStack());

        usernameLayout = rootView.findViewById(R.id.userNameLayout_mp);
        firstNameLayout = rootView.findViewById(R.id.firstNameLayout_mp);
        lastNameLayout = rootView.findViewById(R.id.lastNameLayout_mp);
        bioLayout = rootView.findViewById(R.id.bioLayout_mp);
        sendEdit = rootView.findViewById(R.id.button_complete_profile);
        recyclerView = rootView.findViewById(R.id.recycler_view_preferenze_mp);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        usernameLayout.getEditText().setText(currentUser.getUsername());
        firstNameLayout.getEditText().setText(currentUser.getFirstName());
        lastNameLayout.getEditText().setText(currentUser.getLastName());
        bioLayout.getEditText().setText(currentUser.getBio());

        CategoriesViewModel categoriesViewModel = new ViewModelProvider(this, new CategoriesViewModelFactory(requireActivity().getApplicationContext())).get(CategoriesViewModel.class);

        categoriesViewModel.getCategories().observe(this.getViewLifecycleOwner(), setResult -> {
            if(setResult.isSuccess()){
                Set<SelectableCategory> categories = setResult.getData().stream().map(SelectableCategory::new).map(category -> {
                    Category castCategory = category;
                    Log.d(TAG, String.valueOf(currentUser.getPreferences().contains(castCategory)));
                    if(currentUser.getPreferences().contains(category))
                        category.setSelected(true);
                    return category;
                }).collect(Collectors.toSet());

                SelectableCategoryAdapter mAdapter = new SelectableCategoryAdapter(new ArrayList<>(categories));
                recyclerView.setAdapter(mAdapter);
            }else{
                Log.d(TAG, setResult.getThrowable().toString());
            }
        });

        sendEdit.setOnClickListener(v -> {
            currentUser.setUsername(usernameLayout.getEditText().getText().toString());
            currentUser.setFirstName(firstNameLayout.getEditText().getText().toString());
            currentUser.setLastName(lastNameLayout.getEditText().getText().toString());
            currentUser.setBio(bioLayout.getEditText().getText().toString().trim());

            SelectableCategoryAdapter adapter = (SelectableCategoryAdapter) recyclerView.getAdapter();
            Set<Category> selected =  adapter.getCategories().stream()
                    .filter(SelectableCategory::isSelected)
                    .collect(Collectors.toSet());

            currentUser.setPreferences(selected);

            username = currentUser.getUsername();
            firstName = currentUser.getFirstName();
            lastName = currentUser.getLastName();
            bio = currentUser.getBio();

            validateFields();

            if(!usernameLayout.isErrorEnabled() && !firstNameLayout.isErrorEnabled() && !lastNameLayout.isErrorEnabled() && !bioLayout.isErrorEnabled()) {
                profileViewModel.updateProfile(currentUser);
            }
        });

        //RIGUARDA nullpointers
        usernameLayout.getEditText().addTextChangedListener(new ClearErrorTextWatcher(usernameLayout));
        firstNameLayout.getEditText().addTextChangedListener(new ClearErrorTextWatcher(firstNameLayout));
        lastNameLayout.getEditText().addTextChangedListener(new ClearErrorTextWatcher(lastNameLayout));
        bioLayout.getEditText().addTextChangedListener(new ClearErrorTextWatcher(bioLayout));

        return rootView;
    }

    private static class ClearErrorTextWatcher implements TextWatcher {
        private TextInputLayout layout;

        ClearErrorTextWatcher(TextInputLayout layout) {
            this.layout = layout;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            if (layout.isErrorEnabled()) {
                layout.setErrorEnabled(false);
            }
        }
    }

    private void validateFields() {
        if (username.isEmpty()) {
            usernameLayout.setError("Riempi il campo Nome Visibile");
            usernameLayout.setErrorEnabled(true);
        }
        if (firstName.isEmpty()) {
            firstNameLayout.setError("Riempi il campo Nome");
            firstNameLayout.setErrorEnabled(true);
        }
        if (lastName.isEmpty()) {
            lastNameLayout.setError("Riempi il campo Cognome");
            lastNameLayout.setErrorEnabled(true);
        }
        if (bio.isEmpty()) {
            bioLayout.setError("Riempi il campo Bio");
            bioLayout.setErrorEnabled(true);
        }
    }
}
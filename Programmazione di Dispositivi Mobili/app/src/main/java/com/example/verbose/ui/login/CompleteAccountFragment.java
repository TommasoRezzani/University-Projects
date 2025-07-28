package com.example.verbose.ui.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.verbose.R;
import com.example.verbose.model.AuthUser;
import com.example.verbose.ui.ViewModels.AuthUserViewModel;
import com.google.android.material.textfield.TextInputLayout;

public class CompleteAccountFragment extends Fragment {
    private static final String TAG = CompleteAccountFragment.class.getSimpleName();

    private TextInputLayout userNameLayout;
    private TextInputLayout firstNameLayout;
    private TextInputLayout lastNameLayout;
    private TextInputLayout bioLayout;
    private Button completedProfileButton;

    public CompleteAccountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_complete_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        assert getArguments() != null;
        AuthUser authUser;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            authUser = getArguments().getSerializable("partial_user", AuthUser.class);
        } else {
            authUser = (AuthUser) getArguments().getSerializable("partial_user");
        }

        userNameLayout = requireActivity().findViewById(R.id.userNameLayout);
        userNameLayout.getEditText().setText(authUser.getUsername());
        firstNameLayout = requireActivity().findViewById(R.id.firstNameLayout);
        firstNameLayout.getEditText().setText(authUser.getFirstName());
        lastNameLayout = requireActivity().findViewById(R.id.lastNameLayout);
        lastNameLayout.getEditText().setText(authUser.getLastName());
        bioLayout = requireActivity().findViewById(R.id.bioLayout);
        bioLayout.getEditText().setText(authUser.getBio());
        completedProfileButton = requireActivity().findViewById(R.id.button_complete_profile);

        AuthUserViewModel authUserViewModel = new ViewModelProvider(this).get(AuthUserViewModel.class);

        authUserViewModel.getFreeUserNameLiveData().observe(getViewLifecycleOwner(), booleanResult -> {
            if(booleanResult.isSuccess()){
                if (booleanResult.getData()) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("partial_user", authUser);
                    Navigation.findNavController(requireView()).navigate(R.id.action_completeAccountFragment_to_categoriesFragment, bundle);
                } else {
                    userNameLayout.setError(getString(R.string.username_taken));
                    userNameLayout.setErrorEnabled(true);
                }
            }
        });

        completedProfileButton.setOnClickListener(b -> {
            authUser.setUsername(userNameLayout.getEditText().getText().toString());
            authUser.setFirstName(firstNameLayout.getEditText().getText().toString());
            authUser.setLastName(lastNameLayout.getEditText().getText().toString());
            authUser.setBio(bioLayout.getEditText().getText().toString());

            authUserViewModel.checkUsername(authUser.getUsername());
        });
    }
}
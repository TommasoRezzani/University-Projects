package com.example.verbose.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.verbose.R;
import com.example.verbose.model.Result;
import com.example.verbose.ui.ViewModels.AuthUserViewModel;
import com.example.verbose.ui.mainApp.MainActivity;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.GsonBuilder;

import org.apache.commons.validator.routines.EmailValidator;

public class RegistrationFragment extends Fragment {

    private static final String TAG = RegistrationFragment.class.getSimpleName();

    private AuthUserViewModel authUserViewModel;

    private TextInputLayout emailInputLayout;
    private TextInputLayout passwordInputLayout;
    private TextInputLayout repeatPasswordInputLayout;
    private Button sendRegistrationButton;
    private Button googleRegistrationButton;

    private SignInClient oneTapClient;
    private BeginSignInRequest signupRequest;

    private ActivityResultLauncher<IntentSenderRequest> googleSignUpRegistry;

    public RegistrationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        oneTapClient = Identity.getSignInClient(requireContext());
        signupRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // Your server's client ID, not your Android client ID.
                        .setServerClientId(getString(R.string.default_web_client_id))
                        // Only show accounts previously used to sign in when set to true
                        .setFilterByAuthorizedAccounts(false)
                        .build()).setAutoSelectEnabled(true).build();

        this.authUserViewModel = new ViewModelProvider(this).get(AuthUserViewModel.class);

        googleSignUpRegistry = registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(), o -> {
            Intent data = o.getData();
            SignInCredential googleCredential;
            try {
                googleCredential = oneTapClient.getSignInCredentialFromIntent(data);
            } catch (ApiException e) {
                throw new RuntimeException(e);
            }
            String idToken = googleCredential.getGoogleIdToken();

            this.authUserViewModel.signUp(idToken);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.authUserViewModel.getUserLiveData().observe(getViewLifecycleOwner(), authUserResult -> {
            if(authUserResult.getEventType() == Result.EventType.SIGN_UP && !authUserResult.isHandled()) {
                if(authUserResult.isSuccess()){
                    if(authUserResult.getData().isCompleteProfile()){
                        Intent intent = new Intent(requireContext(), MainActivity.class);
                        startActivity(intent);
                        requireActivity().finish();
                    }else{
                        //potrebbe cambiare in futuro a causa della definizione di TypeAdapter differenti per AuthUser
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("partial_user", authUserResult.getData());
                        Navigation.findNavController(requireView()).navigate(R.id.action_registrationFragment_to_completeAccountFragment, bundle);
                    }
                }else{
                    Snackbar.make(requireView(), R.string.email_taken, Snackbar.LENGTH_LONG).show();
                }
                authUserResult.handle();
            }
        });

        return inflater.inflate(R.layout.fragment_registration, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        emailInputLayout = requireActivity().findViewById(R.id.email_registration_layout);
        passwordInputLayout = requireActivity().findViewById(R.id.password_registration_layout);
        repeatPasswordInputLayout = requireActivity().findViewById(R.id.repeat_password_input_layout);

        sendRegistrationButton = requireActivity().findViewById(R.id.button_send_registration);
        sendRegistrationButton.setOnClickListener(b -> {
            String email = emailInputLayout.getEditText().getText().toString();
            String password = passwordInputLayout.getEditText().getText().toString();
            String repeatedPassword = repeatPasswordInputLayout.getEditText().getText().toString();
            if(emailIsOk(email) && passwordIsOk(password) && password.equals(repeatedPassword)){
                this.authUserViewModel.signUp(email, password);
            }
            else{
                if(!emailIsOk(email)) {
                    Snackbar.make(requireView(), getString(R.string.invaild_email), Snackbar.LENGTH_LONG).show();
                    return;
                }
                if(!passwordIsOk(password)) {
                    Snackbar.make(requireView(), getString(R.string.short_password), Snackbar.LENGTH_LONG).show();
                    return;
                }
                if(!password.equals(repeatedPassword)) {
                    Snackbar.make(requireView(), getString(R.string.password_not_equal), Snackbar.LENGTH_LONG).show();
                    return;
                }
            }
        });

        //Google sign up flow
        googleRegistrationButton = requireActivity().findViewById(R.id.button_google_signup);
        googleRegistrationButton.setOnClickListener(b -> {
            Log.d(TAG, "Google login");
            oneTapClient.beginSignIn(signupRequest)
                    .addOnSuccessListener(requireActivity(), result -> {
                        try {
                            IntentSenderRequest request = new IntentSenderRequest.Builder(result.getPendingIntent().getIntentSender()).build();
                            googleSignUpRegistry.launch(request);
                        } catch (Exception e) {
                            Log.e(TAG, "Couldn't start One Tap UI: " + e.getLocalizedMessage());
                        }
                    })
                    .addOnFailureListener(requireActivity(), e -> {
                        // No saved credentials found. Launch the One Tap sign-up flow, or
                        // do nothing and continue presenting the signed-out UI.
                        Snackbar.make(b, R.string.missing_google_account, Snackbar.LENGTH_LONG).show();
                        Log.d(TAG, e.getLocalizedMessage());
                    });
        });
    }

    private boolean emailIsOk(String email){
        return EmailValidator.getInstance().isValid(email);
    }
    private boolean passwordIsOk(String password){
        return password!=null && password.length()>=5;
    }
}
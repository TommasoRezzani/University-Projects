package com.example.verbose.ui.login;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.verbose.R;
import com.example.verbose.model.AuthUser;
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
import com.google.firebase.auth.FirebaseAuth;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class LoginFragment extends Fragment {

    private static final String TAG = LoginFragment.class.getSimpleName();

    private AuthUserViewModel authUserViewModel;

    private Button loginButton;
    private Button loginGoogle;
    private Button registrationButton;

    private Button forgotPasswordButton;
    private TextInputLayout emailInputLayout;
    private TextInputLayout passwordInputLayout;
    private SignInClient oneTapClient;
    private BeginSignInRequest signinRequest;

    private ActivityResultLauncher<IntentSenderRequest> googleSignInRegistry;

    private TextView textViewForgot;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onStart();

        this.authUserViewModel = new ViewModelProvider(this).get(AuthUserViewModel.class);

        oneTapClient = Identity.getSignInClient(requireContext());

        signinRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // Your server's client ID, not your Android client ID.
                        .setServerClientId(getString(R.string.default_web_client_id))
                        // Only show accounts previously used to sign in when set to true
                        .setFilterByAuthorizedAccounts(true)
                        .build()).setAutoSelectEnabled(true).build();

        googleSignInRegistry = registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(), o -> {
            Intent data = o.getData();
            SignInCredential googleCredential;
            try {
                googleCredential = oneTapClient.getSignInCredentialFromIntent(data);
            } catch (ApiException e) {
                return; //TODO
                //throw new RuntimeException(e);
            }
            String idToken = googleCredential.getGoogleIdToken();

            authUserViewModel.signIn(idToken);

            requireActivity().findViewById(R.id.loginView).setVisibility(View.GONE);
            requireActivity().findViewById(R.id.loadingView).setVisibility(View.VISIBLE);
        });
    }

    private void startMainActivity() {
        Intent intent = new Intent(requireContext(), MainActivity.class);
        startActivity(intent);
        requireActivity().finish();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        authUserViewModel.getUserLiveData().observe(getViewLifecycleOwner(), authUserResult -> {
            if(authUserResult.isSuccess()) {
                Log.d(TAG, authUserResult.getData().getUsername());
                startMainActivity();
            } else {
                Log.d(TAG, authUserResult.getThrowable().toString());
                Throwable throwable = authUserResult.getThrowable();

                //TODO extract strings
                if(throwable instanceof SocketTimeoutException || throwable instanceof UnknownHostException){
                    Snackbar.make(requireView(), getString(R.string.no_connection_error), Snackbar.LENGTH_LONG).show();
                } else {
                    Snackbar.make(requireView(), getString(R.string.wrong_email_or_password), Snackbar.LENGTH_LONG).show();
                    textViewForgot.setVisibility(View.VISIBLE);
                    forgotPasswordButton.setVisibility(View.VISIBLE);
                }

                requireActivity().findViewById(R.id.loginView).setVisibility(View.VISIBLE);
                requireActivity().findViewById(R.id.loadingView).setVisibility(View.GONE);
            }
        });

        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //TODO check logged user with ViewModel
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            requireActivity().findViewById(R.id.loginView).setVisibility(View.GONE);
            requireActivity().findViewById(R.id.loadingView).setVisibility(View.VISIBLE);
            this.authUserViewModel.loadUserData();
        }

        //Imposta il colore dell'ombra sotto la CardView del login (disponibile con API >= 28)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            requireActivity().findViewById(R.id.login_card).setOutlineSpotShadowColor(ContextCompat.getColor(requireContext(), R.color.verbose_logo_background));
            requireActivity().findViewById(R.id.login_card).setOutlineAmbientShadowColor(ContextCompat.getColor(requireContext(), R.color.verbose_logo_background));
        }

        emailInputLayout = requireActivity().findViewById(R.id.email_input_layout);
        passwordInputLayout = requireActivity().findViewById(R.id.password_input_layout);
        loginButton = requireActivity().findViewById(R.id.login_button);
        loginGoogle = requireActivity().findViewById(R.id.button_google_login);
        registrationButton = requireActivity().findViewById(R.id.button_registration);
        textViewForgot = requireActivity().findViewById(R.id.textview_forgot);
        forgotPasswordButton = requireActivity().findViewById(R.id.new_password_button);



        registrationButton.setOnClickListener(b -> {
            Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_registrationFragment);
        });
        forgotPasswordButton.setOnClickListener(b -> {
            Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_updatePasswordFragment);
        });

        //email and password authentication flow
        loginButton.setOnClickListener(b->{
            String email = emailInputLayout.getEditText().getText().toString();
            String password = passwordInputLayout.getEditText().getText().toString();
            if(email.isEmpty()){
                emailInputLayout.setError("Riempi il campo email");
                emailInputLayout.setErrorEnabled(true);
                return;
            }else{
                emailInputLayout.setErrorEnabled(false);
            }
            if(password.isEmpty()){
                passwordInputLayout.setError("Riempi il campo password");
                passwordInputLayout.setErrorEnabled(true);
                return;
            }else{
                passwordInputLayout.setErrorEnabled(false);
            }

            requireActivity().findViewById(R.id.loginView).setVisibility(View.GONE);
            requireActivity().findViewById(R.id.loadingView).setVisibility(View.VISIBLE);

            this.authUserViewModel.signIn(email, password);
        });

        //Google authentication flow
        loginGoogle.setOnClickListener(b -> {
            Log.d(TAG, "Google login");

            oneTapClient.beginSignIn(signinRequest)
                    .addOnSuccessListener(requireActivity(), result -> {
                        try {
                            IntentSenderRequest request = new IntentSenderRequest.Builder(result.getPendingIntent().getIntentSender()).build();
                            googleSignInRegistry.launch(request);
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
}
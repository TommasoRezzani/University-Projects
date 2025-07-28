package com.example.verbose.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.verbose.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import org.apache.commons.validator.routines.EmailValidator;

public class UpdatePasswordFragment extends Fragment {

    private static final String TAG = UpdatePasswordFragment.class.getSimpleName();
    private TextInputLayout emailInputLayout;
    private Button sendEmail;
    private FirebaseAuth auth;

    public UpdatePasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update_password, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        emailInputLayout = requireActivity().findViewById(R.id.email_recupero);
        sendEmail= requireActivity().findViewById(R.id.send_email_button);
        sendEmail.setOnClickListener(b -> {
            String email = emailInputLayout.getEditText().getText().toString();
            if(emailIsOk(email)) {
                auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Email sent.");
                                Intent intent = new Intent(requireContext(), LoginActivity.class);
                                requireActivity().startActivity(intent);
                                requireActivity().finish();
                            }
                        }).addOnFailureListener(e -> Log.d(TAG, e.getLocalizedMessage()));
            }
            else {
                Snackbar.make(requireView(), getString(R.string.not_an_account), Snackbar.LENGTH_LONG).show();}});

        }
    private boolean emailIsOk(String email){
        return EmailValidator.getInstance().isValid(email);
    }
    }
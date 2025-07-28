package com.example.verbose.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.verbose.R;
import com.google.android.material.textfield.TextInputLayout;

public class RequestDialog extends DialogFragment {
    private static final String TAG = DeleteDialog.class.getSimpleName();

    private TextView textView;
    private TextInputLayout requestInput;

    private final String text;
    private String inputText;


    public RequestDialog(String text) {
        this.text = text;
    }

    public RequestDialog(String text, String inputText) {
        this.text = text;
        this.inputText = inputText;
    }

    public interface DialogListener{
        void onDialogPositiveClick(DialogInterface dialog, int which, String requestText);
        void onDialogNegativeClick(DialogInterface dialog, int which);
    }

    private DialogListener dialogListener;



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View rootView = getLayoutInflater().inflate(R.layout.input_dialog, null);
        textView = rootView.findViewById(R.id.custom_dialog_text);
        requestInput = rootView.findViewById(R.id.request_input_layout);

        textView.setText(this.text);
        requestInput.getEditText().setText(this.inputText);

        return new AlertDialog.Builder(requireActivity(), R.style.CustomDialogStyle)
                .setView(rootView)
                .setPositiveButton(R.string.delete_appointment_positive, (dialog, which) -> dialogListener.onDialogPositiveClick(dialog, which, requestInput.getEditText().getText().toString()))
                .setNegativeButton(R.string.delete_appointment_negative, dialogListener::onDialogNegativeClick)
                .create();
    }

    public void setDialogListener(DialogListener dialogListener) {
        this.dialogListener = dialogListener;
    }
}

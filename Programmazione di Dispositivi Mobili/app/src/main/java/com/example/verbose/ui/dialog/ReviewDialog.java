package com.example.verbose.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.verbose.R;
import com.google.android.material.textfield.TextInputLayout;

public class ReviewDialog extends DialogFragment {
    private static final String TAG = ReviewDialog.class.getSimpleName();

    private TextView textView;
    private TextInputLayout requestInput;
    private RatingBar ratingBar;

    private final String text;
    private String currentContent;
    private float currentVote;


    public ReviewDialog(String text) {
        this.text = text;
    }

    public ReviewDialog(String text, String currentContent, float currentVote) {
        this.text = text;
        this.currentContent = currentContent;
        this.currentVote = currentVote;
    }

    public interface DialogListener{
        void onDialogPositiveClick(DialogInterface dialog, int which, String reviewText, float vote);
        void onDialogNegativeClick(DialogInterface dialog, int which);
    }

    private ReviewDialog.DialogListener dialogListener;



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View rootView = getLayoutInflater().inflate(R.layout.review_dialog, null);
        textView = rootView.findViewById(R.id.custom_dialog_text);
        requestInput = rootView.findViewById(R.id.review_input_layout);
        ratingBar = rootView.findViewById(R.id.review_vote_bar);

        textView.setText(this.text);
        requestInput.getEditText().setText(this.currentContent);
        ratingBar.setRating(this.currentVote);

        return new AlertDialog.Builder(requireActivity(), R.style.CustomDialogStyle)
                .setView(rootView)
                .setPositiveButton(
                        R.string.delete_appointment_positive,
                        (dialog, which) ->
                                dialogListener.onDialogPositiveClick(
                                        dialog,
                                        which,
                                        requestInput.getEditText().getText().toString(),
                                        ratingBar.getRating()
                                )
                )
                .setNegativeButton(R.string.delete_appointment_negative, dialogListener::onDialogNegativeClick)
                .create();
    }

    public void setDialogListener(ReviewDialog.DialogListener dialogListener) {
        this.dialogListener = dialogListener;
    }
}

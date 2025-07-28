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

//TODO setNonReversibleText
public class DeleteDialog extends DialogFragment {
    private static final String TAG = DeleteDialog.class.getSimpleName();

    protected TextView textView;
    protected TextView reversibleText;

    protected final String text;
    protected final boolean showAlert;
    protected final boolean showPositive;
    protected final boolean showNegative;

    public interface DialogListener{
        void onDialogPositiveClick(DialogInterface dialog, int which);
        void onDialogNegativeClick(DialogInterface dialog, int which);
    }

    private DialogListener dialogListener;

    public DeleteDialog(String text, boolean showAlert) {
        this.text = text;
        this.showAlert = showAlert;
        this.showPositive = true;
        this.showNegative = true;
    }

    public DeleteDialog(String text, boolean showAlert, boolean showPositive, boolean showNegative) {
        this.text = text;
        this.showAlert = showAlert;
        this.showPositive = showPositive;
        this.showNegative = showNegative;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View rootView = getLayoutInflater().inflate(R.layout.delete_dialog, null);
        textView = rootView.findViewById(R.id.custom_dialog_text);
        reversibleText = rootView.findViewById(R.id.non_reversible_text);

        textView.setText(this.text);

        reversibleText.setVisibility(showAlert ? View.VISIBLE : View.GONE);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity(), R.style.CustomDialogStyle)
                .setView(rootView);
        if(showPositive)
                builder.setPositiveButton(R.string.delete_appointment_positive, dialogListener::onDialogPositiveClick);
        if(showNegative)
                builder.setNegativeButton(R.string.delete_appointment_negative, dialogListener::onDialogNegativeClick);

        return builder.create();
    }

    public void setDialogListener(DialogListener dialogListener) {
        this.dialogListener = dialogListener;
    }
}

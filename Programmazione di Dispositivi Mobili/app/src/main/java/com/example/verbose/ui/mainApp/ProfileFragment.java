package com.example.verbose.ui.mainApp;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.verbose.R;
import com.example.verbose.model.AuthUser;
import com.example.verbose.model.Result;
import com.example.verbose.ui.ViewModels.ProfileViewModel;
import com.example.verbose.ui.ViewModels.ProfileViewModelFactory;
import com.example.verbose.util.GlideApp;
import com.google.gson.GsonBuilder;

import java.io.File;


public class ProfileFragment extends Fragment {
    private static final String TAG = ProfileFragment.class.getSimpleName();

    private TextView username;
    private TextView firstName;
    private TextView bio;
    private ImageView profilePicture;

    private ActivityResultLauncher<Intent> selectImage;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        selectImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), o -> {
            if(o.getData() != null) {
                Uri uri = o.getData().getData();
                Uri filePathUri = null;
                Cursor cursor = requireActivity().getContentResolver().query(uri, null, null, null, null);
                assert cursor != null;
                if(cursor.moveToFirst()){
                    int column_index = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                    filePathUri = Uri.parse(cursor.getString(column_index));
                }
                cursor.close();
                File file = new File(filePathUri.getPath());
                ProfileViewModel profileViewModel = new ViewModelProvider(this, new ProfileViewModelFactory(getContext())).get(ProfileViewModel.class);
                profileViewModel.updateProfilePic(file);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        username = rootView.findViewById(R.id.profile_username_text_view);
        firstName = rootView.findViewById(R.id.firstLastName);
        bio = rootView.findViewById(R.id.profile_bio_text_view);
        profilePicture = rootView.findViewById(R.id.profile_picture);

        profilePicture.setOnClickListener(v -> selectImage.launch(new Intent(Intent.ACTION_PICK).setType("image/*")));

        ProfileViewModel profileViewModel = new ViewModelProvider(this, new ProfileViewModelFactory(getContext())).get(ProfileViewModel.class);

        profileViewModel.getUserLiveData().observe(getViewLifecycleOwner(), authUserResult -> {
            if(authUserResult.getEventType() == Result.EventType.UPDATE_USER || authUserResult.getEventType() == Result.EventType.GET_USER) {
                username.setText(authUserResult.getData().getUsername());
                firstName.setText(authUserResult.getData().getFirstName() + " " + authUserResult.getData().getLastName());
                bio.setText(authUserResult.getData().getBio());

                GlideApp.with(ProfileFragment.this)
                        .load(authUserResult.getData().getProfilePicture())
                        .placeholder(R.drawable.baseline_account_circle_24)
                        .circleCrop()
                        .into(profilePicture);
            }
        });

        if(!profileViewModel.getUserLiveData().isInitialized())
            profileViewModel.getUserData();

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ProfileViewModel profileViewModel = new ViewModelProvider(this, new ProfileViewModelFactory(getContext())).get(ProfileViewModel.class);

        requireActivity().findViewById(R.id.cardView5).setOnClickListener(b -> {
            profileViewModel.logout().observe(getViewLifecycleOwner(), authUserResult -> {
                if(authUserResult.getEventType() == Result.EventType.LOGOUT) {
                    authUserResult.handle();
                    requireActivity().finish();
                }
            });
        });

        requireActivity().findViewById(R.id.card_view_mp).setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("user_profile", profileViewModel.getUserLiveData().getValue().getData());
            Navigation.findNavController(requireView()).navigate(R.id.modificaProfiloFragment, bundle);
        });

        requireActivity().findViewById(R.id.card_view_lro).setOnClickListener(v -> {
            Navigation.findNavController(requireView()).navigate(R.id.lroProfiloFragment);
        });

        requireActivity().findViewById(R.id.card_view_i).setOnClickListener(v -> {
            Navigation.findNavController(requireView()).navigate(R.id.impostazioniProfiloFragment);
        });

    }
}
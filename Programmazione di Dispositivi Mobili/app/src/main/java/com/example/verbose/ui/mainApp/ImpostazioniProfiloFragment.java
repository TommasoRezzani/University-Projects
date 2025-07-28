package com.example.verbose.ui.mainApp;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.verbose.Constants;
import com.example.verbose.R;
import com.example.verbose.ui.ViewModels.SettingsViewModel;
import com.example.verbose.ui.ViewModels.SettingsViewModelFactory;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.Locale;

public class ImpostazioniProfiloFragment extends Fragment {
    private Spinner languages;
    private SwitchMaterial notificationsSwitch;

    public ImpostazioniProfiloFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_impostazioni_profilo, container, false);
        languages = rootView.findViewById(R.id.spinner_cambia_lingua);
        notificationsSwitch = rootView.findViewById(R.id.switch_notifiche);

        boolean enabled = requireActivity().getSharedPreferences(Constants.LOCAL_STORAGE, Context.MODE_PRIVATE).getBoolean(Constants.NOTIFICATIONS_SETTINGS, true);

        SettingsViewModel settingsViewModel = new ViewModelProvider(requireActivity(), new SettingsViewModelFactory(enabled)).get(SettingsViewModel.class);

        if(settingsViewModel.getSelectedLanguageLiveData().isInitialized())
            languages.setSelection(settingsViewModel.getSelectedLanguageLiveData().getValue().first);

        if(settingsViewModel.getNotificationsEnabledLiveData().isInitialized())
            notificationsSwitch.setChecked(settingsViewModel.getNotificationsEnabledLiveData().getValue());

        settingsViewModel.getSelectedLanguageLiveData().observe(getViewLifecycleOwner(), pair -> {
            Locale locale = Locale.forLanguageTag(pair.second);
            Locale.setDefault(locale);
            Resources resources = requireActivity().getResources();
            Configuration config = resources.getConfiguration();
            config.setLocale(locale);
            resources.updateConfiguration(config, resources.getDisplayMetrics());
        });

        settingsViewModel.getNotificationsEnabledLiveData().observe(getViewLifecycleOwner(), isChecked ->
                requireActivity()
                    .getSharedPreferences(Constants.LOCAL_STORAGE, Context.MODE_PRIVATE)
                    .edit().putBoolean(Constants.NOTIFICATIONS_SETTINGS, isChecked)
                    .apply());

        languages.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                settingsViewModel.changeLanguage(position,languages.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        notificationsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            settingsViewModel.setNotificationsEnabled(isChecked);
        });

        return rootView;
    }
}
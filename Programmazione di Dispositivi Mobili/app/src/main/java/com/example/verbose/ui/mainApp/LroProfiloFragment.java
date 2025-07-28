package com.example.verbose.ui.mainApp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.verbose.R;
import com.example.verbose.adapter.ListRepetitionProfileAdapter;
import com.example.verbose.model.Repetition;
import com.example.verbose.ui.ViewModels.ProfileViewModel;
import com.example.verbose.ui.ViewModels.ProfileViewModelFactory;
import com.example.verbose.ui.dialog.DeleteDialog;
import com.google.android.material.tabs.TabLayout;

import java.util.Comparator;

public class LroProfiloFragment extends Fragment implements Comparator<Repetition>, ListRepetitionProfileAdapter.OnRepetitionCloseListener, ListRepetitionProfileAdapter.OnRepetitionClickListener {
    private RecyclerView recyclerView;
    private TabLayout tabLayout;

    public LroProfiloFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_lro_profilo, container, false);

        tabLayout = rootView.findViewById(R.id.tabLayout);

        recyclerView = rootView.findViewById(R.id.recycler_view_lro);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

        ProfileViewModel profileViewModel = new ViewModelProvider(requireActivity(), new ProfileViewModelFactory(getContext())).get(ProfileViewModel.class);

        profileViewModel.getRepetitions().observe(getViewLifecycleOwner(), listResult -> {
            ListRepetitionProfileAdapter adapter = new ListRepetitionProfileAdapter(listResult.getData(), this);
            adapter.setOnCloseListener(this);
            adapter.setOnClickListener(this);
            recyclerView.setAdapter(adapter);
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                ListRepetitionProfileAdapter adapter = (ListRepetitionProfileAdapter) recyclerView.getAdapter();
                adapter.showOpen(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return rootView;
    }

    @Override
    public void onClose(View v, int position,Repetition target) {
        DeleteDialog verboseDialog = new DeleteDialog(getString(R.string.repetiton_delete_confirmation), true);
        ProfileViewModel profileViewModel = new ViewModelProvider(this, new ProfileViewModelFactory(getContext())).get(ProfileViewModel.class);

        verboseDialog.setDialogListener(new DeleteDialog.DialogListener() {
            @Override
            public void onDialogPositiveClick(DialogInterface dialog, int which) {
                profileViewModel.closeRepetition(target).observe(getViewLifecycleOwner(), listResult -> {
                    recyclerView.getAdapter().notifyItemRemoved(position);
                });
            }

            @Override
            public void onDialogNegativeClick(DialogInterface dialog, int which) {

            }
        });
        verboseDialog.show(requireActivity().getSupportFragmentManager(), "CLOSE REPETITION");

    }

    @Override
    public void onClick(View v, Repetition repetition) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("repetition", repetition);
        Navigation.findNavController(v).navigate(R.id.action_lroProfiloFragment_to_reviewListFragment, bundle);
    }

    @Override
    public int compare(Repetition o1, Repetition o2) {
        return o1.getDate().compareTo(o2.getDate());
    }
}
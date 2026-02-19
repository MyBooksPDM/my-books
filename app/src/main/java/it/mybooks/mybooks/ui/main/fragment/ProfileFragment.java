package it.mybooks.mybooks.ui.main.fragment;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseUser;

import it.mybooks.mybooks.R;
import it.mybooks.mybooks.ui.main.viewmodel.ProfileViewModel;
import it.mybooks.mybooks.ui.onboarding.viewmodel.AuthViewModel;


public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    private TextView profileEmail;
    private TextView profileName;
    private Button buttonLogOut;
    private Button buttonDeleteAccount;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        profileName = view.findViewById(R.id.profileName);
        profileEmail = view.findViewById(R.id.profileEmail);
        buttonLogOut = view.findViewById(R.id.buttonLogOut);
        buttonDeleteAccount = view.findViewById(R.id.buttonDeleteAccount);

        profileViewModel.getCurrentUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                String username = user.getEmail() != null ? user.getEmail().split("@")[0] : "User";

                username = username.contains(".") ? username.split("\\.")[0] : username;
                username = !username.isEmpty() ? username.substring(0, 1).toUpperCase() + username.substring(1) : "User";

                profileName.setText(user.getDisplayName() != null ? user.getDisplayName() : username);
                profileEmail.setText(user.getEmail());
            }
        });

        buttonLogOut.setOnClickListener(v -> {
            AuthViewModel authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
            authViewModel.signOut();
        });
        buttonDeleteAccount.setOnClickListener(v -> showDeleteConfirmationDialog());
    }

    private void showDeleteConfirmationDialog() {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.delete_account_dialog)
                .setMessage(R.string.delete_account_message)
                .setPositiveButton(R.string.delete_account_confirm, (dialog, which) -> {
                    profileViewModel.deleteAccount()
                            .observe(getViewLifecycleOwner(), success -> {
                                if (!success) {
                                    new AlertDialog.Builder(requireContext())
                                            .setTitle(R.string.delete_account_error)
                                            .setMessage(R.string.delete_account_error_message)
                                            .setPositiveButton(R.string.ok, null)
                                            .show();
                                }
                            });
                })
                .setNegativeButton(R.string.delete_account_cancel, null)
                .show();
    }

}
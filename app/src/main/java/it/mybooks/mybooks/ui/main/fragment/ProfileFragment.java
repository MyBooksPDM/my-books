package it.mybooks.mybooks.ui.main.fragment;

import android.app.AlertDialog;
import android.content.Intent;
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

import com.google.firebase.auth.FirebaseUser;

import it.mybooks.mybooks.R;
import it.mybooks.mybooks.ui.main.viewmodel.ProfileViewModel;


public class ProfileFragment extends Fragment {

    private ProfileViewModel viewModel;
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

        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        profileName = view.findViewById(R.id.profileName);
        profileEmail = view.findViewById(R.id.profileEmail);
        buttonLogOut = view.findViewById(R.id.buttonLogOut);
        buttonDeleteAccount = view.findViewById(R.id.buttonDeleteAccount);

        FirebaseUser user = viewModel.getCurrentUser();
        if (user != null) {
            profileEmail.setText(user.getEmail());
        }

        if (user != null && user.getEmail() != null) {

            String email = user.getEmail();

            // Prendiamo la parte prima della @
            String username = email.split("@")[0];

            // Se contiene un punto (es. marco.delprete), prendiamo solo la prima parte
            if (username.contains(".")) {
                username = username.split("\\.")[0];
            }

            // Mettiamo la prima lettera maiuscola
            if (!username.isEmpty()) {
                username = username.substring(0, 1).toUpperCase() + username.substring(1);
            }

            profileName.setText(username);
        }


        buttonLogOut.setOnClickListener(v -> viewModel.logout());

        buttonDeleteAccount.setOnClickListener(v -> showDeleteConfirmationDialog());
    }

    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Eliminare account?")
                .setMessage("Questa operazione è irreversibile. Vuoi continuare?")
                .setPositiveButton("Elimina", (dialog, which) -> {
                    viewModel.deleteAccount()
                            .observe(getViewLifecycleOwner(), success -> {
                                if (!success) {
                                    new AlertDialog.Builder(requireContext())
                                            .setTitle("Errore")
                                            .setMessage("Impossibile eliminare l'account.")
                                            .setPositiveButton("OK", null)
                                            .show();
                                }
                                // Se success = true, la MainActivity intercetta il logout
                            });
                })
                .setNegativeButton("Annulla", null)
                .show();
    }

}
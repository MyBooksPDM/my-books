package it.mybooks.mybooks.ui.onboarding.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import it.mybooks.mybooks.R;
import it.mybooks.mybooks.ui.onboarding.viewmodel.AuthViewModel;

public class WelcomeFragment extends Fragment {

    private AuthViewModel authViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_welcome, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        Button googleLoginButton = view.findViewById(R.id.google_button);

        googleLoginButton.setOnClickListener(v -> {
            authViewModel.signInWithGoogle(getActivity());
        });

//        authViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
//            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
//            btnGoogle.setEnabled(!isLoading);
//        });

        authViewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            }
        });

        Button b = view.findViewById(R.id.email_button);

        b.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_welcomeFragment_to_loginFragment);
        });
    }
}
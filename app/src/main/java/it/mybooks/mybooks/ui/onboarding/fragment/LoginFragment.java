package it.mybooks.mybooks.ui.onboarding.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import it.mybooks.mybooks.R;
import it.mybooks.mybooks.ui.onboarding.viewmodel.AuthViewModel;

public class LoginFragment extends Fragment {

    private AuthViewModel authViewModel;
    private TextInputLayout emailInput;
    private TextInputLayout passwordInput;
    private TextInputEditText emailEditText;
    private TextInputEditText passwordEditText;
    private Button loginButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);

        // Initialize views
        loginButton = view.findViewById(R.id.loginButton);
        Button switchToRegisterButton = view.findViewById(R.id.buttonLoginToRegister);
        emailInput = view.findViewById(R.id.emailTextField);
        passwordInput = view.findViewById(R.id.passwordTextField);

        emailEditText = (TextInputEditText) emailInput.getEditText();
        passwordEditText = (TextInputEditText) passwordInput.getEditText();

        // Setup real-time validation
        setupEmailValidation();
        setupPasswordValidation();

        // Observe loading state
        authViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null) {
                loginButton.setEnabled(!isLoading);
                switchToRegisterButton.setEnabled(!isLoading);

                if (isLoading) {
                    loginButton.setText("Logging in...");
                } else {
                    loginButton.setText("Login");
                }
            }
        });

        // Observe error messages
        authViewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show();
            }
        });

        // Setup login button click listener
        loginButton.setOnClickListener(v -> {
            if (validateForm()) {
                String email = emailEditText.getText() != null ? emailEditText.getText().toString().trim() : "";
                String password = passwordEditText.getText() != null ? passwordEditText.getText().toString() : "";
                authViewModel.signInWithEmail(email, password);
            }
        });

        // Navigate to registration
        switchToRegisterButton.setOnClickListener(v ->
            Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_registerFragment)
        );
    }

    private void setupEmailValidation() {
        if (emailEditText != null) {
            emailEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // Clear error when user starts typing
                    if (emailInput.getError() != null) {
                        emailInput.setError(null);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });
        }
    }

    private void setupPasswordValidation() {
        if (passwordEditText != null) {
            passwordEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // Clear error when user starts typing
                    if (passwordInput.getError() != null) {
                        passwordInput.setError(null);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });
        }
    }

    private boolean validateEmail() {
        if (emailEditText == null || emailEditText.getText() == null) {
            return false;
        }

        String email = emailEditText.getText().toString().trim();

        if (email.isEmpty()) {
            emailInput.setError("Email is required");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.setError("Please enter a valid email address");
            return false;
        } else {
            emailInput.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        if (passwordEditText == null || passwordEditText.getText() == null) {
            return false;
        }

        String password = passwordEditText.getText().toString();

        if (password.isEmpty()) {
            passwordInput.setError("Password is required");
            return false;
        } else {
            passwordInput.setError(null);
            return true;
        }
    }

    private boolean validateForm() {
        boolean isEmailValid = validateEmail();
        boolean isPasswordValid = validatePassword();

        return isEmailValid && isPasswordValid;
    }

}
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

public class RegisterFragment extends Fragment {

    private AuthViewModel authViewModel;
    private TextInputLayout emailInput;
    private TextInputLayout passwordInput;
    private TextInputLayout confirmPasswordInput;
    private TextInputEditText emailEditText;
    private TextInputEditText passwordEditText;
    private TextInputEditText confirmPasswordEditText;
    private Button registerButton;
    private Button buttonRegisterToLogin;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);

        // Initialize views
        buttonRegisterToLogin = view.findViewById(R.id.register_to_login_button);
        registerButton = view.findViewById(R.id.register_button);
        emailInput = view.findViewById(R.id.login_email);
        passwordInput = view.findViewById(R.id.login_password);
        confirmPasswordInput = view.findViewById(R.id.repeat_password);

        emailEditText = (TextInputEditText) emailInput.getEditText();
        passwordEditText = (TextInputEditText) passwordInput.getEditText();
        confirmPasswordEditText = (TextInputEditText) confirmPasswordInput.getEditText();

        // Setup real-time validation
        setupEmailValidation();
        setupPasswordValidation();
        setupConfirmPasswordValidation();

        // Observe loading state
        authViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null) {
                registerButton.setEnabled(!isLoading);
                buttonRegisterToLogin.setEnabled(!isLoading);

                if (isLoading) {
                    registerButton.setText(R.string.creating_account);
                } else {
                    registerButton.setText(R.string.create_an_account);
                }
            }
        });

        // Observe error messages
        authViewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show();
            }
        });

        // Setup register button click listener
        registerButton.setOnClickListener(v -> {
            if (validateForm()) {
                String email = emailEditText.getText() != null ? emailEditText.getText().toString().trim() : "";
                String password = passwordEditText.getText() != null ? passwordEditText.getText().toString() : "";
                authViewModel.signUpWithEmail(email, password);
            }
        });

        buttonRegisterToLogin.setOnClickListener(v ->
            Navigation.findNavController(v).navigate(R.id.action_registerFragment_to_loginFragment)
        );
    }

    private void setupEmailValidation() {
        if (emailEditText != null) {
            emailEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    validateEmail();
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
                    validatePassword();
                    // Also revalidate confirm password when password changes
                    if (confirmPasswordEditText != null && confirmPasswordEditText.getText() != null
                            && confirmPasswordEditText.getText().length() > 0) {
                        validateConfirmPassword();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });
        }
    }

    private void setupConfirmPasswordValidation() {
        if (confirmPasswordEditText != null) {
            confirmPasswordEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    validateConfirmPassword();
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
        } else if (password.length() < 6) {
            passwordInput.setError("Password must be at least 6 characters");
            return false;
        } else if (!password.matches(".*[A-Z].*")) {
            passwordInput.setError("Password must contain at least one uppercase letter");
            return false;
        } else if (!password.matches(".*[a-z].*")) {
            passwordInput.setError("Password must contain at least one lowercase letter");
            return false;
        } else if (!password.matches(".*\\d.*")) {
            passwordInput.setError("Password must contain at least one number");
            return false;
        } else {
            passwordInput.setError(null);
            return true;
        }
    }

    private boolean validateConfirmPassword() {
        if (passwordEditText == null || passwordEditText.getText() == null
                || confirmPasswordEditText == null || confirmPasswordEditText.getText() == null) {
            return false;
        }

        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        if (confirmPassword.isEmpty()) {
            confirmPasswordInput.setError("Please confirm your password");
            return false;
        } else if (!confirmPassword.equals(password)) {
            confirmPasswordInput.setError("Passwords do not match");
            return false;
        } else {
            confirmPasswordInput.setError(null);
            return true;
        }
    }

    private boolean validateForm() {
        boolean isEmailValid = validateEmail();
        boolean isPasswordValid = validatePassword();
        boolean isConfirmPasswordValid = validateConfirmPassword();

        return isEmailValid && isPasswordValid && isConfirmPasswordValid;
    }

}
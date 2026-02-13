package it.mybooks.mybooks.ui.onboarding.viewmodel;

import android.app.Activity;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.credentials.Credential;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.CustomCredential;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.exceptions.GetCredentialException;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;

import it.mybooks.mybooks.data.repository.UserRepository;

public class AuthViewModel extends AndroidViewModel {
    private UserRepository repository;
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public AuthViewModel(@NonNull Application application) {
        super(application);
        repository = UserRepository.getInstance();
    }

    public void signOut() {
        repository.signOut();
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void signInWithGoogle(Activity activity) {
        isLoading.setValue(true);
        CredentialManager credentialManager = CredentialManager.create(activity);

        // 1. Build the Google Option
        GetGoogleIdOption googleIdOption = new GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId("277340849889-9v9m43atsuldva49goav1jvc1id1o0u9.apps.googleusercontent.com") // TODO: Add your ID
                .setAutoSelectEnabled(true)
                .build();

        // 2. Build Request
        GetCredentialRequest request = new GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build();

        // 3. Launch UI
        credentialManager.getCredentialAsync(
                activity,
                request,
                null,
                ContextCompat.getMainExecutor(activity),
                new CredentialManagerCallback<GetCredentialResponse, GetCredentialException>() {
                    @Override
                    public void onResult(GetCredentialResponse result) {
                        handleSignInResult(result);
                    }

                    @Override
                    public void onError(GetCredentialException e) {
                        isLoading.setValue(false);
                        errorMessage.setValue("Google Sign In failed: " + e.getMessage());
                    }
                }
        );
    }

    public void signInWithEmail(String email, String password) {
        isLoading.setValue(true);
        repository.loginWithEmailAndPassword(email, password, new UserRepository.OnLoginListener() {
            @Override
            public void onSuccess() {
                isLoading.setValue(false);
                // Navigation is handled by MainActivity observing MainViewModel
            }

            @Override
            public void onError(String msg) {
                isLoading.setValue(false);
                errorMessage.setValue(msg);
            }
        });
    }

    public void signUpWithEmail(String email, String password) {
        isLoading.setValue(true);
        repository.registerWithEmailAndPassword(email, password, new UserRepository.OnLoginListener() {
            @Override
            public void onSuccess() {
                isLoading.setValue(false);
                // Navigation is handled by MainActivity observing MainViewModel
            }

            @Override
            public void onError(String msg) {
                isLoading.setValue(false);
                errorMessage.setValue(msg);
            }
        });
    }

    private void handleSignInResult(GetCredentialResponse result) {
        // 4. Parse Token
        Credential credential = result.getCredential();
        if (credential instanceof CustomCredential) {
            String type = ((CustomCredential) credential).getType();
            if (GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL.equals(type)) {
                GoogleIdTokenCredential googleId = GoogleIdTokenCredential.createFrom(((CustomCredential) credential).getData());

                // 5. Send to Repository
                repository.loginWithGoogleIdToken(googleId.getIdToken(), new UserRepository.OnLoginListener() {
                    @Override
                    public void onSuccess() {
                        isLoading.setValue(false);
                        // Navigation is handled by MainActivity observing MainViewModel
                    }

                    @Override
                    public void onError(String msg) {
                        isLoading.setValue(false);
                        errorMessage.setValue(msg);
                    }
                });
            }
        }
    }

}

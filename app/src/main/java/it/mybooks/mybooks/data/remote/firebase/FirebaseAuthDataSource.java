package it.mybooks.mybooks.data.remote.firebase;

import android.app.Activity;

import androidx.core.content.ContextCompat;
import androidx.credentials.Credential;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.CustomCredential;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.exceptions.GetCredentialException;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import it.mybooks.mybooks.utils.Constants;

public class FirebaseAuthDataSource {
    private static FirebaseAuthDataSource instance;
    private final FirebaseAuth firebaseAuth;
    private final MutableLiveData<FirebaseUser> userLiveData;

    private FirebaseAuthDataSource() {
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.userLiveData = new MutableLiveData<>();

        // Initialize with current user and listen for auth state changes
        this.userLiveData.postValue(firebaseAuth.getCurrentUser());
        setupAuthStateListener();
    }

    public static FirebaseAuthDataSource getInstance() {
        if (instance == null) {
            instance = new FirebaseAuthDataSource();
        }
        return instance;
    }

    private void setupAuthStateListener() {
        firebaseAuth.addAuthStateListener(auth -> {
            userLiveData.postValue(auth.getCurrentUser());
        });
    }

    public LiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }

    public FirebaseUser getCurrentUser() {
        return firebaseAuth.getCurrentUser();
    }

    public void signOut() {
        firebaseAuth.signOut();
    }

    public void signInWithGoogle(Activity activity, GoogleSignInCallback callback) {
        CredentialManager credentialManager = CredentialManager.create(activity);

        // Build the Google Sign-In option
        GetGoogleIdOption googleIdOption = new GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(Constants.FirebaseAuth.SERVER_CLIENT_ID)
                .setAutoSelectEnabled(true)
                .build();

        // Build the credential request
        GetCredentialRequest request = new GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build();

        // Launch the credential manager UI
        credentialManager.getCredentialAsync(
                activity,
                request,
                null,
                ContextCompat.getMainExecutor(activity),
                new CredentialManagerCallback<>() {
                    @Override
                    public void onResult(GetCredentialResponse result) {
                        handleGoogleCredentialResult(result, callback);
                    }

                    @Override
                    public void onError(GetCredentialException e) {
                        callback.onError("Google Sign In failed: " + e.getMessage());
                    }
                }
        );
    }

    private void handleGoogleCredentialResult(GetCredentialResponse result, GoogleSignInCallback callback) {
        Credential credential = result.getCredential();
        if (credential instanceof CustomCredential) {
            String type = credential.getType();
            if (GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL.equals(type)) {
                GoogleIdTokenCredential googleId = GoogleIdTokenCredential.createFrom(credential.getData());
                String idToken = googleId.getIdToken();

                // Sign in with Firebase using the Google ID token
                signInWithGoogleIdToken(idToken, callback);
            }
        }
    }

    private void signInWithGoogleIdToken(String idToken, GoogleSignInCallback callback) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    } else {
                        String errorMsg = task.getException() != null
                                ? task.getException().getMessage()
                                : "Unknown error";
                        callback.onError(errorMsg);
                    }
                });
    }

    public Task<AuthResult> signInWithEmailAndPassword(String email, String password) {
        return firebaseAuth.signInWithEmailAndPassword(email, password);
    }

    public Task<AuthResult> createUserWithEmailAndPassword(String email, String password) {
        return firebaseAuth.createUserWithEmailAndPassword(email, password);
    }

    public interface GoogleSignInCallback {
        void onSuccess();

        void onError(String message);
    }
}

package it.mybooks.mybooks.data.repository;

import android.app.Activity;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.google.firebase.auth.FirebaseUser;

import it.mybooks.mybooks.data.remote.firebase.FirebaseAuthDataSource;

public class UserRepository {

    private static final String TAG = UserRepository.class.getName();
    private static UserRepository instance;
    private final FirebaseAuthDataSource firebaseAuthDataSource;

    public static UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }

    private UserRepository() {
        this.firebaseAuthDataSource = FirebaseAuthDataSource.getInstance();
    }

    public LiveData<FirebaseUser> getUser() {
        return firebaseAuthDataSource.getUserLiveData();
    }

    public void signInWithGoogle(Activity activity, OnLoginListener listener) {
        firebaseAuthDataSource.signInWithGoogle(activity, new FirebaseAuthDataSource.GoogleSignInCallback() {
            @Override
            public void onSuccess() {
                listener.onSuccess();
            }

            @Override
            public void onError(String message) {
                listener.onError(message);
            }
        });
    }

    public void signInWithEmailAndPassword(String email, String password, OnLoginListener listener) {
        firebaseAuthDataSource.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Auth state listener in FirebaseAuthDataSource will update LiveData
                        listener.onSuccess();
                    } else {
                        listener.onError(task.getException() != null
                            ? task.getException().getMessage()
                            : "Unknown error");
                    }
                });
    }

    public void createUserWithEmailAndPassword(String email, String password, OnLoginListener listener) {
        firebaseAuthDataSource.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.i(TAG, "createUserWithEmailAndPassword: Registration successful for email: " + email);
                        // Auth state listener in FirebaseAuthDataSource will update LiveData
                        listener.onSuccess();
                    } else {
                        String errorMessage = task.getException() != null
                            ? task.getException().getMessage()
                            : "Unknown error";
                        Log.e(TAG, "createUserWithEmailAndPassword: Registration failed for email: " + email + " with error: " + errorMessage);
                        listener.onError(errorMessage);
                    }
                });
    }

    public void signOut() {
        firebaseAuthDataSource.signOut();
    }

    public void deleteAccount(OnDeleteAccountListener listener) {
        firebaseAuthDataSource.deleteAccount(new FirebaseAuthDataSource.DeleteAccountCallback() {
            @Override
            public void onSuccess() {
                listener.onSuccess();
            }

            @Override
            public void onError(String message) {
                listener.onError(message);
            }
        });
    }
    public interface OnLoginListener {
        void onSuccess();

        void onError(String message);
    }

    public interface  OnDeleteAccountListener {
        void onSuccess();

        void onError(String message);
    }
}

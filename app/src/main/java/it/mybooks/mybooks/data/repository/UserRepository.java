package it.mybooks.mybooks.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class UserRepository {
    private static UserRepository instance;
    private FirebaseAuth firebaseAuth;
    private MutableLiveData<FirebaseUser> userLiveData;

    public static UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }

    private UserRepository() {
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.userLiveData = new MutableLiveData<>();

        // Listen for auth state changes (login/logout)
        firebaseAuth.addAuthStateListener(auth -> {
            userLiveData.postValue(auth.getCurrentUser());
        });
    }

    public LiveData<FirebaseUser> getUser() {
        return userLiveData;
    }

    public void signOut() {
        firebaseAuth.signOut();
    }

    public void loginWithGoogleIdToken(String idToken, OnLoginListener listener) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Login successful, userLiveData will be updated by the auth state listener
//                        userLiveData.postValue(firebaseAuth.getCurrentUser());
                        listener.onSuccess();
                    } else {
                        // Handle login failure (e.g., show error message)
//                        userLiveData.postValue(null);
                        listener.onError(task.getException().getMessage());
                    }
                });
    }


    public void loginWithEmailAndPassword(String email, String password) {
        // Implement Firebase Authentication login logic here
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Login successful, userLiveData will be updated by the auth state listener
                        userLiveData.postValue(firebaseAuth.getCurrentUser());
                    } else {
                        // Handle login failure (e.g., show error message)
                        userLiveData.postValue(null);
                    }
                });
    }

    public void registerWithEmailAndPassword(String email, String password) {
        // Implement Firebase Authentication registration logic here
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Registration successful, userLiveData will be updated by the auth state listener
                        userLiveData.postValue(firebaseAuth.getCurrentUser());
                    } else {
                        // Handle registration failure (e.g., show error message)
                        userLiveData.postValue(null);
                    }
                });
    }

    public interface OnLoginListener {
        void onSuccess();

        void onError(String message);
    }
}

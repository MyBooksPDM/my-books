package it.mybooks.mybooks.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.firebase.auth.FirebaseUser;

import it.mybooks.mybooks.data.repository.UserRepository;

public class MainViewModel extends AndroidViewModel {
    // This LiveData tracks if we have a user or null
    public LiveData<FirebaseUser> userSession;

    public MainViewModel(@NonNull Application application) {
        super(application);
        UserRepository userRepo = UserRepository.getInstance();
        userSession = userRepo.getUser();
    }

    public LiveData<FirebaseUser> getUserSession() {
        return userSession;
    }
}

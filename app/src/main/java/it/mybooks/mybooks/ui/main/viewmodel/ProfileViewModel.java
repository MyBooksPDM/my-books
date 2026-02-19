package it.mybooks.mybooks.ui.main.viewmodel;


import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import it.mybooks.mybooks.data.repository.BookRepository;
import it.mybooks.mybooks.data.repository.UserRepository;

public class ProfileViewModel extends AndroidViewModel {

    private final UserRepository userRepository;

    private final BookRepository bookRepository;

    public ProfileViewModel(Application application) {
        super(application);
        userRepository = UserRepository.getInstance();
        bookRepository = BookRepository.getInstance(application);
    }

    public LiveData<FirebaseUser> getCurrentUser() {
        return userRepository.getUser();
    }

    public LiveData<Boolean> deleteAccount() {
        MutableLiveData<Boolean> result = new MutableLiveData<>();

        getCurrentUser().observeForever(firebaseUser -> {
            if (firebaseUser != null) {
                firebaseUser.delete().addOnCompleteListener(task -> {
                    result.setValue(task.isSuccessful());
                    bookRepository.clearLocalBooks();
                });
            } else {
                result.setValue(false);
            }
        });
        return result;
    }
}



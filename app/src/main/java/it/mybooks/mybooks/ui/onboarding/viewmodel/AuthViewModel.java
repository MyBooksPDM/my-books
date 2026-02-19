package it.mybooks.mybooks.ui.onboarding.viewmodel;

import android.app.Activity;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import it.mybooks.mybooks.data.repository.BookRepository;
import it.mybooks.mybooks.data.repository.UserRepository;

public class AuthViewModel extends AndroidViewModel {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public AuthViewModel(@NonNull Application application) {
        super(application);
        userRepository = UserRepository.getInstance();
        bookRepository = BookRepository.getInstance(application);
    }

    public void signOut() {
        bookRepository.clearLocalBooks();
        userRepository.signOut();
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void signInWithGoogle(Activity activity) {
        isLoading.setValue(true);
        userRepository.signInWithGoogle(activity, new UserRepository.OnLoginListener() {
            @Override
            public void onSuccess() {
                isLoading.setValue(false);
            }

            @Override
            public void onError(String msg) {
                isLoading.setValue(false);
                errorMessage.setValue(msg);
            }
        });
    }

    public void signInWithEmail(String email, String password) {
        isLoading.setValue(true);
        userRepository.signInWithEmailAndPassword(email, password, new UserRepository.OnLoginListener() {
            @Override
            public void onSuccess() {
                isLoading.setValue(false);
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
        userRepository.createUserWithEmailAndPassword(email, password, new UserRepository.OnLoginListener() {
            @Override
            public void onSuccess() {
                isLoading.setValue(false);
            }

            @Override
            public void onError(String msg) {
                isLoading.setValue(false);
                errorMessage.setValue(msg);
            }
        });
    }

}
